package com.tandev.musichub.fragment.playlist;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsMoreAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.song.SongMoreAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.section_bottom.SectionBottomTypeAdapter;
import com.tandev.musichub.bottomsheet.BottomSheetRenamePlaylistUser;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.section_bottom.DataSectionBottom;
import com.tandev.musichub.model.section_bottom.DataSectionBottomArtist;
import com.tandev.musichub.model.section_bottom.DataSectionBottomPlaylist;
import com.tandev.musichub.model.section_bottom.SectionBottom;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPlaylistFragment extends Fragment {
    private ImageView imageBackground;

    //tool bar
    private View tool_bar;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;

    private NestedScrollView nested_scroll;
    private GridLayout grid_img;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    // Mảng chứa các ImageView
    private ImageView[] imageViews;

    private ProgressBar progress_image;
    private TextView txt_title_playlist;
    private ImageView img_edit_title_playlist;
    private TextView txt_user_name;
    private TextView txt_song_and_time;
    private LinearLayout btn_play_playlist;
    private ArrayList<Items> itemsArrayList;
    private SongMoreAllAdapter songMoreAllAdapter;

    private RelativeLayout relative_loading;
    private RecyclerView rv_playlist;
    private SharedPreferencesManager sharedPreferencesManager;
    private MusicHelper musicHelper;
    private String endCodeId;

    public BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                Items items = (Items) bundle.get("object_song");
                musicHelper.checkIsPlayingPlaylist(items, itemsArrayList, songMoreAllAdapter);

            }
        };
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        musicHelper = new MusicHelper(requireContext(), sharedPreferencesManager);

        initViews(view);
        initConFigViews();
        initAdapter();
        onClick();
        getDataBundle();
    }

    private void getDataBundle() {
        if (getArguments() != null) {
             endCodeId = getArguments().getString("encodeId");
            getPlaylist(endCodeId);
        }
    }


    private void initViews(View view) {
        tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);


        imageBackground = view.findViewById(R.id.imageBackground);
        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll_view);

        grid_img = view.findViewById(R.id.grid_img);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        imageViews = new ImageView[]{imageView1, imageView2, imageView3, imageView4};

        progress_image = view.findViewById(R.id.progress_image);
        img_edit_title_playlist = view.findViewById(R.id.img_edit_title_playlist);
        txt_title_playlist = view.findViewById(R.id.txt_title_playlist);
        txt_title_playlist.setSelected(true);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_song_and_time = view.findViewById(R.id.txt_song_and_time);
        btn_play_playlist = view.findViewById(R.id.btn_play_playlist);
        rv_playlist = view.findViewById(R.id.rv_playlist);
    }

    private void initConFigViews() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_title_toolbar.setText("");
                    relative_header.setBackgroundResource(android.R.color.transparent);
                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
//                    if (playlistViewModel.getPlaylistMutableLiveData().getValue() == null) {
//                        txt_title_toolbar.setText("");
//                    } else {
//                        txt_title_toolbar.setText(playlistViewModel.getPlaylistMutableLiveData().getValue().getData().getTitle());
//                    }
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
                }
            }
        });

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_playlist.setLayoutManager(layoutManager);
        songMoreAllAdapter = new SongMoreAllAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(songMoreAllAdapter);
    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btn_play_playlist.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), MyService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_song", itemsArrayList.get(0));
            bundle.putInt("position_song", 0);
            bundle.putSerializable("song_list", itemsArrayList);
            intent.putExtras(bundle);

            requireContext().startService(intent);
        });
        img_edit_title_playlist.setOnClickListener(view -> {
            BottomSheetRenamePlaylistUser bottomSheetRenamePlaylistUser = new BottomSheetRenamePlaylistUser(requireContext(), requireActivity(), endCodeId);
            bottomSheetRenamePlaylistUser.show(requireActivity().getSupportFragmentManager(), bottomSheetRenamePlaylistUser.getTag());
        });
    }

    private void getPlaylist(String encodeId) {
        DataPlaylist dataPlaylist = sharedPreferencesManager.getPlaylistUserByEncodeId(encodeId);
        if (dataPlaylist != null) {
            updateUI(dataPlaylist);
        }
    }

    private void updateUI(DataPlaylist dataPlaylist) {
        if (dataPlaylist.getSong() != null && dataPlaylist.getSong().getItems() != null) {
            loadImg(dataPlaylist.getSong().getItems());
            txt_song_and_time.setText(convertLongToString(dataPlaylist.getSong().getItems().size(), dataPlaylist.getSong().getTotalDuration()));
        } else {
            Glide.with(requireContext())
                    .load(R.drawable.holder)
                    .into(imageViews[0]);
            imageViews[0].setVisibility(ImageView.VISIBLE);

            for (int i = 1; i < 4; i++) {
                imageViews[i].setVisibility(ImageView.GONE);
            }
            txt_song_and_time.setText("Không có bài hát nào");
            grid_img.setVisibility(View.VISIBLE);
            progress_image.setVisibility(View.GONE);
        }

        txt_title_playlist.setText(dataPlaylist.getTitle());
        txt_user_name.setText(dataPlaylist.getUserName());


        if (dataPlaylist.getSong() != null) {
            ArrayList<Items> arrayList = dataPlaylist.getSong().getItems();
            if (!arrayList.isEmpty()) {
                requireActivity().runOnUiThread(() -> {
                    itemsArrayList = arrayList;
                    songMoreAllAdapter.setFilterList(arrayList);
                    musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), itemsArrayList, songMoreAllAdapter);
                });
            }
        } else {
            rv_playlist.setVisibility(View.GONE);
        }

        relative_loading.setVisibility(View.GONE);
        nested_scroll.setVisibility(View.VISIBLE);
    }

    private void loadImg(ArrayList<Items> itemsArrayList) {
        int numberOfImagesToLoad = Math.min(itemsArrayList.size(), 4);

        for (int i = 0; i < numberOfImagesToLoad; i++) {
            Glide.with(this)
                    .load(itemsArrayList.get(i).getThumbnailM())
                    .placeholder(R.drawable.holder)
                    .into(imageViews[i]);
        }
        if (itemsArrayList.size() < 4) {
            for (int i = 1; i < 4; i++) {
                imageViews[i].setVisibility(ImageView.GONE);
            }
        }

        grid_img.setVisibility(View.VISIBLE);
        progress_image.setVisibility(View.GONE);
    }

    private String convertLongToString(int size, long time) {
        int gio = (int) (time / 3600);
        int phut = (int) ((time % 3600) / 60);

        return size + " bài hát · " + gio + " giờ " + phut + " phút";
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(createBroadcastReceiver(), new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(createBroadcastReceiver());
    }
}
