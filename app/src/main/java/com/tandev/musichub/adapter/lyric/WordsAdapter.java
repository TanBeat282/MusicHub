package com.tandev.musichub.adapter.lyric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.R;
import com.tandev.musichub.model.song.Word;

import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {
    private ArrayList<com.tandev.musichub.model.song.Word> words;
    private long currentTime;
    private Context context;

    public WordsAdapter(ArrayList<Word> words, Context context) {
        this.words = words;
        this.context = context;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        com.tandev.musichub.model.song.Word word = words.get(position);
        holder.textView.setText(word.getData());

        // Kiểm tra nếu currentTime nằm trong khoảng thời gian của từng item
        if (currentTime >= word.getStartTime() && currentTime <= word.getEndTime()) {
            // Nếu currentTime nằm trong khoảng thời gian của từng item,
            // kiểm tra xem từ đó có trùng startTime với từ trước đó không
            if (position > 0 && word.getStartTime() == words.get(position - 1).getStartTime()) {
                // Nếu trùng startTime với từ trước đó, không highlight
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText2));
            } else {
                // Nếu không trùng startTime với từ trước đó, highlight
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        } else {
            // Nếu không nằm trong khoảng thời gian của từng item, không highlight
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText2));
        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        notifyDataSetChanged();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}

