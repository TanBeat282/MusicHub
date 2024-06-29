package com.tandev.musichub.api.categories;

import com.tandev.musichub.api.base.Base;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HomeCategories extends Base {
    public HomeCategories() {
    }

    // home
    public Map<String, String> getChartHome() throws NoSuchAlgorithmException, IOException, Exception {
        try {
            String sig = createNoIdSig("/api/v2/page/get/chart-home");

            Map<String, String> params = createRequest();
            params.put("sig", sig);
            return params;
        } catch (Exception error) {
            throw error;
        }
    }
}
