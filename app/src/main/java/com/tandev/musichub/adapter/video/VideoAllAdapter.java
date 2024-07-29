package com.tandev.musichub.adapter.video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.video.VideoFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.hub.HubVideo;

import java.util.ArrayList;

public class VideoAllAdapter extends RecyclerView.Adapter<VideoAllAdapter.ViewHolder> {
    private ArrayList<HubVideo> hubVideos;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<HubVideo> hubVideos) {
        this.hubVideos = hubVideos;
        notifyDataSetChanged();
    }

    public VideoAllAdapter(ArrayList<HubVideo> hubVideos, Activity activity, Context context) {
        this.hubVideos = hubVideos;
        this.activity = activity;
        this.context = context;
    }


    @NonNull
    @Override
    public VideoAllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAllAdapter.ViewHolder holder, int position) {
        HubVideo hubVideo = hubVideos.get(position);

        holder.nameTextView.setText(hubVideo.getTitle());
        holder.txt_time_video.setText(Helper.convertDurationToMinutesAndSeconds(hubVideo.getDuration()));
        Glide.with(context)
                .load(hubVideo.getThumbnailM())
                .placeholder(R.drawable.holder_video)
                .into(holder.image_video);

        holder.artistTextView.setText(hubVideo.getArtistsNames());
        Glide.with(context)
                .load(hubVideo.getArtist().getThumbnail())
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);
        holder.txt_premium.setVisibility(hubVideo.getStreamingStatus() == 2 ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(view -> {
            if (hubVideo.getStreamingStatus() == 2) {
                Toast.makeText(context, "Không thể phát video Premium!", Toast.LENGTH_SHORT).show();
            } else {
                VideoFragment videoFragment = new VideoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("encodeId", hubVideo.getEncodeId());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(videoFragment, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hubVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView image_video;
        public RoundedImageView thumbImageView;
        public TextView txt_time_video;
        public TextView txt_premium;
        public TextView nameTextView;
        public TextView artistTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            image_video = itemView.findViewById(R.id.image_video);
            txt_time_video = itemView.findViewById(R.id.txt_time_video);
            txt_premium = itemView.findViewById(R.id.txt_premium);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
        }
    }
}
