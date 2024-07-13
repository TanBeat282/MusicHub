package com.tandev.musichub.adapter.playlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPlaylistUserAdapter extends RecyclerView.Adapter<SelectPlaylistUserAdapter.ViewHolder> {
    private ArrayList<DataPlaylist> dataPlaylists;
    private Items items;
    private final Context context;
    private final Activity activity;
    private PlaylistItemClickListener listener;
    private SharedPreferencesManager sharedPreferencesManager;

    public SelectPlaylistUserAdapter(ArrayList<DataPlaylist> dataPlaylists, Items items, Activity activity, Context context) {
        this.dataPlaylists = dataPlaylists;
        this.items = items;
        this.activity = activity;
        this.context = context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public void setListener(PlaylistItemClickListener listener) {
        this.listener = listener;
    }

    public interface PlaylistItemClickListener {
        void onPlaylistItemClick(boolean isDismiss);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataPlaylist> filterList) {
        this.dataPlaylists = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_user, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataPlaylist dataPlaylist = dataPlaylists.get(position);

        if (dataPlaylist.getSong() != null && dataPlaylist.getSong().getItems() != null) {
            int numberOfImagesToLoad = Math.min(dataPlaylist.getSong().getItems().size(), 4);
            for (int i = 0; i < numberOfImagesToLoad; i++) {
                Glide.with(context)
                        .load(dataPlaylist.getSong().getItems().get(i).getThumbnailM())
                        .placeholder(R.drawable.holder)
                        .into(holder.imageViews[i]);
            }
            if (dataPlaylist.getSong().getItems().size() < 4) {
                for (int i = 1; i < 4; i++) {
                    holder.imageViews[i].setVisibility(ImageView.GONE);
                }
            }
        } else {
            Glide.with(context)
                    .load(R.drawable.holder)  // Đặt hình ảnh mặc định ở đây
                    .into(holder.imageViews[0]);
            holder.imageViews[0].setVisibility(ImageView.VISIBLE);  // Đảm bảo ImageView này được hiển thị

            for (int i = 1; i < 4; i++) {
                holder.imageViews[i].setVisibility(ImageView.GONE);
            }
        }

        holder.txt_title_playlist.setText(dataPlaylist.getTitle());
        holder.txt_user_name_playlist.setText("Cá nhân");

        holder.itemView.setOnClickListener(view -> {
            addSongToPlaylist(dataPlaylist.getEncodeId(), items);
        });

    }


    @Override
    public int getItemCount() {
        return dataPlaylists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView imageView3;
        private ImageView imageView4;
        // Mảng chứa các ImageView
        private ImageView[] imageViews;
        public TextView txt_title_playlist;
        public TextView txt_user_name_playlist;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);
            imageView4 = itemView.findViewById(R.id.imageView4);
            imageViews = new ImageView[]{imageView1, imageView2, imageView3, imageView4};

            txt_title_playlist = itemView.findViewById(R.id.txt_title_playlist);
            txt_user_name_playlist = itemView.findViewById(R.id.txt_user_name_playlist);
            txt_title_playlist.setSelected(true);
        }
    }

    private void addSongToPlaylist(String playlistId, Items items) {
        sharedPreferencesManager.addSongToPlaylistByEncodeId(playlistId, items);
        if (listener != null) {
            listener.onPlaylistItemClick(true);
        }
    }

}
