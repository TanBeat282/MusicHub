package com.tandev.musichub.api;

import com.tandev.musichub.model.chart.chart_home.ChartHome;
import com.tandev.musichub.model.chart.new_release.NewRelease;
import com.tandev.musichub.model.chart.weekchart.WeekChart;
import com.tandev.musichub.model.genre.GenreInfo;
import com.tandev.musichub.model.hub.hub_home.HubHome;
import com.tandev.musichub.model.new_release.NewReleaseAlbum;
import com.tandev.musichub.model.new_release.NewReleaseSong;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.preview_premium.PreviewPremium;
import com.tandev.musichub.model.radio.host.announcement.AnnouncementRadio;
import com.tandev.musichub.model.radio.host.detail.HostDetail;
import com.tandev.musichub.model.radio.host.info.HostInfo;
import com.tandev.musichub.model.radio.user.comment.CommentUserRadio;
import com.tandev.musichub.model.search.search_multil.SearchMulti;
import com.tandev.musichub.model.search.search_recommend.SearchRecommend;
import com.tandev.musichub.model.search.search_suggestion.SearchSuggestions;
import com.tandev.musichub.model.search.song.SearchSong;
import com.tandev.musichub.model.song.Lyric;
import com.tandev.musichub.model.song.SongAudio;
import com.tandev.musichub.model.song.SongDetail;
import com.tandev.musichub.model.song.song_recommend.SongRecommend;
import com.tandev.musichub.model.song_of_artist.SongOfArtist;
import com.tandev.musichub.model.top100.Top100;
import com.tandev.musichub.model.user_active_radio.UserActiveRadio;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String pathChartHome = "/api/v2/page/get/chart-home?";
    String pathHome = "/api/v2/page/get/home?";
    String pathChartNewRelease = "/api/v2/page/get/newrelease-chart?";
    String pathNewRelease = "/api/v2/chart/get/new-release?";
    String pathWeekChart = "/api/v2/page/get/week-chart?";
    String pathSectionBottom = "/api/v2/playlist/get/section-bottom?";
    String pathTop100 = "/api/v2/page/get/top-100?";
    String pathDetailSong = "/api/v2/song/get/info?";
    String pathAudioSong = "/api/v2/song/get/streaming?";
    String pathPreviewPremium = "/api/v2/song/get/vip-preview-info?";
    String pathSongRecommend = "/api/v2/recommend/get/songs?";
    String pathGenreInfo = "/api/v2/genre/get/info?";
    String pathArtist = "/api/v2/page/get/artist?";
    String pathSongListOfArtist = "/api/v2/song/get/list?";
    String pathPlaylist = "/api/v2/page/get/playlist?";
    String pathAlbum = "/api/v2/page/get/album?";
    String pathLyric = "/api/v2/lyric/get/lyric?";

    //search
    String pathSearchMulti = "/api/v2/search/multi?";
    String pathSearchType = "/api/v2/search?";
    String pathSearchSuggestion = "/v1/web/ac-suggestions?";
    String pathSearchRecommend = "/api/v2/app/get/recommend-keyword?";


    //radio
    String pathUserActiveRadio = "/api/v2/livestream/get/active-user?";
    String pathInfoRadio = "/api/v2/livestream/get/info?";
    String pathProgramDetailRadio = "/api/v2/livestream/program/get/detail?";
    String pathCommentRadio = "/api/v2/download/livestream/get/comments?";
    String pathAnnouncementRadio = "/api/v2/download/livestream/get/announcement?";

    //hub
    String pathHub = "/api/v2/page/get/hub-detail?";

    //pathHubHome
    String pathHubHome = "/api/v2/page/get/hub-home?";


    //HOME
    @GET(pathChartHome)
    Call<ChartHome> CHART_HOME_CALL(@Query("sig") String sig,
                                    @Query("ctime") String ctime,
                                    @Query("version") String version,
                                    @Query("apiKey") String apiKey);

    @GET(pathHome)
    Call<ResponseBody> HOME_CALL(@Query("page") String page,
                                 @Query("count") String count,
                                 @Query("sig") String sig,
                                 @Query("ctime") String ctime,
                                 @Query("version") String version,
                                 @Query("apiKey") String apiKey);

    @GET(pathChartNewRelease)
    Call<NewRelease> CHART_NEW_RELEASE_CALL(@Query("sig") String sig,
                                            @Query("ctime") String ctime,
                                            @Query("version") String version,
                                            @Query("apiKey") String apiKey);

    @GET(pathNewRelease)
    Call<NewReleaseSong> NEW_RELEASE_SONG_CALL(@Query("type") String type,
                                               @Query("sig") String sig,
                                               @Query("ctime") String ctime,
                                               @Query("version") String version,
                                               @Query("apiKey") String apiKey);

    @GET(pathNewRelease)
    Call<NewReleaseAlbum> NEW_RELEASE_ALBUM_CALL(@Query("type") String type,
                                                 @Query("sig") String sig,
                                                 @Query("ctime") String ctime,
                                                 @Query("version") String version,
                                                 @Query("apiKey") String apiKey);

    @GET(pathWeekChart)
    Call<WeekChart> WEEK_CHART_CALL(@Query("id") String id,
                                    @Query("week") String week,
                                    @Query("year") String year,
                                    @Query("sig") String sig,
                                    @Query("ctime") String ctime,
                                    @Query("version") String version,
                                    @Query("apiKey") String apiKey);

    @GET(pathSectionBottom)
    Call<ResponseBody> SECTION_BOTTOM_CALL(@Query("id") String id,
                                           @Query("sig") String sig,
                                           @Query("ctime") String ctime,
                                           @Query("version") String version,
                                           @Query("apiKey") String apiKey);

    @GET(pathTop100)
    Call<Top100> TOP100_CALL(@Query("sig") String sig,
                             @Query("ctime") String ctime,
                             @Query("version") String version,
                             @Query("apiKey") String apiKey);


    //SONG
    @GET(pathDetailSong)
    Call<SongDetail> SONG_DETAIL_CALL(@Query("id") String id,
                                      @Query("sig") String sig,
                                      @Query("ctime") String ctime,
                                      @Query("version") String version,
                                      @Query("apiKey") String apiKey);

    // get link song audio
    @GET(pathAudioSong)
    Call<SongAudio> SONG_AUDIO_CALL(@Query("id") String id,
                                    @Query("sig") String sig,
                                    @Query("ctime") String ctime,
                                    @Query("version") String version,
                                    @Query("apiKey") String apiKey);


    // preview song premium
    @GET(pathPreviewPremium)
    Call<PreviewPremium> PREVIEW_PREMIUM_CALL(@Query("id") String id,
                                              @Query("sig") String sig,
                                              @Query("ctime") String ctime,
                                              @Query("version") String version,
                                              @Query("apiKey") String apiKey);

    //get song recommend
    @GET(pathSongRecommend)
    Call<SongRecommend> SONG_RECOMMEND_CALL(@Query("id") String id,
                                            @Query("sig") String sig,
                                            @Query("ctime") String ctime,
                                            @Query("version") String version,
                                            @Query("apiKey") String apiKey);


    // get info genre
    @GET(pathGenreInfo)
    Call<GenreInfo> GENRE_INFO_CALL(@Query("id") String id,
                                    @Query("type") String type,
                                    @Query("sig") String sig,
                                    @Query("ctime") String ctime,
                                    @Query("version") String version,
                                    @Query("apiKey") String apiKey);


    //get link lyric
    @GET(pathLyric)
    Call<Lyric> LYRIC_CALL(@Query("id") String id,
                           @Query("sig") String sig,
                           @Query("ctime") String ctime,
                           @Query("version") String version,
                           @Query("apiKey") String apiKey);


    //get detail artist
    @GET(pathArtist)
    Call<ResponseBody> ARTISTS_CALL(@Query("alias") String alias,
                                    @Query("sig") String sig,
                                    @Query("ctime") String ctime,
                                    @Query("version") String version,
                                    @Query("apiKey") String apiKey);


    // get song of artist
    @GET(pathSongListOfArtist)
    Call<SongOfArtist> SONG_LIST_OF_ARTIST_CALL(@Query("id") String id,
                                                @Query("type") String type,
                                                @Query("page") String page,
                                                @Query("count") String count,
                                                @Query("sort") String sort,
                                                @Query("sectionId") String sectionId,
                                                @Query("sig") String sig,
                                                @Query("ctime") String ctime,
                                                @Query("version") String version,
                                                @Query("apiKey") String apiKey);

    // get playlist
    @GET(pathPlaylist)
    Call<Playlist> PLAYLIST_CALL(@Query("id") String id,
                                 @Query("sig") String sig,
                                 @Query("ctime") String ctime,
                                 @Query("version") String version,
                                 @Query("apiKey") String apiKey);


    // get album
    @GET(pathAlbum)
    Call<com.tandev.musichub.model.album.Album> ALBUM_CALL(@Query("id") String id,
                                                           @Query("sig") String sig,
                                                           @Query("ctime") String ctime,
                                                           @Query("version") String version,
                                                           @Query("apiKey") String apiKey);


    //SEARCH
    @GET(pathSearchSuggestion)
    Call<ResponseBody> SEARCH_SUGGESTIONS_CALL(@Query("num") String num,
                                               @Query("query") String query,
                                               @Query("language") String language,
                                               @Query("sig") String sig,
                                               @Query("ctime") String ctime,
                                               @Query("version") String version,
                                               @Query("apiKey") String apiKey);

    @GET(pathSearchMulti)
    Call<SearchMulti> SEARCH_MULTI_CALL(@Query("q") String q,
                                        @Query("allowCorrect") String allowCorrect,
                                        @Query("sig") String sig,
                                        @Query("ctime") String ctime,
                                        @Query("version") String version,
                                        @Query("apiKey") String apiKey);

    @GET(pathSearchType)
    Call<ResponseBody> SEARCH_TYPE_CALL(@Query("q") String q,
                                        @Query("type") String type,
                                        @Query("page") String page,
                                        @Query("count") String count,
                                        @Query("allowCorrect") String allowCorrect,
                                        @Query("sig") String sig,
                                        @Query("ctime") String ctime,
                                        @Query("version") String version,
                                        @Query("apiKey") String apiKey);

    @GET(pathSearchRecommend)
    Call<SearchRecommend> SEARCH_RECOMMEND_CALL(@Query("sig") String sig,
                                                @Query("ctime") String ctime,
                                                @Query("version") String version,
                                                @Query("apiKey") String apiKey);


    //RADIO
    @GET(pathUserActiveRadio)
    Call<UserActiveRadio> USER_ACTIVE_RADIO_CALL(@Query("ids") String ids,
                                                 @Query("sig") String sig,
                                                 @Query("ctime") String ctime,
                                                 @Query("version") String version,
                                                 @Query("apiKey") String apiKey);

    @GET(pathInfoRadio)
    Call<HostInfo> INFO_RADIO_CALL(@Query("id") String id,
                                   @Query("sig") String sig,
                                   @Query("ctime") String ctime,
                                   @Query("version") String version,
                                   @Query("apiKey") String apiKey);

    @GET(pathProgramDetailRadio)
    Call<HostDetail> PROGRAM_DETAIL_RADIO_CALL(@Query("id") String id,
                                               @Query("sig") String sig,
                                               @Query("ctime") String ctime,
                                               @Query("version") String version,
                                               @Query("apiKey") String apiKey);

    @GET(pathCommentRadio)
    Call<CommentUserRadio> COMMENT_RADIO_CALL(@Query("id") String id,
                                              @Query("count") String count,
                                              @Query("cmtType") String cmtType,
                                              @Query("sig") String sig,
                                              @Query("ctime") String ctime,
                                              @Query("version") String version,
                                              @Query("apiKey") String apiKey);

    @GET(pathCommentRadio)
    Call<CommentUserRadio> REFRESH_COMMENT_RADIO_CALL(@Query("id") String id,
                                                      @Query("commentId") String commentId,
                                                      @Query("getType") String getType,
                                                      @Query("count") String count,
                                                      @Query("cmtType") String cmtType,
                                                      @Query("sig") String sig,
                                                      @Query("ctime") String ctime,
                                                      @Query("version") String version,
                                                      @Query("apiKey") String apiKey);

    @GET(pathAnnouncementRadio)
    Call<AnnouncementRadio> ANNOUNCEMENT_RADIO_CALL(@Query("id") String id,
                                                    @Query("commentId") String commentId,
                                                    @Query("getType") String getType,
                                                    @Query("count") String count,
                                                    @Query("sig") String sig,
                                                    @Query("ctime") String ctime,
                                                    @Query("version") String version,
                                                    @Query("apiKey") String apiKey);


    //HUB
    @GET(pathHub)
    Call<ResponseBody> HUB_DETAIL_CALL(@Query("id") String id,
                                       @Query("sig") String sig,
                                       @Query("ctime") String ctime,
                                       @Query("version") String version,
                                       @Query("apiKey") String apiKey);

    //HUB
    @GET(pathHubHome)
    Call<HubHome> HUB_HOME_CALL(@Query("sig") String sig,
                                @Query("ctime") String ctime,
                                @Query("version") String version,
                                @Query("apiKey") String apiKey);


    // Method để thiết lập cookie
    void setCookie(String cookie);
}
