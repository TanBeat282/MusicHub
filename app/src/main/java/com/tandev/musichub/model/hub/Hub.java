package com.tandev.musichub.model.hub;

import com.tandev.musichub.model.user_active_radio.DataUserActiveRadio;

import java.io.Serializable;

public class Hub implements Serializable {
    private int err;
    private String msg;
    private DataHub data;
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

    public DataHub getData() {
        return data;
    }

    public void setData(DataHub data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
