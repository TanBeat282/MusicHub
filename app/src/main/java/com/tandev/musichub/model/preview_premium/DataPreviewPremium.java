package com.tandev.musichub.model.preview_premium;

import java.io.Serializable;

public class DataPreviewPremium implements Serializable {
    private BumperAudio bumperAudio;
    private String link;
    private int startTime;
    private int endTime;

    public BumperAudio getBumperAudio() {
        return bumperAudio;
    }

    public void setBumperAudio(BumperAudio bumperAudio) {
        this.bumperAudio = bumperAudio;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
