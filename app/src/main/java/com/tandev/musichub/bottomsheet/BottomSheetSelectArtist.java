package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.artist.SelectArtistAdapter;
import com.tandev.musichub.model.chart.chart_home.Artists;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class BottomSheetSelectArtist extends BottomSheetDialogFragment implements SelectArtistAdapter.ArtistItemClickListener {
    private final Context context;
    private final Activity activity;
    private ArrayList<Artists> artistsArrayList;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView rv_select_artist;
    private SelectArtistAdapter selectArtistAdapter;

    public BottomSheetSelectArtist(Context context, Activity activity, ArrayList<Artists> artistsArrayList) {
        this.context = context;
        this.activity = activity;
        this.artistsArrayList = artistsArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_select_artist, null);
        bottomSheetDialog.setContentView(view);

        rv_select_artist = bottomSheetDialog.findViewById(R.id.rv_select_artist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_select_artist.setLayoutManager(linearLayoutManager);

        selectArtistAdapter = new SelectArtistAdapter(artistsArrayList, activity, context);
        rv_select_artist.setAdapter(selectArtistAdapter);

        selectArtistAdapter.setFilterList(artistsArrayList);

        selectArtistAdapter.setListener(isDismiss -> {
            if (isDismiss){
                bottomSheetDialog.dismiss();
            }
        });


        return bottomSheetDialog;
    }

    @Override
    public void onArtistItemClick(boolean isDismiss) {

    }
}
