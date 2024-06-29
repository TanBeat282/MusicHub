package com.tandev.musichub.model.artist;

import java.io.Serializable;
import java.util.ArrayList;

public class DataArtist implements Serializable {
    private String id;
    private String name;
    private String link;
    private boolean spotlight;
    private String alias;
    private String playlistId;
    private String cover;
    private String thumbnail;
    private String biography;
    private String sortBiography;
    private String thumbnailM;
    private String national;
    private String birthday;
    private String realname;
    private int totalFollow;
    private int follow;
    private String oalink;
    private int oaid;
    private ArrayList<SectionArtist> sections;
    private String sectionId;
    private boolean isOABrand;
    private ArrayList<Integer> tabs;
    private boolean hasOA;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSpotlight() {
        return spotlight;
    }

    public void setSpotlight(boolean spotlight) {
        this.spotlight = spotlight;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSortBiography() {
        return sortBiography;
    }

    public void setSortBiography(String sortBiography) {
        this.sortBiography = sortBiography;
    }

    public String getThumbnailM() {
        return thumbnailM;
    }

    public void setThumbnailM(String thumbnailM) {
        this.thumbnailM = thumbnailM;
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getTotalFollow() {
        return totalFollow;
    }

    public void setTotalFollow(int totalFollow) {
        this.totalFollow = totalFollow;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getOalink() {
        return oalink;
    }

    public void setOalink(String oalink) {
        this.oalink = oalink;
    }

    public int getOaid() {
        return oaid;
    }

    public void setOaid(int oaid) {
        this.oaid = oaid;
    }

    public ArrayList<SectionArtist> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionArtist> sections) {
        this.sections = sections;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public boolean isOABrand() {
        return isOABrand;
    }

    public void setOABrand(boolean OABrand) {
        isOABrand = OABrand;
    }

    public ArrayList<Integer> getTabs() {
        return tabs;
    }

    public void setTabs(ArrayList<Integer> tabs) {
        this.tabs = tabs;
    }

    public boolean isHasOA() {
        return hasOA;
    }

    public void setHasOA(boolean hasOA) {
        this.hasOA = hasOA;
    }
}
