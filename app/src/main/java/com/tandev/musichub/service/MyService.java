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
    private ArrayList<Items> mSongList;
    private int mPositionSong = -1;
    private boolean isNextMusicLoading = false;
    private SharedPreferencesManager sharedPreferencesManager;
    private final Handler seekBarHandler = new Handler();
    private final Handler stopServiceHandler = new Handler();
    private final GetUrlAudioHelper getUrlAudioHelper = new GetUrlAudioHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
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
            if (bundle.containsKey("song_list")) {
                mSongList = (ArrayList<Items>) bundle.getSerializable("song_list");
            }

            if (song != null) {
                mSong = song;
                mPositionSong = positionSong;
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
                // Lấy bài hát và phát nhạc trước khi seek
                mSongList = sharedPreferencesManager.restoreSongArrayList();
                mPositionSong = sharedPreferencesManager.restoreSongPosition();
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

        int actionMusic = intent.getIntExtra("action_music_service", 0);
        handleActionMusic(actionMusic);

        String action = intent.getAction();
        if ("ACTION_OPEN_EQUALIZER".equals(action)) {
            openEqualizer();
        }
    }

    private void saveSongListAndPosition(Items mSong, int positionSong, ArrayList<Items> mSongList) {
        sharedPreferencesManager.saveSongState(mSong);
        sharedPreferencesManager.saveSongPosition(positionSong);
        sharedPreferencesManager.saveSongArrayList(mSongList);
    }

    private void getSongListAndPosition() {
        ArrayList<Items> songs = sharedPreferencesManager.restoreSongArrayList();
        int position = sharedPreferencesManager.restoreSongPosition();
        Items song = sharedPreferencesManager.restoreSongState();

        mSongList = songs;
        mSong = song;
        mPositionSong = position;
        startMusic(song);
        sendNotificationMedia(song, true);
        saveSongListAndPosition(mSong, position, mSongList);
    }

    private void startMusic(Items song) {
        getColor(song.getThumbnailM());

        getUrlAudioHelper.getSongAudio(song.getEncodeId(), new GetUrlAudioHelper.SongAudioCallback() {
            @Override
            public void onSuccess(SongAudio songAudio) {

                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();

                    // Set audio attributes for MediaPlayer
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
                    // Đăng ký listener khi bài hát phát xong
                    mediaPlayer.setOnCompletionListener(mp -> {
                        if (!isNextMusicLoading) {
                            isNextMusicLoading = true;
                            nextMusic();
                        }
                    });

                }

                if (sharedPreferencesManager.restoreIsRepeatOneState()) {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(sharedPreferencesManager.restoreQualityAudioState() == 1 ? songAudio.getData().getHigh() : sharedPreferencesManager.restoreQualityAudioState() == 2 ? songAudio.getData().getLossless() : songAudio.getData().getLow());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(mp -> {

                            mediaPlayer.start();
                            isPlaying = true;
                            isNextMusicLoading = false;
                            currentSongId = song.getEncodeId();
                            sendActionToActivity(ACTION_START);
                            startUpdatingSeekBar();

                            sharedPreferencesManager.saveSongState(song);
                            sharedPreferencesManager.saveIsPlayState(true);
                            sharedPreferencesManager.saveActionState(MyService.ACTION_START);
                            sharedPreferencesManager.saveSongArrayListHistory(song);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!currentSongId.equals(song.getEncodeId())) {
                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(songAudio.getData().getLow());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(mp -> {

                                mediaPlayer.start();
                                isPlaying = true;
                                isNextMusicLoading = false;
                                currentSongId = song.getEncodeId();
                                sendActionToActivity(ACTION_START);
                                startUpdatingSeekBar();

                                sharedPreferencesManager.saveSongState(song);
                                sharedPreferencesManager.saveIsPlayState(true);
                                sharedPreferencesManager.saveActionState(MyService.ACTION_START);
                                sharedPreferencesManager.saveSongArrayListHistory(song);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
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
                sendActionToActivity(ACTION_CLEAR);
                stopUpdatingSeekBar();
                break;
            case ACTION_NEXT:
                nextMusic();
                break;
            case ACTION_PREVIOUS:
                previousMusic();
                break;
        }
    }

    private void nextMusic() {
        // Lấy bài hát từ danh sách và phát
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset(); // Reset mediaPlayer
                isPlaying = false;
            }

            // Nếu không phải trạng thái lặp lại một bài hát
            if (!sharedPreferencesManager.restoreIsRepeatOneState()) {
                if (sharedPreferencesManager.restoreIsShuffleState()) {
                    // Trạng thái shuffle
                    Random random = new Random();
                    mPositionSong = random.nextInt(mSongList.size()); // Chọn một chỉ số ngẫu nhiên
                } else {
                    // Trạng thái không shuffle
                    mPositionSong++;
                    if (mPositionSong >= mSongList.size()) {
                        mPositionSong = 0;
                    }
                }
            } // Không cần thay đổi mPositionSong nếu đang trong trạng thái lặp lại một bài

        } else {
            mSongList = sharedPreferencesManager.restoreSongArrayList();
            mPositionSong = sharedPreferencesManager.restoreSongPosition();
            // Nếu không phải trạng thái lặp lại một bài hát
            if (!sharedPreferencesManager.restoreIsRepeatOneState()) {
                if (sharedPreferencesManager.restoreIsShuffleState()) {
                    // Trạng thái shuffle
                    Random random = new Random();
                    mPositionSong = random.nextInt(mSongList.size()); // Chọn một chỉ số ngẫu nhiên
                } else {
                    // Trạng thái không shuffle
                    mPositionSong++;
                    if (mPositionSong >= mSongList.size()) {
                        mPositionSong = 0;
                    }
                }
            } // Không cần thay đổi mPositionSong nếu đang trong trạng thái lặp lại một bài

        }
        if (mPositionSong >= 0 && mPositionSong < mSongList.size()) {
            // Lấy bài hát từ danh sách và phát
            mSong = mSongList.get(mPositionSong);
            startMusic(mSong);
            sendNotificationMedia(mSong, true);
            saveSongListAndPosition(mSong, mPositionSong, mSongList);
        }
    }


    private void previousMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset(); // Reset mediaPlayer
                isPlaying = false;
            }

            if (!sharedPreferencesManager.restoreIsRepeatOneState()) {

                if (sharedPreferencesManager.restoreIsShuffleState()) {
                    Random random = new Random();
                    mPositionSong = random.nextInt(mSongList.size()); // Chọn một chỉ số ngẫu nhiên
                } else {
                    if (mPositionSong < 0) {
                        mPositionSong = mSongList.size() - 1;
                    } else {
                        mPositionSong--;
                    }
                }
            }

        } else {
            mSongList = sharedPreferencesManager.restoreSongArrayList();
            mPositionSong = sharedPreferencesManager.restoreSongPosition();
            if (!sharedPreferencesManager.restoreIsRepeatOneState()) {

                if (sharedPreferencesManager.restoreIsShuffleState()) {
                    Random random = new Random();
                    mPositionSong = random.nextInt(mSongList.size()); // Chọn một chỉ số ngẫu nhiên
                } else {
                    if (mPositionSong < 0) {
                        mPositionSong = mSongList.size() - 1;
                    } else {
                        mPositionSong--;
                    }
                }
            }

        }
        if (mPositionSong >= 0 && mPositionSong < mSongList.size()) {
            mSong = mSongList.get(mPositionSong);
            startMusic(mSong);
            sendNotificationMedia(mSong, true);
            saveSongListAndPosition(mSong, mPositionSong, mSongList);
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            sendNotificationMedia(mSong, false);
            sendActionToActivity(ACTION_PAUSE);

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
            sendActionToActivity(ACTION_RESUME);

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
                                        .putString(MediaMetadata.METADATA_KEY_TITLE, mSong.getTitle())
                                        .putString(MediaMetadata.METADATA_KEY_ARTIST, mSong.getArtistsNames())
                                        .build());

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_android_black_24dp)
                                .setContentTitle(song.getTitle())
                                .setContentText(song.getArtistsNames())
                                .setLargeIcon(resource)
                                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                        .setShowActionsInCompactView(0, 1, 2)
                                        .setMediaSession(mediaSessionCompat.getSessionToken()));

                        // Tạo Intent và đính kèm dữ liệu
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("is_click_notification", true);
                        intent.putExtra("from_notification", true);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                        if (isPlaying) {
                            // Cập nhật trạng thái phát nhạc và hiển thị các nút tương ứng
                            notificationBuilder
                                    .addAction(R.drawable.baseline_skip_previous_24, "Previous", getPendingIntent(getApplicationContext(), ACTION_PREVIOUS)) // #0
                                    .addAction(R.drawable.baseline_pause_24, "Pause", getPendingIntent(getApplicationContext(), ACTION_PAUSE)) // #1
                                    .addAction(R.drawable.baseline_skip_next_24, "Next", getPendingIntent(getApplicationContext(), ACTION_NEXT)); // #2
                            notificationBuilder.setContentIntent(pendingIntent);
                        } else {
                            // Cập nhật trạng thái dừng và hiển thị nút play
                            notificationBuilder
                                    .addAction(R.drawable.baseline_skip_previous_24, "Previous", getPendingIntent(getApplicationContext(), ACTION_PREVIOUS)) // #0
                                    .addAction(R.drawable.baseline_play_arrow_24, "Play", getPendingIntent(getApplicationContext(), ACTION_RESUME)) // #1
                                    .addAction(R.drawable.baseline_skip_next_24, "Next", getPendingIntent(getApplicationContext(), ACTION_NEXT)); // #2
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

    private void openEqualizer() {
        if (mediaPlayer != null) {
            int audioSessionId = mediaPlayer.getAudioSessionId();
            if (audioSessionId != AudioEffect.ERROR_BAD_VALUE) {
                Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, audioSessionId);
                intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
                intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Thêm cờ này để mở hoạt động từ Service
                startActivity(intent);
            } else {
                // Thiết bị không hỗ trợ Equalizer
                Toast.makeText(this, "Equalizer không được hỗ trợ trên thiết bị này", Toast.LENGTH_SHORT).show();
            }
        } else {
            // MediaPlayer chưa được khởi tạo
            Toast.makeText(this, "Trình phát nhạc chưa được khởi tạo", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;

        }
        stopUpdatingSeekBar();
    }

    private void startUpdatingSeekBar() {
        seekBarHandler.removeCallbacks(updateSeekBar);
        seekBarHandler.postDelayed(updateSeekBar, 0);
    }

    private void stopUpdatingSeekBar() {
        seekBarHandler.removeCallbacks(updateSeekBar);
    }

    private void sendSeekBarUpdate(int currentTime, int totalTime) {
        Intent intent = new Intent("send_seekbar_update");
        intent.putExtra("current_time", currentTime);
        intent.putExtra("total_time", totalTime);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

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

    private void sendActionToActivity(int action) {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", mSong);
        bundle.putBoolean("status_player", isPlaying);
        bundle.putInt("action_music", action);

        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendExpandToActivity() {
        Intent intent = new Intent("send_expand_to_activity");
        intent.putExtra("is_expand", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
