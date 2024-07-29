package com.tandev.musichub.model.video.section_relate;

import com.tandev.musichub.model.video.DataVideo;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionRelateVideo implements Serializable {
    public int err;
    public String msg;
    public ArrayList<DataSectionRelateVideo> data;
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

    public ArrayList<DataSectionRelateVideo> getData() {
        return data;
    }

    public void setData(ArrayList<DataSectionRelateVideo> data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
