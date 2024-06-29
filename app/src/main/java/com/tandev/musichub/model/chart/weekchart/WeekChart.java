package com.tandev.musichub.model.chart.weekchart;

import com.tandev.musichub.model.chart.chart_home.Data;
import com.tandev.musichub.model.chart.chart_home.ItemWeekChart;

import java.io.Serializable;

public class WeekChart implements Serializable {
    private int err;
    private String msg;
    private ItemWeekChart data;
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

    public ItemWeekChart getData() {
        return data;
    }

    public void setData(ItemWeekChart data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
