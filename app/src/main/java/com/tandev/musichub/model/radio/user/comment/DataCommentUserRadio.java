package com.tandev.musichub.model.radio.user.comment;

import java.io.Serializable;
import java.util.ArrayList;

public class DataCommentUserRadio implements Serializable {
    private ArrayList<ItemDataCommentUserRadio> items;
    private int total;
    private boolean hasMore;

    public ArrayList<ItemDataCommentUserRadio> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemDataCommentUserRadio> items) {
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
