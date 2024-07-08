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

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.chart.chart_home.Items;
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
    private ArrayList<Items> songArrayList;
    private SharedPreferencesManager sharedPreferencesManager;

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

        rv_song_recommend = view.findViewById(R.id.rv_song_recommend);
        songArrayList = new ArrayList<>();
        rv_song_recommend.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songAllAdapter = new SongAllAdapter(songArrayList, requireActivity(), requireContext());
        rv_song_recommend.setAdapter(songAllAdapter);

        getSongPlaying();
    }

    private void getSongPlaying() {
        items = sharedPreferencesManager.restoreSongState();
        is_playing = sharedPreferencesManager.restoreIsPlayState();
        songArrayList = sharedPreferencesManager.restoreSongArrayList();
        if (items != null && songArrayList != null) {
            getRelateSong(items.getEncodeId());
        }
    }

    private void getRelateSong(String endCodeID) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SongCategories songCategories = new SongCategories();
                    Map<String, String> map = songCategories.getSongRecommend(endCodeID);

                    retrofit2.Call<SongRecommend> call = service.SONG_RECOMMEND_CALL(endCodeID, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
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

    private void updateUI(SongRecommend songRecommend) {
        if (songRecommend != null) {
            songArrayList = songRecommend.getData().getItems();
            songAllAdapter.setFilterList(songArrayList);
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