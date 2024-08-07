package com.tandev.musichub.fragment.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.fragment.album.AlbumFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.GetUrlAudioHelper;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.genre.GenreInfo;
import com.tandev.musichub.model.new_release.NewReleaseSong;
import com.tandev.musichub.model.song.SongDetail;
import com.tandev.musichub.model.song.song_recommend.SongRecommend;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RelateFragment extends Fragment {
    private Items items;
    private boolean is_playing;
    private SongDetail songDetail;
    private ArrayList<Items> songArrayList;
    private SharedPreferencesManager sharedPreferencesManager;

    private LinearLayout linear_info_song;
    private RoundedImageView thumbImageView;
    private TextView nameTextView;
    private TextView artistTextView;
    private TextView txt_like;
    private TextView txt_listen;
    private TextView txt_album;
    private TextView txt_composed;
    private TextView txt_genre;
    private TextView txt_release;
    private TextView txt_provide;


    private RecyclerView rv_song_recommend;
    private SongAllAdapter songAllAdapter;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            items = (Items) bundle.get("object_song");
            int action = bundle.getInt("action_music");
            if (action == MyService.ACTION_START || action == MyService.ACTION_NEXT || action == MyService.ACTION_PREVIOUS) {
                getRelateSong(items.getEncodeId());
                getSongDetail(items.getEncodeId());
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_relate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());

        linear_info_song = view.findViewById(R.id.linear_info_song);
        thumbImageView = view.findViewById(R.id.thumbImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        artistTextView = view.findViewById(R.id.artistTextView);
        txt_like = view.findViewById(R.id.txt_like);
        txt_listen = view.findViewById(R.id.txt_listen);
        txt_album = view.findViewById(R.id.txt_album);
        txt_composed = view.findViewById(R.id.txt_composed);
        txt_genre = view.findViewById(R.id.txt_genre);
        txt_release = view.findViewById(R.id.txt_release);
        txt_provide = view.findViewById(R.id.txt_provide);

        rv_song_recommend = view.findViewById(R.id.rv_song_recommend);
        songArrayList = new ArrayList<>();
        rv_song_recommend.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songAllAdapter = new SongAllAdapter(songArrayList, requireActivity(), requireContext());
        rv_song_recommend.setAdapter(songAllAdapter);

        linear_info_song.setOnClickListener(view1 -> {
            if (songDetail.getData().getAlbum() != null) {
                AlbumFragment albumFragment = new AlbumFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("album_endCodeId", songDetail.getData().getAlbum().getEncodeId());

                if (requireContext() instanceof MainActivity) {
                    ((MainActivity) requireContext()).replaceFragmentWithBundle(albumFragment, bundle);
                }
                sendExpandToActivity();
            }
        });

        getSongPlaying();
    }

    private void getSongPlaying() {
        items = sharedPreferencesManager.restoreSongState();
        is_playing = sharedPreferencesManager.restoreIsPlayState();
        songArrayList = sharedPreferencesManager.restoreSongArrayList();
        if (items != null && songArrayList != null) {
            getRelateSong( items.getEncodeId());
            getSongDetail(items.getEncodeId());
        }
    }

    private void sendExpandToActivity() {
        Intent intent = new Intent("send_expand_to_activity");
        intent.putExtra("is_expand", true);
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
    }

    private void getRelateSong(String id) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getSongRecommend(id);

                    retrofit2.Call<SongRecommend> call = service.SONG_RECOMMEND_CALL(id, "0", "999", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SongRecommend>() {
                        @Override
                        public void onResponse(Call<SongRecommend> call, Response<SongRecommend> response) {
                            LogUtil.d(Constants.TAG, "getRelateSong: " + call.request().url());
                            if (response.isSuccessful()) {
                                SongRecommend songRecommend = response.body();
                                if (songRecommend != null) {
                                    updateUI(songRecommend);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SongRecommend> call, Throwable throwable) {

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

    private void getSongDetail(String encodeId) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getInfo(encodeId);

                    retrofit2.Call<SongDetail> call = service.SONG_DETAIL_CALL(encodeId, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SongDetail>() {
                        @Override
                        public void onResponse(@NonNull Call<SongDetail> call, @NonNull Response<SongDetail> response) {
                            LogUtil.d(Constants.TAG, "getSongDetail: " + call.request().url());
                            if (response.isSuccessful()) {
                                songDetail = response.body();
                                if (songDetail != null) {
                                    setDataPLayer(songDetail);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SongDetail> call, @NonNull Throwable throwable) {
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


    public interface GenreInfoCallback {
        void onGenreInfoFetched(GenreInfo genreInfo);

        void onError(String error);
    }

    private void getGenreInfo(String encodeId, GenreInfoCallback callback) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getGenreInfo(encodeId, "album");

                    retrofit2.Call<GenreInfo> call = service.GENRE_INFO_CALL(encodeId, "album", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<GenreInfo>() {
                        @Override
                        public void onResponse(Call<GenreInfo> call, Response<GenreInfo> response) {
                            LogUtil.d(Constants.TAG, "getGenreInfo: " + call.request().url());
                            if (response.isSuccessful()) {
                                GenreInfo genreInfo = response.body();
                                if (genreInfo != null) {
                                    if (genreInfo.getErr() == 0) {
                                        callback.onGenreInfoFetched(genreInfo);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GenreInfo> call, Throwable throwable) {
                            LogUtil.d(Constants.TAG, "onFailure: " + call.request().url());
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

    private void setDataPLayer(SongDetail songDetail) {
        if (!(requireActivity()).isDestroyed()) {
            Glide.with(requireActivity())
                    .load(songDetail.getData().getThumbnailM())
                    .into(thumbImageView);
        }
        nameTextView.setText(songDetail.getData().getTitle());
        artistTextView.setText(songDetail.getData().getArtistsNames());
        txt_like.setText(Helper.convertToIntString(songDetail.getData().getLike()));
        txt_listen.setText(Helper.convertToIntString(songDetail.getData().getListen()));
        txt_album.setText(songDetail.getData().getAlbum() != null ? songDetail.getData().getAlbum().getTitle() : "");


        if (songDetail.getData().getComposers() != null) {
            String composers = "";
            if (songDetail.getData().getComposers().size() > 1) {
                for (int i = 0; i < songDetail.getData().getComposers().size(); i++) {
                    composers += songDetail.getData().getComposers().get(i).getName() + ", ";
                }
            } else {
                composers = songDetail.getData().getComposers().get(0).getName();
            }
            txt_composed.setText(composers);
        } else {
            txt_composed.setText("");
        }

        if (songDetail.getData().getGenreIds() != null && !songDetail.getData().getGenreIds().isEmpty()) {
            StringBuilder genreBuilder = new StringBuilder();
            int[] remainingCalls = {songDetail.getData().getGenreIds().size()};

            for (String genreId : songDetail.getData().getGenreIds()) {
                getGenreInfo(genreId, new GenreInfoCallback() {
                    @Override
                    public void onGenreInfoFetched(GenreInfo genreInfo) {
                        if (genreInfo != null && genreInfo.getData() != null && genreInfo.getErr() == 0) {
                            synchronized (genreBuilder) {
                                genreBuilder.append(genreInfo.getData().getName()).append(", ");
                            }
                        }

                        synchronized (remainingCalls) {
                            remainingCalls[0]--;
                            if (remainingCalls[0] == 0) {
                                if (genreBuilder.length() > 0) {
                                    genreBuilder.setLength(genreBuilder.length() - 2); // Xóa dấu phẩy cuối cùng
                                }
                                txt_genre.setText(genreBuilder.toString());
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        synchronized (remainingCalls) {
                            remainingCalls[0]--;
                            if (remainingCalls[0] == 0) {
                                txt_genre.setText(genreBuilder.length() > 0 ? genreBuilder.toString() : "N/A");
                            }
                        }
                    }
                });
            }
        } else {
            txt_genre.setText("N/A");
        }


        txt_release.setText(songDetail.getData().getReleaseDate() != 0 ? Helper.convertLongToTime(String.valueOf(songDetail.getData().getReleaseDate())) : "");
        txt_provide.setText(songDetail.getData().getDistributor() != null ? songDetail.getData().getDistributor() : "");

        linear_info_song.setVisibility(View.VISIBLE);
    }


    private void updateUI(SongRecommend songRecommend) {
        if (songRecommend != null) {
            songArrayList = songRecommend.getData().getItems();
            songAllAdapter.setFilterList(songArrayList);
            rv_song_recommend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }
}