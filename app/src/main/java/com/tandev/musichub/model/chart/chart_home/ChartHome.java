package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;

public class ChartHome implements Serializable {
    private int err;
    private String msg;
    private Data data;
    private long timestamp;

    public ChartHome() {
    }

    public ChartHome(int err, String msg, Data data, long timestamp) {
        this.err = err;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
