package com.tandev.musichub.view_model.chart_home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.chart.chart_home.ChartHome;
import com.tandev.musichub.model.chart.home.home_new.Home;

public class ChartHomeViewModel extends ViewModel {
    private MutableLiveData<ChartHome> chartHomeMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ChartHome> getChartHomeMutableLiveData() {
        return chartHomeMutableLiveData;
    }

    public void setChartHomeMutableLiveData(ChartHome chartHome) {
        chartHomeMutableLiveData.setValue(chartHome);
    }
}
