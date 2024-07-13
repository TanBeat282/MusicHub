package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.fragment.profile.ProfileFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;


public class BottomSheetOptionPlaylist extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;

    private DataPlaylist dataPlaylist;
    private LinearLayout linear_playlist;
    private RoundedImageView thumbImageView;
    private TextView txt_title_playlist, txt_user_name_playlist;
    private LinearLayout linear_save_playlist, linear_share_playlist;
    private SharedPreferencesManager sharedPreferencesManager;

    public BottomSheetOptionPlaylist(Context context, Activity activity, DataPlaylist dataPlaylist) {
        this.context = context;
        this.activity = activity;
        this.dataPlaylist = dataPlaylist;
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_option_playlist, null);
        bottomSheetDialog.setContentView(view);

        Helper.changeNavigationColor(activity, R.color.gray, true);

        initViews(bottomSheetDialog);
        setDataPlaylist(dataPlaylist);
        onClick();

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        linear_playlist = bottomSheetDialog.findViewById(R.id.linear_playlist);
        thumbImageView = bottomSheetDialog.findViewById(R.id.thumbImageView);
        txt_title_playlist = bottomSheetDialog.findViewById(R.id.txt_title_playlist);
        txt_user_name_playlist = bottomSheetDialog.findViewById(R.id.txt_user_name_playlist);

        linear_save_playlist = bottomSheetDialog.findViewById(R.id.linear_save_playlist);
        linear_share_playlist = bottomSheetDialog.findViewById(R.id.linear_share_playlist);
    }

    private void setDataPlaylist(DataPlaylist dataPlaylist) {
        Glide.with(context)
                .load(dataPlaylist.getThumbnailM())
                .placeholder(R.drawable.holder)
                .into(thumbImageView);
        txt_title_playlist.setText(dataPlaylist.getTitle());
        txt_user_name_playlist.setText(dataPlaylist.getUserName()==null ? "Zing Mp3" : dataPlaylist.getUserName());
    }

    private void onClick() {
        linear_playlist.setOnClickListener(view1 -> {
            PlaylistFragment playlistFragment = new PlaylistFragment();
            Bundle bundle = new Bundle();
            bundle.putString("encodeId", dataPlaylist.getEncodeId());

            if (context instanceof MainActivity) {
                ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
            }
            bottomSheetDialog.dismiss();
        });
        linear_save_playlist.setOnClickListener(view -> {
            sharedPreferencesManager.savePlaylistUser(dataPlaylist);
            Toast.makeText(context, "Đã lưu danh sách phát", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });
    }
}
