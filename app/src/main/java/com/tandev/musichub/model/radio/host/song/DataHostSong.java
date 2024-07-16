package com.tandev.musichub.model.radio.host.song;

import com.tandev.musichub.model.chart.chart_home.Items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataHostSong implements Serializable {
    private ArrayList<Items> top;

    public ArrayList<Items> getTop() {
        return top;
    }

    public void setTop(ArrayList<Items> top) {
        this.top = top;
    }
}
