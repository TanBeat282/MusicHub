package com.tandev.musichub.view_model.hub;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.chart.new_release.NewRelease;
import com.tandev.musichub.model.hub.Hub;

public class HubViewModel extends ViewModel {
    private MutableLiveData<Hub> hubMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Hub> getHubMutableLiveData() {
        return hubMutableLiveData;
    }

    public void setHubMutableLiveData(Hub hub) {
        hubMutableLiveData.setValue(hub);
    }
}
