package com.tandev.musichub.adapter.artist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
//import com.tandev.musichub.activity.ViewAllArtistActivity;
//import com.tandev.musichub.activity.ViewArtistActivity;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ArtistsMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ARTIST = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private ArrayList<Artists> artistsArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Artists> filterList) {
        this.artistsArrayList = filterList;
        notifyDataSetChanged();
    }

    public ArtistsMoreAdapter(ArrayList<Artists> artistsArrayList, Activity activity, Context context) {
        this.artistsArrayList = artistsArrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 5) {
            return VIEW_TYPE_ARTIST;
        } else {
            return VIEW_TYPE_MORE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ARTIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artists, parent, false);
            return new ArtistViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_all, parent, false);
            return new MoreViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ARTIST) {
            Artists artist = artistsArrayList.get(position);
            ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;

            artistViewHolder.txt_name.setText(artist.getName());
            artistViewHolder.txt_follow.setText(Helper.convertToIntString(artist.getTotalFollow()) + " quan tâm");
            Glide.with(context)
                    .load(artist.getThumbnail())
                    .into(artistViewHolder.img_avatar);

            artistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArtistFragment artistFragment = new ArtistFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("alias", artist.getAlias());

                    if (context instanceof MainActivity) {
                        ((MainActivity) context).replaceFragmentWithBundle(artistFragment, bundle);
                    }
                }
            });
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.linear_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, ViewAllArtistActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("artist_arraylist", artistsArrayList);
//                    intent.putExtras(bundle);
//
//                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (artistsArrayList == null || artistsArrayList.isEmpty()) {
            return 0;
        }
        if (artistsArrayList.size() <= 5) {
            return artistsArrayList.size();
        } else {
            return 5 + 1; // Showing 5 items + 1 "More" button
        }
    }


    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView img_avatar;
        public TextView txt_name;
        public TextView txt_follow;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_follow = itemView.findViewById(R.id.txt_follow);
            txt_name.setSelected(true);
            txt_follow.setSelected(true);
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
