package com.tandev.musichub.view_model.chart_new_release;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.artist.ArtistDetail;
import com.tandev.musichub.model.chart.new_release.NewRelease;

public class ChartNewReleaseViewModel extends ViewModel {
    private MutableLiveData<NewRelease> newReleaseMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<NewRelease> getNewReleaseMutableLiveData() {
        return newReleaseMutableLiveData;
    }

    public void setNewReleaseMutableLiveData(NewRelease newRelease) {
        newReleaseMutableLiveData.setValue(newRelease);
    }
}
