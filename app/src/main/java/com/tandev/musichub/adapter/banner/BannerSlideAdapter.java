package com.tandev.musichub.adapter.banner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.chart.home.home_new.banner.HomeDataItemBannerItem;
import com.tandev.musichub.model.song.SongDetail;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.service.MyService;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerSlideAdapter extends RecyclerView.Adapter<BannerSlideAdapter.ViewHolder> {
    private ArrayList<HomeDataItemBannerItem> homeDataItemBannerItems;
    private final ViewPager2 viewPager2;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HomeDataItemBannerItem> homeDataItemBannerItems) {
        this.homeDataItemBannerItems = homeDataItemBannerItems;
        notifyDataSetChanged();
    }

    public BannerSlideAdapter(ArrayList<HomeDataItemBannerItem> homeDataItemBannerItems, ViewPager2 viewPager2, Context context, Activity activity) {
        this.homeDataItemBannerItems = homeDataItemBannerItems;
        this.viewPager2 = viewPager2;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HomeDataItemBannerItem homeDataItemBannerItem = homeDataItemBannerItems.get(position);

        Glide.with(context)
                .load(homeDataItemBannerItem.getBanner())
                .placeholder(R.drawable.holder_video)
                .into(holder.roundedImageView);

        if (position == homeDataItemBannerItems.size() - 2) {
            viewPager2.post(runnable);
        }

        holder.itemView.setOnClickListener(view -> {
            if (homeDataItemBannerItem.getType() == 1) {
                getSongDetail(homeDataItemBannerItem.getEncodeId());
            } else {
                PlaylistFragment playlistFragment = new PlaylistFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", homeDataItemBannerItem.getEncodeId());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeDataItemBannerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView roundedImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.roundedImageView);
        }
    }

    private final Runnable runnable = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            homeDataItemBannerItems.addAll(homeDataItemBannerItems);
            notifyDataSetChanged();
        }
    };

    private void getSongDetail(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getInfo(encodeId);

                    Call<SongDetail> call = service.SONG_DETAIL_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SongDetail>() {
                        @Override
                        public void onResponse(@NonNull Call<SongDetail> call, @NonNull Response<SongDetail> response) {
                            LogUtil.d(Constants.TAG, "getSongDetail: " + call.request().url());
                            if (response.isSuccessful()) {
                                SongDetail songDetail = response.body();
                                if (songDetail != null) {
                                    handleSongDetail(songDetail);
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

            }
        });
    }

    private void handleSongDetail(SongDetail songDetail) {
       if (songDetail.getData().getAlbum()!=null){
           AlbumFragment albumFragment = new AlbumFragment();
           Bundle bundle = new Bundle();
           bundle.putString("album_endCodeId", songDetail.getData().getAlbum().getEncodeId());

           if (context instanceof MainActivity) {
               ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
           }
       }
    }
}
