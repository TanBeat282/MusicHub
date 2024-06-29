package com.tandev.musichub.fragment.album;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.album.AlbumMoreAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.album.DataAlbum;

import java.util.ArrayList;

public class AllAlbumFragment extends Fragment {
    private RelativeLayout relative_header;
    private ImageView img_back;
    private TextView txt_name_artist, txt_view;
    private ImageView img_more;
    private NestedScrollView nested_scroll;
    private TextView txt_playlist;
    private RecyclerView rv_playlist;
    private ArrayList<DataAlbum> dataAlbumArrayList = new ArrayList<>();
    private AlbumMoreAdapter albumMoreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.changeStatusBarColor(requireActivity(), R.color.black);
        initViews(view);
        conFigViews();
        initAdapter();
        getBundle();
    }
    private void getBundle() {
        if (getArguments() != null) {
            dataAlbumArrayList = (ArrayList<DataAlbum>) getArguments().getSerializable("data_album_arraylist");
            albumMoreAdapter.setFilterList(dataAlbumArrayList);
        }
    }
    private void initViews(View view) {
        relative_header = view.findViewById(R.id.relative_header);
        img_back = view.findViewById(R.id.img_back);
        txt_name_artist = view.findViewById(R.id.txt_name_artist);
        txt_view = view.findViewById(R.id.txt_view);
        img_more = view.findViewById(R.id.img_more);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        txt_playlist = view.findViewById(R.id.txt_playlist);
        rv_playlist = view.findViewById(R.id.rv_playlist);

    }

    @SuppressLint("SetTextI18n")
    private void conFigViews() {
        txt_playlist.setText("Album");

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
                    txt_name_artist.setText("Album");
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
        rv_playlist.setLayoutManager(gridLayoutManager);
        albumMoreAdapter = new AlbumMoreAdapter(dataAlbumArrayList, requireActivity(),requireContext());
        rv_playlist.setAdapter(albumMoreAdapter);
    }
}