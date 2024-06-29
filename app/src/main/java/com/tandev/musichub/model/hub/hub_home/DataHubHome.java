package com.tandev.musichub.model.hub.hub_home;

import com.tandev.musichub.model.hub.hub_home.banner.HubHomeBanner;
import com.tandev.musichub.model.hub.hub_home.featured.HubHomeFeatured;
import com.tandev.musichub.model.hub.hub_home.genre.HubHomeGenre;
import com.tandev.musichub.model.hub.hub_home.nations.HubHomeNations;
import com.tandev.musichub.model.hub.hub_home.topTopic.HubHomeTopTopic;
import com.tandev.musichub.model.hub.hub_home.topic.HubHomeTopic;

import java.util.ArrayList;

public class DataHubHome {
    private ArrayList<HubHomeBanner> banners;
    private HubHomeFeatured featured;
    private ArrayList<HubHomeTopTopic> topTopic;
    private ArrayList<HubHomeTopic> topic;
    private ArrayList<HubHomeNations> nations;
    private ArrayList<HubHomeGenre> genre;
    private String sectionId;

    public ArrayList<HubHomeBanner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<HubHomeBanner> banners) {
        this.banners = banners;
    }

    public HubHomeFeatured getFeatured() {
        return featured;
    }

    public void setFeatured(HubHomeFeatured featured) {
        this.featured = featured;
    }

    public ArrayList<HubHomeTopTopic> getTopTopic() {
        return topTopic;
    }

    public void setTopTopic(ArrayList<HubHomeTopTopic> topTopic) {
        this.topTopic = topTopic;
    }

    public ArrayList<HubHomeTopic> getTopic() {
        return topic;
    }

    public void setTopic(ArrayList<HubHomeTopic> topic) {
        this.topic = topic;
    }

    public ArrayList<HubHomeNations> getNations() {
        return nations;
    }

    public void setNations(ArrayList<HubHomeNations> nations) {
        this.nations = nations;
    }

    public ArrayList<HubHomeGenre> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<HubHomeGenre> genre) {
        this.genre = genre;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
