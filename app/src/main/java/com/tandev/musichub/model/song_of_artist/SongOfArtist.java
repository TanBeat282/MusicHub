package com.tandev.musichub.model.song_of_artist;

import com.tandev.musichub.model.album.DataAlbum;

import java.io.Serializable;

public class SongOfArtist implements Serializable {
    private int err;
    private String msg;
    private SongOfArtistData data;
    private long timestamp;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SongOfArtistData getData() {
        return data;
    }

    public void setData(SongOfArtistData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
