package com.tandev.musichub.adapter.week_chart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
//import com.tandev.musichub.fragment.WeekChart.KpopFragment;
//import com.tandev.musichub.fragment.WeekChart.UsUkFragment;
//import com.tandev.musichub.fragment.WeekChart.VnFragment;
import com.tandev.musichub.fragment.week_chart.kpop.KpopFragment;
import com.tandev.musichub.fragment.week_chart.usuk.UsUkFragment;
import com.tandev.musichub.fragment.week_chart.vn.VnFragment;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChartItem;

public class WeekChartViewPageAdapter extends FragmentPagerAdapter {
    private HomeDataItemWeekChartItem homeDataItemWeekChartItem;
    private int weekChartPosition;

    public WeekChartViewPageAdapter(@NonNull FragmentManager fm, int behavior, HomeDataItemWeekChartItem homeDataItemWeekChartItem, int weekChartPosition) {
        super(fm, behavior);
        this.homeDataItemWeekChartItem = homeDataItemWeekChartItem;
        this.weekChartPosition = weekChartPosition;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new VnFragment();
            case 1:
                return new UsUkFragment();
            case 2:
                return new KpopFragment();
            default:
                return new VnFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Việt Nam";
                break;
            case 1:
                title = "US-UK";
                break;
            case 2:
                title = "K-Pop";
                break;
        }
        return title;
    }
}
