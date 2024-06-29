package com.tandev.musichub.model.artist.song;

import java.io.Serializable;

public class ArtistOption implements Serializable {
    private String artistId;

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}
