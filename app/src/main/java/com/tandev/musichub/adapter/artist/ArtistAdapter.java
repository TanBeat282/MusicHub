package com.tandev.musichub.adapter.artist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.single.SingleAdapter;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.adapter.video.VideoMoreAdapter;
import com.tandev.musichub.fragment.song.SongFragment;
import com.tandev.musichub.model.artist.artist.SectionArtistArtist;
import com.tandev.musichub.model.artist.playlist.SectionArtistPlaylist;
import com.tandev.musichub.model.artist.song.SectionArtistSong;
import com.tandev.musichub.model.artist.video.SectionArtistVideo;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SONG = 0;
    private static final int VIEW_TYPE_PLAYLIST = 1;
    private static final int VIEW_TYPE_VIDEO = 2;
    private static final int VIEW_TYPE_ARTIST = 3;


    private ArrayList<SectionArtistSong> sectionArtistSongs;
    private ArrayList<SectionArtistPlaylist> sectionArtistPlaylists;
    private ArrayList<SectionArtistVideo> sectionArtistVideos;
    private ArrayList<SectionArtistArtist> sectionArtistArtists;
    private final Context context;
    private final Activity activity;

    public ArtistAdapter(Context context, Activity activity, ArrayList<SectionArtistSong> sectionArtistSongs, ArrayList<SectionArtistPlaylist> sectionArtistPlaylists, ArrayList<SectionArtistVideo> sectionArtistVideos, ArrayList<SectionArtistArtist> sectionArtistArtists) {
        this.context = context;
        this.activity = activity;
        this.sectionArtistSongs = sectionArtistSongs != null ? sectionArtistSongs : new ArrayList<>();
        this.sectionArtistPlaylists = sectionArtistPlaylists != null ? sectionArtistPlaylists : new ArrayList<>();
        this.sectionArtistVideos = sectionArtistVideos != null ? sectionArtistVideos : new ArrayList<>();
        this.sectionArtistArtists = sectionArtistArtists != null ? sectionArtistArtists : new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<SectionArtistSong> sectionArtistSongs, ArrayList<SectionArtistPlaylist> sectionArtistPlaylists, ArrayList<SectionArtistVideo> sectionArtistVideos, ArrayList<SectionArtistArtist> sectionArtistArtists) {
        this.sectionArtistSongs = sectionArtistSongs != null ? sectionArtistSongs : new ArrayList<>();
        this.sectionArtistPlaylists = sectionArtistPlaylists != null ? sectionArtistPlaylists : new ArrayList<>();
        this.sectionArtistVideos = sectionArtistVideos != null ? sectionArtistVideos : new ArrayList<>();
        this.sectionArtistArtists = sectionArtistArtists != null ? sectionArtistArtists : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int totalSongs = sectionArtistSongs.size();
        int totalPlaylists = sectionArtistPlaylists.size();
        int totalVideos = sectionArtistVideos.size();
        int totalArtists = sectionArtistArtists.size();

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
        int totalSongs = sectionArtistSongs.size();
        int totalPlaylists = sectionArtistPlaylists.size();
        int totalVideos = sectionArtistVideos.size();

        if (holder.getItemViewType() == VIEW_TYPE_SONG) {
            SongViewHolder songViewHolder = (SongViewHolder) holder;
            SectionArtistSong sectionArtistSong = sectionArtistSongs.get(position);

            songViewHolder.txt_title_song.setText(sectionArtistSong.getTitle());

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4, RecyclerView.HORIZONTAL, false);
            songViewHolder.rv_song_horizontal.setLayoutManager(gridLayoutManager);
            SongAllAdapter songAllAdapter = new SongAllAdapter(sectionArtistSong.getItems(), activity, context);
            songViewHolder.rv_song_horizontal.setAdapter(songAllAdapter);

            songViewHolder.linear_more.setVisibility(sectionArtistSong.getLink().isEmpty() || sectionArtistSong.equals("") ? View.GONE : View.VISIBLE);

            songViewHolder.linear_song.setOnClickListener(view -> {
                SongFragment songFragment = new SongFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", sectionArtistSong.getOptions().getArtistId());
                bundle.putString("sectionId", sectionArtistSong.getSectionId());

                if (context instanceof MainActivity) {
                    ((MainActivity) context).replaceFragmentWithBundle(songFragment, bundle);
                }
            });
        } else if (holder.getItemViewType() == VIEW_TYPE_PLAYLIST) {
            int playlistPosition = position - sectionArtistSongs.size();
            PlaylistViewHolder playlistViewHolder = (PlaylistViewHolder) holder;
            SectionArtistPlaylist sectionArtistPlaylist = sectionArtistPlaylists.get(playlistPosition);

            playlistViewHolder.txt_title_playlist.setText(sectionArtistPlaylist.getTitle());
            playlistViewHolder.linear_more.setVisibility(sectionArtistPlaylist.getLink().isEmpty() || sectionArtistPlaylist.equals("") ? View.GONE : View.VISIBLE);

            if (sectionArtistPlaylist.getSectionId().equals("aSingle") || sectionArtistPlaylist.getSectionId().equals("aAlbum")) {
                playlistViewHolder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                SingleAdapter singleAdapter = new SingleAdapter(sectionArtistPlaylist.getItems(), activity, context);
                playlistViewHolder.rv_playlist_horizontal.setAdapter(singleAdapter);
            } else {
                playlistViewHolder.rv_playlist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                PlaylistAdapter playlistAdapter = new PlaylistAdapter(sectionArtistPlaylist.getItems(), activity, context);
                playlistViewHolder.rv_playlist_horizontal.setAdapter(playlistAdapter);
            }
        } else if (holder.getItemViewType() == VIEW_TYPE_VIDEO) {
            int videoPosition = position - sectionArtistSongs.size() - sectionArtistPlaylists.size();
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            SectionArtistVideo sectionArtistVideo = sectionArtistVideos.get(videoPosition);

            videoViewHolder.txt_title_video.setText(sectionArtistVideo.getTitle());

            videoViewHolder.rv_video_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            VideoMoreAdapter videoMoreAdapter = new VideoMoreAdapter(sectionArtistVideo.getItems(), activity, context);
            videoViewHolder.rv_video_horizontal.setAdapter(videoMoreAdapter);

            videoViewHolder.linear_more.setVisibility(sectionArtistVideo.getLink().isEmpty() || sectionArtistVideo.equals("") ? View.GONE : View.VISIBLE);


            videoViewHolder.linear_video.setOnClickListener(view -> {
                // Xử lý sự kiện click
            });
        } else if (holder.getItemViewType() == VIEW_TYPE_ARTIST) {
            int artistPosition = position - sectionArtistSongs.size() - sectionArtistPlaylists.size() - sectionArtistVideos.size();
            ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;
            SectionArtistArtist sectionArtistArtist = sectionArtistArtists.get(artistPosition);

            artistViewHolder.txt_title_artist.setText(sectionArtistArtist.getTitle());

            artistViewHolder.rv_artist_horizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ArtistsAllAdapter artistsAllAdapter = new ArtistsAllAdapter(sectionArtistArtist.getItems(), activity, context);
            artistViewHolder.rv_artist_horizontal.setAdapter(artistsAllAdapter);

            artistViewHolder.linear_more.setVisibility(sectionArtistArtist.getLink().isEmpty() || sectionArtistArtist.equals("") ? View.GONE : View.VISIBLE);

            artistViewHolder.linear_artist.setOnClickListener(view -> {
                // Xử lý sự kiện click
            });
        }
    }

    @Override
    public int getItemCount() {
        int totalSongs = sectionArtistSongs.size();
        int totalPlaylists = sectionArtistPlaylists.size();
        int totalVideos = sectionArtistVideos.size();
        int totalArtists = sectionArtistArtists.size();

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
