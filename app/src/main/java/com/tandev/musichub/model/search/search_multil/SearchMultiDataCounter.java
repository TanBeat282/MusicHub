package com.tandev.musichub.model.search.search_multil;

import java.io.Serializable;

public class SearchMultiDataCounter implements Serializable {
    private int song;
    private int artist;
    private int playlist;
    private int video;

    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }

    public int getArtist() {
        return artist;
    }

    public void setArtist(int artist) {
        this.artist = artist;
    }

    public int getPlaylist() {
        return playlist;
    }

    public void setPlaylist(int playlist) {
        this.playlist = playlist;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }
}
