package com.tandev.musichub.model.video;

import java.io.Serializable;

public class DataVideoStreaming implements Serializable {
    private ItemVideoStreaming mp4;
    private ItemVideoStreaming hls;

    public ItemVideoStreaming getMp4() {
        return mp4;
    }

    public void setMp4(ItemVideoStreaming mp4) {
        this.mp4 = mp4;
    }

    public ItemVideoStreaming getHls() {
        return hls;
    }

    public void setHls(ItemVideoStreaming hls) {
        this.hls = hls;
    }
}
