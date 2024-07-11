package com.tandev.musichub.model.section_bottom;

import java.io.Serializable;
import java.util.ArrayList;

public class SectionBottom implements Serializable {
    private int err;
    private String msg;
    private ArrayList<DataSectionBottom> data;
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

    public ArrayList<DataSectionBottom>  getData() {
        return data;
    }

    public void setData(ArrayList<DataSectionBottom>  data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
