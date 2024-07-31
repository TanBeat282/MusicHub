package com.tandev.musichub.fragment.week_chart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.week_chart.WeekChartViewPageAdapter;
import com.tandev.musichub.bottomsheet.BottomSheetSelectWeek;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChartItem;
import com.tandev.musichub.model.weekchart.WeekChartSelect;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeekChartFragment extends Fragment implements BottomSheetSelectWeek.SelectWeekListener {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
    private TabLayout tab_layout_new_release_song;
    private ViewPager view_pager_new_release_song;
    private WeekChartViewPageAdapter mViewPagerAdapter;
    private int position_slider = -1;
    private static int weekOfYear = 0;
    private LinearLayout linear_filter_song;
    private TextView txt_filter_song;
    private ImageView img_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initViewPager();
        onClick();
        getBundleSong();
    }

    private void getBundleSong() {
        if (getArguments() != null) {
            position_slider = getArguments().getInt("position_slide");
            if (position_slider != -1) {
                if (position_slider == 1) {
                    position_slider = 0;
                } else {
                    position_slider = 1;
                }
                tab_layout_new_release_song.getTabAt(position_slider).select();
            }
            txt_filter_song.setText(getCurrentWeekInfo());

        }
    }

    private void initView(View view) {
        Helper.changeStatusBarColor(requireActivity(), R.color.bg);


        tab_layout_new_release_song = view.findViewById(R.id.tab_layout_new_release_song);
        view_pager_new_release_song = view.findViewById(R.id.view_pager_new_release_song);
        linear_filter_song = view.findViewById(R.id.linear_filter_song);
        txt_filter_song = view.findViewById(R.id.txt_filter_song);
        img_back = view.findViewById(R.id.img_back);
    }

    private void initViewPager() {
        mViewPagerAdapter = new WeekChartViewPageAdapter(requireActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        view_pager_new_release_song.setAdapter(mViewPagerAdapter);

        tab_layout_new_release_song.setupWithViewPager(view_pager_new_release_song);
    }

    private void onClick() {
        linear_filter_song.setOnClickListener(view -> {
            BottomSheetSelectWeek bottomSheetSelectWeek = new BottomSheetSelectWeek(requireContext(), requireActivity(), weekOfYear);
            bottomSheetSelectWeek.setWeekListener(this);
            bottomSheetSelectWeek.show(requireActivity().getSupportFragmentManager(), bottomSheetSelectWeek.getTag());
        });
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public static String getCurrentWeekInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        // Xác định ngày đầu tuần (Thứ Hai)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDay = sdf.format(calendar.getTime());

        // Xác định ngày cuối tuần (Chủ Nhật)
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        String endDay = sdf.format(calendar.getTime());

        // Xác định tuần của năm
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        // Trả về thông tin tuần
        return "Tuần " + weekOfYear + " (" + startDay + " - " + endDay + ")";
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onWeekSelected(WeekChartSelect weekChartSelect) {
        txt_filter_song.setText("Tuần " + weekChartSelect.getWeek() + " (" + Helper.formatToDayMonth(weekChartSelect.getStartDayWeek()) + " - " + Helper.formatToDayMonth(weekChartSelect.getEndDayWeek()) + ")");

        Intent intent = new Intent("send_week_year_to_fragment");
        intent.putExtra("week_chart", String.valueOf(weekChartSelect.getWeek()));
        intent.putExtra("year_chart", "2024");
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
    }
}