package com.tandev.musichub.model.chart.home.home_new.data;

import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeData implements Serializable {
    private ArrayList<HomeDataItem> items;
    private boolean hasMore;
    private int total;

    public ArrayList<HomeDataItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<HomeDataItem> items) {
        this.items = items;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
