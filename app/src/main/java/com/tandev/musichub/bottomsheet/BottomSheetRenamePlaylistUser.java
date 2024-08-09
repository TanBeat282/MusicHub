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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tandev.musichub.R;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;


public class BottomSheetRenamePlaylistUser extends BottomSheetDialogFragment {
    private final String encodeId;
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;


    private EditText edt_name_playlist;
    private TextView txt_cancel, txt_confirm;

    private SharedPreferencesManager sharedPreferencesManager;

    public BottomSheetRenamePlaylistUser(Context context, Activity activity, String encodeId) {
        this.context = context;
        this.activity = activity;
        this.encodeId = encodeId;
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

        getPlaylist(encodeId);

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
                boolean success = sharedPreferencesManager.renamePlaylistUserByEncodeId(encodeId, playlistName);
                if (success) {
                    Toast.makeText(context, "Đổi tên playlist thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Đổi tên playlist thất bại", Toast.LENGTH_SHORT).show();
                }
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(context, "Vui lòng nhập tên playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void getPlaylist(String encodeId) {
        DataPlaylist dataPlaylist = sharedPreferencesManager.getPlaylistUserByEncodeId(encodeId);
        if (dataPlaylist != null) {
            updateUI(dataPlaylist);
        }
    }

    private void updateUI(DataPlaylist dataPlaylist) {
        edt_name_playlist.setText(dataPlaylist.getTitle());
    }
}
