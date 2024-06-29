package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.util.Map;


public class HubCategories extends Base {

    public HubCategories() {
    }

    public Map<String, String> getHub(String id) throws Exception {
        try {
            String sig = createIdSig("/api/v2/page/get/hub-detail", id);
            Map<String, String> params = createRequest();
            params.put("id", id);
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
    public Map<String, String> getHubHome() throws Exception {
        try {
            String sig = createNoIdSig("/api/v2/page/get/hub-home");
            Map<String, String> params = createRequest();
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
