package com.tandev.musichub.adapter.radio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.model.album.DataAlbum;
import com.tandev.musichub.model.radio.user.comment.ItemDataCommentUserRadio;

import java.util.ArrayList;

public class CommentRadioAdapter extends RecyclerView.Adapter<CommentRadioAdapter.PlaylistViewHolder> {

    private ArrayList<ItemDataCommentUserRadio> itemDataCommentUserRadioArrayList;
    private final Context context;
    private final Activity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<ItemDataCommentUserRadio> filterList) {
        this.itemDataCommentUserRadioArrayList = filterList;
        notifyDataSetChanged();
    }

    public CommentRadioAdapter(ArrayList<ItemDataCommentUserRadio> itemDataCommentUserRadioArrayList, Activity activity, Context context) {
        this.itemDataCommentUserRadioArrayList = itemDataCommentUserRadioArrayList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_radio, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        ItemDataCommentUserRadio itemDataCommentUserRadio = itemDataCommentUserRadioArrayList.get(position);
        holder.txt_name_user.setText(itemDataCommentUserRadio.getUsername());
        Glide.with(context)
                .load(itemDataCommentUserRadio.getAvatar())
                .placeholder(R.drawable.holder)
                .into(holder.img_avatar_user);
        holder.txt_comment.setText(itemDataCommentUserRadio.getContent());

    }

    @Override
    public int getItemCount() {
        if (itemDataCommentUserRadioArrayList == null || itemDataCommentUserRadioArrayList.isEmpty()) {
            return 0;
        }
        return itemDataCommentUserRadioArrayList.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView img_avatar_user;
        public TextView txt_name_user;
        public TextView txt_comment;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            img_avatar_user = itemView.findViewById(R.id.img_avatar_user);
            txt_name_user = itemView.findViewById(R.id.txt_name_user);
            txt_comment = itemView.findViewById(R.id.txt_comment);
        }
    }
}
