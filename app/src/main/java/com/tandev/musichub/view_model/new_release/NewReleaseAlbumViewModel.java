package com.tandev.musichub.view_model.new_release;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.new_release.NewReleaseAlbum;
import com.tandev.musichub.model.playlist.Playlist;

public class NewReleaseAlbumViewModel extends ViewModel {
    private MutableLiveData<NewReleaseAlbum> newReleaseAlbumMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<NewReleaseAlbum> getNewReleaseAlbumMutableLiveData() {
        return newReleaseAlbumMutableLiveData;
    }

    public void setNewReleaseAlbumMutableLiveData(NewReleaseAlbum newReleaseAlbum) {
        newReleaseAlbumMutableLiveData.setValue(newReleaseAlbum);
    }
}
