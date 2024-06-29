package com.tandev.musichub.model.new_release;

import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.song.DataSongDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class NewReleaseSong implements Serializable {
    private int err;
    private String msg;
    private ArrayList<Items> data;
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

    public ArrayList<Items> getData() {
        return data;
    }

    public void setData(ArrayList<Items> data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
