package com.tandev.musichub.fragment.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.lyric.LyricsAdapter;
import com.tandev.musichub.helper.uliti.GetUrlAudioHelper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.lyric.LyricLine;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

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

public class LyricFragment extends Fragment {
    private RecyclerView recyclerViewLyrics;
    private LinearLayout txtNoData;
    private LyricsAdapter lyricsAdapter;
    private List<LyricLine> lyrics;
    private ExecutorService executor;
    private Items song;
    private int currentTime, total_time;
    private boolean isPlaying;
    private int action;
    private GetUrlAudioHelper getUrlAudioHelper;
    private SharedPreferencesManager sharedPreferencesManager;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            song = (Items) bundle.get("object_song");
            isPlaying = bundle.getBoolean("status_player");
            action = bundle.getInt("action_music");
            if (action == MyService.ACTION_START || action == MyService.ACTION_NEXT || action == MyService.ACTION_PREVIOUS) {
                getDataSong(song);
            }
        }
    };

    private final BroadcastReceiver seekBarUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentTime = intent.getIntExtra("current_time", 0);
            total_time = intent.getIntExtra("total_time", 0);
            updateLyricDisplay();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor = Executors.newSingleThreadExecutor(); // Tạo ExecutorService một lần
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lyric, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        song = sharedPreferencesManager.restoreSongState();
        getUrlAudioHelper = new GetUrlAudioHelper();

        lyrics = new ArrayList<>();
        recyclerViewLyrics = view.findViewById(R.id.recyclerViewLyrics);
        txtNoData = view.findViewById(R.id.txtNoData);

        recyclerViewLyrics.setLayoutManager(new LinearLayoutManager(getContext()));
        lyricsAdapter = new LyricsAdapter(getContext(), lyrics);
        recyclerViewLyrics.setAdapter(lyricsAdapter);

        recyclerViewLyrics.setVisibility(View.GONE);
        txtNoData.setVisibility(View.VISIBLE);

        getDataSong(sharedPreferencesManager.restoreSongState());
    }

    private void getDataSong(Items song) {
        if (song != null) {
            getUrlAudioHelper.getLyricUrl(song.getEncodeId(), new GetUrlAudioHelper.LyricUrlCallback() {
                @Override
                public void onSuccess(String lyricUrl) {
                    if (lyricUrl != null && !lyricUrl.trim().isEmpty()) {
                        executor.execute(() -> fetchLyricsFromUrl(lyricUrl));
                        recyclerViewLyrics.setVisibility(View.VISIBLE);
                        recyclerViewLyrics.scrollToPosition(0);
                        txtNoData.setVisibility(View.GONE);
                    } else {
                        recyclerViewLyrics.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.VISIBLE);
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
            requireActivity().runOnUiThread(() -> {
                lyrics = parseLyrics(lyricContent);
                updateLyricDisplay();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLyricDisplay() {
        if (lyrics != null && !lyrics.isEmpty()) {
            int currentLineIndex = -1;
            for (int i = 0; i < lyrics.size(); i++) {
                LyricLine currentLine = lyrics.get(i);
                if (currentTime >= currentLine.getStartTime()) {
                    currentLineIndex = i;
                } else {
                    break;
                }
            }

            if (currentLineIndex != -1) {
                smoothScrollToPosition(currentLineIndex + 10);
            }

            lyricsAdapter.updateLyricLines(lyrics);
            lyricsAdapter.setCurrentPlaybackTime(currentTime);
        }
    }

    private void smoothScrollToPosition(int targetPosition) {
        RecyclerView.LayoutManager layoutManager = recyclerViewLyrics.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int distance = Math.abs(targetPosition - firstVisibleItemPosition);

            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(requireContext()) {
                private static final float MILLISECONDS_PER_INCH_FAST = 50f;  // tốc độ nhanh
                private static final float MILLISECONDS_PER_INCH_NORMAL = 1000f;  // tốc độ bình thường

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    if (distance > 10) {  // nếu khoảng cách lớn hơn 10 mục, cuộn nhanh
                        return MILLISECONDS_PER_INCH_FAST / displayMetrics.densityDpi;
                    } else {  // nếu không, cuộn bình thường
                        return MILLISECONDS_PER_INCH_NORMAL / displayMetrics.densityDpi;
                    }
                }
            };

            smoothScroller.setTargetPosition(targetPosition);
            Objects.requireNonNull(layoutManager).startSmoothScroll(smoothScroller);
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
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(seekBarUpdateReceiver, new IntentFilter("send_seekbar_update"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(seekBarUpdateReceiver);
    }
}
