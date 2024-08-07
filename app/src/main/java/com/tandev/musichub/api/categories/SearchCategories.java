package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class SearchCategories extends Base {
    public SearchCategories() {
    }

    public Map<String, String> getSearchSuggestion() throws NoSuchAlgorithmException, IOException, Exception {
        try {
            String sig = createNoIdSig("/v1/web/ac-suggestions");
            Map<String, String> params = createRequest();
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
    public Map<String, String> getSearchFeatured() throws NoSuchAlgorithmException, IOException, Exception {
        try {
            String sig = createNoIdSig("/v1/web/featured");
            Map<String, String> params = createRequest();
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getSearchMulti(String q) throws Exception {
        try {
            String sig = createNoIdSig("/api/v2/search/multi");
            Map<String, String> params = createRequest();
            params.put("q", q);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getResultByType(String q, int type, int page) throws Exception {
        try {
            page = page != 0 ? page : 1;
            type = type != 0 ? type : 1;
            String typeSearch = "";
            Map<String, String> params = createRequest();
            switch (type) {
                case 1:
                    typeSearch = "song";
                    break;
                case 2:
                    typeSearch = "playlist";
                    break;
                case 3:
                    typeSearch = "artist";
                    break;
                case 4:
                    typeSearch = "video";
                    break;
                default:
                    throw new Exception("Invalid type");
            }
            String sig = createSearchSig("/api/v2/search", typeSearch, page);
            params.put("q", q);
            params.put("type",typeSearch);
            params.put("count", "18");
            params.put("page", String.valueOf(page));
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getRecommendKeyword() throws NoSuchAlgorithmException, IOException, Exception {
        try {
            String sig = createNoIdSig("/api/v2/app/get/recommend-keyword");
            Map<String, String> params = createRequest();
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
