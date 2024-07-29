package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.util.Map;

public class VideoCategories extends Base {
    public VideoCategories() {
    }

    public Map<String, String> getRelatedVideos(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/video/get/section-relate", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getVideo(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/page/get/video", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
