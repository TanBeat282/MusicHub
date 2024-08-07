package com.tandev.musichub.model.search.search_featured;


import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchFeatured implements Serializable {
    private int err;
    private String msg;
    private DataSearchFeatured data;
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

    public DataSearchFeatured getData() {
        return data;
    }

    public void setData(DataSearchFeatured data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
