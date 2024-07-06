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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.tandev.musichub.R;
import com.tandev.musichub.bottomsheet.BottomSheetOptionSong;
import com.tandev.musichub.helper.ui.PlayingStatusUpdater;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.service.MyService;

import java.util.ArrayList;

public class SongMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PlayingStatusUpdater {
    private static final int VIEW_TYPE_SONG = 0;
    private static final int VIEW_TYPE_BUTTON = 1;

    private ArrayList<Items> songList;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;
    private int typeOnClicked = -1;
    // 0 -> New Release
    // 1 -> BXH New Release
    // 2 -> BXH
    // 3 -> Artist -> Bai hat noi bat


    public SongMoreAdapter(ArrayList<Items> songList, int typeOnClicked, Activity activity, Context context) {
        this.songList = songList;
        this.typeOnClicked = typeOnClicked;
        this.activity = activity;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Items> fillterList) {
        this.songList = fillterList;
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
                } else {
                    selectedPosition = -1;
                    notifyDataSetChanged();
                }
            }
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (songList.size() > 5 && position == 5) {
            return VIEW_TYPE_BUTTON;
        } else {
            return VIEW_TYPE_SONG;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SONG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
            return new SongViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_more, parent, false);
            return new ButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (getItemViewType(position) == VIEW_TYPE_SONG) {
            Items song = songList.get(position);
            SongViewHolder songViewHolder = (SongViewHolder) holder;

            songViewHolder.nameTextView.setText(song.getTitle());
            songViewHolder.artistTextView.setText(song.getArtistsNames());
            Glide.with(context)
                    .load(song.getThumbnailM())
                    .into(songViewHolder.thumbImageView);

            if (selectedPosition == position) {
                int colorSpotify = ContextCompat.getColor(context, R.color.colorSpotify);
                songViewHolder.nameTextView.setTextColor(colorSpotify);
                songViewHolder.aniPlay.setVisibility(View.VISIBLE);
            } else {
                songViewHolder.nameTextView.setTextColor(Color.WHITE);
                songViewHolder.aniPlay.setVisibility(View.GONE);
            }
            int premiumColor;
            if (song.getStreamingStatus() == 2) {
                premiumColor = ContextCompat.getColor(context, R.color.yellow);
            } else {
                premiumColor = ContextCompat.getColor(context, R.color.white);
            }
            songViewHolder.nameTextView.setTextColor(premiumColor);

            songViewHolder.btn_more.setOnClickListener(new View.OnClickListener() {
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
        } else {
            // Bind data for button item
            ButtonViewHolder buttonViewHolder = (ButtonViewHolder) holder;
            // Handle button click event
            buttonViewHolder.btn_more.setOnClickListener(v -> {
                // Handle "More" button click event
            });
        }
    }

    @Override
    public int getItemCount() {
        if (songList == null || songList.isEmpty()) {
            return 0;
        }
        return songList.size() > 5 ? 6 : songList.size(); // Showing 5 items + 1 "More" button or all items if less than 5
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public LottieAnimationView aniPlay;
        public ImageView btn_more;

        public SongViewHolder(@NonNull View itemView) {
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

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout btn_more;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_more = itemView.findViewById(R.id.btn_more);
        }

    }

    private void showBottomSheetInfo(Items items) {
        BottomSheetOptionSong bottomSheetOptionSong = new BottomSheetOptionSong(context, activity, items);
        bottomSheetOptionSong.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionSong.getTag());
    }

}
