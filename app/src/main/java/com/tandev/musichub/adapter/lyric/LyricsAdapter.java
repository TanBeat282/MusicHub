package com.tandev.musichub.adapter.lyric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.tandev.musichub.R;
import com.tandev.musichub.model.lyric.LyricLine;
import com.tandev.musichub.service.MyService;

import java.util.List;

public class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder> {

    private final Context context;
    private List<LyricLine> lyricLines;
    private long currentPlaybackTime;
    private static final int EXTRA_ITEMS_COUNT = 10;

    public LyricsAdapter(Context context, List<LyricLine> lyricLines) {
        this.context = context;
        this.lyricLines = lyricLines;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLyricLines(List<LyricLine> lyricLines) {
        this.lyricLines = lyricLines;
        notifyDataSetChanged();
    }

    public void setCurrentPlaybackTime(long currentPlaybackTime) {
        int previousPosition = findCurrentLyricPosition();
        this.currentPlaybackTime = currentPlaybackTime;
        int currentPosition = findCurrentLyricPosition();

        if (previousPosition != -1) {
            notifyItemChanged(previousPosition);
        }
        if (currentPosition != -1) {
            notifyItemChanged(currentPosition);
        }
    }

    private int findCurrentLyricPosition() {
        for (int i = 0; i < lyricLines.size(); i++) {
            if (isCurrentLyric(i)) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public LyricsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_lyrics, parent, false);
        return new LyricsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LyricsViewHolder holder, int position) {
        LyricLine lyricLine = lyricLines.get(position);
        holder.textViewContent.setText(lyricLine.getContent());

        if (isCurrentLyric(position)) {
            holder.textViewContent.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.textViewContent.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText2));
        }

        holder.linear_lyric.setOnClickListener(view -> {
            Intent intent = new Intent(context, MyService.class);
            intent.putExtra("seek_to_position", (int) lyricLine.getStartTime());
            context.startService(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lyricLines.size();
    }

    static class LyricsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContent;
        LinearLayout linear_lyric;

        public LyricsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            linear_lyric = itemView.findViewById(R.id.linear_lyric);
        }
    }

    private boolean isCurrentLyric(int position) {
        if (position < lyricLines.size() - 1) {
            LyricLine currentLine = lyricLines.get(position);
            LyricLine nextLine = lyricLines.get(position + 1);
            return currentPlaybackTime >= currentLine.getStartTime() && currentPlaybackTime < nextLine.getStartTime();
        } else {
            LyricLine currentLine = lyricLines.get(position);
            return currentPlaybackTime >= currentLine.getStartTime();
        }
    }
}
