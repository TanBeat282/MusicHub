package com.tandev.musichub.service;

import static com.tandev.musichub.application.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.uliti.GetUrlAudioHelper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.preview_premium.PreviewPremium;
import com.tandev.musichub.model.song.SongAudio;
import com.tandev.musichub.receiver.MyReceiver;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    public static final int ACTION_START = 4;
    public static final int ACTION_NEXT = 5;
    public static final int ACTION_PREVIOUS = 6;
    private boolean isPlaying = false;
    private Items mSong;
    private String currentSongId = "";
    private boolean isPlayingBumper = false; // Flag to check if bumper audio is playing

    private SharedPreferencesManager sharedPreferencesManager;
    private final Handler seekBarHandler = new Handler();
    private final Handler stopServiceHandler = new Handler();
    private final GetUrlAudioHelper getUrlAudioHelper = new GetUrlAudioHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        mediaPlayer.setOnCompletionListener(mp -> {
            // Kiểm tra nếu thời gian hiện tại gần thời gian kết thúc bài hát
            if (mediaPlayer != null) {
                if (mediaPlayer.getDuration() > 0 && mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration() - 100) {
                    if (isPlayingBumper) {
                        // If bumper audio is playing, play the next song
                        isPlayingBumper = false;
                        nextMusic();
                    } else if (mSong.getStreamingStatus() == 2) {
                        getUrlAudioHelper.getPremiumSongAudio(mSong.getEncodeId(), new GetUrlAudioHelper.PremiumSongAudioCallback() {
                            @Override
                            public void onSuccess(PreviewPremium previewPremium) {
                                if (previewPremium.getData() != null && previewPremium.getData().getBumperAudio() != null) {
                                    try {
                                        mediaPlayer.reset();
                                        mediaPlayer.setDataSource(previewPremium.getData().getBumperAudio().getLink());
                                        mediaPlayer.prepareAsync();
                                        mediaPlayer.setOnPreparedListener(mp -> {
                                            mediaPlayer.start();
                                            isPlayingBumper = true; // Set the flag to true when bumper audio is playing
                                        });
                                    } catch (IOException e) {
                                        nextMusic();
                                    }
                                } else {
                                    nextMusic();
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                nextMusic();
                            }
                        });
                    } else {
                        nextMusic();
                    }
                }
            }
        });
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            sendBufferingUpdate(percent);
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    private void handleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Items song = (Items) bundle.getSerializable("object_song");
            int positionSong = bundle.getInt("position_song");
            ArrayList<Items> mSongList = (ArrayList<Items>) bundle.getSerializable("song_list");

            if (song != null) {
                mSong = song;
                startMusic(song);
                sendNotificationMedia(song, true);
                saveSongListAndPosition(mSong, positionSong, mSongList);
            }
        } else {
            getSongListAndPosition();
        }

        if (intent.hasExtra("seek_to_position")) {
            int seekToPosition = intent.getIntExtra("seek_to_position", 0);

            // Đảm bảo rằng MediaPlayer đã được khởi tạo
            if (mediaPlayer == null) {
                mSong = sharedPreferencesManager.restoreSongState();
                startMusic(mSong);
            }

            if (mediaPlayer != null) {
                // Seek đến vị trí mới
                mediaPlayer.seekTo(seekToPosition);
            }

            if (!isPlaying) {
                handleActionMusic(ACTION_RESUME);
            }
        }

        if (intent.hasExtra("action_music_service")) {
            int actionMusic = intent.getIntExtra("action_music_service", 0);
            handleActionMusic(actionMusic);
        }
    }

    private void saveSongListAndPosition(Items mSong, int positionSong, ArrayList<Items> mSongList) {
        sharedPreferencesManager.saveSongState(mSong);
        sharedPreferencesManager.saveSongPosition(positionSong);
        sharedPreferencesManager.saveSongArrayList(mSongList);
    }

    private void getSongListAndPosition() {
        Items song = sharedPreferencesManager.restoreSongState();
        mSong = song;
        startMusic(song);
        sendNotificationMedia(song, true);
    }

    private void startMusic(Items song) {
        getColor(song.getThumbnailM());

        if (song.getStreamingStatus() == 2) {
            getUrlAudioHelper.getPremiumSongAudio(song.getEncodeId(), new GetUrlAudioHelper.PremiumSongAudioCallback() {
                @Override
                public void onSuccess(PreviewPremium previewPremium) {
                    if (sharedPreferencesManager.restoreIsRepeatOneState()) {
                        if (previewPremium.getData() != null) {
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(previewPremium.getData().getLink());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(mp -> {

                                    mediaPlayer.start();
                                    isPlaying = true;
                                    currentSongId = song.getEncodeId();
                                    sendActionToActivity(mSong, isPlaying, ACTION_START);
                                    startUpdatingSeekBar();

                                    sharedPreferencesManager.saveSongState(song);
                                    sharedPreferencesManager.saveIsPlayState(true);
                                    sharedPreferencesManager.saveActionState(MyService.ACTION_START);
                                    sharedPreferencesManager.saveSongArrayListHistory(song);
                                });
                            } catch (IOException e) {
                                sendIsSongPremium();
                                nextMusic();
                            }
                        } else {
                            sendIsSongPremium();
                            nextMusic();
                        }
                    } else {
                        if (!currentSongId.equals(song.getEncodeId())) {
                            if (previewPremium.getData() != null) {
                                try {
                                    mediaPlayer.reset();
                                    mediaPlayer.setDataSource(previewPremium.getData().getLink());
                                    mediaPlayer.prepareAsync();
                                    mediaPlayer.setOnPreparedListener(mp -> {

                                        mediaPlayer.start();
                                        isPlaying = true;
                                        currentSongId = song.getEncodeId();
                                        sendActionToActivity(mSong, isPlaying, ACTION_START);
                                        startUpdatingSeekBar();

                                        sharedPreferencesManager.saveSongState(song);
                                        sharedPreferencesManager.saveIsPlayState(true);
                                        sharedPreferencesManager.saveActionState(MyService.ACTION_START);
                                        sharedPreferencesManager.saveSongArrayListHistory(song);
                                    });
                                } catch (IOException e) {
                                    sendIsSongPremium();
                                    nextMusic();
                                }
                            } else {
                                sendIsSongPremium();
                                nextMusic();
                            }
                        } else {
                            sendExpandToActivity();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } else {
            getUrlAudioHelper.getSongAudio(song.getEncodeId(), new GetUrlAudioHelper.SongAudioCallback() {
                @Override
                public void onSuccess(SongAudio songAudio) {
                    if (sharedPreferencesManager.restoreIsRepeatOneState()) {
                        if (songAudio.getData() != null) {
                            try {
                                mediaPlayer.reset();
                                mediaPlayer.setDataSource(sharedPreferencesManager.restoreQualityAudioState() == 1 ? songAudio.getData().getHigh() : sharedPreferencesManager.restoreQualityAudioState() == 2 ? songAudio.getData().getLossless() : songAudio.getData().getLow());
                                mediaPlayer.prepareAsync();
                                mediaPlayer.setOnPreparedListener(mp -> {

                                    mediaPlayer.start();
                                    isPlaying = true;
                                    currentSongId = song.getEncodeId();
                                    sendActionToActivity(mSong, isPlaying, ACTION_START);
                                    startUpdatingSeekBar();

                                    sharedPreferencesManager.saveSongState(song);
                                    sharedPreferencesManager.saveIsPlayState(true);
                                    sharedPreferencesManager.saveActionState(MyService.ACTION_START);
                                    sharedPreferencesManager.saveSongArrayListHistory(song);
                                });
                            } catch (IOException e) {
                                sendIsSongPremium();
                                nextMusic();
                            }
                        } else {
                            sendIsSongPremium();
                            nextMusic();
                        }
                    } else {
                        if (!currentSongId.equals(song.getEncodeId())) {
                            if (songAudio.getData() != null) {
                                try {
                                    mediaPlayer.reset();
                                    mediaPlayer.setDataSource(songAudio.getData().getLow());
                                    mediaPlayer.prepareAsync();
                                    mediaPlayer.setOnPreparedListener(mp -> {

                                        mediaPlayer.start();
                                        isPlaying = true;
                                        currentSongId = song.getEncodeId();
                                        sendActionToActivity(mSong, isPlaying, ACTION_START);
                                        startUpdatingSeekBar();

                                        sharedPreferencesManager.saveSongState(song);
                                        sharedPreferencesManager.saveIsPlayState(true);
                                        sharedPreferencesManager.saveActionState(MyService.ACTION_START);
                                        sharedPreferencesManager.saveSongArrayListHistory(song);
                                    });
                                } catch (IOException e) {
                                    sendIsSongPremium();
                                    nextMusic();
                                }
                            } else {
                                sendIsSongPremium();
                                nextMusic();
                            }
                        } else {
                            sendExpandToActivity();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    sendIsSongPremium();
                    nextMusic();
                }
            });
        }
    }

    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                pauseMusic();
                stopUpdatingSeekBar();
                break;
            case ACTION_RESUME:
                resumeMusic();
                startUpdatingSeekBar();
                break;
            case ACTION_CLEAR:
                stopSelf();
                sendActionToActivity(null, isPlaying, ACTION_CLEAR);
                stopUpdatingSeekBar();
                sharedPreferencesManager.deleteSongState();
                sharedPreferencesManager.deleteSongPosition();
                sharedPreferencesManager.deleteSongArrayList();
                break;
            case ACTION_NEXT:
                nextMusic();
                break;
            case ACTION_PREVIOUS:
                previousMusic();
                break;
        }
    }

    private void handleNextOrPreviousMusic(boolean isNext) {
        if (sharedPreferencesManager.restoreIsRepeatOneState()) {
            // Repeat One: Không thay đổi mPositionSong, phát lại bài hát hiện tại
            startMusic(mSong);
        } else {
            int mPositionSong = -1;
            if (sharedPreferencesManager.restoreIsShuffleState()) {
                // Shuffle: Chọn một chỉ số ngẫu nhiên
                Random random = new Random();
                mPositionSong = random.nextInt(sharedPreferencesManager.restoreSongArrayList().size());
            } else {
                // Không Shuffle: Tiếp tục theo thứ tự
                if (isNext) {
                    mPositionSong = sharedPreferencesManager.restoreSongPosition() + 1;
                    if (mPositionSong >= sharedPreferencesManager.restoreSongArrayList().size()) {
                        mPositionSong = 0;
                    }
                } else {
                    mPositionSong = sharedPreferencesManager.restoreSongPosition() - 1;
                    if (mPositionSong < 0) {
                        mPositionSong = sharedPreferencesManager.restoreSongArrayList().size() - 1;
                    }
                }
            }

            if (mPositionSong >= 0 && mPositionSong < sharedPreferencesManager.restoreSongArrayList().size()) {
                mSong = sharedPreferencesManager.restoreSongArrayList().get(mPositionSong);
                startMusic(mSong);
                sendNotificationMedia(mSong, true);
                saveSongListAndPosition(mSong, mPositionSong, sharedPreferencesManager.restoreSongArrayList());
            }
        }
    }

    private void nextMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                isPlaying = false;
            }
            // Xử lý theo trạng thái Repeat và Shuffle
            handleNextOrPreviousMusic(true);
        } else {
            handleNextOrPreviousMusic(true);
        }
    }

    private void previousMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                isPlaying = false;
            }
            // Xử lý theo trạng thái Repeat và Shuffle
            handleNextOrPreviousMusic(false);
        } else {
            handleNextOrPreviousMusic(false);
        }

    }

    private void pauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            sendNotificationMedia(mSong, false);
            sendActionToActivity(mSong, isPlaying, ACTION_PAUSE);

            sharedPreferencesManager.saveIsPlayState(false);
            sharedPreferencesManager.saveActionState(MyService.ACTION_PAUSE);

            stopServiceHandler.removeCallbacks(stopServiceRunnable);
            stopServiceHandler.postDelayed(stopServiceRunnable, 15 * 60 * 1000);
        }
    }

    private void resumeMusic() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            sendNotificationMedia(mSong, true);
            sendActionToActivity(mSong, isPlaying, ACTION_RESUME);

            sharedPreferencesManager.saveIsPlayState(true);
            sharedPreferencesManager.saveActionState(MyService.ACTION_RESUME);

            stopServiceHandler.removeCallbacks(stopServiceRunnable);
        }
    }

    private void sendNotificationMedia(Items song, boolean isPlaying) {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(song.getThumbnail())
                .into(new CustomTarget<Bitmap>() {
                    @SuppressLint("ForegroundServiceType")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(getApplicationContext(), "tag");

                        mediaSessionCompat.setMetadata(
                                new MediaMetadataCompat.Builder()
                                        .putString(MediaMetadata.METADATA_KEY_TITLE, song.getTitle())
                                        .putString(MediaMetadata.METADATA_KEY_ARTIST, song.getArtistsNames())
                                        .build());

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_android_black_24dp)
                                .setContentTitle(song.getTitle())
                                .setContentText(song.getArtistsNames())
                                .setLargeIcon(resource)
                                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                        .setShowActionsInCompactView(0)
                                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                                .setPriority(NotificationCompat.PRIORITY_LOW);  // Thay đổi priority

                        // Tạo Intent và đính kèm dữ liệu
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("is_click_notification", true);
                        intent.putExtra("from_notification", true);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                        if (isPlaying) {
                            notificationBuilder
                                    .addAction(R.drawable.baseline_pause_24, "Pause", getPendingIntent(getApplicationContext(), ACTION_PAUSE));
                            notificationBuilder.setContentIntent(pendingIntent);
                        } else {
                            notificationBuilder
                                    .addAction(R.drawable.baseline_play_arrow_24, "Play", getPendingIntent(getApplicationContext(), ACTION_RESUME));
                            notificationBuilder.setContentIntent(pendingIntent);
                        }

                        Notification notification = notificationBuilder.build();
                        startForeground(1, notification);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Implement if needed
                    }
                });
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void getColor(String urlImage) {
        if (urlImage.equals(Constants.URL_IMAGE_DEFAULT)) {
            int blackColor = ContextCompat.getColor(this, R.color.black);
            int grayColor = ContextCompat.getColor(this, R.color.colorPrimaryText);
            sharedPreferencesManager.saveColorBackgroundState(blackColor, grayColor);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(urlImage)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, @NonNull Target<Bitmap> target, boolean isFirstResource) {
                            int blackColor = ContextCompat.getColor(getApplicationContext(), R.color.black);
                            int grayColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryText);
                            sharedPreferencesManager.saveColorBackgroundState(blackColor, grayColor);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Bitmap resource, @NonNull Object model, Target<Bitmap> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            Palette.from(resource).generate(palette -> {
                                assert palette != null;
                                int vibrantDarkColor = palette.getDarkMutedColor(0); // Lấy màu Vibrant Dark
                                if (vibrantDarkColor == 0) {
                                    vibrantDarkColor = palette.getDominantColor(0);
                                }
                                float[] hsv = new float[3];
                                Color.colorToHSV(vibrantDarkColor, hsv);
                                hsv[2] *= 1.2f;
                                int brighterColor = Color.HSVToColor(hsv);
                                sharedPreferencesManager.saveColorBackgroundState(vibrantDarkColor, brighterColor);
                            });
                            return false;
                        }
                    })
                    .submit();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
        stopUpdatingSeekBar();
    }

    private void startUpdatingSeekBar() {
        seekBarHandler.removeCallbacks(updateSeekBar);
        seekBarHandler.postDelayed(updateSeekBar, 0);
    }

    private void stopUpdatingSeekBar() {
        seekBarHandler.removeCallbacks(updateSeekBar);
    }

    private final Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && isPlaying) {
                int currentTime = mediaPlayer.getCurrentPosition();
                int totalTime = mediaPlayer.getDuration();
                // Cập nhật SeekBar với thời gian hiện tại
                sendSeekBarUpdate(currentTime, totalTime);

                // Lên lịch để cập nhật lại SeekBar sau một khoảng thời gian nhỏ (ví dụ: 1000ms)
                seekBarHandler.postDelayed(this, 100);
            }
        }
    };

    private final Runnable stopServiceRunnable = this::stopSelf;

    private void sendActionToActivity(Items mSong, boolean isPlaying, int action) {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", mSong);
        bundle.putBoolean("status_player", isPlaying);
        bundle.putInt("action_music", action);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendSeekBarUpdate(int currentTime, int totalTime) {
        Intent intent = new Intent("send_seekbar_update");
        intent.putExtra("current_time", currentTime);
        intent.putExtra("total_time", totalTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    private void sendExpandToActivity() {
        Intent intent = new Intent("send_expand_to_activity");
        intent.putExtra("is_expand", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendBufferingUpdate(int percent) {
        Intent intent = new Intent("send_load_music_update");
        intent.putExtra("buffering", percent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendIsSongPremium() {
        Intent intent = new Intent("send_is_song_premium");
        intent.putExtra("is_song_premium", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}