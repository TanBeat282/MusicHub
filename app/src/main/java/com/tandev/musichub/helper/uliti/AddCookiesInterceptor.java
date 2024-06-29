package com.tandev.musichub.helper.uliti;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    private String cookie;

    public AddCookiesInterceptor(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        // Thêm cookie vào header
        builder.addHeader("Cookie", cookie);
        return chain.proceed(builder.build());
    }
}
