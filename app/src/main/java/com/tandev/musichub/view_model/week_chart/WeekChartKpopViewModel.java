package com.tandev.musichub.view_model.week_chart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.chart.weekchart.WeekChart;

public class WeekChartKpopViewModel extends ViewModel {
    private MutableLiveData<WeekChart> weekChartMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<WeekChart> getWeekChartMutableLiveData() {
        return weekChartMutableLiveData;
    }

    public void setWeekChartMutableLiveData(WeekChart weekChart) {
        weekChartMutableLiveData.setValue(weekChart);
    }
}
