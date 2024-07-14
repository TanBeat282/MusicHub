package com.tandev.musichub.fragment.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistUserAdapter;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private RecyclerView rv_playlist;
    private RecyclerView rv_song;
    private ArrayList<DataPlaylist> dataPlaylists;
    private ArrayList<Items> itemsArrayList;
    private PlaylistUserAdapter playlistUserAdapter;
    private SongAllAdapter songAllAdapter;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        rv_playlist = view.findViewById(R.id.rv_playlist);
        rv_song = view.findViewById(R.id.rv_song);

        dataPlaylists = new ArrayList<>();
        itemsArrayList = new ArrayList<>();

        rv_playlist.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        playlistUserAdapter = new PlaylistUserAdapter(dataPlaylists, requireActivity(), requireContext());
        rv_playlist.setAdapter(playlistUserAdapter);

        rv_song.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songAllAdapter = new SongAllAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_song.setAdapter(songAllAdapter);

        dataPlaylists = sharedPreferencesManager.restorePlaylistUser();
        playlistUserAdapter.setFilterList(dataPlaylists);

        itemsArrayList = sharedPreferencesManager.restoreSongArrayListFavorite();
        songAllAdapter.setFilterList(itemsArrayList);
    }
}