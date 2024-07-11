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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.model.album.DataAlbum;
import com.tandev.musichub.model.section_bottom.DataSectionBottom;
import com.tandev.musichub.model.section_bottom.DataSectionBottomPlaylist;
import com.tandev.musichub.model.section_bottom.DataSectionBottomPlaylistOfAritst;

import java.util.ArrayList;

public class AlbumSectionBottomAdapter extends RecyclerView.Adapter<AlbumSectionBottomAdapter.PlaylistViewHolder> {

    private ArrayList<DataSectionBottom> dataSectionBottoms;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataSectionBottom> filterList) {
        this.dataSectionBottoms = filterList;
        notifyDataSetChanged();
    }

    public AlbumSectionBottomAdapter(ArrayList<DataSectionBottom> dataSectionBottoms, Activity activity, Context context) {
        this.dataSectionBottoms = dataSectionBottoms;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hub_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {

        DataSectionBottom dataSectionBottom = dataSectionBottoms.get(position);
        if (dataSectionBottom instanceof DataSectionBottomPlaylist) {
            DataSectionBottomPlaylist dataSectionBottomPlaylist = (DataSectionBottomPlaylist) dataSectionBottom;

            holder.txt_title_playlist.setText(dataSectionBottomPlaylist.getTitle());

            holder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            PlaylistMoreAdapter playlistAdapter = new PlaylistMoreAdapter(dataSectionBottomPlaylist.getItems(), activity, context);
            holder.rv_playlist_horizontal.setAdapter(playlistAdapter);

            holder.linear_more.setVisibility(dataSectionBottomPlaylist.getItems().size() > 5 ? View.VISIBLE : View.GONE);

        } else if (dataSectionBottom instanceof DataSectionBottomPlaylistOfAritst) {
            DataSectionBottomPlaylistOfAritst dataSectionBottomPlaylistOfAritst = (DataSectionBottomPlaylistOfAritst) dataSectionBottom;

            holder.txt_title_playlist.setText(dataSectionBottomPlaylistOfAritst.getTitle());

            holder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            PlaylistMoreAdapter playlistAdapter = new PlaylistMoreAdapter(dataSectionBottomPlaylistOfAritst.getItems(), activity, context);
            holder.rv_playlist_horizontal.setAdapter(playlistAdapter);

            holder.linear_more.setVisibility(dataSectionBottomPlaylistOfAritst.getItems().size() > 5 ? View.VISIBLE : View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        if (dataSectionBottoms == null || dataSectionBottoms.isEmpty()) {
            return 0;
        }
        return Math.max(0, dataSectionBottoms.size() - 1);
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_playlist;
        public LinearLayout linear_more;
        public TextView txt_title_playlist;
        public RecyclerView rv_playlist_horizontal;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            linear_playlist = itemView.findViewById(R.id.linear_playlist);
            linear_more = itemView.findViewById(R.id.linear_more);
            txt_title_playlist = itemView.findViewById(R.id.txt_title_playlist);
            rv_playlist_horizontal = itemView.findViewById(R.id.rv_playlist_horizontal);
        }
    }
}
