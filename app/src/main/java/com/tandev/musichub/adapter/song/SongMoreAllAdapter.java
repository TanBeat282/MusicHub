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

public class SongMoreAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PlayingStatusUpdater {
    private ArrayList<Items> songList;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;
    private boolean isExpanded = false;

    private static final int ITEM_TYPE_SONG = 0;
    private static final int ITEM_TYPE_MORE = 1;

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

    public SongMoreAllAdapter(ArrayList<Items> songList, Activity activity, Context context) {
        this.songList = songList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isExpanded && songList.size() > 5 && position == 5) {
            return ITEM_TYPE_MORE;
        }
        return ITEM_TYPE_SONG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_MORE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_more, parent, false);
            return new MoreViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
            return new SongViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof MoreViewHolder) {
            ((MoreViewHolder) holder).bind();
        } else if (holder instanceof SongViewHolder) {
            SongViewHolder songHolder = (SongViewHolder) holder;
            Items song = songList.get(position);

            songHolder.nameTextView.setText(song.getTitle());
            songHolder.artistTextView.setText(song.getArtistsNames());
            Glide.with(context)
                    .load(song.getThumbnailM())  // Tải ảnh thumbnail từ liên kết
                    .placeholder(R.drawable.holder)
                    .into(songHolder.thumbImageView);  // Hiển thị thumbnail lên ImageView

            if (selectedPosition == position) {
                songHolder.itemView.setBackgroundResource(R.drawable.select_item_background);
                songHolder.aniPlay.setVisibility(View.VISIBLE);
            } else {
                songHolder.itemView.setBackgroundResource(R.drawable.select_item);
                songHolder.aniPlay.setVisibility(View.GONE);
            }
            int premiumColor;
            if (song.getStreamingStatus() == 2) {
                premiumColor = ContextCompat.getColor(context, R.color.yellow);
            } else {
                premiumColor = ContextCompat.getColor(context, R.color.white);
            }
            songHolder.nameTextView.setTextColor(premiumColor);

            songHolder.btn_more.setOnClickListener(view -> showBottomSheetInfo(song));
            songHolder.itemView.setOnLongClickListener(view -> {
                showBottomSheetInfo(song);
                return false;
            });

            songHolder.itemView.setOnClickListener(v -> {
                if (song.getStreamingStatus() == 2) {
                    Toast.makeText(context, "Bạn đang nghe thử bài hát Premium!", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(context, MyService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_song", song);
                bundle.putInt("position_song", position);
                bundle.putSerializable("song_list", songList);
                intent.putExtras(bundle);

                context.startService(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (!isExpanded && songList.size() > 5) {
            return 6; // 5 items + 1 "more" item
        } else {
            return songList.size();
        }
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

    public class MoreViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout btn_more;

        public MoreViewHolder(View itemView) {
            super(itemView);
            btn_more = itemView.findViewById(R.id.btn_more);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind() {
            btn_more.setOnClickListener(v -> {
                isExpanded = true;
                notifyDataSetChanged();
            });
        }
    }

    private void showBottomSheetInfo(Items items) {
        BottomSheetOptionSong bottomSheetOptionSong = new BottomSheetOptionSong(context, activity, items,1);
        bottomSheetOptionSong.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionSong.getTag());
    }
}
