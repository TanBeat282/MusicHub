package com.tandev.musichub.fragment.player;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.song.ItemTouchHelperAdapter;
import com.tandev.musichub.adapter.song.ItemTouchHelperCallback;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.adapter.song.SongTouchAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.song.song_recommend.SongRecommend;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContinueFragment extends Fragment implements ItemTouchHelperAdapter {
    private RelativeLayout relative_loading;
    private NestedScrollView nestedScrollView;
    private Items items;
    private ArrayList<Items> songArrayList;
    private SharedPreferencesManager sharedPreferencesManager;
    private RecyclerView rv_song_continue;
    private SongTouchAdapter songTouchAdapter;
    private MusicHelper musicHelper;

    private LinearLayout linear_recommend;
    private SwitchCompat switch_recommend;
    private RecyclerView rv_song_recommend;
    private SongAllAdapter songAllAdapter;
    private ArrayList<Items> songRecommendArrayList;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            items = (Items) bundle.get("object_song");
            int action = bundle.getInt("action_music");
            if (items != null) {
                if (action == MyService.ACTION_START || action == MyService.ACTION_NEXT || action == MyService.ACTION_PREVIOUS) {
                    musicHelper.checkIsPlayingPlaylist(items, songArrayList, songTouchAdapter);
                }
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
        return inflater.inflate(R.layout.fragment_continue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        musicHelper = new MusicHelper(requireContext(), sharedPreferencesManager);

        initViews(view);
        initAdapter();
    }

    private void initViews(View view) {
        relative_loading = view.findViewById(R.id.relative_loading);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        rv_song_continue = view.findViewById(R.id.rv_song_continue);

        linear_recommend = view.findViewById(R.id.linear_recommend);
        switch_recommend = view.findViewById(R.id.switch_recommend);
        rv_song_recommend = view.findViewById(R.id.rv_song_recommend);
    }

    private void initAdapter() {
        songArrayList = new ArrayList<>();
        rv_song_continue.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songTouchAdapter = new SongTouchAdapter(songArrayList, requireActivity(), requireContext());
        rv_song_continue.setAdapter(songTouchAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(songTouchAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv_song_continue);
        songTouchAdapter.setItemTouchHelper(itemTouchHelper);

        songRecommendArrayList = new ArrayList<>();
        rv_song_recommend.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songAllAdapter = new SongAllAdapter(songRecommendArrayList, requireActivity(), requireContext());
        rv_song_recommend.setAdapter(songAllAdapter);
    }

    private void getSongPlaying() {
        items = sharedPreferencesManager.restoreSongState();
        songArrayList = sharedPreferencesManager.restoreSongArrayList();
        if (items != null && songArrayList != null) {
            updateUI(songArrayList);
            if (songArrayList.size() < 5) {
                getRelateSong(items.getEncodeId());
            }
        }
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
                            LogUtil.d(Constants.TAG, "getRelateSongContinue: " + call.request().url());
                            if (response.isSuccessful()) {
                                SongRecommend songRecommend = response.body();
                                if (songRecommend != null) {
                                    updateUIRecommend(songRecommend);
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

    private void updateUIRecommend(SongRecommend songRecommend) {
        if (songRecommend != null) {
            songRecommendArrayList = songRecommend.getData().getItems();
            songAllAdapter.setFilterList(songRecommendArrayList);
            linear_recommend.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(ArrayList<Items> items) {
        if (items != null) {
            songArrayList = items;
            songTouchAdapter.setFilterList(songArrayList);
            musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), songArrayList, songTouchAdapter);
            relative_loading.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
            if (songArrayList.size() > 10) {
                rv_song_continue.scrollToPosition(sharedPreferencesManager.restoreSongPosition() - 1);
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        getSongPlaying();
        songTouchAdapter.notifyDataSetChanged();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition != toPosition) {
            Items movedItem = songArrayList.remove(fromPosition);
            songArrayList.add(toPosition, movedItem);
            songTouchAdapter.notifyDataSetChanged();
            sharedPreferencesManager.updatePositionSongOfArrayList(movedItem, toPosition);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemDismiss(int position) {
        Items item = songArrayList.get(position);
        songArrayList.remove(position);
        songTouchAdapter.notifyDataSetChanged();
        sharedPreferencesManager.removeSongFromList(item);
        Toast.makeText(requireContext(), "Đã bài hát khỏi danh sách phát!", Toast.LENGTH_SHORT).show();
    }
}