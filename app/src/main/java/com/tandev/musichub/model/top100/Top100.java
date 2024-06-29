package com.tandev.musichub.model.top100;


import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;

import java.io.Serializable;
import java.util.ArrayList;

public class Top100 implements Serializable {
    private int err;
    private String msg;
    private ArrayList<DataTop100>data;
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

    public ArrayList<DataTop100> getData() {
        return data;
    }

    public void setData(ArrayList<DataTop100> data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
