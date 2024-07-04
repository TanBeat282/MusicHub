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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.song.SongAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SongCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.ui.Helper;
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

public class ContinueFragment extends Fragment {
    private Items items;
    private ArrayList<Items> songArrayList;
    private SharedPreferencesManager sharedPreferencesManager;

    private RecyclerView rv_song_continue;
    private SongAllAdapter songAllAdapter;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            items = (Items) bundle.get("object_song");
            if (items != null) {
                setContinueSong(items);
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

        rv_song_continue = view.findViewById(R.id.rv_song_continue);

        songArrayList = new ArrayList<>();
        rv_song_continue.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songAllAdapter = new SongAllAdapter(songArrayList, requireActivity(), requireContext());
        rv_song_continue.setAdapter(songAllAdapter);

        getSongPlaying();
    }


    private void getSongPlaying() {
        items = sharedPreferencesManager.restoreSongState();
        songArrayList = sharedPreferencesManager.restoreSongArrayList();
        if (items != null && songArrayList != null) {
            updateUI(songArrayList);
            setContinueSong(items);
        }
    }

    private void updateUI(ArrayList<Items> items) {
        if (items != null) {
            songArrayList = items;
            songAllAdapter.setFilterList(songArrayList);
        }
    }
    private void setContinueSong(Items item) {
        if (item != null) {
            int removeIndex = -1;
            for (int i = 0; i < songArrayList.size(); i++) {
                if (songArrayList.get(i).getEncodeId().equals(item.getEncodeId())) {
                    removeIndex = i;
                    break;
                }
            }

            if (removeIndex != -1) {
                songArrayList = new ArrayList<>(songArrayList.subList(removeIndex + 1, songArrayList.size()));
                songAllAdapter.setFilterList(songArrayList);
            }
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