package com.tandev.musichub.adapter.lyric;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.R;
import com.tandev.musichub.model.lyric.LyricLine;

import java.util.ArrayList;
import java.util.List;

public class LyricsAdapter2 extends RecyclerView.Adapter<LyricsAdapter2.LyricViewHolder> {
    private List<LyricLine> lyrics;
    private final Context context;
    private int currentHighlightPosition = -1; // Vị trí hiện tại được highlight

    public LyricsAdapter2(Context context,List<LyricLine> lyrics) {
        this.context = context;
        this.lyrics = lyrics;
    }

    @Override
    public LyricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lyrics, parent, false);
        return new LyricViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LyricViewHolder holder, int position) {
        LyricLine lyric = lyrics.get(position);
        holder.lyricTextView.setText(lyric.getContent());

        if (position == currentHighlightPosition)  {
            holder.lyricTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.lyricTextView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText2));
        }
    }

    @Override
    public int getItemCount() {
        return lyrics.size();
    }

    public void highlightLine(int position) {
        int previousHighlightPosition = currentHighlightPosition;
        currentHighlightPosition = position;

        notifyItemChanged(previousHighlightPosition);
        notifyItemChanged(currentHighlightPosition);
    }

    public static class LyricViewHolder extends RecyclerView.ViewHolder {
        TextView lyricTextView;

        public LyricViewHolder(View itemView) {
            super(itemView);
            lyricTextView = itemView.findViewById(R.id.textViewContent);
        }
    }
}
