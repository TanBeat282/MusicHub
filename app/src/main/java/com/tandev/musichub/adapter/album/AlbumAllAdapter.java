package com.tandev.musichub.adapter.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.model.album.DataAlbum;

import java.util.ArrayList;

public class AlbumAllAdapter extends RecyclerView.Adapter<AlbumAllAdapter.PlaylistViewHolder> {

    private ArrayList<DataAlbum> dataAlbumArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataAlbum> filterList) {
        this.dataAlbumArrayList = filterList;
        notifyDataSetChanged();
    }

    public AlbumAllAdapter(ArrayList<DataAlbum> dataAlbumArrayList, Activity activity, Context context) {
        this.dataAlbumArrayList = dataAlbumArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        DataAlbum dataAlbum = dataAlbumArrayList.get(position);
        holder.nameTextView.setText(dataAlbum.getTitle());
        Glide.with(context)
                .load(dataAlbum.getThumbnailM())
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);

        holder.itemView.setOnClickListener(v -> {
            AlbumFragment albumFragment = new AlbumFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("album_endCodeId", dataAlbum.getEncodeId());

            if (context instanceof MainActivity) {
                ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataAlbumArrayList == null || dataAlbumArrayList.isEmpty()) {
            return 0;
        }
        return dataAlbumArrayList.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView nameTextView;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
