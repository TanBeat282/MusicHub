package com.tandev.musichub.model.hub.hub_home.featured;

import java.io.Serializable;
import java.util.ArrayList;

public class HubHomeFeatured implements Serializable {
    private String title;
    private ArrayList<HubHomeFeaturedItems> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<HubHomeFeaturedItems> getItems() {
        return items;
    }

    public void setItems(ArrayList<HubHomeFeaturedItems> items) {
        this.items = items;
    }
}
