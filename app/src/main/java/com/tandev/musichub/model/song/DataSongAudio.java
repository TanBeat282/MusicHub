package com.tandev.musichub.model.song;

import com.google.gson.annotations.SerializedName;

public class DataSongAudio {

    @SerializedName("128")
    private String low;
    @SerializedName("320")
    private String high;
    @SerializedName("lossless")
    private String lossless;

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLossless() {
        return lossless;
    }

    public void setLossless(String lossless) {
        this.lossless = lossless;
    }
}
