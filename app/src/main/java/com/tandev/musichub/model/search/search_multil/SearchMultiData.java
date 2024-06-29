package com.tandev.musichub.model.search.search_multil;

import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchMultiData implements Serializable {
    private SearchMultiDataTop top;
    private ArrayList<Artists> artists;
    private ArrayList<Items> songs;
    private ArrayList<SearchMultiDataVideo> videos;
    private ArrayList<DataPlaylist> playlists;
    private SearchMultiDataCounter counter;
    private String sectionId;

    public SearchMultiDataTop getTop() {
        return top;
    }

    public void setTop(SearchMultiDataTop top) {
        this.top = top;
    }

    public ArrayList<Artists> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artists> artists) {
        this.artists = artists;
    }

    public ArrayList<Items> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Items> songs) {
        this.songs = songs;
    }

    public ArrayList<SearchMultiDataVideo> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<SearchMultiDataVideo> videos) {
        this.videos = videos;
    }

    public ArrayList<DataPlaylist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<DataPlaylist> playlists) {
        this.playlists = playlists;
    }

    public SearchMultiDataCounter getCounter() {
        return counter;
    }

    public void setCounter(SearchMultiDataCounter counter) {
        this.counter = counter;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
