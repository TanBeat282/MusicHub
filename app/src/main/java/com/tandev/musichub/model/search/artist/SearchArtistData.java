package com.tandev.musichub.model.search.artist;

import com.tandev.musichub.model.chart.chart_home.Artists;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchArtistData implements Serializable {
    private String correctKeyword;
    private ArrayList<Artists> items;
    private int total;
    private String sectionId;


    public String getCorrectKeyword() {
        return correctKeyword;
    }

    public void setCorrectKeyword(String correctKeyword) {
        this.correctKeyword = correctKeyword;
    }

    public ArrayList<Artists> getItems() {
        return items;
    }

    public void setItems(ArrayList<Artists> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
