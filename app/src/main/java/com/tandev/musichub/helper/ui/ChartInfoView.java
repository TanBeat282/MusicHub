package com.tandev.musichub.helper.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.song.SongDetail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartInfoView {
    private final View view;
    private final TextView txt_rank_status;
    private final RoundedImageView thumbImageView;

    public ChartInfoView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_chart_info_song, null);
        txt_rank_status = view.findViewById(R.id.txt_rank_status);
        thumbImageView = view.findViewById(R.id.thumbImageView);
    }

    public void show(ViewGroup parent, float x, float y, int lineOrder, String text) {
        getSongDetail(lineOrder, text);

        // Xóa view nếu đã tồn tại
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        // Thêm view vào parent
        parent.addView(view);

        // Lấy kích thước của parent và view
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();

        // Điều chỉnh vị trí x và y để đảm bảo view không bị ra ngoài màn hình
        if (x + viewWidth > parentWidth) {
            x = parentWidth - viewWidth;
        }
        if (x < 0) {
            x = 0;
        }
        if (y - viewHeight < 0) {
            y = viewHeight;
        }

        // Điều chỉnh để view gần với điểm nhấn hơn
        view.setX(x - viewWidth / 2 + viewWidth / 2);
        view.setY(y - viewHeight - 10); // Đẩy lên một chút so với điểm nhấn
    }



    public void hide() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void getSongDetail(int lineOrder, String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getInfo(encodeId);

                    retrofit2.Call<SongDetail> call = service.SONG_DETAIL_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SongDetail>() {
                        @Override
                        public void onResponse(@NonNull Call<SongDetail> call, @NonNull Response<SongDetail> response) {
                            LogUtil.d(Constants.TAG, "getSongDetail: " + call.request().url());
                            if (response.isSuccessful()) {
                                SongDetail songDetail = response.body();
                                if (songDetail != null) {
                                    updateUI(lineOrder,songDetail);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SongDetail> call, @NonNull Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }

    private void updateUI(int lineOrder,SongDetail songDetail) {
        txt_rank_status.setText(String.valueOf(lineOrder));
        Glide.with(thumbImageView.getContext()).load(songDetail.getData().getThumbnailM()).into(thumbImageView);
    }

}
