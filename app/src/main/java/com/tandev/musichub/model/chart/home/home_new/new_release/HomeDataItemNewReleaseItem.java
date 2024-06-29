package com.tandev.musichub.model.chart.home.home_new.new_release;

import com.tandev.musichub.model.chart.chart_home.Items;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDataItemNewReleaseItem implements Serializable {
    private ArrayList<Items> all;
    private ArrayList<Items> vPop;
    private ArrayList<Items> others;

    public ArrayList<Items> getAll() {
        return all;
    }

    public void setAll(ArrayList<Items> all) {
        this.all = all;
    }

    public ArrayList<Items> getvPop() {
        return vPop;
    }

    public void setvPop(ArrayList<Items> vPop) {
        this.vPop = vPop;
    }

    public ArrayList<Items> getOthers() {
        return others;
    }

    public void setOthers(ArrayList<Items> others) {
        this.others = others;
    }
}
