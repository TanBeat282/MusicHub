package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.profile.ProfileFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;


public class BottomSheetCreatePlaylistUser extends BottomSheetDialogFragment {
    private Items items;
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;


    private EditText edt_name_playlist;
    private TextView txt_cancel, txt_confirm;

    private SharedPreferencesManager sharedPreferencesManager;

    public BottomSheetCreatePlaylistUser(Context context, Activity activity, Items items) {
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
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_create_playlist_user, null);
        bottomSheetDialog.setContentView(view);

        Helper.changeNavigationColor(activity, R.color.gray, true);
        sharedPreferencesManager = new SharedPreferencesManager(context);

        initViews(bottomSheetDialog);
        onClick();

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        edt_name_playlist = bottomSheetDialog.findViewById(R.id.edt_name_playlist);
        txt_cancel = bottomSheetDialog.findViewById(R.id.txt_cancel);
        txt_confirm = bottomSheetDialog.findViewById(R.id.txt_confirm);
    }

    private void onClick() {
        txt_cancel.setOnClickListener(view -> bottomSheetDialog.dismiss());
        txt_confirm.setOnClickListener(view -> {
            String playlistName = edt_name_playlist.getText().toString().trim();
            if (!playlistName.isEmpty()) {
                DataPlaylist dataPlaylist = new DataPlaylist();
                dataPlaylist.setTitle(playlistName);
                String encodeId = sharedPreferencesManager.savePlaylistUserReturnEncodeId(dataPlaylist);
                sharedPreferencesManager.addSongToPlaylistByEncodeId(encodeId, items);
                Toast.makeText(context, "Tạo playlist thành công", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(context, "Vui lòng nhập tên playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
