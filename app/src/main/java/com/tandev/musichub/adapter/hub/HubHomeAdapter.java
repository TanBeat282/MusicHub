package com.tandev.musichub.adapter.hub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.fragment.hub.HubFragment;
import com.tandev.musichub.fragment.hub.hub_home.HubHomeFragment;
import com.tandev.musichub.model.hub.hub_home.featured.HubHomeFeaturedItems;
import com.tandev.musichub.model.hub.hub_home.nations.HubHomeNations;

import java.util.ArrayList;

public class HubHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PLAYLIST = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private ArrayList<HubHomeFeaturedItems> hubHomeFeaturedItemsArrayList;
    private ArrayList<HubHomeNations> hubHomeNationsArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HubHomeFeaturedItems> hubHomeFeaturedItemsArrayList, ArrayList<HubHomeNations> hubHomeNationsArrayList) {
        this.hubHomeFeaturedItemsArrayList = hubHomeFeaturedItemsArrayList;
        this.hubHomeNationsArrayList = hubHomeNationsArrayList;
        notifyDataSetChanged();
    }

    public HubHomeAdapter(ArrayList<HubHomeFeaturedItems> hubHomeFeaturedItemsArrayList, ArrayList<HubHomeNations> hubHomeNationsArrayList, Activity activity, Context context) {
        this.hubHomeFeaturedItemsArrayList = hubHomeFeaturedItemsArrayList;
        this.hubHomeNationsArrayList = hubHomeNationsArrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 5) {
            return VIEW_TYPE_PLAYLIST;
        } else {
            return VIEW_TYPE_MORE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PLAYLIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hub_home, parent, false);
            return new PlaylistViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_all_hub_home, parent, false);
            return new MoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_PLAYLIST) {
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;
            if (position < hubHomeFeaturedItemsArrayList.size()) {
                HubHomeFeaturedItems item = hubHomeFeaturedItemsArrayList.get(position);
                Glide.with(context).load(item.getThumbnailHasText()).placeholder(R.drawable.holder_video).into(playlistViewHolder.thumbImageView);
                playlistViewHolder.itemView.setOnClickListener(view -> {
                    HubFragment hubFragment = new HubFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("encodeId", item.getEncodeId());

                    if (context instanceof MainActivity) {
                        ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                    }
                });
            } else {
                int adjustedPosition = position - hubHomeFeaturedItemsArrayList.size();
                if (adjustedPosition < hubHomeNationsArrayList.size()) {
                    HubHomeNations item = hubHomeNationsArrayList.get(adjustedPosition);
                    Glide.with(context).load(item.getThumbnailHasText()).placeholder(R.drawable.holder_video).into(playlistViewHolder.thumbImageView);
                    playlistViewHolder.itemView.setOnClickListener(view -> {
                        HubFragment hubFragment = new HubFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("encodeId", item.getEncodeId());

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                        }
                    });
                }
            }
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.linear_more.setOnClickListener(v -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).replaceFragment(new HubHomeFragment());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return Math.min(5, hubHomeFeaturedItemsArrayList.size() + hubHomeNationsArrayList.size()) + 1;
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView nameTextView;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_more;

        public MoreViewHolder(View itemView) {
            super(itemView);
            linear_more = itemView.findViewById(R.id.linear_more);
        }
    }
}
