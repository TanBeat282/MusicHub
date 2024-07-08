package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.constants.PermissionConstants;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.CheckIsFile;
import com.tandev.musichub.helper.uliti.DownloadAudio;
import com.tandev.musichub.helper.uliti.GetUrlAudioHelper;
import com.tandev.musichub.helper.uliti.PermissionUtils;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.song.SongAudio;
import com.tandev.musichub.model.song.SongDetail;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class BottomSheetInfoSong extends BottomSheetDialogFragment implements PermissionUtils.PermissionCallback{
    private final Context context;
    private final Activity activity;
    private final Items items;
    private BottomSheetDialog bottomSheetDialog;
    private TextView txtTile, txtArtist, txtDownload;
    private ImageView img_Download;
    private RoundedImageView img_album_song;

    private GetUrlAudioHelper getUrlAudioHelper;
    private DownloadAudio downloadAudio;
    private long downloadID;
    private PermissionUtils permissionUtils;

    public BottomSheetInfoSong(Context context, Activity activity, Items items) {
        this.context = context;
        this.activity = activity;
        this.items = items;
        this.permissionUtils = new PermissionUtils(context, this);
    }
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_info, null);
        bottomSheetDialog.setContentView(view);

        Helper.changeNavigationColor(activity, R.color.gray, true);
        downloadAudio = new DownloadAudio(context);
        getUrlAudioHelper = new GetUrlAudioHelper();


        img_album_song = bottomSheetDialog.findViewById(R.id.img_album_song);
        txtTile = bottomSheetDialog.findViewById(R.id.txtTile);
        txtArtist = bottomSheetDialog.findViewById(R.id.txtArtist);

        LinearLayout linear_play_cont = bottomSheetDialog.findViewById(R.id.linear_play_cont);
        LinearLayout linear_playlist_add = bottomSheetDialog.findViewById(R.id.linear_playlist_add);
        LinearLayout linear_share = bottomSheetDialog.findViewById(R.id.linear_share);
        LinearLayout linear_download = bottomSheetDialog.findViewById(R.id.linear_download);
        txtDownload = bottomSheetDialog.findViewById(R.id.txtDownload);
        img_Download = bottomSheetDialog.findViewById(R.id.img_Download);
        LinearLayout linear_album = bottomSheetDialog.findViewById(R.id.linear_album);
        LinearLayout linear_artist = bottomSheetDialog.findViewById(R.id.linear_artist);
        LinearLayout linear_eq = bottomSheetDialog.findViewById(R.id.linear_eq);

        assert linear_download != null;
        linear_download.setOnClickListener(v -> {
            if (CheckIsFile.isFileDownloaded(items.getTitle() + " - " + items.getArtistsNames() + ".mp3")) {
                if (CheckIsFile.deleteFileIfExists(items.getTitle() + " - " + items.getArtistsNames() + ".mp3")) {
                    txtDownload.setText("Tải xuống");
                    img_Download.setImageResource(R.drawable.ic_download);
                    Toast.makeText(context, "Xóa bài hát tải xuống thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi bất định, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                permissionStorage();
            }
        });

        linear_artist.setOnClickListener(view1 -> {
            if (items.getArtists().size() >= 2) {
                BottomSheetSelectArtist bottomSheetSelectArtist = new BottomSheetSelectArtist(context, activity, items.getArtists());
                bottomSheetSelectArtist.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetSelectArtist.getTag());
            } else {
                ArtistFragment artistFragment = new ArtistFragment();
                Bundle bundle = new Bundle();
                bundle.putString("alias", items.getArtists().get(0).getAlias());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(artistFragment, bundle);
                }

            }

            sendExpandToActivity();
            bottomSheetDialog.dismiss();
        });

        linear_album.setOnClickListener(view12 -> {

            AlbumFragment albumFragment = new AlbumFragment();
            Bundle bundle = new Bundle();
            bundle.putString("album_endCodeId", items.getAlbum().getEncodeId());

            if (context instanceof MainActivity) {
                ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
            }
            sendExpandToActivity();
            bottomSheetDialog.dismiss();
        });
        linear_eq.setOnClickListener(view13 -> {
            Intent intent = new Intent(context, MyService.class);
            intent.setAction("ACTION_OPEN_EQUALIZER");
            context.startService(intent);
        });


        if (items.getAlbum() == null) {
            linear_album.setVisibility(View.GONE);
        } else {
            linear_album.setVisibility(View.VISIBLE);
        }

        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);
        setInfoSong(items);

        return bottomSheetDialog;
    }

    @SuppressLint("SetTextI18n")
    private void setInfoSong(Items items) {
        txtTile.setText(items.getTitle());
        txtArtist.setText(items.getArtistsNames());
        Glide.with(context)
                .load(items.getThumbnail())
                .placeholder(R.drawable.holder)
                .into(img_album_song);

        if (CheckIsFile.isFileDownloaded(items.getTitle() + " - " + items.getArtistsNames() + ".mp3")) {
            txtDownload.setText("Đã tải xuống");
            img_Download.setImageResource(R.drawable.ic_file_download_done);
        } else {
            txtDownload.setText("Tải xuống");
            img_Download.setImageResource(R.drawable.ic_download);
        }
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadID) {
                Toast.makeText(context, "Tải thành công", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        }
    };

    private void permissionStorage() {
        permissionUtils.checkAndRequestPermissions(PermissionConstants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionConstants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (items.getStreamingStatus() == 2) {
                Toast.makeText(context, "Không thể tải bài hát Premium!", Toast.LENGTH_SHORT).show();
            } else {
                getUrlAudioHelper.getSongAudio(items.getEncodeId(), new GetUrlAudioHelper.SongAudioCallback() {
                    @Override
                    public void onSuccess(SongAudio songAudio) {
                        Toast.makeText(context, "Đang tải bài hát!", Toast.LENGTH_SHORT).show();
                        downloadAudio.downloadAudio(songAudio.getData().getLow(), items.getTitle() + " - " + items.getArtistsNames());
                        downloadID = downloadAudio.getDownloadID();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(context, "Lỗi bất định, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PermissionConstants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            Toast.makeText(context, "Quyền bị từ chối. Không thể tải bài hát.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unregisterReceiver(onDownloadComplete);
    }
    private void sendExpandToActivity() {
        Intent intent = new Intent("send_expand_to_activity");
        intent.putExtra("is_expand", false);
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
    }
}
