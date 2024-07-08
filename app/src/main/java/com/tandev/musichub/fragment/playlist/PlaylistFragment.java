package com.tandev.musichub.fragment.playlist;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsMoreAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.BlurAndBlackOverlayTransformation;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.sectionBottom.SectionBottom;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;
import com.tandev.musichub.view_model.playlist.PlaylistViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistFragment extends Fragment {
    private PlaylistViewModel playlistViewModel;

    private ImageView imageBackground;

    //tool bar
    private View tool_bar;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;

    private RoundedImageView img_playlist;
    private ProgressBar progress_image;
    private TextView txt_title_playlist;
    private TextView txt_user_name;
    private TextView txt_song_and_time;
    private TextView txt_content_playlist;
    private ArrayList<Items> itemsArrayList;
    private SongAllAdapter songAllAdapter;
    private NestedScrollView nested_scroll;
    private RelativeLayout relative_loading;
    private RecyclerView rv_playlist;
    private LinearLayout btn_play_playlist;

    private RelativeLayout relative_single, relative_playlist;
    private LinearLayout linear_playlist_like, linear_single;
    private TextView txt_title_single, txt_title_playlist_like;
    private RecyclerView rv_single, rv_playlist_like;
    private PlaylistMoreAdapter playlistMoreAdapter;
    private ArtistsMoreAdapter artistsMoreAdapter;
    private ArrayList<DataPlaylist> dataPlaylistArrayList;
    private ArrayList<Artists> artistsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initConFigViews();
        initAdapter();
        onClick();
        initViewModel();

    }

    private void initViewModel() {
        playlistViewModel.getPlaylistMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getDataBundle();
            }
        });

        if (playlistViewModel.getPlaylistMutableLiveData().getValue() == null) {
            getDataBundle();
        }
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String endCodeId = getArguments().getString("encodeId");
            getPlaylist(endCodeId);
            getSectionBottom(endCodeId);
        }
    }


    private void initViews(View view) {
        tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);


        imageBackground = view.findViewById(R.id.imageBackground);
        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll_view);

        img_playlist = view.findViewById(R.id.img_playlist);
        progress_image = view.findViewById(R.id.progress_image);
        txt_title_playlist = view.findViewById(R.id.txt_title_playlist);
        txt_title_playlist.setSelected(true);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_song_and_time = view.findViewById(R.id.txt_song_and_time);
        btn_play_playlist = view.findViewById(R.id.btn_play_playlist);
        txt_content_playlist = view.findViewById(R.id.txt_content_playlist);
        rv_playlist = view.findViewById(R.id.rv_playlist);


        // Single
        linear_single = view.findViewById(R.id.linear_single);
        linear_playlist_like = view.findViewById(R.id.linear_playlist_like);
        relative_single = view.findViewById(R.id.relative_single);
        relative_playlist = view.findViewById(R.id.relative_playlist);
        txt_title_single = view.findViewById(R.id.txt_title_single);
        txt_title_playlist_like = view.findViewById(R.id.txt_title_playlist_like);
        rv_single = view.findViewById(R.id.rv_single);
        rv_playlist_like = view.findViewById(R.id.rv_playlist_like);
    }

    private void initConFigViews() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_title_toolbar.setText("");
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status bar
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang

                    txt_title_toolbar.setText(playlistViewModel.getPlaylistMutableLiveData().getValue().getData().getTitle());
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_playlist.setLayoutManager(layoutManager);
        songAllAdapter = new SongAllAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(songAllAdapter);

        LinearLayoutManager layoutManagerSingle = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_single.setLayoutManager(layoutManagerSingle);
        artistsMoreAdapter = new ArtistsMoreAdapter(artistsArrayList, requireActivity(), requireContext());
        rv_single.setAdapter(artistsMoreAdapter);

        LinearLayoutManager layoutManagerArtists = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_playlist_like.setLayoutManager(layoutManagerArtists);
        playlistMoreAdapter = new PlaylistMoreAdapter(dataPlaylistArrayList, requireActivity(), requireContext());
        rv_playlist_like.setAdapter(playlistMoreAdapter);
    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        linear_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý sự kiện khi người dùng nhấn vào Single
            }
        });
    }

    private void getPlaylist(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getPlaylist(encodeId);

                    retrofit2.Call<Playlist> call = service.PLAYLIST_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<Playlist>() {
                        @Override
                        public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                            String requestUrl = call.request().url().toString();
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getPlaylist " + requestUrl);
                            if (response.isSuccessful()) {
                                Playlist playlist = response.body();
                                if (playlist != null && playlist.getErr() == 0) {
                                    playlistViewModel.setPlaylistMutableLiveData(playlist);
                                } else {
                                    Log.d("TAG", "Error: ");
                                }
                            } else {
                                Log.d("TAG", "Failed to retrieve data: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable throwable) {
                            Log.d("TAG", "Failed to retrieve data: " + throwable);
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

    private void updateUI(Playlist playlist) {
        ArrayList<Items> arrayList = playlist.getData().getSong().getItems();
        if (!arrayList.isEmpty()) {
            requireActivity().runOnUiThread(() -> {
                itemsArrayList = arrayList;
                songAllAdapter.setFilterList(arrayList);
            });
        }

        img_playlist.setVisibility(View.VISIBLE);
        progress_image.setVisibility(View.GONE);

        txt_title_playlist.setText(playlist.getData().getTitle());
        txt_user_name.setText(playlist.getData().getUserName());

        txt_song_and_time.setText(convertLongToString(playlist.getData().getSong().getItems().size(), playlist.getData().getSong().getTotalDuration()));
        txt_content_playlist.setText(playlist.getData().getDescription());
        // Sử dụng Glide để tải và áp dụng hiệu ứng mờ
        Glide.with(requireContext())
                .load(playlist.getData().getThumbnailM())
                .placeholder(R.color.black)
                .transform(new CenterCrop(), new BlurAndBlackOverlayTransformation(requireContext(), 25, 220)) // 25 là mức độ mờ, 150 là độ mờ của lớp phủ đen
                .into(imageBackground);

        //avatar
        Glide.with(requireContext())
                .load(playlist.getData().getThumbnailM())
                .placeholder(R.drawable.holder)
                .into(img_playlist);

        relative_loading.setVisibility(View.GONE);
        nested_scroll.setVisibility(View.VISIBLE);
    }

    private void getSectionBottom(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getSectionBottom(encodeId);
                    Call<ResponseBody> call = service.SECTION_BOTTOM_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            String requestUrl = call.request().url().toString();
                            Log.d(">>>>>>>>>>>>>>>>>>>", " - " + requestUrl);
                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    String jsonData = response.body().string();
                                    JSONObject jsonObject = new JSONObject(jsonData);
                                    SectionBottom sectionBottom = new SectionBottom();
                                    sectionBottom.parseFromJson(jsonObject);

                                    requireActivity().runOnUiThread(() -> {
                                        if (sectionBottom.getData() != null && sectionBottom.getData().getArtist() != null && sectionBottom.getData().getPlaylist() != null) {
                                            relative_single.setVisibility(View.VISIBLE);
                                            relative_playlist.setVisibility(View.VISIBLE);
                                        } else {
                                            relative_single.setVisibility(View.GONE);
                                            relative_playlist.setVisibility(View.GONE);
                                        }
                                        txt_title_single.setText(sectionBottom.getData().getArtist().getTitle());
                                        txt_title_playlist_like.setText(sectionBottom.getData().getPlaylist().getTitle());

                                        artistsArrayList = sectionBottom.getData().getArtist().getItems();
                                        artistsMoreAdapter.setFilterList(artistsArrayList);

                                        dataPlaylistArrayList = sectionBottom.getData().getPlaylist().getItems();
                                        playlistMoreAdapter.setFilterList(dataPlaylistArrayList);
                                    });
                                } catch (Exception e) {
                                    Log.e("TAG", "Error: " + e.getMessage(), e);
                                }
                            } else {
                                Log.d("TAG", "Failed to retrieve data: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }

    private String convertLongToString(int size, long time) {
        int gio = (int) (time / 3600);
        int phut = (int) ((time % 3600) / 60);

        return size + " bài hát · " + gio + " giờ " + phut + " phút";
    }

}