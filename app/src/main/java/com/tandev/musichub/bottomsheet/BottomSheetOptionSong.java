package com.tandev.musichub.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class BottomSheetOptionSong extends BottomSheetDialogFragment implements PermissionUtils.PermissionCallback {
    private final Context context;
    private final Activity activity;
    private final Items items;
    private BottomSheetDialog bottomSheetDialog;

    private LinearLayout linear_favorite;
    private LinearLayout linear_play_cont;
    private LinearLayout linear_playlist_add;
    private LinearLayout linear_share;
    private LinearLayout linear_download;
    private LinearLayout linear_delete_song_history;
    private LinearLayout linear_album;
    private LinearLayout linear_artist;

    private ImageView img_favorite;
    private TextView txt_title_song, txt_artist, txtDownload;
    private ImageView img_Download;
    private RoundedImageView img_album_song;

    private boolean isSongInFavorite;
    private GetUrlAudioHelper getUrlAudioHelper;
    private DownloadAudio downloadAudio;
    private long downloadID;

    private PermissionUtils permissionUtils;
    private SharedPreferencesManager sharedPreferencesManager;
    private int views = -1;

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


    public BottomSheetOptionSong(Context context, Activity activity, Items items, int views) {
        this.context = context;
        this.activity = activity;
        this.items = items;
        this.views = views;
        this.permissionUtils = new PermissionUtils(context, this);
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_option_song, null);
        bottomSheetDialog.setContentView(view);

        Helper.changeNavigationColor(activity, R.color.gray, true);
        downloadAudio = new DownloadAudio(context);
        getUrlAudioHelper = new GetUrlAudioHelper();
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);

        initViews(bottomSheetDialog);
        initViewFavorite();
        initViewPlayCont();
        initViewAddPlaylist();
        initViewAddMyPlaylist();
        initViewDeleteSongHistory();
        initViewDownload();
        initViewAlbum();
        initViewArtist();

        setInfoSong(items);

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        img_album_song = bottomSheetDialog.findViewById(R.id.img_album_song);
        txt_title_song = bottomSheetDialog.findViewById(R.id.txt_title_song);
        txt_artist = bottomSheetDialog.findViewById(R.id.txt_artist);
        linear_favorite = bottomSheetDialog.findViewById(R.id.linear_favorite);
        img_favorite = bottomSheetDialog.findViewById(R.id.img_favorite);

        linear_play_cont = bottomSheetDialog.findViewById(R.id.linear_play_cont);
        linear_playlist_add = bottomSheetDialog.findViewById(R.id.linear_playlist_add);
        linear_share = bottomSheetDialog.findViewById(R.id.linear_share);
        linear_download = bottomSheetDialog.findViewById(R.id.linear_download);
        linear_delete_song_history = bottomSheetDialog.findViewById(R.id.linear_delete_song_history);
        txtDownload = bottomSheetDialog.findViewById(R.id.txtDownload);
        img_Download = bottomSheetDialog.findViewById(R.id.img_Download);
        linear_album = bottomSheetDialog.findViewById(R.id.linear_album);
        linear_artist = bottomSheetDialog.findViewById(R.id.linear_artist);

        ArrayList<Items> favoriteSongs = sharedPreferencesManager.restoreSongArrayList();
        if (favoriteSongs == null || favoriteSongs.isEmpty()) {
            linear_play_cont.setEnabled(false);
            linear_playlist_add.setEnabled(false);
        }

    }

    private void initViewFavorite() {
        isSongInFavorite = sharedPreferencesManager.isSongInFavorite(items.getEncodeId());
        if (items.getStreamingStatus() == 2) {
            linear_favorite.setVisibility(View.GONE);
        } else {
            linear_favorite.setVisibility(View.VISIBLE);
        }
        if (isSongInFavorite) {
            img_favorite.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            img_favorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }
        linear_favorite.setOnClickListener(view16 -> {
            if (isSongInFavorite) {
                sharedPreferencesManager.deleteSongFromFavorite(items.getEncodeId());
                img_favorite.setImageResource(R.drawable.baseline_favorite_border_24);
                Toast.makeText(context, "Đã xóa bài hát yêu thích!", Toast.LENGTH_SHORT).show();
                isSongInFavorite = false;
            } else {
                sharedPreferencesManager.saveSongArrayListFavorite(items);
                img_favorite.setImageResource(R.drawable.baseline_favorite_24);
                Toast.makeText(context, "Đã thêm bài hát vào danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                isSongInFavorite = true;
            }
        });
    }

    private void initViewPlayCont() {
        linear_play_cont.setOnClickListener(view14 -> {
            if (items.getStreamingStatus() == 2) {
                Toast.makeText(context, "Không thể phát bài hát Premium tiếp theo!", Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferencesManager.addSongAfterCurrentPlaying(items);
                Toast.makeText(context, "Đã thêm bài hát vào phát tiếp theo!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void initViewAddPlaylist() {
        linear_playlist_add.setOnClickListener(view13 -> {
            if (items.getStreamingStatus() == 2) {
                Toast.makeText(context, "Không thể thêm bài hát Premium vào danh sách phát!", Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferencesManager.addSongToEndOfArrayList(items);
                Toast.makeText(context, "Đã thêm bài hát vào danh sách phát!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void initViewAddMyPlaylist() {
        linear_share.setOnClickListener(view15 -> {
            BottomSheetSelectPlaylistUser bottomSheetSelectPlaylistUser = new BottomSheetSelectPlaylistUser(context, activity, items);
            bottomSheetSelectPlaylistUser.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetSelectPlaylistUser.getTag());
            bottomSheetDialog.dismiss();
        });
    }

    private void initViewDeleteSongHistory() {
        if (views == 5) {
            linear_delete_song_history.setVisibility(View.VISIBLE);
        }else {
            linear_delete_song_history.setVisibility(View.GONE);
        }
        linear_delete_song_history.setOnClickListener(v -> {
            sharedPreferencesManager.deleteSongFromHistory(items.getEncodeId());
            Toast.makeText(context, "Đã xóa bài hát khỏi lịch sử nghe!", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });
    }

    private void initViewDownload() {
        if (CheckIsFile.isFileDownloaded(items.getTitle() + " - " + items.getArtistsNames() + ".mp3")) {
            txtDownload.setText("Đã tải xuống");
            img_Download.setImageResource(R.drawable.ic_file_download_done);
        } else {
            txtDownload.setText("Tải xuống");
            img_Download.setImageResource(R.drawable.ic_download);
        }

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
    }

    private void initViewAlbum() {
        if (items.getAlbum() == null) {
            linear_album.setVisibility(View.GONE);
        } else {
            linear_album.setVisibility(View.VISIBLE);
        }
        linear_album.setOnClickListener(view12 -> {

            AlbumFragment albumFragment = new AlbumFragment();
            Bundle bundle = new Bundle();
            bundle.putString("album_endCodeId", items.getAlbum().getEncodeId());

            if (context instanceof MainActivity) {
                ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
            }
            bottomSheetDialog.dismiss();
        });
    }

    private void initViewArtist() {
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
            bottomSheetDialog.dismiss();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setInfoSong(Items items) {
        txt_title_song.setText(items.getTitle());
        txt_artist.setText(items.getArtistsNames());
        Glide.with(context)
                .load(items.getThumbnail())
                .placeholder(R.drawable.holder)
                .into(img_album_song);
    }

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
}
