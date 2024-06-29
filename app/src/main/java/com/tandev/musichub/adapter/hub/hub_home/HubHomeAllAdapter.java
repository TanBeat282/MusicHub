package com.tandev.musichub.adapter.hub.hub_home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.hub.HubFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.model.hub.hub_home.featured.HubHomeFeaturedItems;
import com.tandev.musichub.model.hub.hub_home.genre.HubHomeGenre;
import com.tandev.musichub.model.hub.hub_home.nations.HubHomeNations;
import com.tandev.musichub.model.hub.hub_home.topic.HubHomeTopic;

import java.util.ArrayList;

public class HubHomeAllAdapter extends RecyclerView.Adapter<HubHomeAllAdapter.ViewHolder> {
    private ArrayList<?> itemsArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<?> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    public HubHomeAllAdapter(ArrayList<?> itemsArrayList, Activity activity, Context context) {
        this.itemsArrayList = itemsArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hub, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Object item = itemsArrayList.get(position);
        if (item instanceof HubHomeFeaturedItems) {
            HubHomeFeaturedItems homeFeaturedItems = (HubHomeFeaturedItems) item;
            Glide.with(context).load(homeFeaturedItems.getThumbnailHasText()).into(holder.thumbImageView);
            holder.itemView.setOnClickListener(view -> {
                HubFragment hubFragment = new HubFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", homeFeaturedItems.getEncodeId());


                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                }
            });
        } else if (item instanceof HubHomeNations) {
            HubHomeNations homeNations = (HubHomeNations) item;
            Glide.with(context).load(homeNations.getThumbnailHasText()).into(holder.thumbImageView);
            holder.itemView.setOnClickListener(view -> {
                HubFragment hubFragment = new HubFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", homeNations.getEncodeId());


                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                }
            });
        } else if (item instanceof HubHomeTopic) {
            HubHomeTopic hubHomeTopic = (HubHomeTopic) item;
            Glide.with(context).load(hubHomeTopic.getThumbnailHasText()).into(holder.thumbImageView);
            holder.itemView.setOnClickListener(view -> {
                HubFragment hubFragment = new HubFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", hubHomeTopic.getEncodeId());


                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                }
            });
        } else {
            HubHomeGenre hubHomeGenre = (HubHomeGenre) item;
            Glide.with(context).load(hubHomeGenre.getThumbnailHasText()).into(holder.thumbImageView);
            holder.itemView.setOnClickListener(view -> {
                HubFragment hubFragment = new HubFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", hubHomeGenre.getEncodeId());


                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
        }
    }

}
