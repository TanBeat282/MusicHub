package com.tandev.musichub.adapter.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.fragment.hub.HubFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.home.home_new.editor_theme.HomeDataItemPlaylistEditorTheme;
import com.tandev.musichub.model.chart.home.home_new.editor_theme_3.HomeDataItemPlaylistEditorTheme3;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.season_theme.HomeDataItemPlaylistSeasonTheme;

import java.util.ArrayList;

public class HomePlaylistAdapter extends RecyclerView.Adapter<HomePlaylistAdapter.PlaylistViewHolder> {
    private ArrayList<HomeDataItem> homeDataItems;
    private final Context context;
    private final Activity activity;

    public HomePlaylistAdapter(Context context, Activity activity, ArrayList<HomeDataItem> homeDataItems) {
        this.context = context;
        this.activity = activity;
        this.homeDataItems = homeDataItems;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HomeDataItem> homeDataItems) {
        this.homeDataItems = homeDataItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hub_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        HomeDataItem homeDataItem = homeDataItems.get(position);
        if (homeDataItem instanceof HomeDataItemPlaylistSeasonTheme) {
            // hSeasonTheme
            HomeDataItemPlaylistSeasonTheme homeDataItemPlaylistSeasonTheme = (HomeDataItemPlaylistSeasonTheme) homeDataItem;

            holder.txt_title_playlist.setText(homeDataItemPlaylistSeasonTheme.getTitle());

            holder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            if (homeDataItemPlaylistSeasonTheme.getLink() != null) {
                holder.linear_more.setVisibility(View.VISIBLE);

                PlaylistMoreAdapter playlistMoreAdapter = new PlaylistMoreAdapter(homeDataItemPlaylistSeasonTheme.getItems(), activity, context);
                holder.rv_playlist_horizontal.setAdapter(playlistMoreAdapter);

                holder.linear_playlist.setOnClickListener(view -> {
                    HubFragment hubFragment = new HubFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("encodeId", Helper.extractEndCodeID(homeDataItemPlaylistSeasonTheme.getLink()));

                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                });
            } else {
                holder.linear_more.setVisibility(View.GONE);

                PlaylistAdapter playlistAdapter = new PlaylistAdapter(homeDataItemPlaylistSeasonTheme.getItems(), activity, context);
                holder.rv_playlist_horizontal.setAdapter(playlistAdapter);
            }
        } else if (homeDataItem instanceof HomeDataItemPlaylistEditorTheme) {
            // hEditorTheme
            HomeDataItemPlaylistEditorTheme homeDataItemPlaylistEditorTheme = (HomeDataItemPlaylistEditorTheme) homeDataItem;

            holder.txt_title_playlist.setText(homeDataItemPlaylistEditorTheme.getTitle());

            holder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            if (homeDataItemPlaylistEditorTheme.getLink() != null) {
                holder.linear_more.setVisibility(View.VISIBLE);

                PlaylistMoreAdapter playlistMoreAdapter = new PlaylistMoreAdapter(homeDataItemPlaylistEditorTheme.getItems(), activity, context);
                holder.rv_playlist_horizontal.setAdapter(playlistMoreAdapter);

                holder.linear_playlist.setOnClickListener(view -> {
                    HubFragment hubFragment = new HubFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("encodeId", Helper.extractEndCodeID(homeDataItemPlaylistEditorTheme.getLink()));

                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                });
            } else {
                holder.linear_more.setVisibility(View.GONE);

                PlaylistAdapter playlistAdapter = new PlaylistAdapter(homeDataItemPlaylistEditorTheme.getItems(), activity, context);
                holder.rv_playlist_horizontal.setAdapter(playlistAdapter);
            }
        } else if (homeDataItem instanceof HomeDataItemPlaylistEditorTheme3) {
            // hEditorTheme3
            HomeDataItemPlaylistEditorTheme3 homeDataItemPlaylistEditorTheme3 = (HomeDataItemPlaylistEditorTheme3) homeDataItem;

            holder.txt_title_playlist.setText(homeDataItemPlaylistEditorTheme3.getTitle());

            holder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            if (homeDataItemPlaylistEditorTheme3.getLink() != null) {
                holder.linear_more.setVisibility(View.VISIBLE);

                PlaylistMoreAdapter playlistMoreAdapter = new PlaylistMoreAdapter(homeDataItemPlaylistEditorTheme3.getItems(), activity, context);
                holder.rv_playlist_horizontal.setAdapter(playlistMoreAdapter);

                holder.linear_playlist.setOnClickListener(view -> {
                    HubFragment hubFragment = new HubFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("encodeId", Helper.extractEndCodeID(homeDataItemPlaylistEditorTheme3.getLink()));

                    ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                });
            } else {
                holder.linear_more.setVisibility(View.GONE);

                PlaylistAdapter playlistAdapter = new PlaylistAdapter(homeDataItemPlaylistEditorTheme3.getItems(), activity, context);
                holder.rv_playlist_horizontal.setAdapter(playlistAdapter);
            }
        } else {
            Log.d("TAG", "Unknown HomeDataItem type: " + homeDataItem.getClass().getSimpleName());
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(homeDataItems.size(), 3);
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_playlist;
        public TextView txt_title_playlist;
        public LinearLayout linear_more;
        public RecyclerView rv_playlist_horizontal;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            linear_playlist = itemView.findViewById(R.id.linear_playlist);
            txt_title_playlist = itemView.findViewById(R.id.txt_title_playlist);
            linear_more = itemView.findViewById(R.id.linear_more);
            rv_playlist_horizontal = itemView.findViewById(R.id.rv_playlist_horizontal);
        }
    }
}
