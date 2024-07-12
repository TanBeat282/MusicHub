package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.adapter.playlist.SelectPlaylistUserAdapter;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;


public class BottomSheetSelectPlaylistUser extends BottomSheetDialogFragment implements SelectPlaylistUserAdapter.PlaylistItemClickListener {
    private BottomSheetDialog bottomSheetDialog;

    private Items items;
    private final Context context;
    private final Activity activity;
    private LinearLayout linear_create_playlist;
    private RecyclerView rv_select_playlist_user;
    private ArrayList<DataPlaylist> dataPlaylists;
    private SelectPlaylistUserAdapter selectPlaylistUserAdapter;
    private SharedPreferencesManager sharedPreferencesManager;

    public BottomSheetSelectPlaylistUser(Context context, Activity activity, Items items) {
        this.context = context;
        this.activity = activity;
        this.items = items;

    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_select_playlist_user, null);
        bottomSheetDialog.setContentView(view);

        sharedPreferencesManager = new SharedPreferencesManager(context);


        linear_create_playlist = bottomSheetDialog.findViewById(R.id.linear_create_playlist);
        rv_select_playlist_user = bottomSheetDialog.findViewById(R.id.rv_select_playlist_user);


        rv_select_playlist_user.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        selectPlaylistUserAdapter = new SelectPlaylistUserAdapter(dataPlaylists, items, activity, context);
        rv_select_playlist_user.setAdapter(selectPlaylistUserAdapter);
        selectPlaylistUserAdapter.setListener(this);

        linear_create_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCreatePlaylistUser bottomSheetCreatePlaylistUser = new BottomSheetCreatePlaylistUser(context, activity);
                bottomSheetCreatePlaylistUser.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetCreatePlaylistUser.getTag());
            }
        });

        getPlaylistUser();

        return bottomSheetDialog;
    }

    @Override
    public void onPlaylistItemClick(boolean isDismiss) {
        if (isDismiss) {
            Toast.makeText(context, "Đã thêm bài hát vào danh sách phát!", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        }
    }

    private void getPlaylistUser() {
        dataPlaylists = sharedPreferencesManager.restorePlaylistUser();
        if (dataPlaylists != null) {
            selectPlaylistUserAdapter.setFilterList(dataPlaylists);
        }
    }
}
