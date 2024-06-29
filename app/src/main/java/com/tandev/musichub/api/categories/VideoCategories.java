package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.util.Map;

public class VideoCategories extends Base {
    public VideoCategories() {
    }

    public Map<String, String> getRelatedVideos(String videoId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/video/get/section-relate", videoId);
            Map<String, String> params = createRequest();
            params.put("videoId", videoId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }

    public Map<String, String> getDetail(String videoId) throws Exception {
        try {
            String sig = createIdSig("/api/v2/page/get/video", videoId);
            Map<String, String> params = createRequest();
            params.put("videoId", videoId);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
