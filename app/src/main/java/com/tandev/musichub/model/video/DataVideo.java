package com.tandev.musichub.model.video;

import com.tandev.musichub.model.chart.chart_home.Album;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.genre.ItemGenreInfo;
import com.tandev.musichub.model.hub.HubVideo;

import java.io.Serializable;
import java.util.ArrayList;

public class DataVideo implements Serializable {
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
    public String thumbnail;
    private int duration;
    private int streamingStatus;
    private Artists artist;
    private int startTime;
    private int endTime;
    private String statusName;
    private int statusCode;
    private long createdAt;
    private boolean disabledAds;
    private String privacy;
    private String lyric;
    private Items song;
    private ArrayList<ItemGenreInfo> genres;
    private ArrayList<Artists> composers;
    private Album album;
    private ArrayList<DataVideoLyric> lyrics;
    private ArrayList<HubVideo> recommends;
    private int like;
    private int listen;
    private boolean liked;
    private int comment;
    private DataVideoStreaming streaming;

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

    public int getStreamingStatus() {
        return streamingStatus;
    }

    public void setStreamingStatus(int streamingStatus) {
        this.streamingStatus = streamingStatus;
    }

    public Artists getArtist() {
        return artist;
    }

    public void setArtist(Artists artist) {
        this.artist = artist;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDisabledAds() {
        return disabledAds;
    }

    public void setDisabledAds(boolean disabledAds) {
        this.disabledAds = disabledAds;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Items getSong() {
        return song;
    }

    public void setSong(Items song) {
        this.song = song;
    }

    public ArrayList<ItemGenreInfo> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<ItemGenreInfo> genres) {
        this.genres = genres;
    }

    public ArrayList<Artists> getComposers() {
        return composers;
    }

    public void setComposers(ArrayList<Artists> composers) {
        this.composers = composers;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public ArrayList<DataVideoLyric> getLyrics() {
        return lyrics;
    }

    public void setLyrics(ArrayList<DataVideoLyric> lyrics) {
        this.lyrics = lyrics;
    }

    public ArrayList<HubVideo> getRecommends() {
        return recommends;
    }

    public void setRecommends(ArrayList<HubVideo> recommends) {
        this.recommends = recommends;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getListen() {
        return listen;
    }

    public void setListen(int listen) {
        this.listen = listen;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public DataVideoStreaming getStreaming() {
        return streaming;
    }

    public void setStreaming(DataVideoStreaming streaming) {
        this.streaming = streaming;
    }
}
