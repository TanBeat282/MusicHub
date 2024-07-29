package com.tandev.musichub.model.video;

import java.io.Serializable;

public class DataVideoLyric implements Serializable {
    private String id;
    private String content;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
