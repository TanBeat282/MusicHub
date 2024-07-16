package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.util.Map;


public class SongCategories extends Base {

    public SongCategories() {
    }

    public Map<String, String> getAudio(String songId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/song/get/streaming", songId);
            Map<String, String> params = createRequest();
            params.put("id", songId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
    public Map<String, String> getPremiumAudio(String songId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/song/get/vip-preview-info", songId);
            Map<String, String> params = createRequest();
            params.put("id", songId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getInfo(String songId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/song/get/info", songId);
            Map<String, String> params = createRequest();
            params.put("id", songId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getLyrics(String songId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/lyric/get/lyric", songId);
            Map<String, String> params = createRequest();
            params.put("id", songId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getArtist(String artistId) throws Exception {
        try {
            String sig = createNoIdSig("/api/v2/page/get/artist");
            Map<String, String> params = createRequest();
            params.put("alias", artistId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getPlaylist(String artistId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/page/get/playlist", artistId);
            Map<String, String> params = createRequest();
            params.put("id", artistId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getAlbum(String artistId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/page/get/album", artistId);
            Map<String, String> params = createRequest();
            params.put("id", artistId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getNewRelease(String type) throws Exception {
        //  String type = song or album
        try {
            String sig = createNewReleaseSig("/api/v2/chart/get/new-release", type);
            Map<String, String> params = createRequest();
            params.put("type", type);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getSectionBottom(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/playlist/get/section-bottom", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getSongRecommend(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/recommend/get/songs", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
