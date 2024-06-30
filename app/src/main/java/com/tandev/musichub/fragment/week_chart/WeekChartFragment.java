package com.tandev.musichub.fragment.week_chart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.week_chart.WeekChartViewPageAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChartItem;

public class WeekChartFragment extends Fragment {
    private TabLayout tab_layout_new_release_song;
    private ViewPager view_pager_new_release_song;
    private WeekChartViewPageAdapter mViewPagerAdapter;
    private HomeDataItemWeekChartItem homeDataItemWeekChartItem;
    private int position_slider = -1;
    private int week_chart = 0;
    private LinearLayout linear_filter_song;
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
            homeDataItemWeekChartItem = (HomeDataItemWeekChartItem) getArguments().getSerializable("itemWeekChart");
            position_slider = getArguments().getInt("position_slide");
            if (homeDataItemWeekChartItem != null && position_slider != -1) {
                int position = -1;
                if (position_slider == 1) {
                    position = 0;
                } else if (position_slider == 2) {
                    position = 2;
                } else {
                    position = 1;
                }
                tab_layout_new_release_song.getTabAt(position).select();
            }
        }
    }

    private void initView(View view) {
        Helper.changeStatusBarColor(requireActivity(), R.color.black);


        tab_layout_new_release_song = view.findViewById(R.id.tab_layout_new_release_song);
        view_pager_new_release_song = view.findViewById(R.id.view_pager_new_release_song);
        linear_filter_song = view.findViewById(R.id.linear_filter_song);
        img_back = view.findViewById(R.id.img_back);
    }

    private void initViewPager() {
        mViewPagerAdapter = new WeekChartViewPageAdapter(requireActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, homeDataItemWeekChartItem, week_chart);
        view_pager_new_release_song.setAdapter(mViewPagerAdapter);

        tab_layout_new_release_song.setupWithViewPager(view_pager_new_release_song);
    }
    private void onClick(){
        linear_filter_song.setOnClickListener(view -> {
            Intent intent = new Intent("send_week_year_to_fragment");
            intent.putExtra("week_chart", "24");
            intent.putExtra("year_chart", "2024");
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
        });
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

}