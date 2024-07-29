package com.tandev.musichub.adapter.video.select_quality_video;

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
import com.tandev.musichub.model.video.ItemVideoStreaming;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectQualityVideoAdapter extends RecyclerView.Adapter<SelectQualityVideoAdapter.ViewHolder> {
    private ArrayList<ItemVideoStreaming> itemVideoStreamings;
    private final Context context;
    private final Activity activity;
    private QualityVideoItemClickListener listener;

    public SelectQualityVideoAdapter(ArrayList<ItemVideoStreaming> itemVideoStreamings, Activity activity, Context context) {
        this.itemVideoStreamings = itemVideoStreamings;
        this.activity = activity;
        this.context = context;
    }

    public void setListener(QualityVideoItemClickListener listener) {
        this.listener = listener;
    }

    public interface QualityVideoItemClickListener {
        void onQualityVideoItemClick(int quality, boolean dismiss);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<ItemVideoStreaming> filterList) {
        this.itemVideoStreamings = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quality_video, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemVideoStreaming itemVideoStreaming = itemVideoStreamings.get(position);

        holder.txt_quality_video.setText(itemVideoStreaming.getP360());

//        holder.itemView.setOnClickListener(view -> {
//            if (listener != null) {
//                listener.onQualityVideoItemClick(itemVideoStreaming,true);
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return itemVideoStreamings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_quality_video;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_quality_video = itemView.findViewById(R.id.txt_quality_video);
        }
    }
    //0 360p
    //1 480p
    //2 720p
    //3 1080p
//
//    private int selectQualityVideo(ItemVideoStreaming  itemVideoStreaming){
//        if (itemVideoStreaming)
//    }


}
