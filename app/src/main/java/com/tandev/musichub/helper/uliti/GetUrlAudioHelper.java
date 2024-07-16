package com.tandev.musichub.helper.uliti;

import android.util.Log;

import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.model.preview_premium.PreviewPremium;
import com.tandev.musichub.model.song.Lyric;
import com.tandev.musichub.model.song.SongAudio;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUrlAudioHelper {
    private SongCategories songCategories = new SongCategories();

    public interface SongAudioCallback {
        void onSuccess(SongAudio songAudio);

        void onFailure(Throwable throwable);
    }

    public interface LyricUrlCallback {
        void onSuccess(String lyricUrl);

        void onFailure(Throwable throwable);
    }

    public interface PremiumSongAudioCallback {
        void onSuccess(PreviewPremium previewPremium);

        void onFailure(Throwable throwable);
    }

    public void getSongAudio(String endcodeID, SongAudioCallback callback) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    Map<String, String> map = songCategories.getAudio(endcodeID);

                    Call<SongAudio> call = service.SONG_AUDIO_CALL(endcodeID, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SongAudio>() {
                        @Override
                        public void onResponse(Call<SongAudio> call, Response<SongAudio> response) {
                            if (response.isSuccessful()) {
                                SongAudio songAudio = response.body();
                                Log.d(">>>>>>>>>>>>>>>", "getSongAudio: " + call.request().url().toString());
                                if (songAudio != null) {
                                    if (songAudio.getErr() != -201) {
                                        callback.onSuccess(songAudio);
                                    } else {
                                        callback.onFailure(new RuntimeException("Error -201: Unable to get audio URL"));
                                    }
                                } else {
                                    callback.onFailure(new RuntimeException("Null response body from server"));
                                }
                            } else {
                                callback.onFailure(new RuntimeException("Unsuccessful response from server. Error code: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<SongAudio> call, Throwable throwable) {
                            callback.onFailure(throwable);
                        }
                    });

                } catch (Exception e) {
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void getPremiumSongAudio(String endcodeID, PremiumSongAudioCallback callback) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    Map<String, String> map = songCategories.getPremiumAudio(endcodeID);

                    Call<PreviewPremium> call = service.PREVIEW_PREMIUM_CALL(endcodeID, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<PreviewPremium>() {
                        @Override
                        public void onResponse(Call<PreviewPremium> call, Response<PreviewPremium> response) {
                            if (response.isSuccessful()) {
                                PreviewPremium previewPremium = response.body();
                                Log.d(">>>>>>>>>>>>>>>", "getPremiumSongAudio " + call.request().url().toString());
                                if (previewPremium != null) {
                                    if (previewPremium.getErr() != -201) {
                                        callback.onSuccess(previewPremium);
                                    } else {
                                        callback.onFailure(new RuntimeException("Error -201: Unable to get audio URL"));
                                    }
                                } else {
                                    callback.onFailure(new RuntimeException("Null response body from server"));
                                }
                            } else {
                                callback.onFailure(new RuntimeException("Unsuccessful response from server. Error code: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<PreviewPremium> call, Throwable throwable) {
                            callback.onFailure(throwable);
                        }
                    });

                } catch (Exception e) {
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void getLyricUrl(String endcodeID, LyricUrlCallback callback) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    Map<String, String> map = songCategories.getLyrics(endcodeID);

                    Call<Lyric> call = service.LYRIC_CALL(endcodeID, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<Lyric>() {
                        @Override
                        public void onResponse(Call<Lyric> call, Response<Lyric> response) {
                            String requestUrl = call.request().url().toString();
                            Log.d(">>>>>>>>>>>>>>>>>>>", " - " + requestUrl);
                            if (response.isSuccessful()) {
                                Lyric lyric = response.body();
                                if (lyric != null) {
                                    if (lyric.getErr() != -201) {

                                        callback.onSuccess(lyric.getData().getFile());
                                    } else {
                                        callback.onFailure(new RuntimeException("Error -201: Unable to get audio URL"));
                                    }
                                } else {
                                    callback.onFailure(new RuntimeException("Null response body from server"));
                                }
                            } else {
                                callback.onFailure(new RuntimeException("Unsuccessful response from server. Error code: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Lyric> call, Throwable throwable) {
                            callback.onFailure(throwable);
                        }
                    });

                } catch (Exception e) {
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi ở đây
            }
        });


    }
}
