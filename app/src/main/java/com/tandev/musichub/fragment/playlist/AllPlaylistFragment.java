package com.tandev.musichub.fragment.playlist;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.artist.playlist.SectionArtistPlaylist;
import com.tandev.musichub.model.hub.SectionHubPlaylist;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.top100.DataTop100;

import java.util.ArrayList;

public class AllPlaylistFragment extends Fragment {

    private RelativeLayout relative_header;
    private RelativeLayout relative_loading;
    private ImageView img_back;
    private ImageView img_more;
    private TextView txt_name_artist;
    private TextView txt_view;
    private NestedScrollView nested_scroll;
    private TextView txt_playlist;
    private RecyclerView rv_playlist;
    private PlaylistAdapter playlistAllAdapter;
    private String title;

    //data_playlist
    private ArrayList<DataPlaylist> dataPlaylistArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_view_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initAdapter();
        conFigViews();
        initOnClick();
        getBundle();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("data_top_100")) {
                DataTop100 dataTop100 = (DataTop100) bundle.getSerializable("data_top_100");
                if (dataTop100 != null) {
                    title = dataTop100.getTitle();
                    txt_playlist.setText(title);
                    dataPlaylistArrayList = dataTop100.getItems();
                    playlistAllAdapter.setFilterList(dataPlaylistArrayList);
                    relative_loading.setVisibility(View.GONE);
                    nested_scroll.setVisibility(View.VISIBLE);
                }
            } else if (bundle.containsKey("section_hub_playlist")) {
                SectionHubPlaylist sectionHubPlaylist = (SectionHubPlaylist) bundle.getSerializable("section_hub_playlist");
                if (sectionHubPlaylist != null) {
                    title = sectionHubPlaylist.getTitle();
                    txt_playlist.setText(title);
                    dataPlaylistArrayList = sectionHubPlaylist.getItems();
                    playlistAllAdapter.setFilterList(dataPlaylistArrayList);
                    relative_loading.setVisibility(View.GONE);
                    nested_scroll.setVisibility(View.VISIBLE);
                }
            } else if (bundle.containsKey("section_artist_playlist")) {
                SectionArtistPlaylist sectionArtistPlaylist = (SectionArtistPlaylist) bundle.getSerializable("section_artist_playlist");
                if (sectionArtistPlaylist != null) {
                    title = sectionArtistPlaylist.getTitle();
                    txt_playlist.setText(title);
                    dataPlaylistArrayList = sectionArtistPlaylist.getItems();
                    playlistAllAdapter.setFilterList(dataPlaylistArrayList);
                    relative_loading.setVisibility(View.GONE);
                    nested_scroll.setVisibility(View.VISIBLE);
                }
            }else if (bundle.containsKey("data_playlist_arraylist")) {
                ArrayList<DataPlaylist> dataPlaylistArrayList = (ArrayList<DataPlaylist>) bundle.getSerializable("data_playlist_arraylist");
                if (dataPlaylistArrayList != null) {
                    title = "sectionArtistPlaylist.getTitle()";
                    txt_playlist.setText(title);
                    playlistAllAdapter.setFilterList(dataPlaylistArrayList);
                    relative_loading.setVisibility(View.GONE);
                    nested_scroll.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void initViews(View view) {
        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        img_back = view.findViewById(R.id.img_back);
        img_more = view.findViewById(R.id.img_more);

        relative_header = view.findViewById(R.id.relative_header);
        txt_playlist = view.findViewById(R.id.txt_playlist);
        txt_name_artist = view.findViewById(R.id.txt_name_artist);
        txt_view = view.findViewById(R.id.txt_view);

        rv_playlist = view.findViewById(R.id.rv_playlist);
    }

    private void initAdapter() {
        dataPlaylistArrayList = new ArrayList<>();
        rv_playlist.setLayoutManager(new GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false));

        playlistAllAdapter = new PlaylistAdapter(dataPlaylistArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(playlistAllAdapter);

    }

    private void conFigViews() {
        Helper.changeStatusBarColor(requireActivity(), R.color.black);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 200) {
                    txt_name_artist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 300) {
                    txt_name_artist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_artist.setText(title);
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });
    }

    private void initOnClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img_more.setOnClickListener(view -> {
            //
        });
    }
}