package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.util.Map;


public class ArtistCategories extends Base {

    public ArtistCategories() {
        super();
    }

    public Map<String, String> getArtist2(String artistId) throws Exception {
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

    public Map<String, String> getSongListOfArtist(String id, String page) throws Exception {
        try {
            String sig = createSongListOfArtistSig("/api/v2/song/get/list", id, page);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("page", page);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
