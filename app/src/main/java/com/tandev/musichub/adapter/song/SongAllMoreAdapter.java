package com.tandev.musichub.adapter.song;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.tandev.musichub.R;
//import com.example.musichub.activity.PlayNowActivity;
import com.tandev.musichub.bottomsheet.BottomSheetOptionSong;
import com.tandev.musichub.helper.ui.PlayingStatusUpdater;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.service.MyService;

import java.util.ArrayList;

public class SongAllMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PlayingStatusUpdater {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private ArrayList<Items> songList;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;
    private boolean isLoading;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Items> filterList) {
        this.songList = filterList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updatePlayingStatus(String currentEncodeId) {
        if (songList != null) {
            for (int i = 0; i < songList.size(); i++) {
                Items item = songList.get(i);
                if (item.getEncodeId().equals(currentEncodeId)) {
                    selectedPosition = i;
                    notifyDataSetChanged();
                    return;
                }
            }
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public SongAllMoreAdapter(ArrayList<Items> songList, Activity activity, Context context) {
        this.songList = songList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == songList.size() && isLoading) {
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
            view = inflater.inflate(R.layout.item_song, parent, false);
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

    private void bindSongViewHolder(SongViewHolder holder, int position) {
        Items song = songList.get(position);

        holder.nameTextView.setText(song.getTitle());
        holder.artistTextView.setText(song.getArtistsNames());
        Glide.with(context)
                .load(song.getThumbnailM())
                .into(holder.thumbImageView);

        if (selectedPosition == position) {
            int colorSpotify = ContextCompat.getColor(context, R.color.colorSpotify);
            holder.nameTextView.setTextColor(colorSpotify);
            holder.aniPlay.setVisibility(View.VISIBLE);
        } else {
            holder.nameTextView.setTextColor(Color.WHITE);
            holder.aniPlay.setVisibility(View.GONE);
        }

        int premiumColor;
        if (song.getStreamingStatus() == 2) {
            premiumColor = ContextCompat.getColor(context, R.color.yellow);
        } else {
            premiumColor = ContextCompat.getColor(context, R.color.white);
        }
        holder.nameTextView.setTextColor(premiumColor);

        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetInfo(song);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showBottomSheetInfo(song);
                return false;
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (song.getStreamingStatus() == 2) {
                Toast.makeText(context, "Không thể phát bài hát Premium!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, MyService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_song", song);
                bundle.putInt("position_song", position);
                bundle.putSerializable("song_list", songList);
                intent.putExtras(bundle);

                context.startService(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return isLoading ? songList.size() + 1 : songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public LottieAnimationView aniPlay;
        public ImageView btn_more;

        public SongViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            aniPlay = itemView.findViewById(R.id.aniPlay);
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
        notifyItemInserted(songList.size());
    }

    public void removeFooterLoading() {
        isLoading = false;
        int position = songList.size();
        notifyItemRemoved(position);
    }

    private void showBottomSheetInfo(Items items) {
        BottomSheetOptionSong bottomSheetOptionSong = new BottomSheetOptionSong(context, activity, items);
        bottomSheetOptionSong.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionSong.getTag());
    }
}
