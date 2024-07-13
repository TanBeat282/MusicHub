package com.tandev.musichub;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Outline;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tandev.musichub.adapter.lyric.LyricsAdapter;
import com.tandev.musichub.adapter.lyric.LyricsAdapter2;
import com.tandev.musichub.helper.uliti.GetUrlAudioHelper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.lyric.LyricLine;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.service.MyService2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity2 extends AppCompatActivity {
    private MyService2 myService;
    private boolean isBound = false;
    private RecyclerView recyclerView;
    private LyricsAdapter2 lyricsAdapter;
    private List<LyricLine> lyrics = new ArrayList<>(); // Khởi tạo lyrics
    private GetUrlAudioHelper getUrlAudioHelper;
    private ExecutorService executor;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MyService2.LocalBinder binder = (MyService2.LocalBinder) service;
            myService = binder.getService();
            isBound = true;
            startUpdatingLyrics();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Khởi tạo ExecutorService
        executor = Executors.newSingleThreadExecutor();

        recyclerView = findViewById(R.id.recycler_view);

        // Khởi tạo getUrlAudioHelper
        getUrlAudioHelper = new GetUrlAudioHelper();

        Intent intent = new Intent(this, MyService2.class);
        intent.putExtra("url", "https://a128-z3.zmdcdn.me/90087d3044a1452842374850ecfcd5e9?authen=exp=1721023162~acl=/90087d3044a1452842374850ecfcd5e9/*~hmac=c7e0286d440132e7f3f6cb1673abc266");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        startService(intent);

        getDataSong("Z76EZE8E");
    }

    private void getDataSong(String id) {
        if (id != null && getUrlAudioHelper != null) {
            getUrlAudioHelper.getLyricUrl(id, new GetUrlAudioHelper.LyricUrlCallback() {
                @Override
                public void onSuccess(String lyricUrl) {
                    if (lyricUrl != null && !lyricUrl.trim().isEmpty()) {
                        executor.execute(() -> fetchLyricsFromUrl(lyricUrl));
                        runOnUiThread(() -> {
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.scrollToPosition(0);
                        });
                    } else {
                        runOnUiThread(() -> recyclerView.setVisibility(View.GONE));
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    // Xử lý lỗi nếu cần
                }
            });
        }
    }

    private void fetchLyricsFromUrl(String url) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
            String lyricContent = result.toString();
            runOnUiThread(() -> {
                lyrics = parseLyrics(lyricContent);
                lyricsAdapter = new LyricsAdapter2(this,lyrics);
                recyclerView.setAdapter(lyricsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<LyricLine> parseLyrics(String lyricContent) {
        List<LyricLine> lyricLines = new ArrayList<>();

        String[] lines = lyricContent.split("\n");
        for (String line : lines) {
            String regex = "\\[(\\d{2}):(\\d{2}).(\\d{2})](.*)";
            if (line.matches(regex)) {
                String minuteStr = line.replaceAll(regex, "$1");
                String secondStr = line.replaceAll(regex, "$2");
                String millisecondStr = line.replaceAll(regex, "$3");
                String content = line.replaceAll(regex, "$4");

                int minute = Integer.parseInt(minuteStr);
                int second = Integer.parseInt(secondStr);
                int millisecond = Integer.parseInt(millisecondStr);
                int startTime = minute * 60 * 1000 + second * 1000 + millisecond * 10;

                lyricLines.add(new LyricLine(startTime, content));
            }
        }

        return lyricLines;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private void startUpdatingLyrics() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isBound) {
                    long currentPosition = myService.getCurrentPosition();
                    updateHighlightedLyric(currentPosition);
                    handler.postDelayed(this, 100); // Cập nhật mỗi 100ms
                }
            }
        };
        handler.post(runnable);
    }

    private void updateHighlightedLyric(long currentPosition) {
        if (lyrics != null && !lyrics.isEmpty()) {
            for (int i = 0; i < lyrics.size(); i++) {
                if (lyrics.get(i).getStartTime() > currentPosition) {
                    lyricsAdapter.highlightLine(i - 1);
                    smoothScrollToPosition(i + 10);
                    recyclerView.scrollToPosition(i + 10);
                    break;
                }
            }
        }
    }

    private void smoothScrollToPosition(int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity2.this) {
            private static final float MILLISECONDS_PER_INCH_NORMAL = 1000f;

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH_NORMAL / displayMetrics.densityDpi;
            }
        };

        smoothScroller.setTargetPosition(position);
        Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
    }

}
