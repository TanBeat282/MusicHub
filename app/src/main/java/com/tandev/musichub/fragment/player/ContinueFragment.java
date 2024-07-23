package com.tandev.musichub.fragment.player;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.song.ItemTouchHelperAdapter;
import com.tandev.musichub.adapter.song.ItemTouchHelperCallback;
import com.tandev.musichub.adapter.song.SongTouchAdapter;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class ContinueFragment extends Fragment implements ItemTouchHelperAdapter {
    private Items items;
    private ArrayList<Items> songArrayList;
    private SharedPreferencesManager sharedPreferencesManager;
    private RecyclerView rv_song_continue;
    private SongTouchAdapter songTouchAdapter;
    private MusicHelper musicHelper;

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

        rv_song_continue = view.findViewById(R.id.rv_song_continue);

        songArrayList = new ArrayList<>();
        rv_song_continue.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        songTouchAdapter = new SongTouchAdapter(songArrayList, requireActivity(), requireContext());
        rv_song_continue.setAdapter(songTouchAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(songTouchAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv_song_continue);
        songTouchAdapter.setItemTouchHelper(itemTouchHelper);

        getSongPlaying();
    }


    private void getSongPlaying() {
        items = sharedPreferencesManager.restoreSongState();
        songArrayList = sharedPreferencesManager.restoreSongArrayList();
        if (items != null && songArrayList != null) {
            updateUI(songArrayList);
        }
    }

    private void updateUI(ArrayList<Items> items) {
        if (items != null) {
            songArrayList = items;
            songTouchAdapter.setFilterList(songArrayList);
            rv_song_continue.scrollToPosition(sharedPreferencesManager.restoreSongPosition() - 1);
            musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), songArrayList, songTouchAdapter);
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