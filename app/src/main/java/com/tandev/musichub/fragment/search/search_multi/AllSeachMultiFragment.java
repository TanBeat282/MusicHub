package com.tandev.musichub.fragment.search.search_multi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsMoreAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.search.search_multi.top.SearchSongTopAdapter;
import com.tandev.musichub.adapter.song.SongMoreAdapter;
import com.tandev.musichub.adapter.video.VideoMoreAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SearchCategories;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.service.RetrofitClient;
import com.tandev.musichub.api.type_adapter_Factory.artist.ArtistTypeAdapter;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.artist.ArtistDetail;
import com.tandev.musichub.model.artist.SectionArtist;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.hub.HubVideo;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.search.search_featured.SearchFeatured;
import com.tandev.musichub.model.search.search_multil.SearchMulti;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.model.search.search_multil.SearchMultiDataTop;
import com.tandev.musichub.view_model.artist.ArtistViewModel;
import com.tandev.musichub.view_model.search.SearchMultiAllViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSeachMultiFragment extends Fragment {
    private SearchMultiAllViewModel searchMultiAllViewModel;
    private static final String ARG_DATA = "query";
    private static final String allowCorrect = "1";
    private String query;
    private ApiService apiService;


    //view
    private NestedScrollView nested_scroll_view;
    private RelativeLayout relative_loading;
    private RelativeLayout linear_empty_search;

    private LinearLayout linear_correct_keyword;
    private TextView txt_correct_keyword;
    private TextView txt_query;

    private LinearLayout linear_top;
    private LinearLayout linear_info;
    private RoundedImageView img_avatar;
    private TextView tv_name;
    private TextView txt_followers;
    private RecyclerView rv_song_noibat;
    private SearchSongTopAdapter searchSongTopAdapter;
    private ArrayList<Items> searchSongTopArrayList;

    private LinearLayout linear_playlist;
    private RoundedImageView img_avatar_artist_playlist;
    private TextView txt_name_artist_playlist;
    private RecyclerView rv_playlist;
    private PlaylistMoreAdapter playlistMoreAdapter;
    private ArrayList<DataPlaylist> dataPlaylistArrayList;

    private LinearLayout linear_song;
    private RecyclerView rv_song;
    private SongMoreAdapter songMoreAdapter;
    private ArrayList<Items> songArrayList;

    private LinearLayout linear_artist;
    private RecyclerView rv_artist;
    private ArtistsMoreAdapter artistsMoreAdapter;
    private ArrayList<Artists> artistsArrayList;


    private LinearLayout linear_video;
    private RecyclerView rv_video;
    private VideoMoreAdapter videoMoreAdapter;
    private ArrayList<HubVideo> hubVideos;

    private final BroadcastReceiver broadcastReceiverSearchMulti = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            query = bundle.getString("query");
            searchMulti(query);
            try {
                getSearchFeatured(query);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static AllSeachMultiFragment newInstance(String data) {
        AllSeachMultiFragment fragment = new AllSeachMultiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchMultiAllViewModel = new ViewModelProvider(this).get(SearchMultiAllViewModel.class);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_DATA);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_seach_multi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RetrofitClient.getSearchSuggestion().create(ApiService.class);
        initView(view);
        conFigViews();
        initAdapter();
        onClick();
        initViewModel();
    }

    private void initViewModel() {
        searchMultiAllViewModel.getSearchMultiMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                searchMulti(query);
                try {
                    getSearchFeatured(query);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if (searchMultiAllViewModel.getSearchMultiMutableLiveData().getValue() == null) {
            searchMulti(query);
            try {
                getSearchFeatured(query);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initView(View view) {
        nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
        relative_loading = view.findViewById(R.id.relative_loading);
        linear_empty_search = view.findViewById(R.id.linear_empty_search);

        linear_correct_keyword = view.findViewById(R.id.linear_correct_keyword);
        txt_correct_keyword = view.findViewById(R.id.txt_correct_keyword);
        txt_query = view.findViewById(R.id.txt_query);

        linear_top = view.findViewById(R.id.linear_top);
        linear_info = view.findViewById(R.id.linear_info);
        img_avatar = view.findViewById(R.id.img_avatar);
        tv_name = view.findViewById(R.id.tv_name);
        txt_followers = view.findViewById(R.id.txt_followers);

        rv_song_noibat = view.findViewById(R.id.rv_song_noibat);

        linear_playlist = view.findViewById(R.id.linear_playlist);
        img_avatar_artist_playlist = view.findViewById(R.id.img_avatar_artist_playlist);
        txt_name_artist_playlist = view.findViewById(R.id.txt_name_artist_playlist);
        rv_playlist = view.findViewById(R.id.rv_playlist);

        linear_song = view.findViewById(R.id.linear_song);
        rv_song = view.findViewById(R.id.rv_song);
        linear_video = view.findViewById(R.id.linear_video);
        rv_video = view.findViewById(R.id.rv_video);

        linear_artist = view.findViewById(R.id.linear_artist);
        rv_artist = view.findViewById(R.id.rv_artist);
    }

    private void conFigViews() {

    }

    private void initAdapter() {
        searchSongTopArrayList = new ArrayList<>();
        dataPlaylistArrayList = new ArrayList<>();
        songArrayList = new ArrayList<>();
        artistsArrayList = new ArrayList<>();
        hubVideos = new ArrayList<>();

        rv_song_noibat.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        searchSongTopAdapter = new SearchSongTopAdapter(searchSongTopArrayList, requireActivity(), requireContext());
        rv_song_noibat.setAdapter(searchSongTopAdapter);

        rv_playlist.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        playlistMoreAdapter = new PlaylistMoreAdapter(dataPlaylistArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(playlistMoreAdapter);

        rv_song.setLayoutManager(new GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false));
        songMoreAdapter = new SongMoreAdapter(songArrayList, 3, requireActivity(), requireContext());
        rv_song.setAdapter(songMoreAdapter);

        rv_video.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        videoMoreAdapter = new VideoMoreAdapter(hubVideos, requireActivity(), requireContext());
        rv_video.setAdapter(videoMoreAdapter);

        rv_artist.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        artistsMoreAdapter = new ArtistsMoreAdapter(artistsArrayList, requireActivity(), requireContext());
        rv_artist.setAdapter(artistsMoreAdapter);
    }

    private void onClick() {
        linear_info.setOnClickListener(view -> {
            ArtistFragment artistFragment = new ArtistFragment();
            Bundle bundle = new Bundle();
            if (searchMultiAllViewModel.getSearchMultiMutableLiveData().getValue().getData().getTop().getObjectType().equals("artist")) {
                bundle.putString("alias", searchMultiAllViewModel.getSearchMultiMutableLiveData().getValue().getData().getTop().getAlias());
            } else if (searchMultiAllViewModel.getSearchMultiMutableLiveData().getValue().getData().getTop().getObjectType().equals("playlist")) {
                bundle.putString("alias", searchMultiAllViewModel.getSearchMultiMutableLiveData().getValue().getData().getTop().getArtists().get(0).getAlias());
            } else {
                bundle.putString("alias", searchMultiAllViewModel.getSearchMultiMutableLiveData().getValue().getData().getTop().getArtists().get(0).getAlias());
            }

            if (requireContext() instanceof MainActivity) {
                ((MainActivity) requireContext()).replaceFragmentWithBundle(artistFragment, bundle);
            }
        });

        linear_playlist.setOnClickListener(view -> {
            sendBroadcast(2);
        });

        linear_artist.setOnClickListener(view -> {
            sendBroadcast(3);
        });
    }

    private void searchMulti(String query) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SearchCategories searchCategories = new SearchCategories();
                    Map<String, String> map = searchCategories.getSearchMulti(query);

                    Call<SearchMulti> call = service.SEARCH_MULTI_CALL(map.get("q"), allowCorrect, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SearchMulti>() {
                        @Override
                        public void onResponse(@NonNull Call<SearchMulti> call, @NonNull Response<SearchMulti> response) {
                            LogUtil.d(Constants.TAG, "searchMulti: " + call.request().url());
                            if (response.isSuccessful()) {
                                SearchMulti searchMulti = response.body();
                                if (searchMulti != null && searchMulti.getData().getSongs() != null) {
                                    searchMultiAllViewModel.setSearchMultiMutableLiveData(searchMulti);
                                } else {
                                    relative_loading.setVisibility(View.GONE);
                                    linear_empty_search.setVisibility(View.VISIBLE);
                                    nested_scroll_view.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SearchMulti> call, @NonNull Throwable throwable) {
                            LogUtil.d(Constants.TAG, "searchMulti2: " + call.request().url());
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

    private void getSearchFeatured(String query) throws Exception {
        // Gọi phương thức trong ApiService
        SearchCategories searchCategories = new SearchCategories();
        Map<String, String> map = searchCategories.getSearchFeatured();

        Call<SearchFeatured> call = apiService.SEARCH_FEATURED_CALL(query, "1", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));

        // Thực hiện cuộc gọi bất đồng bộ
        call.enqueue(new Callback<SearchFeatured>() {
            @Override
            public void onResponse(Call<SearchFeatured> call, Response<SearchFeatured> response) {
                LogUtil.d(Constants.TAG, "getSearchFeatured: " + call.request().url());
                if (response.isSuccessful()) {
                    SearchFeatured searchFeatured = response.body();
                    if (searchFeatured != null) {
                        updateUISearchFeatured(searchFeatured);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchFeatured> call, Throwable throwable) {

            }
        });
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    private void updateUISearchFeatured(SearchFeatured searchFeatured) {
        if (!searchFeatured.getData().getCorrectKeyword().isEmpty()) {
            linear_correct_keyword.setVisibility(View.VISIBLE);
            SpannableString spannableString = new SpannableString("Đang hiển thị kết quả cho " + searchFeatured.getData().getCorrectKeyword());
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 26, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            txt_correct_keyword.setText(spannableString);

            // Lấy giá trị màu từ tài nguyên màu
            int color = ContextCompat.getColor(requireContext(), R.color.colorSecondaryText1);

            SpannableString spannableString2 = new SpannableString("Tìm kiếm và thay thế cho " + query);
            spannableString2.setSpan(new ForegroundColorSpan(color), 0, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannableString2.setSpan(new ForegroundColorSpan(Color.BLUE), 25, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            txt_query.setText(spannableString2);
        } else {
            linear_correct_keyword.setVisibility(View.GONE);
        }
    }

    private void getArtist(String artistId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getArtist(artistId);

                    Call<ResponseBody> call = service.ARTISTS_CALL(artistId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getArtist " + call.request().url());
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String jsonData = response.body().string();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(SectionArtist.class, new ArtistTypeAdapter());
                                    Gson gson = gsonBuilder.create();

                                    ArtistDetail artistDetail = gson.fromJson(jsonData, ArtistDetail.class);

                                    if (artistDetail != null && artistDetail.getData() != null) {
                                        setDataTop(artistDetail);
                                    }

                                } catch (Exception e) {
                                    Log.e("TAG", "Error: " + e.getMessage(), e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
                        }
                    });
                } catch (
                        Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setDataTop(ArtistDetail artistDetail) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        tv_name.setText(artistDetail.getData().getName());
        Glide.with(requireContext()).load(artistDetail.getData().getThumbnail()).placeholder(R.drawable.holder).into(img_avatar);

        txt_followers.setText(Helper.convertToIntString(artistDetail.getData().getTotalFollow()) + " quan tâm");

        linear_top.setVisibility(View.VISIBLE);
        linear_info.setVisibility(View.VISIBLE);

    }

    private void setDataSongTop(ArrayList<Items> itemsArrayList) {
        if (itemsArrayList != null) {
            searchSongTopAdapter.setFilterList(itemsArrayList);
            rv_song_noibat.setVisibility(View.VISIBLE);
        }
    }

    private void setDataPlaylist(ArrayList<DataPlaylist> dataPlaylistArrayList, Artists artists) {
        if (dataPlaylistArrayList != null && artists != null) {
            txt_name_artist_playlist.setText(artists.getName());
            Glide.with(requireContext()).load(artists.getThumbnail()).placeholder(R.drawable.holder).into(img_avatar_artist_playlist);
            playlistMoreAdapter.setFilterList(dataPlaylistArrayList);

            linear_playlist.setVisibility(View.VISIBLE);
            rv_playlist.setVisibility(View.VISIBLE);
        }
    }

    private void setDataSong(ArrayList<Items> itemsArrayList) {
        if (itemsArrayList != null) {
            songMoreAdapter.setFilterList(itemsArrayList);
            linear_song.setVisibility(View.VISIBLE);
            rv_song.setVisibility(View.VISIBLE);
        }
    }

    private void setDataVideo(ArrayList<HubVideo> hubVideos) {
        if (hubVideos != null) {
            videoMoreAdapter.setFilterList(hubVideos);
            linear_video.setVisibility(View.VISIBLE);
            rv_video.setVisibility(View.VISIBLE);
        }
    }

    private void setDataArtist(ArrayList<Artists> artists) {
        if (artists != null) {
            artistsMoreAdapter.setFilterList(artists);
            linear_artist.setVisibility(View.VISIBLE);
            rv_artist.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(SearchMulti searchMulti) {
        if (searchMulti.getData().getTop() != null) {
            if (searchMulti.getData().getTop().getObjectType().equals("artist")) {
                getArtist(searchMulti.getData().getTop().getAlias());
            } else if (searchMulti.getData().getTop().getObjectType().equals("playlist")) {
                getArtist(searchMulti.getData().getTop().getArtists().get(0).getAlias());
            } else {
                getArtist(searchMulti.getData().getTop().getArtists().get(0).getAlias());
            }
            dataPlaylistArrayList = searchMulti.getData().getPlaylists();
            setDataPlaylist(dataPlaylistArrayList, searchMulti.getData().getPlaylists().get(0).getArtists().get(0));
        } else {
            linear_top.setVisibility(View.VISIBLE);
            linear_info.setVisibility(View.VISIBLE);
        }

        searchSongTopArrayList = searchMulti.getData().getSongs();
        setDataSongTop(searchSongTopArrayList);

        songArrayList = searchMulti.getData().getSongs();
        setDataSong(songArrayList);


        hubVideos = searchMulti.getData().getVideos();
        setDataVideo(hubVideos);

        artistsArrayList = searchMulti.getData().getArtists();
        setDataArtist(artistsArrayList);

        relative_loading.setVisibility(View.GONE);
        linear_empty_search.setVisibility(View.GONE);
        nested_scroll_view.setVisibility(View.VISIBLE);


    }

    private void sendBroadcast(int tab_layout_position) {
        Intent intent = new Intent("tab_layout_position");
        Bundle bundle = new Bundle();
        bundle.putInt("position", tab_layout_position);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiverSearchMulti, new IntentFilter("search_query"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiverSearchMulti);
    }
}