package com.tandev.musichub.fragment.search.search_multi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsMoreAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.search.search_multi.top.SearchSongTopAdapter;
import com.tandev.musichub.adapter.song.SongMoreAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SearchCategories;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.fragment.artist.ArtistFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.search.search_multil.SearchMulti;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSeachMultiFragment extends Fragment {
    private static final String ARG_DATA = "query";
    private static final String allowCorrect = "1";
    private String query;

    private SearchMulti searchMulti;

    //view
    private NestedScrollView nested_scroll_view;
    private RelativeLayout relative_loading;

    private LinearLayout linear_top;
    private LinearLayout linear_info;
    private RoundedImageView img_avatar;
    private TextView tv_name;
    private TextView txt_followers;
    private RecyclerView rv_song_noibat;
    private SearchSongTopAdapter searchSongTopAdapter;
    private final ArrayList<Items> searchSongTopArrayList = new ArrayList<>();

    private LinearLayout linear_playlist;
    private RoundedImageView img_avatar_artist_playlist;
    private TextView txt_name_artist_playlist;
    private RecyclerView rv_playlist;
    private PlaylistMoreAdapter playlistMoreAdapter;
    private final ArrayList<DataPlaylist> dataPlaylistArrayList = new ArrayList<>();

    private RecyclerView rv_song;
    private SongMoreAdapter songMoreAdapter;
    private final ArrayList<Items> songArrayList = new ArrayList<>();

    private LinearLayout linear_artist;
    private RecyclerView rv_artist;
    private ArtistsMoreAdapter artistsMoreAdapter;
    private final ArrayList<Artists> artistsArrayList = new ArrayList<>();


    private final BroadcastReceiver broadcastReceiverSearchMulti = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            query = bundle.getString("query");
            searchMulti(query);
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
        if (getArguments() != null) {
            query = getArguments().getString(ARG_DATA);
            searchMulti(query);
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

        initView(view);
        conFigViews();
        initAdapter();
        onClick();
    }

    private void initView(View view) {
        nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
        relative_loading = view.findViewById(R.id.relative_loading);

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

        rv_song = view.findViewById(R.id.rv_song);

        linear_artist = view.findViewById(R.id.linear_artist);
        rv_artist = view.findViewById(R.id.rv_artist);
    }

    private void conFigViews() {

    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_song_noibat.setLayoutManager(linearLayoutManager);
        searchSongTopAdapter = new SearchSongTopAdapter(searchSongTopArrayList, requireActivity(), requireContext());
        rv_song_noibat.setAdapter(searchSongTopAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_playlist.setLayoutManager(linearLayoutManager1);
        playlistMoreAdapter = new PlaylistMoreAdapter(dataPlaylistArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(playlistMoreAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false);
        rv_song.setLayoutManager(gridLayoutManager);
        songMoreAdapter = new SongMoreAdapter(songArrayList, 3, requireActivity(), requireContext());
        rv_song.setAdapter(songMoreAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_artist.setLayoutManager(linearLayoutManager2);
        artistsMoreAdapter = new ArtistsMoreAdapter(artistsArrayList, requireActivity(), requireContext());
        rv_artist.setAdapter(artistsMoreAdapter);
    }

    private void onClick() {
        linear_info.setOnClickListener(view -> {
            ArtistFragment artistFragment = new ArtistFragment();
            Bundle bundle = new Bundle();
            bundle.putString("alias", searchMulti.getData().getTop().getAlias());

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

    public interface ArtistFollowersCallback {
        void onFollowersFetched(int totalFollow);

        void onError(String error);
    }

    @SuppressLint("SetTextI18n")
    private void setDataTop(Artists artists) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        tv_name.setText(artists.getName());
        Glide.with(requireContext()).load(artists.getThumbnail()).into(img_avatar);

        txt_followers.setText("Loading...");
        getFollowerOfArtist(artists.getAlias(), new ArtistFollowersCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFollowersFetched(int totalFollow) {
                txt_followers.setText(Helper.convertToIntString(totalFollow) + " quan tâm");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(String error) {
                txt_followers.setText("Error");
            }
        });
    }

    private void setDataSongTop(ArrayList<Items> itemsArrayList) {
        if (itemsArrayList != null) {
            searchSongTopAdapter.setFilterList(itemsArrayList);
        }
    }

    private void setDataPlaylist(ArrayList<DataPlaylist> dataPlaylistArrayList, Artists artists) {
        if (dataPlaylistArrayList != null && artists != null) {
            txt_name_artist_playlist.setText(artists.getName());
            Glide.with(requireContext()).load(artists.getThumbnail()).into(img_avatar_artist_playlist);
            playlistMoreAdapter.setFilterList(dataPlaylistArrayList);
        }
    }

    private void setDataSong(ArrayList<Items> itemsArrayList) {
        if (itemsArrayList != null) {
            songMoreAdapter.setFilterList(itemsArrayList);
        }
    }

    private void setDataArtist(ArrayList<Artists> artists) {
        if (artists != null) {
            artistsMoreAdapter.setFilterList(artists);
        }
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
                                searchMulti = response.body();
                                if (searchMulti != null && searchMulti.getErr() == 0) {
                                    relative_loading.setVisibility(View.GONE);
                                    nested_scroll_view.setVisibility(View.VISIBLE);

                                    setDataTop(searchMulti.getData().getArtists().get(0));
                                    setDataSongTop(searchMulti.getData().getSongs());
                                    setDataPlaylist(searchMulti.getData().getPlaylists(), searchMulti.getData().getArtists().get(0));
                                    setDataSong(searchMulti.getData().getSongs());
                                    setDataArtist(searchMulti.getData().getArtists());
                                } else {
                                    Log.d("TAG", "Error: ");
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

    private void getFollowerOfArtist(String artistId, ArtistFollowersCallback callback) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getArtist(artistId);

                    Call<ResponseBody> call = service.ARTISTS_CALL(artistId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String responseBody = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseBody);

                                    if (jsonObject.getInt("err") == 0) {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        int totalFollow = data.getInt("totalFollow");
                                        requireActivity().runOnUiThread(() -> callback.onFollowersFetched(totalFollow));
                                    } else {
                                        requireActivity().runOnUiThread(() -> callback.onError("Error: "));
                                    }
                                } catch (Exception e) {
                                    requireActivity().runOnUiThread(() -> callback.onError("Error parsing response: " + e.getMessage()));
                                }
                            } else {
                                requireActivity().runOnUiThread(() -> callback.onError("Response unsuccessful: " + response.message()));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                            requireActivity().runOnUiThread(() -> callback.onError("API call failed: " + throwable.getMessage()));
                        }
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> callback.onError("Error: " + e.getMessage()));
                }
            }

            @Override
            public void onError(Exception e) {
                requireActivity().runOnUiThread(() -> callback.onError("Service creation error: " + e.getMessage()));
            }
        });
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
        if (searchMulti != null) {
            searchMulti = null;
        }
        searchMulti(query);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiverSearchMulti, new IntentFilter("search_query"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiverSearchMulti);
    }
}