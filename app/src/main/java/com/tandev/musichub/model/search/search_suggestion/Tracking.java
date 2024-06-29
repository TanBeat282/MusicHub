package com.tandev.musichub.model.search.search_suggestion;

import java.io.Serializable;

public class Tracking implements SearchSuggestionsDataItem, Serializable {
    private String tracking;

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
