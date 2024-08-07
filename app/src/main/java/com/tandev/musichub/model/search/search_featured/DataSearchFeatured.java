package com.tandev.musichub.model.search.search_featured;


import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSearchFeatured implements Serializable {
   private String tracking;
   private String correctKeyword;

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getCorrectKeyword() {
        return correctKeyword;
    }

    public void setCorrectKeyword(String correctKeyword) {
        this.correctKeyword = correctKeyword;
    }
}
