package com.tandev.musichub.view_model.new_release;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.new_release.NewReleaseSong;
import com.tandev.musichub.model.playlist.Playlist;

public class NewReleaseSongViewModel extends ViewModel {
    private MutableLiveData<NewReleaseSong> newReleaseSongMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<NewReleaseSong> getNewReleaseSongMutableLiveData() {
        return newReleaseSongMutableLiveData;
    }

    public void setNewReleaseSongMutableLiveData(NewReleaseSong newReleaseSong) {
        newReleaseSongMutableLiveData.setValue(newReleaseSong);
    }
}
