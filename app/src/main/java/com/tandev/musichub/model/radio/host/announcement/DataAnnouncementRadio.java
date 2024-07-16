package com.tandev.musichub.model.radio.host.announcement;

import java.io.Serializable;
import java.util.ArrayList;

public class DataAnnouncementRadio implements Serializable {
    private ArrayList<ItemDataAnnouncementRadio> items;
    private int total;
    private boolean hasMore;

    public ArrayList<ItemDataAnnouncementRadio> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemDataAnnouncementRadio> items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
