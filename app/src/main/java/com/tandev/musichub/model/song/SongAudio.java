package com.tandev.musichub.model.song;

public class SongAudio {
    private int err;
    private String msg;
    private DataSongAudio data;
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

    public DataSongAudio getData() {
        return data;
    }

    public void setData(DataSongAudio data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
