package com.tandev.musichub.model.playlist;

import com.tandev.musichub.model.chart.chart_home.Items;

import java.io.Serializable;
import java.util.ArrayList;

import java.io.Serializable;
import java.util.ArrayList;

public class DataItems implements Serializable {
    private ArrayList<Items> items;
    private int total;
    private int totalDuration;

    public DataItems() {
        this.items = new ArrayList<>();
        this.total = 0;
        this.totalDuration = 0;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
        updateTotal();
        updateTotalDuration();
    }

    public void addItem(Items item) {
        this.items.add(item);
        updateTotal();
        updateTotalDuration();
    }

    public int getTotal() {
        return total;
    }

    public void updateTotal() {
        this.total = this.items.size();
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void updateTotalDuration() {
        this.totalDuration = 0;
        for (Items item : items) {
            this.totalDuration += item.getDuration();
        }
    }
}
