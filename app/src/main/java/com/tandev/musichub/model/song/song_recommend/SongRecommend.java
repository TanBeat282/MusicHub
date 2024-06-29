package com.tandev.musichub.model.song.song_recommend;

import com.tandev.musichub.model.song.DataSongDetail;

import java.io.Serializable;

public class SongRecommend implements Serializable {
    private int err;
    private String msg;
    private SongRecommendData data;
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

    public SongRecommendData getData() {
        return data;
    }

    public void setData(SongRecommendData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
