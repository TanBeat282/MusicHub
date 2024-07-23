package com.tandev.musichub.adapter.week_chart;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
//import com.tandev.musichub.fragment.WeekChart.KpopFragment;
//import com.tandev.musichub.fragment.WeekChart.UsUkFragment;
//import com.tandev.musichub.fragment.WeekChart.VnFragment;
import com.tandev.musichub.fragment.week_chart.kpop.KpopFragment;
import com.tandev.musichub.fragment.week_chart.usuk.UsUkFragment;
import com.tandev.musichub.fragment.week_chart.vn.VnFragment;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChartItem;

public class WeekChartViewPageAdapter extends FragmentStatePagerAdapter {

    public WeekChartViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
