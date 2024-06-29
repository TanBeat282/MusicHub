package com.tandev.musichub.adapter.hub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.adapter.song.SongMoreAdapter;
import com.tandev.musichub.adapter.video.VideoMoreAdapter;
import com.tandev.musichub.model.hub.SectionHubArtist;
import com.tandev.musichub.model.hub.SectionHubPlaylist;
import com.tandev.musichub.model.hub.SectionHubSong;
import com.tandev.musichub.model.hub.SectionHubVideo;

import java.util.ArrayList;

public class HubVerticalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SONG = 0;
    private static final int VIEW_TYPE_PLAYLIST = 1;
    private static final int VIEW_TYPE_VIDEO = 2;
    private static final int VIEW_TYPE_ARTIST = 3;

    private ArrayList<SectionHubPlaylist> sectionHubPlaylists = new ArrayList<>();
    private ArrayList<SectionHubSong> sectionHubSongs = new ArrayList<>();
    private ArrayList<SectionHubVideo> sectionHubVideos = new ArrayList<>();
    private ArrayList<SectionHubArtist> sectionHubArtists = new ArrayList<>();
    private final Context context;
    private final Activity activity;

    public HubVerticalAdapter(Context context, Activity activity, ArrayList<SectionHubSong> sectionHubSongs, ArrayList<SectionHubPlaylist> sectionHubPlaylists, ArrayList<SectionHubVideo> sectionHubVideos, ArrayList<SectionHubArtist> sectionHubArtists) {
        this.context = context;
        this.activity = activity;
        this.sectionHubSongs = sectionHubSongs != null ? sectionHubSongs : new ArrayList<>();
        this.sectionHubPlaylists = sectionHubPlaylists != null ? sectionHubPlaylists : new ArrayList<>();
        this.sectionHubVideos = sectionHubVideos != null ? sectionHubVideos : new ArrayList<>();
        this.sectionHubArtists = sectionHubArtists != null ? sectionHubArtists : new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<SectionHubSong> sectionHubSongs, ArrayList<SectionHubPlaylist> sectionHubPlaylists, ArrayList<SectionHubVideo> sectionHubVideos, ArrayList<SectionHubArtist> sectionHubArtists) {
        this.sectionHubSongs = sectionHubSongs != null ? sectionHubSongs : new ArrayList<>();
        this.sectionHubPlaylists = sectionHubPlaylists != null ? sectionHubPlaylists : new ArrayList<>();
        this.sectionHubVideos = sectionHubVideos != null ? sectionHubVideos : new ArrayList<>();
        this.sectionHubArtists = sectionHubArtists != null ? sectionHubArtists : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int totalSongs = sectionHubSongs.size();
        int totalPlaylists = sectionHubPlaylists.size();
        int totalVideos = sectionHubVideos.size();
        int totalArtists = sectionHubArtists.size();

        if (position < totalSongs) {
            return VIEW_TYPE_SONG;
        } else if (position < totalSongs + totalPlaylists) {
            return VIEW_TYPE_PLAYLIST;
        } else if (position < totalSongs + totalPlaylists + totalVideos) {
            return VIEW_TYPE_VIDEO;
        } else {
            return VIEW_TYPE_ARTIST;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SONG) {
            view = inflater.inflate(R.layout.item_hub_song, parent, false);
            return new SongViewHolder(view);
        } else if (viewType == VIEW_TYPE_PLAYLIST) {
            view = inflater.inflate(R.layout.item_hub_playlist, parent, false);
            return new PlaylistViewHolder(view);
        } else if (viewType == VIEW_TYPE_VIDEO) {
            view = inflater.inflate(R.layout.item_hub_video, parent, false);
            return new VideoViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_hub_artist, parent, false);
            return new ArtistViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int totalSongs = sectionHubSongs.size();
        int totalPlaylists = sectionHubPlaylists.size();
        int totalVideos = sectionHubVideos.size();

        if (holder.getItemViewType() == VIEW_TYPE_SONG) {
            SongViewHolder songViewHolder = (SongViewHolder) holder;
            SectionHubSong sectionHubSong = sectionHubSongs.get(position);

            songViewHolder.txt_title_song.setText(sectionHubSong.getTitle());

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, RecyclerView.HORIZONTAL, false);
            songViewHolder.rv_song_horizontal.setLayoutManager(gridLayoutManager);
            SongAllAdapter songAllAdapter = new SongAllAdapter(sectionHubSong.getItems(), activity, context);
            songViewHolder.rv_song_horizontal.setAdapter(songAllAdapter);

            songViewHolder.linear_more.setVisibility(sectionHubSong.getLink().isEmpty() || sectionHubSong.equals("") ? View.GONE : View.VISIBLE);

            songViewHolder.linear_song.setOnClickListener(view -> {
                // Xử lý sự kiện click
            });
        } else if (holder.getItemViewType() == VIEW_TYPE_PLAYLIST) {
            int playlistPosition = position - sectionHubSongs.size();
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;
            SectionHubPlaylist sectionHubPlaylist = sectionHubPlaylists.get(playlistPosition);

            playlistViewHolder.txt_title_playlist.setText(sectionHubPlaylist.getTitle());

            playlistViewHolder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            PlaylistAdapter playlistAdapter = new PlaylistAdapter(sectionHubPlaylist.getItems(), activity, context);
            playlistViewHolder.rv_playlist_horizontal.setAdapter(playlistAdapter);

            playlistViewHolder.linear_more.setVisibility(sectionHubPlaylist.getLink().isEmpty() || sectionHubPlaylist.equals("") ? View.GONE : View.VISIBLE);

            playlistViewHolder.linear_playlist.setOnClickListener(view -> {
                // Xử lý sự kiện click
            });
        } else if (holder.getItemViewType() == VIEW_TYPE_VIDEO) {
            int videoPosition = position - sectionHubSongs.size() - sectionHubPlaylists.size();
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            SectionHubVideo sectionHubVideo = sectionHubVideos.get(videoPosition);

            videoViewHolder.txt_title_video.setText(sectionHubVideo.getTitle());

            videoViewHolder.rv_video_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            VideoMoreAdapter videoMoreAdapter = new VideoMoreAdapter(sectionHubVideo.getItems(), activity, context);
            videoViewHolder.rv_video_horizontal.setAdapter(videoMoreAdapter);

            videoViewHolder.linear_more.setVisibility(sectionHubVideo.getLink().isEmpty() || sectionHubVideo.equals("") ? View.GONE : View.VISIBLE);


            videoViewHolder.linear_video.setOnClickListener(view -> {
                // Xử lý sự kiện click
            });
        } else if (holder.getItemViewType() == VIEW_TYPE_ARTIST) {
            int artistPosition = position - sectionHubSongs.size() - sectionHubPlaylists.size() - sectionHubVideos.size();
            ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;
            SectionHubArtist sectionHubArtist = sectionHubArtists.get(artistPosition);

            artistViewHolder.txt_title_artist.setText(sectionHubArtist.getTitle());

            artistViewHolder.rv_artist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ArtistsAdapter artistsAdapter = new ArtistsAdapter(sectionHubArtist.getItems(), activity, context);
            artistViewHolder.rv_artist_horizontal.setAdapter(artistsAdapter);

            artistViewHolder.linear_more.setVisibility(sectionHubArtist.getLink().isEmpty() || sectionHubArtist.equals("") ? View.GONE : View.VISIBLE);

            artistViewHolder.linear_artist.setOnClickListener(view -> {
                // Xử lý sự kiện click
            });
        }
    }

    @Override
    public int getItemCount() {
        int totalSongs = sectionHubSongs.size();
        int totalPlaylists = sectionHubPlaylists.size();
        int totalVideos = sectionHubVideos.size();
        int totalArtists = sectionHubArtists.size();

        return (totalSongs > 0 ? totalSongs : 0) +
                (totalPlaylists > 0 ? totalPlaylists : 0) +
                (totalVideos > 0 ? totalVideos : 0) +
                (totalArtists > 0 ? totalArtists : 0);
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_playlist;
        public LinearLayout linear_more;
        public TextView txt_title_playlist;
        public RecyclerView rv_playlist_horizontal;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            linear_playlist = itemView.findViewById(R.id.linear_playlist);
            linear_more = itemView.findViewById(R.id.linear_more);
            txt_title_playlist = itemView.findViewById(R.id.txt_title_playlist);
            rv_playlist_horizontal = itemView.findViewById(R.id.rv_playlist_horizontal);
        }
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_song;
        public LinearLayout linear_more;
        public TextView txt_title_song;
        public RecyclerView rv_song_horizontal;

        public SongViewHolder(View itemView) {
            super(itemView);
            linear_song = itemView.findViewById(R.id.linear_song);
            linear_more = itemView.findViewById(R.id.linear_more);
            txt_title_song = itemView.findViewById(R.id.txt_title_song);
            rv_song_horizontal = itemView.findViewById(R.id.rv_song_horizontal);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_video;
        public LinearLayout linear_more;
        public TextView txt_title_video;
        public RecyclerView rv_video_horizontal;

        public VideoViewHolder(View itemView) {
            super(itemView);
            linear_video = itemView.findViewById(R.id.linear_video);
            linear_more = itemView.findViewById(R.id.linear_more);
            txt_title_video = itemView.findViewById(R.id.txt_title_video);
            rv_video_horizontal = itemView.findViewById(R.id.rv_video_horizontal);
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linear_artist;
        public LinearLayout linear_more;
        public TextView txt_title_artist;
        public RecyclerView rv_artist_horizontal;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            linear_artist = itemView.findViewById(R.id.linear_artist);
            linear_more = itemView.findViewById(R.id.linear_more);
            txt_title_artist = itemView.findViewById(R.id.txt_title_artist);
            rv_artist_horizontal = itemView.findViewById(R.id.rv_artist_horizontal);
        }
    }
}
