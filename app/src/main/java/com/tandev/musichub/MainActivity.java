package com.tandev.musichub;

import static com.tandev.musichub.service.MyService.ACTION_CLEAR;
import static com.tandev.musichub.service.MyService.ACTION_NEXT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.slider.Slider;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.bottomsheet.BottomSheetInfoSong;
import com.tandev.musichub.bottomsheet.BottomSheetPlayer;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.fragment.home.HomeFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.SwipeGestureListener;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.song.SongDetail;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private SharedPreferencesManager sharedPreferencesManager;
    private LinearLayout layout_bottom_player_main;
    private View layoutPlayerBottom, view_player;
    private RelativeLayout relative_player;

    //bottom player
    private RoundedImageView img_album_song;
    private TextView txt_title, txt_artist;
    private LinearLayout linear_play_pause, linear_close;
    private ImageView img_play_pause;
    private LinearProgressIndicator progress_indicator;

    private ImageView image_back, image_more;
    private TextView txt_play_from;

    private ImageView image_background;
    private RelativeLayout relative_image_song;
    private RoundedImageView image_song;
    private ProgressBar progress_image;

    // tua 5s
    private LinearLayout linear_detail_song;
    private LinearLayout linear_previous_five_second;
    private LinearLayout linear_next_five_second;

    //
    private LinearLayout linear_info;
    private TextView txt_title_song, txt_artist_song;

    private SeekBar seek_bar;
    private LinearLayout linear_controller;
    private TextView txt_current_time, txt_total_time;
    private LottieAnimationView lottie_play_pause;
    private ImageButton img_btn_next, img_btn_repeat, img_btn_previous, img_btn_shuffle;

    //bottom sheet player
    private LinearLayout linear_bottom;
    private TextView txt_continue_song, txt_lyric_song, txt_relate_song;


    //data
    private Items items;
    private boolean isPlaying;
    private int action;
    private SongDetail songDetail;
    private int currentTime = 0;
    private int totalTime = 0;
    private GestureDetector gestureDetector;


    public BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                items = (Items) bundle.get("object_song");
                isPlaying = bundle.getBoolean("status_player");
                action = bundle.getInt("action_music");
                //bottom player
                handleLayoutMusic(action);

                if (action == MyService.ACTION_START || action == MyService.ACTION_NEXT || action == MyService.ACTION_PREVIOUS) {
                    //player
                    getSongDetail(items.getEncodeId());
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        getColorBackground();
                    }

                }

            }
        };
    }

    public BroadcastReceiver createSeekBarUpdateReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currentTime = intent.getIntExtra("current_time", 0);
                totalTime = intent.getIntExtra("total_time", 0);
                updateIndicator(currentTime, totalTime);

                //player
                updateSeekBar(currentTime, totalTime);
            }
        };
    }

    public BroadcastReceiver createExpandReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean is_expand = intent.getBooleanExtra("is_expand", false);
                showBottomSheetNowPlaying(is_expand);
            }
        };
    }

    public BroadcastReceiver progressIndicatorReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int percent = intent.getIntExtra("buffering", 0);

            }
        };
    }

    public BroadcastReceiver isSongPremiumReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean is_song_premium = intent.getBooleanExtra("is_song_premium", false);
                if (is_song_premium) {
                    Toast.makeText(MainActivity.this, "Đã bỏ qua bài hát premium!", Toast.LENGTH_SHORT).show();

                }

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferencesManager = new SharedPreferencesManager(this);
        gestureDetector = new GestureDetector(this, new SwipeGestureListener(this));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_player, new HomeFragment())
                    .commit();
        }
        //main
        initViewsMain();

        //bottom main
        initViewBottomMain();
        conFigViewsBottomMain();
        onClickBottomMain();

        //player
        initViewPlayer();
        conFigViewsPlayer();
        onClickPlayer();

        //bottom sheet player
        initViewBottomSheetPlayer();
        conFigViewsBottomSheetPlayer();
        onClickBottomSheetPlayer();

        //data
        getDataSharedPreferencesSong();
    }

    private void initViewsMain() {
        //main
        layout_bottom_player_main = findViewById(R.id.layout_bottom_player_main);

        //layout bottom player
        layoutPlayerBottom = findViewById(R.id.layoutPlayerBottom);

        // view main
        view_player = findViewById(R.id.view_player);

        //layout player
        relative_player = findViewById(R.id.relative_player);

        //bottom player
        bottomSheetBehavior = BottomSheetBehavior.from(layout_bottom_player_main);
    }

    private void showBottomSheetNowPlaying(boolean isExpanded) {
        if (isExpanded) {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        } else {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
    }

    //bottom player
    private void initViewBottomMain() {
        View layoutPlayerBottom = layout_bottom_player_main.findViewById(R.id.layoutPlayerBottom);
        img_album_song = layoutPlayerBottom.findViewById(R.id.img_album_song);
        txt_title = layoutPlayerBottom.findViewById(R.id.txt_title_song);
        txt_title.setSelected(true);
        txt_artist = layoutPlayerBottom.findViewById(R.id.txt_artist);
        linear_play_pause = layoutPlayerBottom.findViewById(R.id.linear_play_pause);
        img_play_pause = layoutPlayerBottom.findViewById(R.id.img_play_pause);
        linear_close = layoutPlayerBottom.findViewById(R.id.linear_close);
        progress_indicator = layoutPlayerBottom.findViewById(R.id.progress_indicator);
    }

    private void conFigViewsBottomMain() {
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        layoutPlayerBottom.setVisibility(View.VISIBLE);
                        relative_player.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        layoutPlayerBottom.setVisibility(View.GONE);
                        relative_player.setVisibility(View.VISIBLE);

                        break;
                    default:
                        // Xử lý các trạng thái khác nếu cần thiết
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.5) {
                    layoutPlayerBottom.setVisibility(View.GONE);
                    relative_player.setVisibility(View.VISIBLE);
                    getColorBackground();
                } else {
                    layoutPlayerBottom.setVisibility(View.VISIBLE);
                    relative_player.setVisibility(View.GONE);
                    Helper.changeStatusBarColor(MainActivity.this, R.color.black);
                    Helper.changeNavigationColor(MainActivity.this, R.color.gray, false);
                }
            }
        });

        layout_bottom_player_main.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

    }

    private void getDataSharedPreferencesSong() {
        items = sharedPreferencesManager.restoreSongState();
        isPlaying = sharedPreferencesManager.restoreIsPlayState();
        action = sharedPreferencesManager.restoreActionState();
        if (!Helper.isMyServiceRunning(this, MyService.class)) {
            isPlaying = false;
        }
        if (items != null) {
            layout_bottom_player_main.setVisibility(View.VISIBLE);
            view_player.setVisibility(View.VISIBLE);
            setDataBottomSheet(items);
            getSongDetail(items.getEncodeId());
            getColorBackground();
        } else {
            layout_bottom_player_main.setVisibility(View.GONE);
            view_player.setVisibility(View.GONE);
        }
    }

    private void setDataBottomSheet(Items items) {
        if (!(MainActivity.this).isDestroyed()) {
            Glide.with(this)
                    .load(items.getThumbnail())
                    .placeholder(R.drawable.holder)
                    .into(img_album_song);
        }

        txt_title.setText(items.getTitle());
        if (items.getStreamingStatus() == 2) {
            txt_title.setTextColor(ContextCompat.getColor(this, R.color.yellow));
        } else {
            txt_title.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        txt_artist.setText(items.getArtistsNames());
        img_play_pause.setImageResource(isPlaying ? R.drawable.baseline_pause_24 : R.drawable.baseline_play_arrow_24);
    }

    public void handleLayoutMusic(int action) {
        switch (action) {
            case MyService.ACTION_START:
            case MyService.ACTION_PAUSE:
            case MyService.ACTION_RESUME:
                layout_bottom_player_main.setVisibility(View.VISIBLE);
                view_player.setVisibility(View.VISIBLE);

                //bottom player
                setDataBottomSheet(items);
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_CLEAR:
                layout_bottom_player_main.setVisibility(View.GONE);
                view_player.setVisibility(View.GONE);
                //clear data
                sharedPreferencesManager.deleteSongState();
                break;
        }
    }

    public void updateIndicator(int currentTime, int totalTime) {
        if (totalTime > 0) {
            float progress = (float) currentTime / totalTime;
            int progressInt = (int) (progress * 100);
            progress_indicator.setProgressCompat(progressInt, true);
        }
    }

    private void onClickBottomMain() {
        layout_bottom_player_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetNowPlaying(true);
            }
        });
        linear_play_pause.setOnClickListener(v -> {
            if (!Helper.isMyServiceRunning(this, MyService.class)) {
                startService(new Intent(this, MyService.class));
                isPlaying = false;
            }
            if (isPlaying) {
                sendActionToService(MyService.ACTION_PAUSE);
            } else {
                sendActionToService(MyService.ACTION_RESUME);
            }
        });

        linear_close.setOnClickListener(view -> sendActionToService(ACTION_CLEAR));
    }


    //player
    private void initViewPlayer() {
        RelativeLayout relative_player = layout_bottom_player_main.findViewById(R.id.relative_player);

        //container
        //player
        RelativeLayout container = relative_player.findViewById(R.id.container);
        image_back = relative_player.findViewById(R.id.image_back);
        txt_play_from = relative_player.findViewById(R.id.txt_play_from);
        image_more = relative_player.findViewById(R.id.image_more);

        //background
        image_background = relative_player.findViewById(R.id.image_background);

        //image
        relative_image_song = relative_player.findViewById(R.id.relative_image_song);
        image_song = relative_player.findViewById(R.id.image_song);
        progress_image = relative_player.findViewById(R.id.progress_image);

        //tua 5s
        linear_detail_song = relative_player.findViewById(R.id.linear_detail_song);
        linear_previous_five_second = relative_player.findViewById(R.id.linear_previous_five_second);
        linear_next_five_second = relative_player.findViewById(R.id.linear_next_five_second);

        //info
        linear_info = relative_player.findViewById(R.id.linear_info);
        txt_title_song = relative_player.findViewById(R.id.txt_title_song);
        txt_title_song.setSelected(true);
        txt_artist_song = relative_player.findViewById(R.id.txt_artist_song);
        txt_artist_song.setSelected(true);

        //
        seek_bar = relative_player.findViewById(R.id.seek_bar);

        //controller
        linear_controller = relative_player.findViewById(R.id.linear_controller);
        txt_current_time = relative_player.findViewById(R.id.txt_current_time);
        txt_total_time = relative_player.findViewById(R.id.txt_total_time);
        lottie_play_pause = relative_player.findViewById(R.id.lottie_play_pause);
        img_btn_next = relative_player.findViewById(R.id.img_btn_next);
        img_btn_repeat = relative_player.findViewById(R.id.img_btn_repeat);
        img_btn_previous = relative_player.findViewById(R.id.img_btn_previous);
        img_btn_shuffle = relative_player.findViewById(R.id.img_btn_shuffle);
    }

    private void conFigViewsPlayer() {
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    intent.putExtra("seek_to_position", progress);
                    startService(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
        if (!Helper.isMyServiceRunning(this, MyService.class)) {
            isPlaying = false;
        }
        //player
        if (!isPlaying) {
            lottie_play_pause.setMinAndMaxProgress(0.5f, 1.0f); // pause >
            lottie_play_pause.playAnimation();
        } else {
            lottie_play_pause.setMinAndMaxProgress(0.0f, 0.5f); // play ||
            lottie_play_pause.playAnimation();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDataPLayer(SongDetail songDetail) {
        if (!(MainActivity.this).isDestroyed()) {
            Glide.with(this)
                    .load(Helper.changeQualityUrl(songDetail.getData().getThumbnailM() != null ? songDetail.getData().getThumbnailM() : songDetail.getData().getThumbnail()))
                    .placeholder(R.drawable.holder)
                    .into(image_song);
        }
        image_song.setVisibility(View.VISIBLE);
        progress_image.setVisibility(View.GONE);


        txt_play_from.setText("Phát từ \n Danh sách phát");
        txt_title_song.setText(songDetail.getData().getTitle());
        if (songDetail.getData().getStreamingStatus() == 2) {
            txt_title_song.setTextColor(ContextCompat.getColor(this, R.color.yellow));
        } else {
            txt_title_song.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
        txt_artist_song.setText(songDetail.getData().getArtistsNames());

        txt_current_time.setText("00:00");
        txt_total_time.setText(Helper.convertDurationToMinutesAndSeconds(songDetail.getData().getDuration()));

        //btn
        setRepeatButtonState();
        getIntentNotification();
    }

    private void setRepeatButtonState() {
        boolean isShuffle = sharedPreferencesManager.restoreIsShuffleState();
        boolean isRepeat = sharedPreferencesManager.restoreIsRepeatState();
        boolean isRepeatOne = sharedPreferencesManager.restoreIsRepeatOneState();

        if (isShuffle) {
            img_btn_shuffle.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorSpotify)));
        } else {
            img_btn_shuffle.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.white)));
        }

        if (isRepeat) {
            if (isRepeatOne) {
                // Nếu repeatOne đang bật
                img_btn_repeat.setImageResource(R.drawable.ic_repeat_one);
                img_btn_repeat.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorSpotify)));
            } else {
                // Chỉ repeat đang bật
                img_btn_repeat.setImageResource(R.drawable.ic_repeat);
                img_btn_repeat.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorSpotify)));
            }
        } else {
            // Không có gì được bật
            img_btn_repeat.setImageResource(R.drawable.ic_repeat);
            img_btn_repeat.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
        }

    }

    private void onClickPlayer() {
        img_btn_previous.setOnClickListener(v -> sendActionToService(MyService.ACTION_PREVIOUS));
        img_btn_next.setOnClickListener(v -> sendActionToService(ACTION_NEXT));
        image_back.setOnClickListener(view -> showBottomSheetNowPlaying(false));
        image_more.setOnClickListener(v -> showBottomSheetInfo());

        linear_next_five_second.setOnClickListener(view -> nextPreviousFiveSecond(linear_next_five_second, false));
        linear_previous_five_second.setOnClickListener(view -> nextPreviousFiveSecond(linear_previous_five_second, true));

        lottie_play_pause.setOnClickListener(v -> {
            if (!Helper.isMyServiceRunning(this, MyService.class)) {
                startService(new Intent(this, MyService.class));
                isPlaying = false;
            }
            if (isPlaying) {
                sendActionToService(MyService.ACTION_PAUSE);
                lottie_play_pause.setMinAndMaxProgress(0.5f, 1.0f); // pause >
                lottie_play_pause.playAnimation();
            } else {
                lottie_play_pause.setMinAndMaxProgress(0.0f, 0.5f); // play ||
                lottie_play_pause.playAnimation();
                sendActionToService(MyService.ACTION_RESUME);
            }
        });

        img_btn_shuffle.setOnClickListener(view -> {
            if (sharedPreferencesManager.restoreIsShuffleState()) {
                sharedPreferencesManager.saveIsShuffleState(false);
                img_btn_shuffle.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.white)));
            } else {
                sharedPreferencesManager.saveIsShuffleState(true);
                img_btn_shuffle.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorSpotify)));
            }
        });

        img_btn_repeat.setOnClickListener(view -> {
            // Kiểm tra trạng thái hiện tại và thay đổi dựa trên trạng thái đó
            boolean isRepeat = sharedPreferencesManager.restoreIsRepeatState();
            boolean isRepeatOne = sharedPreferencesManager.restoreIsRepeatOneState();

            if (isRepeat) {
                if (isRepeatOne) {
                    // Nếu repeatOne đang bật, tắt repeatOne và quay lại trạng thái không bật gì
                    sharedPreferencesManager.saveIsRepeatOneState(false);
                    sharedPreferencesManager.saveIsRepeatState(false); // Thêm dòng này nếu bạn muốn tắt cả repeat khi tắt repeatOne
                    img_btn_repeat.setImageResource(R.drawable.ic_repeat);
                    img_btn_repeat.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.white)));
                } else {
                    // Nếu chỉ repeat đang bật, chuyển sang repeatOne
                    sharedPreferencesManager.saveIsRepeatOneState(true);
                    img_btn_repeat.setImageResource(R.drawable.ic_repeat_one);
                    img_btn_repeat.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorSpotify)));
                }
            } else {
                // Nếu không có gì bật, bật repeat
                sharedPreferencesManager.saveIsRepeatState(true);
                sharedPreferencesManager.saveIsRepeatOneState(false); // Đảm bảo rằng repeatOne không bật
                img_btn_repeat.setImageResource(R.drawable.ic_repeat);
                img_btn_repeat.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorSpotify)));
            }
        });
    }

    private void nextPreviousFiveSecond(View view, boolean isPrevious) {
        view.setOnClickListener(new View.OnClickListener() {
            private long lastClickTime = 0;
            private int clickCount = 0;
            private Handler handler = new Handler();
            private Runnable sendServiceRunnable = new Runnable() {
                @Override
                public void run() {
                    if (clickCount > 1) { // Only send service if clicked more than once
                        int totalSeconds = (clickCount - 1) * 5; // Click count - 1, since the first click does nothing
                        Intent intent = new Intent(MainActivity.this, MyService.class);
                        if (isPrevious) {
                            intent.putExtra("seek_to_position", currentTime - (totalSeconds * 1000)); // Adjust currentTime as needed
                        } else {
                            intent.putExtra("seek_to_position", currentTime + (totalSeconds * 1000)); // Adjust currentTime as needed
                        }
                        startService(intent);
                    }
                    clickCount = 0; // Reset click count after sending service
                }
            };

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                // Post delayed new callback
                // Remove previous callback
                if (clickTime - lastClickTime < 500) { // 500 milliseconds threshold for multiple clicks
                    clickCount++; // Increase click count
                } else {
                    clickCount = 1; // Reset to 1 for a single click
                }
                handler.removeCallbacks(sendServiceRunnable); // Remove previous callback
                handler.postDelayed(sendServiceRunnable, 500); // Post delayed new callback
                lastClickTime = clickTime;
            }
        });
    }

    private void getSongDetail(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getInfo(encodeId);

                    retrofit2.Call<SongDetail> call = service.SONG_DETAIL_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SongDetail>() {
                        @Override
                        public void onResponse(@NonNull Call<SongDetail> call, @NonNull Response<SongDetail> response) {
                            LogUtil.d(Constants.TAG, "getSongDetail: " + call.request().url());
                            if (response.isSuccessful()) {
                                songDetail = response.body();
                                if (songDetail != null) {
                                    runOnUiThread(() -> setDataPLayer(songDetail));
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SongDetail> call, @NonNull Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }

    public void setStatusButtonPlayOrPause() {
        if (!Helper.isMyServiceRunning(this, MyService.class)) {
            isPlaying = false;
        }

        //bottom player
        img_play_pause.setImageResource(isPlaying ? R.drawable.baseline_pause_24 : R.drawable.baseline_play_arrow_24);


        //player
        if (isPlaying) {
            lottie_play_pause.setMinAndMaxProgress(0.0f, 0.5f); // play ||
            lottie_play_pause.playAnimation();
        } else {
            lottie_play_pause.setMinAndMaxProgress(0.5f, 1.0f); // pause >
            lottie_play_pause.playAnimation();
        }

    }

    //bottom sheet player
    private void initViewBottomSheetPlayer() {
        RelativeLayout relative_player = layout_bottom_player_main.findViewById(R.id.relative_player);
        linear_bottom = relative_player.findViewById(R.id.linear_bottom);
        txt_continue_song = relative_player.findViewById(R.id.txt_continue_song);
        txt_lyric_song = relative_player.findViewById(R.id.txt_lyric_song);
        txt_relate_song = relative_player.findViewById(R.id.txt_relate_song);
    }

    private void conFigViewsBottomSheetPlayer() {

    }

    private void onClickBottomSheetPlayer() {
        linear_bottom.setOnClickListener(view -> showBottomSheetPlayer(1));
        txt_continue_song.setOnClickListener(view -> showBottomSheetPlayer(0));
        txt_lyric_song.setOnClickListener(view -> showBottomSheetPlayer(1));
        txt_relate_song.setOnClickListener(view -> showBottomSheetPlayer(2));
    }

    private void showBottomSheetPlayer(int tab_layout) {
        BottomSheetPlayer bottomSheetPlayer = new BottomSheetPlayer(MainActivity.this, MainActivity.this, tab_layout);
        bottomSheetPlayer.show(getSupportFragmentManager(), bottomSheetPlayer.getTag());
    }

    private void showBottomSheetInfo() {
        BottomSheetInfoSong bottomSheetInfoSong = new BottomSheetInfoSong(MainActivity.this, MainActivity.this, items);
        bottomSheetInfoSong.show(getSupportFragmentManager(), bottomSheetInfoSong.getTag());
    }

    private void updateSeekBar(int currentTime, int total_time) {
        seek_bar.setMax(totalTime);
        seek_bar.setProgress(currentTime);
        txt_current_time.setText(formatTime(currentTime));
    }


    private String formatTime(int timeInMillis) {
        String formattedTime;
        int minutes = timeInMillis / 1000 / 60;
        int seconds = (timeInMillis / 1000) % 60;
        formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        return formattedTime;
    }

    private void setBackground(int color_background, int color_bottomsheet) {
        image_background.setBackgroundColor(color_background);
        Window window = getWindow();
        window.setStatusBarColor(color_background);
        window.setNavigationBarColor(color_bottomsheet);

        ColorStateList colorStateList = ColorStateList.valueOf(color_bottomsheet);
        ViewCompat.setBackgroundTintList(linear_bottom, colorStateList);
        window.setNavigationBarColor(color_bottomsheet);
    }

    private void getColorBackground() {
        int[] colors = sharedPreferencesManager.restoreColorBackgrounState();
        int color_background = colors[0];
        int color_bottomsheet = colors[1];
        setBackground(color_background, color_bottomsheet);
    }

    public void sendActionToService(int action) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("action_music_service", action);
        startService(intent);
    }

    private void getIntentNotification() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            boolean is_click_notification = intent.getBooleanExtra("is_click_notification", false);
            boolean fromNotification = intent.getBooleanExtra("from_notification", false);
            if (fromNotification) {
                if (is_click_notification) {
                    showBottomSheetNowPlaying(true);
                }
            }
        }
    }


    // Thay thế Fragment với hiệu ứng chuyển đổi
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thêm hiệu ứng chuyển đổi
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right,  // Hiệu ứng khi Fragment mới vào
                R.anim.exit_to_left,      // Hiệu ứng khi Fragment hiện tại ra
                R.anim.enter_from_left,   // Hiệu ứng khi Fragment trước trở lại (khi bấm nút back)
                R.anim.exit_to_right      // Hiệu ứng khi Fragment mới ra (khi bấm nút back)
        );

        fragmentTransaction.replace(R.id.frame_layout_player, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Thay thế Fragment với Bundle và hiệu ứng chuyển đổi
    public void replaceFragmentWithBundle(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Thêm hiệu ứng chuyển đổi
        transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
        );

        transaction.replace(R.id.frame_layout_player, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(createBroadcastReceiver(), new IntentFilter("send_data_to_activity"));
        LocalBroadcastManager.getInstance(this).registerReceiver(createSeekBarUpdateReceiver(), new IntentFilter("send_seekbar_update"));
        LocalBroadcastManager.getInstance(this).registerReceiver(createExpandReceiver(), new IntentFilter("send_expand_to_activity"));

        LocalBroadcastManager.getInstance(this).registerReceiver(progressIndicatorReceiver(), new IntentFilter("send_load_music_update"));
        LocalBroadcastManager.getInstance(this).registerReceiver(isSongPremiumReceiver(), new IntentFilter("send_is_song_premium"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createBroadcastReceiver());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createSeekBarUpdateReceiver());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createExpandReceiver());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(progressIndicatorReceiver());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(isSongPremiumReceiver());
    }
}