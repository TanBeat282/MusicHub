package com.tandev.musichub.view_model.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.chart.home.home_new.Home;
import com.tandev.musichub.model.chart.new_release.NewRelease;
import com.tandev.musichub.model.hub.hub_home.HubHome;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Home> homeMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Home> getHomeMutableLiveData() {
        return homeMutableLiveData;
    }

    public void setHubHomeMutableLiveData(Home home) {
        homeMutableLiveData.setValue(home);
    }
}
