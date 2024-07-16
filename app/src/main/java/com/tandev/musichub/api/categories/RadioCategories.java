package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.util.Map;


public class RadioCategories extends Base {

    public RadioCategories() {
    }

    public Map<String, String> getUserActiveRadio(String ids) throws Exception {
        try {
            String sig = createNoIdSig("/api/v2/livestream/get/active-user");
            Map<String, String> params = createRequest();
            params.put("ids", ids);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getInfoRadio(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/livestream/get/info", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getProgramDetailRadio(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/livestream/program/get/detail", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
    public Map<String, String> getALlCommentRadio(String id) throws Exception {
        try {
            String sig = createCommentSig("/api/v2/download/livestream/get/comments", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
    public Map<String, String> getRefreshCommentRadio(String id) throws Exception {
        try {
            String sig = createCommentSig("/api/v2/download/livestream/get/comments", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
    public Map<String, String> getAnnouncementRadio(String id) throws Exception {
        try {
            String sig = createAnnouncementSig("/api/v2/download/livestream/get/announcement", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

}
