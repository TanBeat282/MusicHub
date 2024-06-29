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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.ArtistAdapter;
import com.tandev.musichub.adapter.artist.ArtistsAllAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistAdapter;
import com.tandev.musichub.adapter.single.SingleAdapter;
import com.tandev.musichub.adapter.song.SongMoreAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.artist.ArtistTypeAdapter;
import com.tandev.musichub.api.type_adapter_Factory.home.HubSectionTypeAdapter;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.fragment.song.SongFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.artist.ArtistDetail;
import com.tandev.musichub.model.artist.SectionArtist;
import com.tandev.musichub.model.artist.artist.SectionArtistArtist;
import com.tandev.musichub.model.artist.playlist.SectionArtistPlaylist;
import com.tandev.musichub.model.artist.song.SectionArtistSong;
import com.tandev.musichub.model.artist.video.SectionArtistVideo;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.hub.Hub;
import com.tandev.musichub.model.hub.HubSection;
import com.tandev.musichub.model.hub.SectionHubArtist;
import com.tandev.musichub.model.hub.SectionHubPlaylist;
import com.tandev.musichub.model.hub.SectionHubSong;
import com.tandev.musichub.model.hub.SectionHubVideo;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistFragment extends Fragment {

    private ArtistAdapter artistAdapter;
    private final ArrayList<SectionArtistSong> sectionArtistSongs = new ArrayList<>();
    private final ArrayList<SectionArtistPlaylist> sectionArtistPlaylists = new ArrayList<>();
    private final ArrayList<SectionArtistVideo> sectionArtistVideos = new ArrayList<>();
    private final ArrayList<SectionArtistArtist> sectionArtistArtists = new ArrayList<>();

    private RelativeLayout relative_header;
    private RelativeLayout relative_loading;
    private NestedScrollView nested_scroll;
    private TextView txt_name_artist;
    private TextView txt_view;
    private ImageView img_back;
    private ImageView img_more;
    private ImageView img_artist;
    private ProgressBar progress_image;
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
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);

        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);


        img_back = view.findViewById(R.id.img_back);
        img_more = view.findViewById(R.id.img_more);

        relative_header = view.findViewById(R.id.relative_header);
        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        txt_name_artist = view.findViewById(R.id.txt_name_artist);
        txt_view = view.findViewById(R.id.txt_view);

        img_artist = view.findViewById(R.id.img_artist);
        progress_image = view.findViewById(R.id.progress_image);
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

        rv_artist_vertical.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        artistAdapter = new ArtistAdapter(requireContext(), requireActivity(), sectionArtistSongs, sectionArtistPlaylists, sectionArtistVideos, sectionArtistArtists);
        rv_artist_vertical.setAdapter(artistAdapter);


        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_name_artist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status bar
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                    }

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    txt_name_artist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_artist.setText(name);
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });


        relative_new_song.setOnClickListener(view1 -> {
//            Intent intent = new Intent(ViewArtistActivity.this, ViewAlbumActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("album_endCodeId", dataPlaylistNewSong.getEncodeId());
//            intent.putExtras(bundle);
//
//            startActivity(intent);
        });
//        linear_noibat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SongFragment songFragment = new SongFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("id", id);
//                bundle.putString("sectionId", sectionArtistSong.getSectionId());
//
//                if (requireContext() instanceof MainActivity) {
//                    ((MainActivity) requireContext()).replaceFragmentWithBundle(songFragment, bundle);
//                }
//            }
//        });

        getDataBundle();
    }

    private void getDataBundle() {
        if (getArguments() != null) {
            String alias = getArguments().getString("alias");
            getArtist(alias);
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
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getArtist " + call.request().url());
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    String jsonData = response.body().string();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(SectionArtist.class, new ArtistTypeAdapter());
                                    Gson gson = gsonBuilder.create();

                                    ArtistDetail artistDetail = gson.fromJson(jsonData, ArtistDetail.class);

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
                                                .apply(requestOptions)
                                                .into(img_artist);
                                        txt_artist.setText(artistDetail.getData().getName());
                                        txt_follow.setText(Helper.convertToIntString(artistDetail.getData().getTotalFollow()) + " quan tâm");


                                        if (sectionArtistSong.getTopAlbum() != null) {
                                            Glide.with(requireContext())
                                                    .load(sectionArtistSong.getTopAlbum().getThumbnail())
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
                                        name= artistDetail.getData().getName();


                                        nested_scroll.setVisibility(View.VISIBLE);
                                        relative_loading.setVisibility(View.GONE);
                                    } else {
                                        Log.d("TAG", "No data found in JSON");
                                    }

                                } catch (Exception e) {
                                    Log.e("TAG", "Error: " + e.getMessage(), e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
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

}