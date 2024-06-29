package com.tandev.musichub.model.search.search_suggestion.suggestion;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchSuggestionsDataItemSuggestionsSong implements SearchSuggestionsDataItemSuggestionsItem, Serializable {
    private int type;
    private String title;
    private String id;
    private String radioPid;
    private boolean hasVideo;
    private String thumb;
    private String thumbVideo;
    private int duration;
    private String link;
    private long modifiedTime;
    private String lyricLink;
    private String lyricId;
    private String downloadTypes;
    private String orgMD5;
    private int userId;
    private String euId;
    private int privacy;
    private int hLyricVersion;
    private int downloadStatus;
    private int status;
    private int playStatus;
    private SearchSuggestionsVideo video;
    private ArrayList<SearchSuggestionsDataItemSuggestionsArtist> artists;
    private ArrayList<SearchSuggestionsGenres> genres;
    private int disSPlatform;
    private int disDPlatform;
    private int boolAtt;
    private String tracking;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRadioPid() {
        return radioPid;
    }

    public void setRadioPid(String radioPid) {
        this.radioPid = radioPid;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbVideo() {
        return thumbVideo;
    }

    public void setThumbVideo(String thumbVideo) {
        this.thumbVideo = thumbVideo;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getLyricLink() {
        return lyricLink;
    }

    public void setLyricLink(String lyricLink) {
        this.lyricLink = lyricLink;
    }

    public String getLyricId() {
        return lyricId;
    }

    public void setLyricId(String lyricId) {
        this.lyricId = lyricId;
    }

    public String getDownloadTypes() {
        return downloadTypes;
    }

    public void setDownloadTypes(String downloadTypes) {
        this.downloadTypes = downloadTypes;
    }

    public String getOrgMD5() {
        return orgMD5;
    }

    public void setOrgMD5(String orgMD5) {
        this.orgMD5 = orgMD5;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEuId() {
        return euId;
    }

    public void setEuId(String euId) {
        this.euId = euId;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public int gethLyricVersion() {
        return hLyricVersion;
    }

    public void sethLyricVersion(int hLyricVersion) {
        this.hLyricVersion = hLyricVersion;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
    }

    public SearchSuggestionsVideo getVideo() {
        return video;
    }

    public void setVideo(SearchSuggestionsVideo video) {
        this.video = video;
    }

    public ArrayList<SearchSuggestionsDataItemSuggestionsArtist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<SearchSuggestionsDataItemSuggestionsArtist> artists) {
        this.artists = artists;
    }

    public ArrayList<SearchSuggestionsGenres> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<SearchSuggestionsGenres> genres) {
        this.genres = genres;
    }

    public int getDisSPlatform() {
        return disSPlatform;
    }

    public void setDisSPlatform(int disSPlatform) {
        this.disSPlatform = disSPlatform;
    }

    public int getDisDPlatform() {
        return disDPlatform;
    }

    public void setDisDPlatform(int disDPlatform) {
        this.disDPlatform = disDPlatform;
    }

    public int getBoolAtt() {
        return boolAtt;
    }

    public void setBoolAtt(int boolAtt) {
        this.boolAtt = boolAtt;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
