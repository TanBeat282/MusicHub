package com.tandev.musichub.model.user_active_radio;

import java.io.Serializable;

public class DataUserActiveRadio implements Serializable {
    private String encodeId;
    private int activeUsers;

    public String getEncodeId() {
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }
}
