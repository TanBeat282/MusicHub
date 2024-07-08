package com.tandev.musichub.fragment.artist;

import android.annotation.SuppressLint;
import android.graphics.Color;
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

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.artist.ArtistTypeAdapter;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.artist.ArtistDetail;
import com.tandev.musichub.model.artist.SectionArtist;
import com.tandev.musichub.model.artist.artist.SectionArtistArtist;
import com.tandev.musichub.model.artist.playlist.SectionArtistPlaylist;
import com.tandev.musichub.model.artist.song.SectionArtistSong;
import com.tandev.musichub.model.artist.video.SectionArtistVideo;
import com.tandev.musichub.view_model.artist.ArtistViewModel;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragment extends Fragment {
    private ArtistViewModel artistViewModel;

    private ArtistAdapter artistAdapter;
    private ArrayList<SectionArtistSong> sectionArtistSongs;
    private ArrayList<SectionArtistPlaylist> sectionArtistPlaylists;
    private ArrayList<SectionArtistVideo> sectionArtistVideos;
    private ArrayList<SectionArtistArtist> sectionArtistArtists;

    //tool bar
    private View tool_bar;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;

    private RelativeLayout relative_loading;

    private NestedScrollView nested_scroll;

    private ImageView img_artist;
    private TextView txt_artist;
    private TextView txt_follow;

    private RelativeLayout relative_new_song;
    private RoundedImageView img_song;
    private TextView txtTitle;
    private TextView txtArtist;

    private RecyclerView rv_artist_vertical;

    private TextView txt_info;
    private TextView txt_name_real;
    private TextView txt_date_birth;
    private TextView txt_country;
    private TextView txt_genre;
    private String name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistViewModel = new ViewModelProvider(this).get(ArtistViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        conFigViews();
        initAdapter();
        onClick();
        initViewModel();
    }

    private void initViewModel() {
        artistViewModel.getArtistDetail().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getDataBundle();
            }
        });

        if (artistViewModel.getArtistDetail().getValue() == null) {
            getDataBundle();
        }
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String alias = getArguments().getString("alias");
            getArtist(alias);
        }
    }

    private void initViews(View view) {
        tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);


        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        img_artist = view.findViewById(R.id.img_artist);
        ProgressBar progress_image = view.findViewById(R.id.progress_image);
        txt_artist = view.findViewById(R.id.txt_artist);
        txt_follow = view.findViewById(R.id.txt_follow);

        relative_new_song = view.findViewById(R.id.relative_new_song);
        img_song = view.findViewById(R.id.img_song);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtArtist = view.findViewById(R.id.txtArtist);

        rv_artist_vertical = view.findViewById(R.id.rv_artist_vertical);


        txt_info = view.findViewById(R.id.txt_info);
        txt_name_real = view.findViewById(R.id.txt_name_real);
        txt_date_birth = view.findViewById(R.id.txt_date_birth);
        txt_country = view.findViewById(R.id.txt_country);
        txt_genre = view.findViewById(R.id.txt_genre);
    }

    private void conFigViews() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);


        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 600) {
                    txt_title_toolbar.setText("");
                    relative_header.setBackgroundResource(android.R.color.transparent);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }

                } else if (scrollY >= 800) {
                    txt_title_toolbar.setText(name);
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });
    }

    private void initAdapter() {
        sectionArtistSongs = new ArrayList<>();
        sectionArtistPlaylists = new ArrayList<>();
        sectionArtistVideos = new ArrayList<>();
        sectionArtistArtists = new ArrayList<>();

        rv_artist_vertical.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        artistAdapter = new ArtistAdapter(requireContext(), requireActivity(), sectionArtistSongs, sectionArtistPlaylists, sectionArtistVideos, sectionArtistArtists);
        rv_artist_vertical.setAdapter(artistAdapter);
    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        relative_new_song.setOnClickListener(view1 -> {
            SectionArtistSong sectionArtistSong = null;

            for (SectionArtist item : artistViewModel.getArtistDetail().getValue().getData().getSections()) {
                if (item instanceof SectionArtistSong) {
                    sectionArtistSong = (SectionArtistSong) item;
                    break;
                }
            }

            if (sectionArtistSong != null && sectionArtistSong.getTopAlbum() != null) {
                AlbumFragment albumFragment = new AlbumFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("album_endCodeId", sectionArtistSong.getTopAlbum().getEncodeId());

                if (requireContext() instanceof MainActivity) {
                    ((MainActivity) requireContext()).replaceFragmentWithBundle(albumFragment, bundle);
                }
            } else {
                Log.e("TAG", "SectionArtistSong or TopAlbum is null");
            }
        });

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

                                    if (artistDetail != null) {
                                        artistViewModel.setArtistDetail(artistDetail);
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
    private void updateUI(ArtistDetail artistDetail) {
        if (artistDetail != null && artistDetail.getData() != null) {
            SectionArtistSong sectionArtistSong = null;
            ArrayList<SectionArtist> items = artistDetail.getData().getSections();
            for (SectionArtist item : items) {
                if (item instanceof SectionArtistPlaylist) {
                    SectionArtistPlaylist sectionArtistPlaylist = (SectionArtistPlaylist) item;
                    sectionArtistPlaylists.add(sectionArtistPlaylist);
                } else if (item instanceof SectionArtistVideo) {
                    SectionArtistVideo sectionArtistVideo = (SectionArtistVideo) item;
                    sectionArtistVideos.add(sectionArtistVideo);
                } else if (item instanceof SectionArtistArtist) {
                    SectionArtistArtist sectionArtistArtist = (SectionArtistArtist) item;
                    sectionArtistArtists.add(sectionArtistArtist);
                } else {
                    sectionArtistSong = (SectionArtistSong) item;
                    sectionArtistSongs.add(sectionArtistSong);
                }
            }

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.color.gray)
                    .error(R.color.red);
            Glide.with(requireContext())
                    .load(artistDetail.getData().getThumbnailM())
                    .placeholder(R.drawable.holder)
                    .apply(requestOptions)
                    .into(img_artist);
            txt_artist.setText(artistDetail.getData().getName());
            txt_follow.setText(Helper.convertToIntString(artistDetail.getData().getTotalFollow()) + " quan tâm");

            if (sectionArtistSong != null && sectionArtistSong.getTopAlbum() != null) {
                Glide.with(requireContext())
                        .load(sectionArtistSong.getTopAlbum().getThumbnail())
                        .placeholder(R.drawable.holder)
                        .into(img_song);
                txtTitle.setText(sectionArtistSong.getTopAlbum().getTitle());
                txtArtist.setText(sectionArtistSong.getTopAlbum().getArtistsNames());
                relative_new_song.setVisibility(View.VISIBLE);
            } else {
                relative_new_song.setVisibility(View.GONE);
            }

            artistAdapter.setFilterList(sectionArtistSongs, sectionArtistPlaylists, sectionArtistVideos, sectionArtistArtists);

            CharSequence styledText = Html.fromHtml(artistDetail.getData().getBiography());
            txt_info.setText(styledText);
            txt_name_real.setText(artistDetail.getData().getRealname());
            txt_date_birth.setText(artistDetail.getData().getBirthday());
            txt_country.setText(artistDetail.getData().getNational());
            txt_genre.setText(artistDetail.getData().getNational());
            name = artistDetail.getData().getName();

            nested_scroll.setVisibility(View.VISIBLE);
            relative_loading.setVisibility(View.GONE);
        }
    }


}