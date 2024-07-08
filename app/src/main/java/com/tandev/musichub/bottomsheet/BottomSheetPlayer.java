package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.adapter.new_release_song.NewReleaseViewPageAdapter;
import com.tandev.musichub.adapter.player.PlayerViewPageAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;


public class BottomSheetPlayer extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private final int tab_layout;
    private BottomSheetDialog bottomSheetDialog;

    private SharedPreferencesManager sharedPreferencesManager;
    private Items items;
    private boolean is_playing;
    private ArrayList<Items> songArrayList;

    private RelativeLayout layout_bottom_player;
    private RelativeLayout relative_player;

    private LinearLayout layout_player;
    private RoundedImageView img_album_song;
    private TextView txt_title_song;
    private TextView txt_artist;
    private LinearLayout linear_play_pause;
    private ImageView img_play_pause;
    private LinearLayout linear_next;

    private LinearLayout layout_view_pager;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private RelativeLayout relative_loading;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            items = (Items) bundle.get("object_song");
            is_playing = bundle.getBoolean("status_player");
            int action = bundle.getInt("action_music");
            if (action == MyService.ACTION_START || action == MyService.ACTION_NEXT || action == MyService.ACTION_PREVIOUS) {
                relative_player.setVisibility(View.GONE);
                relative_loading.setVisibility(View.VISIBLE);
                getColorBackground();
                getSongPlaying();
            }
        }
    };

    public BottomSheetPlayer(Context context, Activity activity, int tab_layout) {
        this.context = context;
        this.activity = activity;
        this.tab_layout = tab_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_player, null);
        bottomSheetDialog.setContentView(view);

        sharedPreferencesManager = new SharedPreferencesManager(context);
        songArrayList = new ArrayList<>();

        initViews(bottomSheetDialog);
        conFigViews();
        onClick();
        initViewPager();

        getColorBackground();
        getSongPlaying();


        return bottomSheetDialog;
    }

    private void getSongPlaying() {
        items = sharedPreferencesManager.restoreSongState();
        is_playing = sharedPreferencesManager.restoreIsPlayState();
        songArrayList = sharedPreferencesManager.restoreSongArrayList();
        if (items != null && songArrayList != null) {
            setData(items);
            setIsPlaying(is_playing);
        }
    }

    private void initViews(BottomSheetDialog view) {
        layout_bottom_player = view.findViewById(R.id.layout_bottom_player);

        relative_player = view.findViewById(R.id.relative_player);
        relative_loading = view.findViewById(R.id.relative_loading);

        layout_player = view.findViewById(R.id.layout_player);
        img_album_song = view.findViewById(R.id.img_album_song);
        txt_title_song = view.findViewById(R.id.txt_title_song);
        txt_artist = view.findViewById(R.id.txt_artist);

        linear_play_pause = view.findViewById(R.id.linear_play_pause);
        img_play_pause = view.findViewById(R.id.img_play_pause);
        linear_next = view.findViewById(R.id.linear_next);

        layout_view_pager = view.findViewById(R.id.layout_view_pager);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

    }

    private void conFigViews() {

    }

    private void onClick() {
        linear_play_pause.setOnClickListener(view -> {
            if (!Helper.isMyServiceRunning(context, MyService.class)) {
                context.startService(new Intent(context, MyService.class));
                is_playing = false;
            }
            if (is_playing) {
                img_play_pause.setImageResource(R.drawable.baseline_play_arrow_24);
                sendActionToService(MyService.ACTION_PAUSE);
            } else {
                img_play_pause.setImageResource(R.drawable.baseline_pause_24);
                sendActionToService(MyService.ACTION_RESUME);
            }
        });
        linear_next.setOnClickListener(view -> sendActionToService(MyService.ACTION_NEXT));
    }

    private void initViewPager() {
        PlayerViewPageAdapter playerViewPageAdapter = new PlayerViewPageAdapter(requireActivity());
        viewPager.setAdapter(playerViewPageAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("TIẾP THEO");
                    break;
                case 1:
                    tab.setText("LỜI BÀI HÁT");
                    break;
                case 2:
                    tab.setText("LIÊN QUAN");
                    break;
            }
        }).attach();
        viewPager.setCurrentItem(tab_layout, false);
    }

    private void setData(Items items) {
        if (items != null) {
            Glide.with(context).load(items.getThumbnail()).placeholder(R.drawable.holder).into(img_album_song);
            txt_title_song.setText(items.getTitle());
            txt_artist.setText(items.getArtistsNames());

            relative_player.setVisibility(View.VISIBLE);
            relative_loading.setVisibility(View.GONE);
        }
    }

    private void setIsPlaying(boolean is_playing) {
        if (is_playing) {
            img_play_pause.setImageResource(R.drawable.baseline_pause_24);
        } else {
            img_play_pause.setImageResource(R.drawable.baseline_play_arrow_24);
        }
    }

    private void setBackground(int color_background, int color_bottomsheet) {

        ColorStateList colorStateList = ColorStateList.valueOf(color_bottomsheet);
        ViewCompat.setBackgroundTintList(layout_view_pager, colorStateList);

        ColorStateList colorStateList1 = ColorStateList.valueOf(color_background);
        ViewCompat.setBackgroundTintList(layout_player, colorStateList1);

        ColorStateList colorStateList2 = ColorStateList.valueOf(color_bottomsheet);
        ViewCompat.setBackgroundTintList(tabLayout, colorStateList2);

        ColorStateList colorStateList3 = ColorStateList.valueOf(color_background);
        ViewCompat.setBackgroundTintList(layout_bottom_player, colorStateList3);
    }

    private void getColorBackground() {
        int[] colors = sharedPreferencesManager.restoreColorBackgrounState();
        int color_background = colors[0];
        int color_bottomsheet = colors[1];
        setBackground(color_background, color_bottomsheet);
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra("action_music_service", action);
        context.startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }
}
