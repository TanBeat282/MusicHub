package com.tandev.musichub.fragment.album;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.album.AlbumSectionBottomAdapter;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.adapter.song.SongMoreAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.section_bottom.SectionBottomTypeAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.section_bottom.DataSectionBottom;
import com.tandev.musichub.model.section_bottom.SectionBottom;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;
import com.tandev.musichub.view_model.album.AlbumViewModel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragment extends Fragment {
    private AlbumViewModel albumViewModel;

    //tool bar
    private View tool_bar;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;

    private NestedScrollView nested_scroll_view;
    private TextView txt_content_playlist;
    private ImageView imageBackground;
    private RoundedImageView img_playlist;
    private ProgressBar progress_image;
    private TextView txt_title_album;
    private TextView txt_user_name;
    private TextView txt_song_and_time;
    private ImageView btn_play_playlist;

    private ArrayList<Items> itemsArrayList;
    private SongMoreAllAdapter songMoreAllAdapter;
    private RecyclerView rv_song;

    private TextView txt_releaseDate, txt_count_song, txt_time_song, txt_distributor;

    private View view_artist, view_playlist;

    private LinearLayout linear_artist;
    private TextView txt_title_artist;
    private LinearLayout linear_more_artist;
    private RecyclerView rv_artist_horizontal;
    private SelectArtistAdapter selectArtistAdapter;
    private ArrayList<Artists> artistsArrayList;

    private RecyclerView rv_playlist;
    private AlbumSectionBottomAdapter albumSectionBottomAdapter;
    private ArrayList<DataSectionBottom> dataSectionBottoms;
    private MusicHelper musicHelper;
    private SharedPreferencesManager sharedPreferencesManager;

    public BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                Items items = (Items) bundle.get("object_song");
                musicHelper.checkIsPlayingPlaylist(items, itemsArrayList, songMoreAllAdapter);

            }
        };
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumViewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        musicHelper = new MusicHelper(requireContext(), sharedPreferencesManager);

        initView(view);
        configView();
        onClick();
        initAdapter();
        initViewModel();
    }

    private void initViewModel() {
        albumViewModel.getPlaylistMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getDataBundle();
            }
        });

        if (albumViewModel.getPlaylistMutableLiveData().getValue() == null) {
            getDataBundle();
        }
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String album_endCodeId = getArguments().getString("album_endCodeId");
            getAlbum(album_endCodeId);
            getSectionBottom(album_endCodeId);
        }
    }

    private void initView(View view) {
        tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);

        imageBackground = view.findViewById(R.id.imageBackground);
        nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
        txt_content_playlist = view.findViewById(R.id.txt_content_playlist);
        txt_content_playlist.setSelected(true);

        img_playlist = view.findViewById(R.id.img_playlist);
        progress_image = view.findViewById(R.id.progress_image);
        txt_title_album = view.findViewById(R.id.txt_title_album);
        txt_title_album.setSelected(true);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_song_and_time = view.findViewById(R.id.txt_song_and_time);

        btn_play_playlist = view.findViewById(R.id.btn_play_playlist);

        rv_song = view.findViewById(R.id.rv_song);

        txt_releaseDate = view.findViewById(R.id.txt_releaseDate);
        txt_count_song = view.findViewById(R.id.txt_count_song);
        txt_time_song = view.findViewById(R.id.txt_time_song);
        txt_distributor = view.findViewById(R.id.txt_distributor);

        view_artist = view.findViewById(R.id.view_artist);
        linear_artist = view_artist.findViewById(R.id.linear_artist);
        txt_title_artist = view_artist.findViewById(R.id.txt_title_artist);
        linear_more_artist = view_artist.findViewById(R.id.linear_more);
        rv_artist_horizontal = view_artist.findViewById(R.id.rv_artist_horizontal);

        rv_playlist = view.findViewById(R.id.rv_playlist);

    }

    private void configView() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);


        nested_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_title_toolbar.setText("");
                    relative_header.setBackgroundResource(android.R.color.transparent);
                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    if (albumViewModel.getPlaylistMutableLiveData().getValue() == null) {
                        txt_title_toolbar.setText("");
                    } else {
                        txt_title_toolbar.setText(albumViewModel.getPlaylistMutableLiveData().getValue().getData().getTitle());
                    }
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
                }
            }
        });
    }

    private void initAdapter() {
        itemsArrayList = new ArrayList<>();
        artistsArrayList = new ArrayList<>();
        dataSectionBottoms = new ArrayList<>();

        rv_song.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songMoreAllAdapter = new SongMoreAllAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_song.setAdapter(songMoreAllAdapter);

        rv_artist_horizontal.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        selectArtistAdapter = new SelectArtistAdapter(artistsArrayList, requireActivity(), requireContext());
        rv_artist_horizontal.setAdapter(selectArtistAdapter);

        rv_playlist.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        albumSectionBottomAdapter = new AlbumSectionBottomAdapter(dataSectionBottoms, requireActivity(), requireContext());
        rv_playlist.setAdapter(albumSectionBottomAdapter);

    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btn_play_playlist.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), MyService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_song", itemsArrayList.get(0));
            bundle.putInt("position_song", 0);
            bundle.putSerializable("song_list", itemsArrayList);
            intent.putExtras(bundle);

            requireContext().startService(intent);
        });
    }

    private void getAlbum(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getPlaylist(encodeId);

                    retrofit2.Call<Playlist> call = service.PLAYLIST_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<Playlist>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getAlbum " + call.request().url());
                            if (response.isSuccessful()) {
                                Playlist playlist = response.body();
                                if (playlist != null && playlist.getErr() == 0) {
                                    albumViewModel.setPlaylistMutableLiveData(playlist);
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
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getSectionBottom " + call.request().url());
                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    String jsonData = response.body().string();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(DataSectionBottom.class, new SectionBottomTypeAdapter());
                                    Gson gson = gsonBuilder.create();

                                    SectionBottom sectionBottom = gson.fromJson(jsonData, SectionBottom.class);

                                    if (sectionBottom != null && sectionBottom.getData() != null) {
                                        updateUISectionBottom(sectionBottom);
                                    }

                                } catch (Exception e) {
                                    Log.e("TAG", "Error: " + e.getMessage(), e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {

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

    @SuppressLint("SetTextI18n")
    private void updateUI(Playlist playlist) {
        ArrayList<Items> arrayList = playlist.getData().getSong().getItems();

        loadImage(playlist.getData().getThumbnailM());

        img_playlist.setVisibility(View.VISIBLE);
        progress_image.setVisibility(View.GONE);

        txt_title_album.setText(playlist.getData().getTitle());
        txt_user_name.setText(playlist.getData().getArtistsNames());

        txt_song_and_time.setText("Album · 2024");
        txt_content_playlist.setText(playlist.getData().getDescription());

        txt_releaseDate.setText(playlist.getData().getReleaseDate());
        txt_count_song.setText(arrayList.size() + " bài hát, ");
        txt_time_song.setText(convertLongToString(playlist.getData().getSong().getTotalDuration()));
        txt_distributor.setText(playlist.getData().getDistributor());

        itemsArrayList = arrayList;
        songMoreAllAdapter.setFilterList(arrayList);
        musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), itemsArrayList, songMoreAllAdapter);

        txt_title_artist.setText("Nghệ sĩ tham gia");
        linear_more_artist.setVisibility(View.GONE);

        artistsArrayList = playlist.getData().getArtists();
        selectArtistAdapter.setFilterList(artistsArrayList);
    }

    private void updateUISectionBottom(SectionBottom sectionBottom) {
        dataSectionBottoms = sectionBottom.getData();

        albumSectionBottomAdapter.setFilterList(dataSectionBottoms);
    }

    private String convertLongToString(long time) {
        int gio = (int) (time / 3600);
        int phut = (int) ((time % 3600) / 60);

        return gio == 0 ? phut + " phút" : gio + " giờ " + phut + " phút";
    }

    private void loadImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.holder_video)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transform(new BitmapTransformation() {
                    @Override
                    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                        Palette p = Palette.from(toTransform).generate();
                        int defaultColor = 0x000000;
                        int startColor = p.getDarkVibrantColor(defaultColor);

                        Log.d("Check Start Color", "transform: " + startColor);

                        if (startColor == defaultColor) {
                            startColor = p.getDarkMutedColor(defaultColor);
                            if (startColor == defaultColor) {
                                startColor = p.getVibrantColor(defaultColor);
                                if (startColor == defaultColor) {
                                    startColor = p.getMutedColor(defaultColor);
                                    if (startColor == defaultColor) {
                                        startColor = p.getLightVibrantColor(defaultColor);
                                        if (startColor == defaultColor) {
                                            startColor = p.getLightMutedColor(defaultColor);
                                        }
                                    }
                                }
                            }
                            Log.d("Check Start Color", "transform: " + startColor);
                        }

                        int endColor = getResources().getColor(R.color.gray, null);
                        startColor = ColorUtils.setAlphaComponent(startColor, 150);
                        Log.d("Check End Color", "transform: " + endColor);

                        GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[]{startColor, endColor}
                        );
                        gd.setCornerRadius(0f);
                        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                        gd.setGradientRadius(0.2f);

                        requireActivity().runOnUiThread(() -> imageBackground.setBackground(gd));

                        return toTransform;
                    }

                    @Override
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
                        messageDigest.update("paletteTransformer".getBytes());
                    }
                })
                .into(img_playlist);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(createBroadcastReceiver(), new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(createBroadcastReceiver());
    }
}