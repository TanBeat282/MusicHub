package com.tandev.musichub.adapter.lyric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.R;
import com.tandev.musichub.model.song.Sentences;

import java.util.ArrayList;

public class SentencesAdapter extends RecyclerView.Adapter<SentencesAdapter.SentenceViewHolder> {
    private ArrayList<Sentences> sentences;
    private long currentTime;
    private Context context;

    public SentencesAdapter(ArrayList<Sentences> sentences, Context context) {
        this.sentences = sentences;
        this.context = context;
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sentence, parent, false);
        return new SentenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        Sentences sentence = sentences.get(position);
        WordsAdapter wordsAdapter = new WordsAdapter(sentence.getWords(), context);
        holder.recyclerView.setAdapter(wordsAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        wordsAdapter.updateCurrentTime(currentTime);
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateCurrentTime(long currentTime) {
        this.currentTime = currentTime;
        notifyDataSetChanged();
    }

    static class SentenceViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        public SentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerViewWords);
        }
    }
}
