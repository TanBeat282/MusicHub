package com.tandev.musichub.model.search.search_suggestion.playlist;

import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsArtist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsItem;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsGenres;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsVideo;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchSuggestionsDataItemSuggestionsPlaylist implements SearchSuggestionsDataItemSuggestionsItem, Serializable {
    private int type;
    private String id;
    private String title;
    private String thumb;
    private String shortDesc;
    private String preDesc;
    private String link;
    private int privacy;
    private long modifiedTime;
    private long cModifiedTime;
    private String releaseDate;
    private ArrayList<SearchSuggestionsGenres> genres;
    private int status;
    private ArrayList<SearchSuggestionsDataItemSuggestionsArtist> artists;
    private SearchSuggestionsUser user;
    private boolean isPR;
    private int boolAtt;
    private boolean isAlbum;
    private String tracking;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getPreDesc() {
        return preDesc;
    }

    public void setPreDesc(String preDesc) {
        this.preDesc = preDesc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public long getcModifiedTime() {
        return cModifiedTime;
    }

    public void setcModifiedTime(long cModifiedTime) {
        this.cModifiedTime = cModifiedTime;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<SearchSuggestionsGenres> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<SearchSuggestionsGenres> genres) {
        this.genres = genres;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<SearchSuggestionsDataItemSuggestionsArtist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<SearchSuggestionsDataItemSuggestionsArtist> artists) {
        this.artists = artists;
    }

    public SearchSuggestionsUser getUser() {
        return user;
    }

    public void setUser(SearchSuggestionsUser user) {
        this.user = user;
    }

    public boolean isPR() {
        return isPR;
    }

    public void setPR(boolean PR) {
        isPR = PR;
    }

    public int getBoolAtt() {
        return boolAtt;
    }

    public void setBoolAtt(int boolAtt) {
        this.boolAtt = boolAtt;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public void setAlbum(boolean album) {
        isAlbum = album;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
