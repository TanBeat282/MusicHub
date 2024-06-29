package com.tandev.musichub.model.search.song;

import com.tandev.musichub.model.chart.chart_home.Items;
import java.io.Serializable;
import java.util.ArrayList;

public class SearchSongData implements Serializable {
    private String correctKeyword;
    private ArrayList<Items> items;
    private int total;
    private String sectionId;


    public String getCorrectKeyword() {
        return correctKeyword;
    }

    public void setCorrectKeyword(String correctKeyword) {
        this.correctKeyword = correctKeyword;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
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
