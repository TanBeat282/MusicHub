package com.tandev.musichub.model.song_of_artist;

import com.tandev.musichub.model.chart.chart_home.Items;

import java.io.Serializable;
import java.util.ArrayList;

public class SongOfArtistData implements Serializable {
    private int total;
    private ArrayList<Items> items;
    private boolean hasMore;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
