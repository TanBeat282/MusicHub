package com.tandev.musichub.adapter.search.search_history;

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

import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.fragment.hub.HubFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataSearchRecommend> dataSearchRecommends;
    private final Context context;
    private final Activity activity;
    private SearchRecommendClickListener listener;

    private boolean isExpanded = false;

    private static final int ITEM_TYPE_HISTORY = 0;
    private static final int ITEM_TYPE_MORE = 1;


    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataSearchRecommend> filterList) {
        this.dataSearchRecommends = filterList;
        notifyDataSetChanged();
    }

    public void setListener(SearchRecommendClickListener listener) {
        this.listener = listener;
    }

    public interface SearchRecommendClickListener {
        void onSearchRecommendClickListener(String keyword);
    }

    public SearchHistoryAdapter(ArrayList<DataSearchRecommend> dataSearchRecommends, Activity activity, Context context) {
        this.dataSearchRecommends = dataSearchRecommends;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isExpanded && dataSearchRecommends.size() > 5 && position == 5) {
            return ITEM_TYPE_MORE;
        }
        return ITEM_TYPE_HISTORY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_MORE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history_more, parent, false);
            return new HistoryMoreViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
            return new HistoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder.getItemViewType() == ITEM_TYPE_HISTORY) {
            HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;

            DataSearchRecommend dataSearchRecommend = dataSearchRecommends.get(position);

            historyViewHolder.txt_keyword.setText(dataSearchRecommend.getKeyword());

            historyViewHolder.itemView.setOnClickListener(view -> {
                if (!dataSearchRecommend.getLink().isEmpty()) {
                    if (Helper.getType(dataSearchRecommend.getLink()).equals("album")) {
                        AlbumFragment albumFragment = new AlbumFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("album_endCodeId", Helper.extractEndCodeID(dataSearchRecommend.getLink()));

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(albumFragment, bundle);
                        }
                    } else if (Helper.getType(dataSearchRecommend.getLink()).equals("playlist")) {
                        PlaylistFragment playlistFragment = new PlaylistFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("encodeId", Helper.extractEndCodeID(dataSearchRecommend.getLink()));

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
                        }
                    } else {
                        HubFragment hubFragment = new HubFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("encodeId", Helper.extractEndCodeID(dataSearchRecommend.getLink()));

                        if (context instanceof MainActivity) {
                            ((MainActivity) context).replaceFragmentWithBundle(hubFragment, bundle);
                        }
                    }
                } else {
                    listener.onSearchRecommendClickListener(dataSearchRecommend.getKeyword());
                }
            });

            historyViewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
                    sharedPreferencesManager.deleteSearchHistoryItem(dataSearchRecommend);
                    dataSearchRecommends.remove(dataSearchRecommend);
                    notifyDataSetChanged();
                }
            });
        } else {
            HistoryMoreViewHolder historyMoreViewHolder = (HistoryMoreViewHolder) holder;
            historyMoreViewHolder.txt_keyword.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View view) {
                    isExpanded = true;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (!isExpanded && dataSearchRecommends.size() > 5) {
            return 6; // 5 items + 1 "more" item
        } else {
            return dataSearchRecommends.size();
        }
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_keyword;
        public ImageView img_delete;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            txt_keyword = itemView.findViewById(R.id.txt_keyword);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }

    public static class HistoryMoreViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_keyword;

        public HistoryMoreViewHolder(View itemView) {
            super(itemView);
            txt_keyword = itemView.findViewById(R.id.txt_keyword);
        }
    }
}
