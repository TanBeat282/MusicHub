package com.tandev.musichub.adapter.video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.hub.HubVideo;

import java.util.ArrayList;

public class VideoMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_PLAYLIST = 0;
    private static final int VIEW_TYPE_MORE = 1;

    private ArrayList<HubVideo> hubVideos;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HubVideo> hubVideos) {
        this.hubVideos = hubVideos;
        notifyDataSetChanged();
    }

    public VideoMoreAdapter(ArrayList<HubVideo> hubVideos, Activity activity, Context context) {
        this.hubVideos = hubVideos;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (hubVideos.size() > 5 && position == 5) {
            return VIEW_TYPE_MORE;
        } else {
            return VIEW_TYPE_PLAYLIST;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PLAYLIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new PlaylistViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_all, parent, false);
            return new MoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_PLAYLIST) {
            HubVideo hubVideo = hubVideos.get(position);
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;

            playlistViewHolder.nameTextView.setText(hubVideo.getTitle());
            playlistViewHolder.txt_time_video.setText(Helper.convertDurationToMinutesAndSeconds(hubVideo.getDuration()));
            Glide.with(context)
                    .load(hubVideo.getThumbnailM())
                    .placeholder(R.drawable.holder_video)
                    .into(playlistViewHolder.image_video);

            playlistViewHolder.artistTextView.setText(hubVideo.getArtistsNames());
            Glide.with(context)
                    .load(hubVideo.getArtist().getThumbnail())
                    .placeholder(R.drawable.holder)
                    .into(playlistViewHolder.thumbImageView);
            playlistViewHolder.txt_premium.setVisibility(hubVideo.getStreamingStatus() == 2 ? View.VISIBLE : View.GONE);

            playlistViewHolder.itemView.setOnClickListener(view -> {
                if (hubVideo.getStreamingStatus() == 2) {
                    Toast.makeText(context, "Không thể phát video Premium!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle video click
                }
            });
        } else {
            MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
            moreViewHolder.linear_more.setOnClickListener(v -> {
                // Handle "More" item click
            });
        }
    }

    @Override
    public int getItemCount() {
        if (hubVideos == null || hubVideos.isEmpty()) {
            return 0;
        }
        if (hubVideos.size() > 5) {
            return 6; // 5 items + 1 "More" item
        } else {
            return hubVideos.size();
        }
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView image_video;
        public RoundedImageView thumbImageView;
        public TextView txt_time_video;
        public TextView txt_premium;
        public TextView nameTextView;
        public TextView artistTextView;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            image_video = itemView.findViewById(R.id.image_video);
            txt_time_video = itemView.findViewById(R.id.txt_time_video);
            txt_premium = itemView.findViewById(R.id.txt_premium);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
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
