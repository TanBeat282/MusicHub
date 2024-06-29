package com.tandev.musichub.view_model.album;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.tandev.musichub.model.playlist.Playlist;

public class AlbumViewModel extends ViewModel {
    private MutableLiveData<Playlist> playlistMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Playlist> getPlaylistMutableLiveData() {
        return playlistMutableLiveData;
    }

    public void setPlaylistMutableLiveData(Playlist playlist) {
        playlistMutableLiveData.setValue(playlist);
    }
}
