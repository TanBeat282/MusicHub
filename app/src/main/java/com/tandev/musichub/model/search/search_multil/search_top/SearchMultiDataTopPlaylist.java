package com.tandev.musichub.model.search.search_multil.search_top;

import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.playlist.DataItems;
import com.tandev.musichub.model.song.Genres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchMultiDataTopPlaylist implements Serializable {
    private String encodeId;
    private String title;
    private String thumbnail;
    private boolean isoffical;
    private String link;
    private boolean isIndie;
    private String releaseDate;
    private String sortDescription;
    private long releasedAt;
    private List<String> genreIds;
    private boolean PR;
    private ArrayList<Artists> artists;
    private String artistsNames;
    private int playItemMode;
    private int subType;
    private int uid;
    private String thumbnailM;
    private boolean isShuffle;
    private boolean isPrivate;
    private String userName;
    private boolean isAlbum;
    private String textType;
    private boolean isSingle;
    private String distributor;
    private String description;
    private String aliasTitle;
    private String sectionId;
    private long contentLastUpdate;
    private Artists artist;
    private ArrayList<Genres> genres;
    private DataItems song;
    private int like;
    private int listen;
    private boolean liked;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isIsoffical() {
        return isoffical;
    }

    public void setIsoffical(boolean isoffical) {
        this.isoffical = isoffical;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isIndie() {
        return isIndie;
    }

    public void setIndie(boolean indie) {
        isIndie = indie;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSortDescription() {
        return sortDescription;
    }

    public void setSortDescription(String sortDescription) {
        this.sortDescription = sortDescription;
    }

    public long getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(long releasedAt) {
        this.releasedAt = releasedAt;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public boolean isPR() {
        return PR;
    }

    public void setPR(boolean PR) {
        this.PR = PR;
    }

    public ArrayList<Artists> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<Artists> artists) {
        this.artists = artists;
    }

    public String getArtistsNames() {
        return artistsNames;
    }

    public void setArtistsNames(String artistsNames) {
        this.artistsNames = artistsNames;
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

    public String getThumbnailM() {
        return thumbnailM;
    }

    public void setThumbnailM(String thumbnailM) {
        this.thumbnailM = thumbnailM;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAliasTitle() {
        return aliasTitle;
    }

    public void setAliasTitle(String aliasTitle) {
        this.aliasTitle = aliasTitle;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public long getContentLastUpdate() {
        return contentLastUpdate;
    }

    public void setContentLastUpdate(long contentLastUpdate) {
        this.contentLastUpdate = contentLastUpdate;
    }

    public Artists getArtist() {
        return artist;
    }

    public void setArtist(Artists artist) {
        this.artist = artist;
    }

    public ArrayList<Genres> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genres> genres) {
        this.genres = genres;
    }

    public DataItems getSong() {
        return song;
    }

    public void setSong(DataItems song) {
        this.song = song;
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
}
