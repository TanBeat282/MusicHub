package com.tandev.musichub.model.search.search_multil;

import com.tandev.musichub.model.chart.chart_home.Album;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.PreviewInfo;
import com.tandev.musichub.model.search.search_multil.search_top.SearchMultiDataTopArtist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchMultiDataTop implements Serializable {
    private String id;
    private String name;
    private boolean spotlight;
    private String playlistId;
    private String cover;

    private String encodeId;
    private String title;
    private String alias;
    private boolean isOffical;
    private String username;
    private String artistsNames;
    private ArrayList<Artists> artists;
    private boolean isWorldWide;
    private String thumbnailM;
    private String link;
    private String thumbnail;
    private int duration;
    private boolean zingChoice;
    private boolean isPrivate;
    private boolean preRelease;
    private String releaseDate;
    private ArrayList<String> genreIds;
    private String distributor;
    private ArrayList<String> indicators;
    private boolean isIndie;
    private int streamingStatus;
    private boolean allowAudioAds;
    private boolean hasLyric;
    private String objectType;

    private String sortDescription;
    private String releasedAt;
    private boolean PR;
    private int playItemMode;
    private int subType;
    private int uid;
    private boolean isShuffle;
    private String userName;
    private boolean isAlbum;
    private String textType;
    private String isSingle;

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

    public boolean isSpotlight() {
        return spotlight;
    }

    public void setSpotlight(boolean spotlight) {
        this.spotlight = spotlight;
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

    public String getEncodeId() {
        return encodeId;
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
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

    public boolean isOffical() {
        return isOffical;
    }

    public void setOffical(boolean offical) {
        isOffical = offical;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getArtistsNames() {
        return artistsNames;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
    }

    public ArrayList<Artists> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artists> artists) {
        this.artists = artists;
    }

    public boolean isWorldWide() {
        return isWorldWide;
    }

    public void setWorldWide(boolean worldWide) {
        isWorldWide = worldWide;
    }

    public String getThumbnailM() {
        return thumbnailM;
    }

    public void setThumbnailM(String thumbnailM) {
        this.thumbnailM = thumbnailM;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isZingChoice() {
        return zingChoice;
    }

    public void setZingChoice(boolean zingChoice) {
        this.zingChoice = zingChoice;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isPreRelease() {
        return preRelease;
    }

    public void setPreRelease(boolean preRelease) {
        this.preRelease = preRelease;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(ArrayList<String> genreIds) {
        this.genreIds = genreIds;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public ArrayList<String> getIndicators() {
        return indicators;
    }

    public void setIndicators(ArrayList<String> indicators) {
        this.indicators = indicators;
    }

    public boolean isIndie() {
        return isIndie;
    }

    public void setIndie(boolean indie) {
        isIndie = indie;
    }

    public int getStreamingStatus() {
        return streamingStatus;
    }

    public void setStreamingStatus(int streamingStatus) {
        this.streamingStatus = streamingStatus;
    }

    public boolean isAllowAudioAds() {
        return allowAudioAds;
    }

    public void setAllowAudioAds(boolean allowAudioAds) {
        this.allowAudioAds = allowAudioAds;
    }

    public boolean isHasLyric() {
        return hasLyric;
    }

    public void setHasLyric(boolean hasLyric) {
        this.hasLyric = hasLyric;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getSortDescription() {
        return sortDescription;
    }

    public void setSortDescription(String sortDescription) {
        this.sortDescription = sortDescription;
    }

    public String getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(String releasedAt) {
        this.releasedAt = releasedAt;
    }

    public boolean isPR() {
        return PR;
    }

    public void setPR(boolean PR) {
        this.PR = PR;
    }

    public int getPlayItemMode() {
        return playItemMode;
    }

    public void setPlayItemMode(int playItemMode) {
        this.playItemMode = playItemMode;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public void setAlbum(boolean album) {
        isAlbum = album;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(String isSingle) {
        this.isSingle = isSingle;
    }
}
