package com.tandev.musichub.api.base;

import com.tandev.musichub.constants.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Base {
    private final String version;
    private final String ctime;
    private final String secretKey;
    private final String apiKey;

    public Base() {
        this.version = Constants.VERSION;
        this.ctime = String.valueOf(System.currentTimeMillis() / 1000);
        this.secretKey = Constants.SECRET_KEY;
        this.apiKey = Constants.API_KEY;
    }


    private String createHash256(String params) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(params.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String createHmac512(String str, String key) throws Exception {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        hmacSha512.init(secretKeySpec);
        byte[] hash = hmacSha512.doFinal(str.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    protected Map<String, String> createRequest() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("ctime", ctime);
        paramMap.put("version", version);
        paramMap.put("apiKey", apiKey);
        return paramMap;
    }


    // Create Signature //
    protected String createHashAndHmac(String path, String params) throws NoSuchAlgorithmException, Exception {
        String hash256 = createHash256(params);
        String hmac512 = createHmac512(path + hash256, secretKey);
        return hmac512;
    }

    protected String createHomeSig(String path) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "count=30ctime=" + this.ctime + "page=1version=" + this.version);
    }

    protected String createPodcastSig(String path, String type) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "count=20ctime=" + this.ctime + "page=1type=" + type + "version=" + this.version);
    }

    protected String createSearchSig(String path, String type, int page) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "count=18ctime=" + this.ctime + "page=" + page + "type=" + type + "version=" + this.version);
    }

    protected String createCommentSig(String path, String id) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "count=50ctime=" + this.ctime + "id=" + id + "version=" + this.version);
    }

    protected String createAnnouncementSig(String path, String id) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "count=10ctime=" + this.ctime + "id=" + id + "version=" + this.version);
    }

    protected String createIdSig(String path, String id) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "ctime=" + this.ctime + "id=" + id + "version=" + this.version);
    }

    protected String createNoIdSig(String path) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "ctime=" + this.ctime + "version=" + this.version);
    }

    protected String createNewReleaseSig(String path, String type) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "ctime=" + this.ctime + "type=" + type + "version=" + this.version);
    }

    protected String createGenreSig(String path, String id, String type) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "ctime=" + this.ctime + "id=" + id + "type=" + type + "version=" + this.version);
    }

    protected String createSongListOfArtistSig(String path, String id, String page) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "count=30ctime=" + this.ctime + "id=" + id + "page=" + page + "type=artist" + "version=" + this.version);
    }

    protected String createSuggestSig(String path) throws NoSuchAlgorithmException, Exception {
        return createHashAndHmac(path, "ctime=" + this.ctime + "language=vinum=10version=" + this.version);
    }
}

