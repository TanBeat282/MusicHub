package com.tandev.musichub.model.radio.user.comment;

import java.io.Serializable;

public class CommentUserRadio implements Serializable {
    private int err;
    private String msg;
    private DataCommentUserRadio data;
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

    public DataCommentUserRadio getData() {
        return data;
    }

    public void setData(DataCommentUserRadio data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
