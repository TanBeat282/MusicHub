package com.tandev.musichub.adapter.search.search_suggestion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
//import com.tandev.musichub.activity.ViewArtistActivity;
//import com.tandev.musichub.activity.ViewPlaylistActivity;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.fragment.playlist.PlaylistFragment;
import com.tandev.musichub.helper.ui.PlayingStatusUpdater;
import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;
import com.tandev.musichub.model.search.search_suggestion.keyword.SearchSuggestionsDataItemKeyWordsItem;
import com.tandev.musichub.model.search.search_suggestion.playlist.SearchSuggestionsDataItemSuggestionsPlaylist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsArtist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsSong;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PlayingStatusUpdater {
    private static final int VIEW_TYPE_HISTORY = 0;
    private static final int VIEW_TYPE_KEYWORD = 1;
    private static final int VIEW_TYPE_ARTIST = 2;
    private static final int VIEW_TYPE_SONG = 3;
    private static final int VIEW_TYPE_PLAYLIST = 4;

    private ArrayList<DataSearchRecommend> dataSearchHistoryArrayList;
    private ArrayList<SearchSuggestionsDataItemKeyWordsItem> searchSuggestionsDataItemKeyWordsItems;
    private ArrayList<SearchSuggestionsDataItemSuggestionsArtist> searchSuggestionsDataItemSuggestionsArtists;
    private ArrayList<SearchSuggestionsDataItemSuggestionsPlaylist> searchSuggestionsDataItemSuggestionsPlaylists;
    private ArrayList<SearchSuggestionsDataItemSuggestionsSong> searchSuggestionsDataItemSuggestionsSongs;
    private final Context context;
    private final Activity activity;
    private int selectedPosition = -1;
    private int historyCount, keywordCount, artistCount, songCount, playlistCount;

    private KeyWordItemClickListener listener;

    public SearchSuggestionAdapter(Context context, Activity activity, ArrayList<DataSearchRecommend> dataSearchHistoryArrayList, ArrayList<SearchSuggestionsDataItemKeyWordsItem> searchSuggestionsDataItemKeyWordsItems, ArrayList<SearchSuggestionsDataItemSuggestionsArtist> searchSuggestionsDataItemSuggestionsArtists, ArrayList<SearchSuggestionsDataItemSuggestionsPlaylist> searchSuggestionsDataItemSuggestionsPlaylists, ArrayList<SearchSuggestionsDataItemSuggestionsSong> searchSuggestionsDataItemSuggestionsSongs) {
        this.context = context;
        this.activity = activity;
        this.dataSearchHistoryArrayList = dataSearchHistoryArrayList;
        this.searchSuggestionsDataItemKeyWordsItems = searchSuggestionsDataItemKeyWordsItems;
        this.searchSuggestionsDataItemSuggestionsArtists = searchSuggestionsDataItemSuggestionsArtists;
        this.searchSuggestionsDataItemSuggestionsPlaylists = searchSuggestionsDataItemSuggestionsPlaylists;
        this.searchSuggestionsDataItemSuggestionsSongs = searchSuggestionsDataItemSuggestionsSongs;

        updateCounts();
    }

    private void updateCounts() {
        historyCount = dataSearchHistoryArrayList.size();
        keywordCount = searchSuggestionsDataItemKeyWordsItems.size();
        artistCount = searchSuggestionsDataItemSuggestionsArtists.size();
        songCount = searchSuggestionsDataItemSuggestionsSongs.size();
        playlistCount = searchSuggestionsDataItemSuggestionsPlaylists.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void updatePlayingStatus(String currentEncodeId) {
        for (int i = 0; i < searchSuggestionsDataItemSuggestionsSongs.size(); i++) {
            SearchSuggestionsDataItemSuggestionsSong item = searchSuggestionsDataItemSuggestionsSongs.get(i);
            if (item.getId().equals(currentEncodeId)) {
                selectedPosition = i;
                notifyDataSetChanged();
                return;
            }
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<DataSearchRecommend> dataSearchHistoryArrayList, ArrayList<SearchSuggestionsDataItemKeyWordsItem> keyWordsItems, ArrayList<SearchSuggestionsDataItemSuggestionsArtist> artists, ArrayList<SearchSuggestionsDataItemSuggestionsPlaylist> playlists, ArrayList<SearchSuggestionsDataItemSuggestionsSong> songs) {
        this.dataSearchHistoryArrayList = dataSearchHistoryArrayList;
        this.searchSuggestionsDataItemKeyWordsItems = keyWordsItems;
        this.searchSuggestionsDataItemSuggestionsArtists = artists;
        this.searchSuggestionsDataItemSuggestionsSongs = songs;
        this.searchSuggestionsDataItemSuggestionsPlaylists = playlists;

        updateCounts();
        notifyDataSetChanged();
    }

    // Setter method for the listener
    public void setListener(KeyWordItemClickListener listener) {
        this.listener = listener;
    }

    // ViewHolder class and other adapter methods...

    public interface KeyWordItemClickListener {
        void onKeyWordItemClick(String keyword);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HISTORY:
                view = inflater.inflate(R.layout.item_search_history, parent, false);
                return new HistoryViewHolder(view);
            case VIEW_TYPE_KEYWORD:
                view = inflater.inflate(R.layout.item_keyword, parent, false);
                return new KeyWordViewHolder(view);
            case VIEW_TYPE_ARTIST:
                view = inflater.inflate(R.layout.item_select_artist, parent, false);
                return new ArtistViewHolder(view);
            case VIEW_TYPE_PLAYLIST:
                view = inflater.inflate(R.layout.item_search_playlist, parent, false);
                return new PlaylistViewHolder(view);
            case VIEW_TYPE_SONG:
            default:
                view = inflater.inflate(R.layout.item_song, parent, false);
                return new SongViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HISTORY:
                HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
                DataSearchRecommend historyItem = dataSearchHistoryArrayList.get(position);
                historyViewHolder.txt_history.setText(historyItem.getKeyword());

                historyViewHolder.linear_history.setOnClickListener(view -> {
                    if (listener != null) {
                        listener.onKeyWordItemClick(historyItem.getKeyword());
                    }
                });
                break;
            case VIEW_TYPE_KEYWORD:
                KeyWordViewHolder keywordViewHolder = (KeyWordViewHolder) holder;
                SearchSuggestionsDataItemKeyWordsItem keywordItem = searchSuggestionsDataItemKeyWordsItems.get(position - historyCount);
                keywordViewHolder.txt_keyword.setText(keywordItem.getKeyword());

                // Kiểm tra xem position có phải là item cuối cùng không
                if (position == dataSearchHistoryArrayList.size() - 1) {
                    // Nếu là item cuối cùng, thêm marginBottom
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) keywordViewHolder.itemView.getLayoutParams();
                    layoutParams.bottomMargin = 100; // Đặt giá trị marginBottom bạn muốn
                    keywordViewHolder.itemView.setLayoutParams(layoutParams);
                }

                keywordViewHolder.linear_keyword.setOnClickListener(view -> {
                    if (listener != null) {
                        listener.onKeyWordItemClick(keywordItem.getKeyword());
                    }
                });
                break;
            case VIEW_TYPE_ARTIST:
                ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;
                int artistPosition = position - historyCount - keywordCount;
                if (artistPosition >= 0 && artistPosition < artistCount) {
                    SearchSuggestionsDataItemSuggestionsArtist artistItem = searchSuggestionsDataItemSuggestionsArtists.get(artistPosition);
                    artistViewHolder.nameTextView.setText(artistItem.getName());
                    artistViewHolder.artistTextView.setText(artistItem.getFollowers() + " quan tâm");
                    Glide.with(context)
                            .load(artistItem.getAvatar())
                            .placeholder(R.drawable.holder)
                            .into(artistViewHolder.thumbImageView);

                    artistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArtistFragment artistFragment = new ArtistFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("alias", artistItem.getAliasName());

                            if (context instanceof MainActivity) {
                                ((MainActivity) context).replaceFragmentWithBundle(artistFragment, bundle);
                            }
                            ;
                        }
                    });
                }
                break;
            case VIEW_TYPE_PLAYLIST:
                PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;
                int playlistPosition = position - historyCount - keywordCount - artistCount;
                if (playlistPosition >= 0 && playlistPosition < playlistCount) {
                    SearchSuggestionsDataItemSuggestionsPlaylist playlistItem = searchSuggestionsDataItemSuggestionsPlaylists.get(playlistPosition);
                    playlistViewHolder.nameTextView.setText(playlistItem.getTitle());

                    StringBuilder artistsNames = new StringBuilder();
                    for (SearchSuggestionsDataItemSuggestionsArtist artist : playlistItem.getArtists()) {
                        if (artistsNames.length() > 0) {
                            artistsNames.append(", ");
                        }
                        artistsNames.append(artist.getName());
                    }

                    playlistViewHolder.artistTextView.setText(artistsNames.toString());
                    Glide.with(context)
                            .load(playlistItem.getThumb())
                            .placeholder(R.drawable.holder)
                            .into(playlistViewHolder.thumbImageView);
                    playlistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PlaylistFragment playlistFragment = new PlaylistFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("encodeId", playlistItem.getId());

                            if (context instanceof MainActivity) {
                                ((MainActivity) context).replaceFragmentWithBundle(playlistFragment, bundle);
                            }
                        }
                    });
                }
                break;
            case VIEW_TYPE_SONG:
            default:
                SongViewHolder songViewHolder = (SongViewHolder) holder;
                int songPosition = position - historyCount - keywordCount - artistCount - playlistCount;
                if (songPosition >= 0 && songPosition < songCount) {
                    SearchSuggestionsDataItemSuggestionsSong songItem = searchSuggestionsDataItemSuggestionsSongs.get(songPosition);
                    songViewHolder.nameTextView.setText(songItem.getTitle());

                    StringBuilder artistsNames = new StringBuilder();
                    for (SearchSuggestionsDataItemSuggestionsArtist artist : songItem.getArtists()) {
                        if (artistsNames.length() > 0) {
                            artistsNames.append(", ");
                        }
                        artistsNames.append(artist.getName());
                    }
                    songViewHolder.artistTextView.setText(artistsNames.toString());

                    Glide.with(context)
                            .load(songItem.getThumb())
                            .placeholder(R.drawable.holder)
                            .into(songViewHolder.thumbImageView);

                    if (selectedPosition == position) {
                        int colorSpotify = ContextCompat.getColor(context, R.color.colorSpotify);
                        songViewHolder.nameTextView.setTextColor(colorSpotify);
                        songViewHolder.aniPlay.setVisibility(View.VISIBLE);
                    } else {
                        songViewHolder.nameTextView.setTextColor(Color.WHITE);
                        songViewHolder.aniPlay.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return historyCount + keywordCount + artistCount + songCount + playlistCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < historyCount) {
            return VIEW_TYPE_HISTORY;
        } else if (position < historyCount + keywordCount) {
            return VIEW_TYPE_KEYWORD;
        } else if (position < historyCount + keywordCount + artistCount) {
            return VIEW_TYPE_ARTIST;
        } else if (position < historyCount + keywordCount + artistCount + songCount) {
            return VIEW_TYPE_SONG;
        } else {
            return VIEW_TYPE_PLAYLIST;
        }
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView txt_history;
        private final LinearLayout linear_history;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_history = itemView.findViewById(R.id.txt_keyword);
            linear_history = itemView.findViewById(R.id.linear_keyword);
        }
    }

    public static class KeyWordViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_keyword;
        public TextView txt_keyword;

        public KeyWordViewHolder(View itemView) {
            super(itemView);
            linear_keyword = itemView.findViewById(R.id.linear_keyword);
            txt_keyword = itemView.findViewById(R.id.txt_keyword);
        }
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView nameTextView;
        public TextView artistTextView;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            artistTextView.setSelected(true);
            nameTextView.setSelected(true);
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public ImageView btn_more;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            btn_more = itemView.findViewById(R.id.btn_more);
            artistTextView.setSelected(true);
            nameTextView.setSelected(true);
        }
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView thumbImageView;
        public TextView artistTextView;
        public TextView nameTextView;
        public LottieAnimationView aniPlay;
        public ImageView btn_more;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbImageView = itemView.findViewById(R.id.thumbImageView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            aniPlay = itemView.findViewById(R.id.aniPlay);
            btn_more = itemView.findViewById(R.id.btn_more);
            artistTextView.setSelected(true);
            nameTextView.setSelected(true);
        }
    }
}
