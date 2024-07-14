package com.tandev.musichub.fragment.history;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.history.HistoryAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    //tool bar
    private View tool_bar;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;


    private LinearLayout linear_da_nghe, linear_nghe_nhieu, linear_no_data;
    private ArrayList<Items> songListLichSuBaiHat;
    private ArrayList<Items> songListLichSuBaiHatNgheNhieu;
    private HistoryAdapter lichSuBaiHatNgheNhieuAdapter, lichSuBaiHatAdapter;
    private SharedPreferencesManager sharedPreferencesManager;
    private MusicHelper musicHelper;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
           Items items = (Items) bundle.get("object_song");
            int action = bundle.getInt("action_music");
            if (items != null) {
                if (action == MyService.ACTION_START || action == MyService.ACTION_NEXT || action == MyService.ACTION_PREVIOUS) {
                    musicHelper.checkIsPlayingPlaylist(items, songListLichSuBaiHat, lichSuBaiHatAdapter);
                    musicHelper.checkIsPlayingPlaylist(items, songListLichSuBaiHatNgheNhieu, lichSuBaiHatNgheNhieuAdapter);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.changeStatusBarColor(requireActivity(), R.color.black);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        musicHelper = new MusicHelper(requireContext(), sharedPreferencesManager);

        tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);


        NestedScrollView nested_scroll = view.findViewById(R.id.nested_scroll);

        linear_nghe_nhieu = view.findViewById(R.id.linear_nghe_nhieu);
        RecyclerView rv_history_count = view.findViewById(R.id.rv_history_count);
        linear_da_nghe = view.findViewById(R.id.linear_da_nghe);
        RecyclerView rv_history = view.findViewById(R.id.rv_history);
        linear_no_data = view.findViewById(R.id.linear_no_data);


        rv_history_count.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        lichSuBaiHatNgheNhieuAdapter = new HistoryAdapter(requireContext(), requireActivity(), songListLichSuBaiHatNgheNhieu);
        rv_history_count.setAdapter(lichSuBaiHatNgheNhieuAdapter);

        rv_history.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        lichSuBaiHatAdapter = new HistoryAdapter(requireContext(), requireActivity(), songListLichSuBaiHat);
        rv_history.setAdapter(lichSuBaiHatAdapter);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 200) {
                    txt_title_toolbar.setText("");
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 300) {
                    txt_title_toolbar.setText("Lịch sử nghe");
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });

        img_back.setOnClickListener(v -> {
            // Xử lý khi nhấn nút back
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        getSongHistory();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getSongHistory() {

        songListLichSuBaiHat = new ArrayList<>();
        songListLichSuBaiHatNgheNhieu = new ArrayList<>();

        // Khởi tạo danh sách bài hát lịch sử
        songListLichSuBaiHat = sharedPreferencesManager.restoreSongArrayListHistory();

        if (songListLichSuBaiHat.isEmpty()) {
            linear_nghe_nhieu.setVisibility(View.GONE);
            linear_da_nghe.setVisibility(View.GONE);
            linear_no_data.setVisibility(View.VISIBLE);
        } else {
            // Sắp xếp songListLichSuBaiHat theo historyCount từ lớn đến nhỏ
            songListLichSuBaiHat.sort((o1, o2) -> Integer.compare(o2.getHistoryCount(), o1.getHistoryCount()));

            // Lấy 3 mục có historyCount lớn nhất và thêm vào songListLichSuBaiHatNgheNhieu
            for (int i = 0; i < Math.min(songListLichSuBaiHat.size(), 4); i++) {
                songListLichSuBaiHatNgheNhieu.add(songListLichSuBaiHat.get(i));
            }

            // Xóa 3 mục đó ra khỏi songListLichSuBaiHat
            songListLichSuBaiHat.removeAll(songListLichSuBaiHatNgheNhieu);

            // Cập nhật Adapter và kiểm tra bài hát đang phát
            if (!songListLichSuBaiHatNgheNhieu.isEmpty()) {
                linear_nghe_nhieu.setVisibility(View.VISIBLE);
                lichSuBaiHatNgheNhieuAdapter.setFilterList(songListLichSuBaiHatNgheNhieu);
                lichSuBaiHatNgheNhieuAdapter.notifyDataSetChanged(); // Thông báo cho Adapter biết dữ liệu đã thay đổi
                musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), songListLichSuBaiHatNgheNhieu, lichSuBaiHatNgheNhieuAdapter);
            }

            // Cập nhật Adapter cho danh sách lịch sử chung và kiểm tra bài hát đang phát
            linear_da_nghe.setVisibility(View.VISIBLE);
            linear_no_data.setVisibility(View.GONE);
            lichSuBaiHatAdapter.setFilterList(songListLichSuBaiHat);
            lichSuBaiHatAdapter.notifyDataSetChanged(); // Thông báo cho Adapter biết dữ liệu đã thay đổi
            musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), songListLichSuBaiHat, lichSuBaiHatAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        songListLichSuBaiHat.clear();
        songListLichSuBaiHatNgheNhieu.clear();
        getSongHistory();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }
}