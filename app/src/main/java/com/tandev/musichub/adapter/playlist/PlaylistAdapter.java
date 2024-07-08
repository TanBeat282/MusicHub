package com.tandev.musichub.adapter.playlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
//import com.tandev.musichub.activity.ViewPlaylistActivity;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private ArrayList<DataPlaylist> dataPlaylistArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataPlaylist> fillterList) {
        this.dataPlaylistArrayList = fillterList;
        notifyDataSetChanged();
    }

    public PlaylistAdapter(ArrayList<DataPlaylist> dataPlaylistArrayList, Activity activity, Context context) {
        this.dataPlaylistArrayList = dataPlaylistArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_all, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataPlaylist dataPlaylist = dataPlaylistArrayList.get(position);

        holder.nameTextView.setText(dataPlaylist.getTitle());
        Glide.with(context)
                .load(dataPlaylist.getThumbnailM())
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);

        holder.itemView.setOnClickListener(v -> {
            PlaylistFragment playlistFragment = new PlaylistFragment();
            Bundle bundle = new Bundle();
            bundle.putString("encodeId", dataPlaylist.getEncodeId());

            if (context instanceof MainActivity) {
                ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataPlaylistArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

}
