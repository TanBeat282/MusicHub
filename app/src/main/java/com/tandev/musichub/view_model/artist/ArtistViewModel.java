package com.tandev.musichub.view_model.artist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.artist.ArtistDetail;

public class ArtistViewModel extends ViewModel {
    private MutableLiveData<ArtistDetail> artistDetail = new MutableLiveData<>();

    public MutableLiveData<ArtistDetail> getArtistDetail() {
        return artistDetail;
    }

    public void setArtistDetail(ArtistDetail detail) {
        artistDetail.setValue(detail);
    }
}
