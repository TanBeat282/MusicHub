package com.tandev.musichub.adapter.hub.hub_home_banner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.hub.HubFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.home.home_new.banner.HomeDataItemBannerItem;
import com.tandev.musichub.model.hub.hub_home.banner.HubHomeBanner;

import java.util.ArrayList;

public class HubHomeBannerAdapter extends RecyclerView.Adapter<HubHomeBannerAdapter.ViewHolder> {
    private ArrayList<HubHomeBanner> hubHomeBannerArrayList;
    private ViewPager2 viewPager2;
    private final Context context;
    private final Activity activity;


    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HubHomeBanner> hubHomeBannerArrayList) {
        this.hubHomeBannerArrayList = hubHomeBannerArrayList;
        notifyDataSetChanged();
    }

    public HubHomeBannerAdapter(ArrayList<HubHomeBanner> hubHomeBannerArrayList, ViewPager2 viewPager2, Context context, Activity activity) {
        this.hubHomeBannerArrayList = hubHomeBannerArrayList;
        this.viewPager2 = viewPager2;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_hub_home, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HubHomeBanner hubHomeBanner = hubHomeBannerArrayList.get(position);

        Glide.with(context)
                .load(hubHomeBanner.getCover())
                .placeholder(R.drawable.holder_video)
                .into(holder.roundedImageView);

        if (position == hubHomeBannerArrayList.size() - 2) {
            viewPager2.post(runnable);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HubFragment hubFragment = new HubFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", Helper.extractEndCodeID(hubHomeBanner.getLink()));
                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return hubHomeBannerArrayList.size();
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
            hubHomeBannerArrayList.addAll(hubHomeBannerArrayList);
            notifyDataSetChanged();
        }
    };

}
