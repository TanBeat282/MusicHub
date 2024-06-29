package com.tandev.musichub.model.lyric;

public class Word {
    private long startTime;
    private long endTime;
    private String data;

    public Word(long startTime, long endTime, String data) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
