package com.tandev.musichub.adapter.top100;

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

import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.fragment.playlist.AllPlaylistFragment;
import com.tandev.musichub.model.top100.DataTop100;

import java.util.ArrayList;

public class Top100Adapter extends RecyclerView.Adapter<Top100Adapter.ViewHolder> {
    private ArrayList<DataTop100> dataTop100s;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataTop100> filterList) {
        this.dataTop100s = filterList;
        notifyDataSetChanged();
    }

    public Top100Adapter(ArrayList<DataTop100> dataTop100s, Activity activity, Context context) {
        this.dataTop100s = dataTop100s;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hub_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataTop100 dataTop100 = dataTop100s.get(position);

        holder.txt_title_playlist.setText(dataTop100.getTitle());
        holder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        PlaylistMoreAdapter playlistMoreAdapter = new PlaylistMoreAdapter(dataTop100.getItems(), activity, context);
        holder.rv_playlist_horizontal.setAdapter(playlistMoreAdapter);

        holder.linear_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPlaylistFragment allPlaylistFragment = new AllPlaylistFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data_top_100", dataTop100);

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(allPlaylistFragment, bundle);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataTop100s.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_playlist;
        public TextView txt_title_playlist;
        public RecyclerView rv_playlist_horizontal;

        public ViewHolder(View itemView) {
            super(itemView);
            linear_playlist = itemView.findViewById(R.id.linear_playlist);
            txt_title_playlist = itemView.findViewById(R.id.txt_title_playlist);
            rv_playlist_horizontal = itemView.findViewById(R.id.rv_playlist_horizontal);
        }
    }

}
