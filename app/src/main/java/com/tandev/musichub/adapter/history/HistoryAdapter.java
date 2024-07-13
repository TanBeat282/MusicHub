package com.tandev.musichub.adapter.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.tandev.musichub.R;
//import com.tandev.musichub.activity.PlayNowActivity;
import com.tandev.musichub.bottomsheet.BottomSheetOptionSong;
import com.tandev.musichub.helper.ui.PlayingStatusUpdater;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.service.MyService;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements PlayingStatusUpdater {
    private ArrayList<Items> songList;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;

    public HistoryAdapter(Context context, Activity activity, ArrayList<Items> songList) {
        this.context = context;
        this.activity = activity;
        this.songList = songList;
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
                }
            }
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Items items = songList.get(position);

        holder.nameTextView.setText(items.getTitle());
        holder.artistTextView.setText(items.getArtistsNames());
        Glide.with(context)
                .load(items.getThumbnail())
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);

        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.select_item_background);
            holder.aniPlay.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.select_item);
            holder.aniPlay.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (items.getStreamingStatus() == 2) {
                Toast.makeText(context, "Không thể phát bài hát Premium!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, MyService.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_song", items);
                bundle.putInt("position_song", position);
                bundle.putSerializable("song_list", songList);
                intent.putExtras(bundle);

                context.startService(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showBottomSheetInfo(items);
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public LottieAnimationView aniPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            aniPlay = itemView.findViewById(R.id.aniPlay);
            artistTextView.setSelected(true);
            nameTextView.setSelected(true);
        }
    }

    private void showBottomSheetInfo(Items items) {
        BottomSheetOptionSong bottomSheetOptionSong = new BottomSheetOptionSong(context, activity, items);
        bottomSheetOptionSong.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionSong.getTag());
    }

}
