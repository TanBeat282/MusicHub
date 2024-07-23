package com.tandev.musichub.adapter.chart_home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.bottomsheet.BottomSheetOptionSong;
import com.tandev.musichub.helper.ui.PlayingStatusUpdater;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.service.MyService;

import java.util.ArrayList;

public class WeekChartHomeAdapter extends RecyclerView.Adapter<WeekChartHomeAdapter.ViewHolder> {
    private ArrayList<Items> itemsArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Items> filterList) {
        this.itemsArrayList = filterList;
        notifyDataSetChanged();
    }

    public WeekChartHomeAdapter(ArrayList<Items> itemsArrayList, Activity activity, Context context) {
        this.itemsArrayList = itemsArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week_chart_home, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Items song = itemsArrayList.get(position);


        holder.txt_number.setText(position + 1 + ". ");
        holder.txt_name_song.setText(song.getTitle());
    }


    @Override
    public int getItemCount() {
        return Math.min(3, itemsArrayList.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_number;
        public TextView txt_name_song;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_number = itemView.findViewById(R.id.txt_number);
            txt_name_song = itemView.findViewById(R.id.txt_name_song);
            txt_name_song.setSelected(true);
        }
    }

}
