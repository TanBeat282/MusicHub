package com.tandev.musichub.model.video;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemVideoStreaming implements Serializable {
    @SerializedName("360p")
    public String p360;

    @SerializedName("480p")
    public String p480;

    @SerializedName("720p")
    public String p720;

    @SerializedName("1080p")
    public String p1080;

    public String getP360() {
        return p360;
    }

    public void setP360(String p360) {
        this.p360 = p360;
    }

    public String getP480() {
        return p480;
    }

    public void setP480(String p480) {
        this.p480 = p480;
    }

    public String getP720() {
        return p720;
    }

    public void setP720(String p720) {
        this.p720 = p720;
    }

    public String getP1080() {
        return p1080;
    }

    public void setP1080(String p1080) {
        this.p1080 = p1080;
    }
}
