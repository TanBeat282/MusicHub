package com.tandev.musichub.model.song.song_recommend;

import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.song.DataSongDetail;

import java.io.Serializable;
import java.util.ArrayList;

public class SongRecommendData  implements Serializable {
    private ArrayList<Items> items;
    private int total;
    private SimData simData;

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

    public SimData getSimData() {
        return simData;
    }

    public void setSimData(SimData simData) {
        this.simData = simData;
    }
}
