package com.tandev.musichub.fragment.playlist;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistsMoreAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.song.SongMoreAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.section_bottom.SectionBottomTypeAdapter;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.section_bottom.DataSectionBottom;
import com.tandev.musichub.model.section_bottom.DataSectionBottomArtist;
import com.tandev.musichub.model.section_bottom.DataSectionBottomPlaylist;
import com.tandev.musichub.model.section_bottom.SectionBottom;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;
import com.tandev.musichub.view_model.playlist.PlaylistViewModel;
import com.tandev.musichub.view_model.section_bottom.SectionBottomViewModel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistFragment extends Fragment {
    private PlaylistViewModel playlistViewModel;
    private SectionBottomViewModel sectionBottomViewModel;

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
    private SongMoreAllAdapter songMoreAllAdapter;
    private NestedScrollView nested_scroll;
    private RelativeLayout relative_loading;
    private RecyclerView rv_playlist;
    private LinearLayout btn_play_playlist;
    private LinearLayout linear_save_playlist;
    private TextView txt_check_playlist;
    private ImageView img_check_playlist;

    private RelativeLayout relative_single, relative_playlist;
    private LinearLayout linear_playlist_like, linear_single;
    private TextView txt_title_single, txt_title_playlist_like;
    private RecyclerView rv_single, rv_playlist_like;
    private PlaylistMoreAdapter playlistMoreAdapter;
    private ArtistsMoreAdapter artistsMoreAdapter;
    private ArrayList<DataPlaylist> dataPlaylistArrayList;
    private ArrayList<Artists> artistsArrayList;

    private SharedPreferencesManager sharedPreferencesManager;
    private MusicHelper musicHelper;

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
        playlistViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        sectionBottomViewModel = new ViewModelProvider(this).get(SectionBottomViewModel.class);
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
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        musicHelper = new MusicHelper(requireContext(), sharedPreferencesManager);

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
                getDataBundlePlaylist();
            }
        });
        playlistViewModel.getSectionBottomMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getDataBundlePlaylist();
            }
        });

        if (playlistViewModel.getPlaylistMutableLiveData().getValue() == null) {
            getDataBundlePlaylist();
        }
        if (playlistViewModel.getSectionBottomMutableLiveData().getValue() == null) {
            getDataBundleSectionBottom();
        }
    }

    private void getDataBundlePlaylist() {
        if (getArguments() != null) {
            String endCodeId = getArguments().getString("encodeId");
            getPlaylist(endCodeId);
        }
    }

    private void getDataBundleSectionBottom() {
        if (getArguments() != null) {
            String endCodeId = getArguments().getString("encodeId");
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
        linear_save_playlist = view.findViewById(R.id.linear_save_playlist);
        img_check_playlist = view.findViewById(R.id.img_check_playlist);
        txt_check_playlist = view.findViewById(R.id.txt_check_playlist);
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
                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    if (playlistViewModel.getPlaylistMutableLiveData().getValue() == null) {
                        txt_title_toolbar.setText("");
                    } else {
                        txt_title_toolbar.setText(playlistViewModel.getPlaylistMutableLiveData().getValue().getData().getTitle());
                    }
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
                }
            }
        });

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_playlist.setLayoutManager(layoutManager);
        songMoreAllAdapter = new SongMoreAllAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_playlist.setAdapter(songMoreAllAdapter);

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
        btn_play_playlist.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), MyService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_song", itemsArrayList.get(0));
            bundle.putInt("position_song", 0);
            bundle.putSerializable("song_list", itemsArrayList);
            intent.putExtras(bundle);

            requireContext().startService(intent);
        });
        linear_save_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesManager.savePlaylistUser(playlistViewModel.getPlaylistMutableLiveData().getValue().getData());
                Toast.makeText(requireContext(), "Đã lưu playlist", Toast.LENGTH_SHORT).show();
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

        loadImage(playlist.getData().getThumbnailM());

        img_playlist.setVisibility(View.VISIBLE);
        progress_image.setVisibility(View.GONE);

        txt_title_playlist.setText(playlist.getData().getTitle());
        txt_user_name.setText(playlist.getData().getUserName());

        txt_song_and_time.setText(convertLongToString(playlist.getData().getSong().getItems().size(), playlist.getData().getSong().getTotalDuration()));
        txt_content_playlist.setText(playlist.getData().getDescription());

        img_check_playlist.setImageResource(sharedPreferencesManager.isPlaylistExists(playlist.getData().getEncodeId()) ? R.drawable.playlist_add_check_24px : R.drawable.ic_playlist_add);
        txt_check_playlist.setText(sharedPreferencesManager.isPlaylistExists(playlist.getData().getEncodeId()) ? "Đã Thêm" : "Thêm");

        ArrayList<Items> arrayList = playlist.getData().getSong().getItems();
        if (!arrayList.isEmpty()) {
            itemsArrayList = arrayList;
            songMoreAllAdapter.setFilterList(arrayList);
            musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), itemsArrayList, songMoreAllAdapter);
        }

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
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getSectionBottom " + requestUrl);
                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    String jsonData = response.body().string();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(DataSectionBottom.class, new SectionBottomTypeAdapter());
                                    Gson gson = gsonBuilder.create();

                                    SectionBottom sectionBottom = gson.fromJson(jsonData, SectionBottom.class);

                                    if (sectionBottom != null && sectionBottom.getData() != null) {
                                        playlistViewModel.setSectionBottomMutableLiveData(sectionBottom);
                                    }

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

    private void updateUI(SectionBottom sectionBottom) {
        ArrayList<DataSectionBottom> items = sectionBottom.getData();
        for (DataSectionBottom item : items) {
            if (item instanceof DataSectionBottomArtist) {
                DataSectionBottomArtist dataSectionBottomArtist = (DataSectionBottomArtist) item;

                txt_title_single.setText(dataSectionBottomArtist.getTitle());
                artistsArrayList = dataSectionBottomArtist.getItems();
            } else if (item instanceof DataSectionBottomPlaylist) {
                DataSectionBottomPlaylist dataSectionBottomPlaylist = (DataSectionBottomPlaylist) item;

                txt_title_playlist_like.setText(dataSectionBottomPlaylist.getTitle());
                dataPlaylistArrayList = dataSectionBottomPlaylist.getItems();
            }
        }
        playlistMoreAdapter.setFilterList(dataPlaylistArrayList);
        artistsMoreAdapter.setFilterList(artistsArrayList);
        relative_single.setVisibility(View.VISIBLE);
        relative_playlist.setVisibility(View.VISIBLE);
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
                        }

                        int endColor = getResources().getColor(R.color.gray, null);
                        startColor = ColorUtils.setAlphaComponent(startColor, 150);

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

    private String convertLongToString(int size, long time) {
        int gio = (int) (time / 3600);
        int phut = (int) ((time % 3600) / 60);

        return size + " bài hát · " + gio + " giờ " + phut + " phút";
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