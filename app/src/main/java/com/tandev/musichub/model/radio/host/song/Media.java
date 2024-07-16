package com.tandev.musichub.model.radio.host.song;

import java.io.Serializable;
import java.util.List;

public class Media implements Serializable {
    private String encodeId;
    private String title;
    private String alias;
    private boolean isOffical;
    private String username;
    private String artistsNames;
    private String artists;
    private boolean isWorldWide;
    private String thumbnailM;
    private String link;
    private String thumbnail;
    private int duration;
    private boolean zingChoice;
    private boolean isPrivate;
    private boolean preRelease;
    private long releaseDate;
    private List<String> genreIds;
    private String distributor;
    private List<Object> indicators;
    private int radioId;
    private boolean isIndie;
    private String mvlink;
    private int streamingStatus;
    private boolean allowAudioAds;
    private boolean hasLyric;

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

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
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

    public long getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public List<Object> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Object> indicators) {
        this.indicators = indicators;
    }

    public int getRadioId() {
        return radioId;
    }

    public void setRadioId(int radioId) {
        this.radioId = radioId;
    }

    public boolean isIndie() {
        return isIndie;
    }

    public void setIndie(boolean indie) {
        isIndie = indie;
    }

    public String getMvlink() {
        return mvlink;
    }

    public void setMvlink(String mvlink) {
        this.mvlink = mvlink;
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
}
