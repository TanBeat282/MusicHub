package com.tandev.musichub.adapter.radio;

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
//import com.tandev.musichub.activity.PlayRadioActivity;
//import com.tandev.musichub.activity.ViewAllArtistActivity;
//import com.tandev.musichub.activity.ViewArtistActivity;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.fragment.radio.RadioFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.home.home_new.radio.HomeDataItemRadioItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class RadioMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ARTIST = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private ArrayList<HomeDataItemRadioItem> homeDataItemRadioItemArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HomeDataItemRadioItem> filterList) {
        this.homeDataItemRadioItemArrayList = filterList;
        notifyDataSetChanged();
    }

    public RadioMoreAdapter(ArrayList<HomeDataItemRadioItem> homeDataItemRadioItemArrayList, Activity activity, Context context) {
        this.homeDataItemRadioItemArrayList = homeDataItemRadioItemArrayList;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radio, parent, false);
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
            HomeDataItemRadioItem homeDataItemRadioItem = homeDataItemRadioItemArrayList.get(position);
            ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;

            artistViewHolder.txt_name.setText(homeDataItemRadioItem.getHost().getName());
            artistViewHolder.txt_view.setText(homeDataItemRadioItem.getActiveUsers() + " người đang nghe");
            Glide.with(context)
                    .load(homeDataItemRadioItem.getProgram() == null ? homeDataItemRadioItem.getThumbnailM() : homeDataItemRadioItem.getProgram().getThumbnail())
                    .placeholder(R.drawable.holder)
                    .into(artistViewHolder.img_avatar);
            Glide.with(context)
                    .load(homeDataItemRadioItem.getHost().getThumbnail())
                    .placeholder(R.drawable.holder)
                    .into(artistViewHolder.img_user);

            artistViewHolder.itemView.setOnClickListener(view -> {
                RadioFragment radioFragment = new RadioFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("encodeId", homeDataItemRadioItem.getEncodeId());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(radioFragment, bundle);
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
        if (homeDataItemRadioItemArrayList == null || homeDataItemRadioItemArrayList.isEmpty()) {
            return 0;
        }
        return Math.min(homeDataItemRadioItemArrayList.size(), 5) + 1; // Showing 5 items + 1 "More" button
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView img_avatar;
        public RoundedImageView img_user;
        public TextView txt_name;
        public TextView txt_view;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            img_user = itemView.findViewById(R.id.img_user);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_view = itemView.findViewById(R.id.txt_view);
            txt_name.setSelected(true);
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
