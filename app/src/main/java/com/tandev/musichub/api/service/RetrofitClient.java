package com.tandev.musichub.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.uliti.AddCookiesInterceptor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static volatile ApiService apiServiceInstance;


    private static String getCookie() throws IOException {
        URL url = new URL(Constants.BASE_URL_MOBILE);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        Map<String, List<String>> headers = connection.getHeaderFields();
        StringBuilder cookie = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase("Set-Cookie")) {
                for (String value : entry.getValue()) {
                    cookie.append(value).append("; ");
                }
            }
        }
        return cookie.toString();
    }


    public static Retrofit getSearchSuggestion() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_WEB)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface ApiServiceCallback {
        void onServiceCreated(ApiService service);

        void onError(Exception e);
    }

    public static void getRefreshCommentClient(final ApiServiceFactory.ApiServiceCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(new AddCookiesInterceptor(getCookie()))
                        .build();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL_MOBILE)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                apiServiceInstance = retrofit.create(ApiService.class);
                // Gọi callback khi đã khởi tạo xong
                if (callback != null) {
                    callback.onServiceCreated(apiServiceInstance);
                }
            } catch (IOException e) {
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

}
