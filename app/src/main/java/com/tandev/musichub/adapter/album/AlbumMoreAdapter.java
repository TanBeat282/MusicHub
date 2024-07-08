package com.tandev.musichub.adapter.album;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.fragment.album.AllAlbumFragment;
import com.tandev.musichub.model.album.DataAlbum;

import java.util.ArrayList;

public class AlbumMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PLAYLIST = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private ArrayList<DataAlbum> dataAlbumArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataAlbum> filterList) {
        this.dataAlbumArrayList = filterList;
        notifyDataSetChanged();
    }

    public AlbumMoreAdapter(ArrayList<DataAlbum> dataAlbumArrayList, Activity activity, Context context) {
        this.dataAlbumArrayList = dataAlbumArrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataAlbumArrayList.size() > 5 && position == 5) {
            return VIEW_TYPE_MORE;
        } else {
            return VIEW_TYPE_PLAYLIST;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PLAYLIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
            return new PlaylistViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_all, parent, false);
            return new MoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_PLAYLIST) {
            DataAlbum dataAlbum = dataAlbumArrayList.get(position);
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;

            playlistViewHolder.nameTextView.setText(dataAlbum.getTitle());
            Glide.with(context)
                    .load(dataAlbum.getThumbnailM())
                    .placeholder(R.drawable.holder)
                    .into(playlistViewHolder.thumbImageView);

            playlistViewHolder.itemView.setOnClickListener(v -> {
                AlbumFragment albumFragment = new AlbumFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("album_endCodeId", dataAlbum.getEncodeId());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
                }
            });
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.linear_more.setOnClickListener(v -> {
                AllAlbumFragment allAlbumFragment = new AllAlbumFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data_album_arraylist", dataAlbumArrayList);

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(allAlbumFragment, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (dataAlbumArrayList == null || dataAlbumArrayList.isEmpty()) {
            return 0;
        }
        if (dataAlbumArrayList.size() > 5) {
            return 6; // Showing 5 items + 1 "More" button
        } else {
            return dataAlbumArrayList.size();
        }
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

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_more;

        public MoreViewHolder(View itemView) {
            super(itemView);
            linear_more = itemView.findViewById(R.id.linear_more);
        }
    }
}
