package com.tandev.musichub.model.genre;

import java.io.Serializable;
import java.util.ArrayList;

public class DataGenreInfo implements Serializable {
    private String id;
    private String name;
    private String title;
    private String alias;
    private String link;
    private ItemGenreInfo parent;
    private ArrayList<ItemGenreInfo> childs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ItemGenreInfo getParent() {
        return parent;
    }

    public void setParent(ItemGenreInfo parent) {
        this.parent = parent;
    }

    public ArrayList<ItemGenreInfo> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<ItemGenreInfo> childs) {
        this.childs = childs;
    }
}
