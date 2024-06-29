package com.tandev.musichub.fragment.new_release.album;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.album.AlbumNewReleaseAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Album;
import com.tandev.musichub.model.new_release.NewReleaseAlbum;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReleaseAlbumFragment extends Fragment {
    private NestedScrollView nested_scroll_view_album;
    private RelativeLayout relative_loading;
    private RecyclerView recycler_view_album;
    private ArrayList<Album> albumArrayList = new ArrayList<>();
    private AlbumNewReleaseAdapter albumNewReleaseAdapter;
    private static final String VIETNAM_CATEGORY = "IWZ9Z08I";
    private static final String AU_MY_CATEGORY = "IWZ9Z08O";
    private static final String HAN_QUOC_CATEGORY = "IWZ9Z08W";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_release_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        initializeViews(view);
        setupRecyclerView();
        setupButtonListeners();
        getNewReleaseAlbum();
    }

    private void initializeViews(View view) {
        recycler_view_album = view.findViewById(R.id.recycler_view_album);
        nested_scroll_view_album = view.findViewById(R.id.nested_scroll_view_album);
        relative_loading = view.findViewById(R.id.relative_loading);
    }

    private void setupRecyclerView() {
        recycler_view_album.setLayoutManager(new LinearLayoutManager(requireContext()));
        albumNewReleaseAdapter = new AlbumNewReleaseAdapter(albumArrayList, requireActivity(), requireContext());
        recycler_view_album.setAdapter(albumNewReleaseAdapter);
    }

    private void setupButtonListeners() {

    }

    private void getNewReleaseAlbum() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getNewRelease("album");

                    retrofit2.Call<NewReleaseAlbum> call = service.NEW_RELEASE_ALBUM_CALL("album", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<NewReleaseAlbum>() {
                        @Override
                        public void onResponse(Call<NewReleaseAlbum> call, Response<NewReleaseAlbum> response) {
                            if (response.isSuccessful()) {
                                Log.d(">>>>>>>>>>>>>>>>>>", "getNewReleaseAlbum " + call.request().url());
                                NewReleaseAlbum newReleaseAlbum = response.body();
                                if (newReleaseAlbum != null && newReleaseAlbum.getErr() == 0) {
                                    ArrayList<Album> arrayList = newReleaseAlbum.getData();
                                    if (!arrayList.isEmpty()) {
                                        requireActivity().runOnUiThread(() -> {
                                            albumArrayList = arrayList;
                                            albumNewReleaseAdapter.setFilterList(albumArrayList);
                                            nested_scroll_view_album.setVisibility(View.VISIBLE);
                                            relative_loading.setVisibility(View.GONE);
                                        });
                                    } else {
                                        Log.d("TAG", "Items list is empty");
                                    }
                                } else {
                                    Log.d("TAG", "Error: ");
                                }
                            } else {
                                Log.d("TAG", "Failed to retrieve data: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<NewReleaseAlbum> call, Throwable throwable) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewReleaseAlbum();
    }
}