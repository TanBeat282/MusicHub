package com.tandev.musichub.adapter.playlist;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.bottomsheet.BottomSheetOptionPlaylist;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.fragment.playlist.AllPlaylistFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.model.playlist.DataPlaylist;

import java.util.ArrayList;

public class PlaylistMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PLAYLIST = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private ArrayList<DataPlaylist> dataPlaylistArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataPlaylist> filterList) {
        this.dataPlaylistArrayList = filterList;
        notifyDataSetChanged();
    }

    public PlaylistMoreAdapter(ArrayList<DataPlaylist> dataPlaylistArrayList, Activity activity, Context context) {
        this.dataPlaylistArrayList = dataPlaylistArrayList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataPlaylistArrayList.size() > 5 && position == 5) {
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
            DataPlaylist dataPlaylist = dataPlaylistArrayList.get(position);
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;

            playlistViewHolder.nameTextView.setText(dataPlaylist.getTitle());
            Glide.with(context)
                    .load(dataPlaylist.getThumbnailM())
                    .placeholder(R.drawable.holder)
                    .into(playlistViewHolder.thumbImageView);

            playlistViewHolder.itemView.setOnClickListener(v -> {
                if (dataPlaylist.getTextType() != null) {
                    if (dataPlaylist.getTextType().equals("Single")) {
                        AlbumFragment albumFragment = new AlbumFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("album_endCodeId", dataPlaylist.getEncodeId());

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
                        }
                    } else if (dataPlaylist.getTextType().equals("Album")) {
                        AlbumFragment albumFragment = new AlbumFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("album_endCodeId", dataPlaylist.getEncodeId());

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
                        }
                    } else {
                        PlaylistFragment playlistFragment = new PlaylistFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("encodeId", dataPlaylist.getEncodeId());

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
                        }
                    }
                } else {
                    PlaylistFragment playlistFragment = new PlaylistFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("encodeId", dataPlaylist.getEncodeId());

                    if (context instanceof MainActivity) {
                        ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(view -> {
                BottomSheetOptionPlaylist bottomSheetOptionPlaylist = new BottomSheetOptionPlaylist(context, activity, dataPlaylist);
                bottomSheetOptionPlaylist.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionPlaylist.getTag());
                return false;
            });
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.linear_more.setOnClickListener(v -> {
                AllPlaylistFragment allPlaylistFragment = new AllPlaylistFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data_playlist_arraylist", dataPlaylistArrayList);

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(allPlaylistFragment, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (dataPlaylistArrayList == null || dataPlaylistArrayList.isEmpty()) {
            return 0;
        }
        return dataPlaylistArrayList.size() > 5 ? 6 : dataPlaylistArrayList.size(); // Showing 5 items + 1 "More" button or all items if less than 5
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
