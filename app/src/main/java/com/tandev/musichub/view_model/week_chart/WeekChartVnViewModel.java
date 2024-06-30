package com.tandev.musichub.view_model.week_chart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.chart.weekchart.WeekChart;
import com.tandev.musichub.model.hub.Hub;

public class WeekChartVnViewModel extends ViewModel {
    private MutableLiveData<WeekChart> weekChartMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<WeekChart> getWeekChartMutableLiveData() {
        return weekChartMutableLiveData;
    }

    public void setWeekChartMutableLiveData(WeekChart weekChart) {
        weekChartMutableLiveData.setValue(weekChart);
    }
}
