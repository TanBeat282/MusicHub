package com.tandev.musichub.adapter.artist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
//import com.tandev.musichub.activity.PlayNowActivity;
//import com.tandev.musichub.activity.ViewArtistActivity;
import com.tandev.musichub.bottomsheet.BottomSheetOptionSong;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ArtistAllMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private ArrayList<Artists> artistsArrayList;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;
    private boolean isLoading;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Artists> filterList) {
        this.artistsArrayList = filterList;
        notifyDataSetChanged();
    }


    public ArtistAllMoreAdapter(ArrayList<Artists> artistsArrayList, Activity activity, Context context) {
        this.artistsArrayList = artistsArrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == artistsArrayList.size() && isLoading) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.item_select_artist, parent, false);
            return new SongViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_loading_more_song, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            bindSongViewHolder((SongViewHolder) holder, position);
        } else if (holder.getItemViewType() == TYPE_LOADING) {
            // Handle the loading view if necessary
        }
    }

    @SuppressLint("SetTextI18n")
    private void bindSongViewHolder(SongViewHolder holder, int position) {
        Artists artists = artistsArrayList.get(position);

        holder.nameTextView.setText(artists.getName());
        holder.artistTextView.setText(Helper.convertToIntString(artists.getTotalFollow()) + " quan tâm");
        Glide.with(context)
                .load(artists.getThumbnail())
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);


        holder.itemView.setOnClickListener(v -> {
            ArtistFragment artistFragment = new ArtistFragment();
            Bundle bundle = new Bundle();
            bundle.putString("alias", artists.getAlias());

            if (context instanceof MainActivity) {
                ((MainActivity) context).replaceFragmentWithBundle(artistFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return isLoading ? artistsArrayList.size() + 1 : artistsArrayList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public ImageView btn_more;

        public SongViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            btn_more = itemView.findViewById(R.id.btn_more);
            artistTextView.setSelected(true);
            nameTextView.setSelected(true);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progress_bar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progress_bar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoading = true;
        notifyItemInserted(artistsArrayList.size());
    }

    public void removeFooterLoading() {
        isLoading = false;
        int position = artistsArrayList.size();
        notifyItemRemoved(position);
    }

    private void showBottomSheetInfo(Items items) {
        BottomSheetOptionSong bottomSheetOptionSong = new BottomSheetOptionSong(context, activity, items);
        bottomSheetOptionSong.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionSong.getTag());
    }
}
