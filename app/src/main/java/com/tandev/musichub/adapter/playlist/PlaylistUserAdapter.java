package com.tandev.musichub.adapter.playlist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.playlist.MyPlaylistFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class PlaylistUserAdapter extends RecyclerView.Adapter<PlaylistUserAdapter.ViewHolder> {
    private ArrayList<DataPlaylist> dataPlaylistArrayList;
    private final Context context;
    private final Activity activity;
    private SharedPreferencesManager sharedPreferencesManager;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataPlaylist> fillterList) {
        this.dataPlaylistArrayList = fillterList;
        notifyDataSetChanged();
    }

    public PlaylistUserAdapter(ArrayList<DataPlaylist> dataPlaylistArrayList, Activity activity, Context context) {
        this.dataPlaylistArrayList = dataPlaylistArrayList;
        this.activity = activity;
        this.context = context;
        this.sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_user, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DataPlaylist dataPlaylist = dataPlaylistArrayList.get(position);

        if (Helper.isPlaylistUser(dataPlaylist.getEncodeId())) {
            loadImagesPlaylistUser(dataPlaylist, holder);
            holder.grid_img.setVisibility(View.VISIBLE);
            holder.thumbImageView.setVisibility(View.GONE);
        } else {
            loadImagesPlaylist(dataPlaylist, holder);
            holder.grid_img.setVisibility(View.GONE);
            holder.thumbImageView.setVisibility(View.VISIBLE);
        }

        holder.txt_title_playlist.setText(dataPlaylist.getTitle());


        holder.itemView.setOnClickListener(v -> {
            if (Helper.isPlaylistUser(dataPlaylist.getEncodeId())) {
                MyPlaylistFragment myPlaylistFragment = new MyPlaylistFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", dataPlaylist.getEncodeId());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(myPlaylistFragment, bundle);
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
            sharedPreferencesManager.deletePlaylistByEncodeId(dataPlaylist.getEncodeId());
            dataPlaylistArrayList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Xóa playlist thành công", Toast.LENGTH_SHORT).show();
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return dataPlaylistArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private GridLayout grid_img;
        private RoundedImageView thumbImageView;
        private ImageView imageView1;
        private ImageView imageView2;
        private ImageView imageView3;
        private ImageView imageView4;
        // Mảng chứa các ImageView
        private ImageView[] imageViews;

        private TextView txt_title_playlist;
        private TextView txt_user_name_playlist;

        public ViewHolder(View itemView) {
            super(itemView);
            grid_img = itemView.findViewById(R.id.grid_img);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            imageView1 = itemView.findViewById(R.id.imageView1);
            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);
            imageView4 = itemView.findViewById(R.id.imageView4);
            imageViews = new ImageView[]{imageView1, imageView2, imageView3, imageView4};

            txt_title_playlist = itemView.findViewById(R.id.txt_title_playlist);
            txt_user_name_playlist = itemView.findViewById(R.id.txt_user_name_playlist);
        }
    }

    private void loadImagesPlaylistUser(DataPlaylist dataPlaylist, ViewHolder holder) {
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
        holder.txt_user_name_playlist.setText("User");
    }

    private void loadImagesPlaylist(DataPlaylist dataPlaylist, ViewHolder holder) {
        Glide.with(context)
                .load(dataPlaylist.getThumbnailM())
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);
        holder.txt_user_name_playlist.setText(dataPlaylist.getUserName() == null ? "Zing Mp3" : dataPlaylist.getUserName());
    }
}
