package com.tandev.musichub.model.chart.chart_home;

import java.io.Serializable;
import java.util.ArrayList;

public class RTCharts implements Serializable {
    private ArrayList<Promotes> promotes;
    private ArrayList<Items> items;

    public ArrayList<Promotes> getPromotes() {
        return promotes;
    }

    public void setPromotes(ArrayList<Promotes> promotes) {
        this.promotes = promotes;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }
}
