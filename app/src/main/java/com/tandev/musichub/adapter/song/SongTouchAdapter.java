package com.tandev.musichub.adapter.song;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.bottomsheet.BottomSheetOptionSong;
import com.tandev.musichub.helper.ui.PlayingStatusUpdater;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class SongTouchAdapter extends RecyclerView.Adapter<SongTouchAdapter.ViewHolder> implements PlayingStatusUpdater, ItemTouchHelperAdapter {
    private ArrayList<Items> songList;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;
    private final SharedPreferencesManager sharedPreferencesManager;
    private ItemTouchHelper itemTouchHelper;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<Items> filterList) {
        this.songList = filterList;
        notifyDataSetChanged();
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updatePlayingStatus(String currentEncodeId) {
        if (songList != null) {
            for (int i = 0; i < songList.size(); i++) {
                Items item = songList.get(i);
                if (item.getEncodeId().equals(currentEncodeId)) {
                    selectedPosition = i;
                    notifyDataSetChanged();
                    return;
                }
            }
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public SongTouchAdapter(ArrayList<Items> songList, Activity activity, Context context) {
        this.songList = songList;
        this.activity = activity;
        this.context = context;
        sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_touch, parent, false);
        return new ViewHolder(view, itemTouchHelper);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Items song = songList.get(position);


        holder.nameTextView.setText(song.getTitle());
        holder.artistTextView.setText(song.getArtistsNames());
        Glide.with(context)
                .load(song.getThumbnailM())  // Tải ảnh thumbnail từ liên kết
                .placeholder(R.drawable.holder)
                .into(holder.thumbImageView);  // Hiển thị thumbnail lên ImageView

        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.select_item_background);
            holder.aniPlay.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.select_item);
            holder.aniPlay.setVisibility(View.GONE);
        }
        int premiumColor;
        if (song.getStreamingStatus() == 2) {
            premiumColor = ContextCompat.getColor(context, R.color.yellow);
        } else {
            premiumColor = ContextCompat.getColor(context, R.color.white);
        }
        holder.nameTextView.setTextColor(premiumColor);

        // Thiết lập hiệu ứng mờ cho các item trước selectedPosition
        if (position < selectedPosition) {
            holder.itemView.setAlpha(0.5f); // Giá trị alpha từ 0.0 (trong suốt) đến 1.0 (rõ ràng)
        } else {
            holder.itemView.setAlpha(1.0f);
        }

        holder.btn_more.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && itemTouchHelper != null) {
                itemTouchHelper.startDrag(holder);  // Bắt đầu kéo
            }
            return true;  // Trả về true để tiêu thụ sự kiện
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showBottomSheetInfo(song);
                return false;
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (song.getStreamingStatus() == 2) {
                Toast.makeText(context, "Bạn đang nghe thử bài hát Premium!", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(context, MyService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_song", song);
            bundle.putInt("position_song", position);
            bundle.putSerializable("song_list", songList);
            intent.putExtras(bundle);

            context.startService(intent);
        });

    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition != toPosition) {
            // Swap the items in the data list
            Items movedItem = songList.remove(fromPosition);
            songList.add(toPosition, movedItem);
            // Notify that item moved
            notifyItemMoved(fromPosition, toPosition);
            // Lưu danh sách mới vào SharedPreferences
            sharedPreferencesManager.updatePositionSongOfArrayList(movedItem, toPosition);
        }
    }


    @Override
    public void onItemDismiss(int position) {
// Xóa item
        Items item = songList.get(position);
        songList.remove(position);
        notifyItemRemoved(position);
        // Xóa bài hát khỏi danh sách trong SharedPreferences
        sharedPreferencesManager.removeSongFromList(item);
        Toast.makeText(context, "Đã bài hát khỏi danh sách phát!", Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public LottieAnimationView aniPlay;
        public ImageView btn_more;

        public ViewHolder(View itemView, ItemTouchHelper itemTouchHelper) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            aniPlay = itemView.findViewById(R.id.aniPlay);
            btn_more = itemView.findViewById(R.id.btn_more);
            artistTextView.setSelected(true);
            nameTextView.setSelected(true);

            // Không cho phép di chuyển toàn bộ itemView
            itemView.setOnLongClickListener(v -> true);  // Trả về true để ngăn chặn kéo thả toàn bộ itemView
        }
    }

    private void showBottomSheetInfo(Items items) {
        BottomSheetOptionSong bottomSheetOptionSong = new BottomSheetOptionSong(context, activity, items, 1);
        bottomSheetOptionSong.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetOptionSong.getTag());
    }


}
