package com.tandev.musichub.view_model.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.hub.hub_home.HubHome;

public class HubHomeViewModel extends ViewModel {
    private MutableLiveData<HubHome> hubHomeMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<HubHome> getHubHomeMutableLiveData() {
        return hubHomeMutableLiveData;
    }

    public void setHubHomeMutableLiveData(HubHome hubHome) {
        hubHomeMutableLiveData.setValue(hubHome);
    }
}
