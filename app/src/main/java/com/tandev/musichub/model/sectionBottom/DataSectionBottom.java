package com.tandev.musichub.model.sectionBottom;

import java.io.Serializable;

public class DataSectionBottom implements Serializable {
    private DataSectionBottomArtist artist;
    private DataSectionBottomPlaylist playlist;

    public DataSectionBottomArtist getArtist() {
        return artist;
    }

    public void setArtist(DataSectionBottomArtist artist) {
        this.artist = artist;
    }

    public DataSectionBottomPlaylist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(DataSectionBottomPlaylist playlist) {
        this.playlist = playlist;
    }
}
