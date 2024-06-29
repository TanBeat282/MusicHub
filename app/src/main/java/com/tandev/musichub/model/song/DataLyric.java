package com.tandev.musichub.model.song;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataLyric implements Serializable {
    private ArrayList<Sentences> sentences;
    private String file;
    private boolean enabledVideoBG;
    private List<String> defaultIBGUrls;
    private int BGMode;

    public ArrayList<Sentences> getSentences() {
        return sentences;
    }

    public void setSentences(ArrayList<Sentences> sentences) {
        this.sentences = sentences;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isEnabledVideoBG() {
        return enabledVideoBG;
    }

    public void setEnabledVideoBG(boolean enabledVideoBG) {
        this.enabledVideoBG = enabledVideoBG;
    }

    public List<String> getDefaultIBGUrls() {
        return defaultIBGUrls;
    }

    public void setDefaultIBGUrls(List<String> defaultIBGUrls) {
        this.defaultIBGUrls = defaultIBGUrls;
    }

    public int getBGMode() {
        return BGMode;
    }

    public void setBGMode(int BGMode) {
        this.BGMode = BGMode;
    }
}
