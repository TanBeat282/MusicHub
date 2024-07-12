package com.tandev.musichub.model.chart.chart_home;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Items implements Serializable {
    private String encodeId;
    private String title;
    private String alias;
    private boolean isOffical;
    private String username;
    private String artistsNames;
    private ArrayList<Artists> artists;
    private boolean isWorldWide;
    private PreviewInfo previewInfo;
    private String thumbnailM;
    private String link;
    private String thumbnail;
    private int duration;
    private boolean zingChoice;
    private boolean isPrivate;
    private boolean preRelease;
    private String releaseDate;
    private List<String> genreIds;
    private Album album;
    private String distributor;
    private boolean isIndie;
    private int streamingStatus;
    private boolean allowAudioAds;
    private boolean hasLyric;
    private int rakingStatus;
    private int score;
    private int totalTopZing;
    private Artists artist;
    private int historyCount;

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

    public PreviewInfo getPreviewInfo() {
        return previewInfo;
    }

    public void setPreviewInfo(PreviewInfo previewInfo) {
        this.previewInfo = previewInfo;
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

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
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

    public int getRakingStatus() {
        return rakingStatus;
    }

    public void setRakingStatus(int rakingStatus) {
        this.rakingStatus = rakingStatus;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalTopZing() {
        return totalTopZing;
    }

    public void setTotalTopZing(int totalTopZing) {
        this.totalTopZing = totalTopZing;
    }

    public Artists getArtist() {
        return artist;
    }

    public void setArtist(Artists artist) {
        this.artist = artist;
    }

    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int historyCount) {
        this.historyCount = historyCount;
    }

    public static Items fromJson(String json) {
        return new Gson().fromJson(json, Items.class);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Items items = (Items) o;
        return Objects.equals(encodeId, items.encodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encodeId);
    }
}
