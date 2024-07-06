package com.tandev.musichub.helper.ui;


import android.content.Context;

import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;

public class MusicHelper {
    private Context context;
    private SharedPreferencesManager sharedPreferencesManager;
    private PlayingStatusUpdater playingStatusUpdater;


    public MusicHelper(Context context, SharedPreferencesManager sharedPreferencesManager) {
        this.context = context;
        this.sharedPreferencesManager = sharedPreferencesManager;
    }

    public void initAdapter(PlayingStatusUpdater playingStatusUpdater) {
        this.playingStatusUpdater = playingStatusUpdater;
    }
    public void checkIsPlayingPlaylist(Items items, ArrayList<Items> songList, PlayingStatusUpdater adapter) {
        if (items == null || songList == null || adapter == null) {
            return;
        }

        String currentEncodeId = items.getEncodeId();
        if (currentEncodeId != null && !currentEncodeId.isEmpty()) {
            adapter.updatePlayingStatus(currentEncodeId);
        }
    }


}
