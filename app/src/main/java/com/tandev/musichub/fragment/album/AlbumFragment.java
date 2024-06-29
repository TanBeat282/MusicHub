package com.tandev.musichub.fragment.album;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.BlurAndBlackOverlayTransformation;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.playlist.Playlist;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragment extends Fragment {
    private Playlist playlist;
    private NestedScrollView nested_scroll_view;
    private RelativeLayout relative_header;
    private ImageView img_back;
    private TextView txt_name_artist;
    private TextView txt_view;
    private TextView txt_content_playlist;
    private ImageView imageBackground;
    private RoundedImageView img_playlist;
    private ProgressBar progress_image;
    private TextView txt_title_playlist;
    private TextView txt_user_name;
    private TextView txt_song_and_time;

    private ArrayList<Items> itemsArrayList = new ArrayList<>();
    private SongAllAdapter songAllAdapter;
    private RecyclerView rv_album;

    private TextView txt_releaseDate, txt_count_song, txt_time_song, txt_distributor;

    private RecyclerView rv_artist;
    private SelectArtistAdapter selectArtistAdapter;
    private ArrayList<Artists> artistsArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);


        initView(view);
        configView();
        onClick();
        initAdapter();
        getDataBundle();
    }

    private void initView(View view) {
        imageBackground = view.findViewById(R.id.imageBackground);
        relative_header = view.findViewById(R.id.relative_header);
        img_back = view.findViewById(R.id.img_back);
        nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
        txt_name_artist = view.findViewById(R.id.txt_name_artist);
        txt_content_playlist = view.findViewById(R.id.txt_content_playlist);
        txt_content_playlist.setSelected(true);
        txt_view = view.findViewById(R.id.txt_view);

        img_playlist = view.findViewById(R.id.img_playlist);
        progress_image = view.findViewById(R.id.progress_image);
        txt_title_playlist = view.findViewById(R.id.txt_title_playlist);
        txt_title_playlist.setSelected(true);
        txt_user_name = view.findViewById(R.id.txt_user_name);
        txt_song_and_time = view.findViewById(R.id.txt_song_and_time);

        rv_album = view.findViewById(R.id.rv_album);

        txt_releaseDate = view.findViewById(R.id.txt_releaseDate);
        txt_count_song = view.findViewById(R.id.txt_count_song);
        txt_time_song = view.findViewById(R.id.txt_time_song);
        txt_distributor = view.findViewById(R.id.txt_distributor);

        rv_artist = view.findViewById(R.id.rv_artist);
    }

    private void configView() {
        nested_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_name_artist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status bar
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    txt_name_artist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_artist.setText(playlist.getData().getTitle());
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_album.setLayoutManager(layoutManager);
        songAllAdapter = new SongAllAdapter(itemsArrayList, requireActivity(),requireContext());
        rv_album.setAdapter(songAllAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_artist.setLayoutManager(linearLayoutManager);
        selectArtistAdapter = new SelectArtistAdapter(artistsArrayList, requireActivity(),requireContext());
        rv_artist.setAdapter(selectArtistAdapter);
    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
    private void getDataBundle() {
        if (getArguments() != null) {
            String album_endCodeId = getArguments().getString("album_endCodeId");
            getAlbum(album_endCodeId);
        }
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
                            String requestUrl = call.request().url().toString();
                            Log.d(">>>>>>>>>>>>>>>>>>>", " - " + requestUrl);
                            if (response.isSuccessful()) {
                                playlist = response.body();
                                if (playlist != null && playlist.getErr() == 0) {
                                    ArrayList<Items> arrayList = playlist.getData().getSong().getItems();

                                    if (!arrayList.isEmpty()) {
                                        requireActivity().runOnUiThread(() -> {

                                            // Sử dụng Glide để tải và áp dụng hiệu ứng mờ
                                            Glide.with(requireContext())
                                                    .load(playlist.getData().getThumbnailM())
                                                    .transform(new CenterCrop(), new BlurAndBlackOverlayTransformation(requireContext(), 25, 220)) // 25 là mức độ mờ, 150 là độ mờ của lớp phủ đen
                                                    .into(imageBackground);

                                            Glide.with(requireContext())
                                                    .load(playlist.getData().getThumbnailM())
                                                    .into(img_playlist);

                                            img_playlist.setVisibility(View.VISIBLE);
                                            progress_image.setVisibility(View.GONE);

                                            txt_title_playlist.setText(playlist.getData().getTitle());
                                            txt_user_name.setText(playlist.getData().getArtistsNames());

                                            txt_song_and_time.setText("Album · 2024");
                                            txt_content_playlist.setText(playlist.getData().getDescription());

                                            itemsArrayList = arrayList;
                                            songAllAdapter.setFilterList(arrayList);

                                            txt_releaseDate.setText(playlist.getData().getReleaseDate());
                                            txt_count_song.setText(arrayList.size() + " bài hát, ");
                                            txt_time_song.setText(convertLongToString(playlist.getData().getSong().getTotalDuration()));
                                            txt_distributor.setText(playlist.getData().getDistributor());
                                            artistsArrayList = playlist.getData().getArtists();

                                            selectArtistAdapter.setFilterList(playlist.getData().getArtists());

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

    private String convertLongToString(long time) {
        int gio = (int) (time / 3600);
        int phut = (int) ((time % 3600) / 60);

        return gio == 0 ? phut + " phút" : gio + " giờ " + phut + " phút";
    }
}