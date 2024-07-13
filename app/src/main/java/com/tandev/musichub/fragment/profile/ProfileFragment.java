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
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private TextView txt_add_playlist;
    private EditText edt_name_playlist;
    private RecyclerView rv_playlist;
    private ArrayList<DataPlaylist> dataPlaylists;
    private PlaylistUserAdapter playlistUserAdapter;
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

        txt_add_playlist = view.findViewById(R.id.txt_add_playlist);
        edt_name_playlist = view.findViewById(R.id.edt_name_playlist);
        rv_playlist = view.findViewById(R.id.rv_playlist);

        dataPlaylists = new ArrayList<>();

        rv_playlist.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        playlistUserAdapter = new PlaylistUserAdapter(dataPlaylists, requireActivity(), requireContext());
        rv_playlist.setAdapter(playlistUserAdapter);

        txt_add_playlist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

            }
        });

        dataPlaylists = sharedPreferencesManager.restorePlaylistUser();
        playlistUserAdapter.setFilterList(dataPlaylists);
    }
}