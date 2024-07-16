package com.tandev.musichub.model.preview_premium;

import java.io.Serializable;

public class PreviewPremium implements Serializable {
    private int err;
    private String msg;
    private DataPreviewPremium data;
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

    public DataPreviewPremium getData() {
        return data;
    }

    public void setData(DataPreviewPremium data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
