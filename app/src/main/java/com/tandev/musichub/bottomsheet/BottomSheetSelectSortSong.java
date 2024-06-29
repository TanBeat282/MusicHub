package com.tandev.musichub.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tandev.musichub.R;

public class BottomSheetSelectSortSong extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private int position;
    private BottomSheetDialog bottomSheetDialog;
    private SortOptionListener mListener;

    public BottomSheetSelectSortSong(Context context, Activity activity, int position) {
        this.context = context;
        this.activity = activity;
        this.position = position;
    }

    public interface SortOptionListener {
        void onSortOptionSelected(int sortOption);
    }

    public void setSortOptionListener(SortOptionListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_select_sort_song, null);
        bottomSheetDialog.setContentView(view);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton radioNewRelease = view.findViewById(R.id.radio_new_release);
        RadioButton radioPopular = view.findViewById(R.id.radio_popular);

        if (position == 1) {
            radioNewRelease.setChecked(true);
        } else if (position == 2) {
            radioPopular.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedPosition = (checkedId == R.id.radio_new_release) ? 1 : 2;
            if (mListener != null) {
                mListener.onSortOptionSelected(selectedPosition);
            }
            bottomSheetDialog.dismiss();
        });

        return bottomSheetDialog;
    }
}
