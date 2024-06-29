package com.tandev.musichub.model.new_release;

import com.tandev.musichub.model.chart.chart_home.Album;
import com.tandev.musichub.model.chart.chart_home.Items;

import java.io.Serializable;
import java.util.ArrayList;

public class NewReleaseAlbum implements Serializable {
    private int err;
    private String msg;
    private ArrayList<Album> data;
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

    public ArrayList<Album> getData() {
        return data;
    }

    public void setData(ArrayList<Album> data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
