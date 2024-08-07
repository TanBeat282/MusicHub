package com.tandev.musichub.bottomsheet;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.fragment.history.HistoryFragment;
import com.tandev.musichub.fragment.profile.ProfileFragment;
import com.tandev.musichub.fragment.search.SearchFragment;
import com.tandev.musichub.helper.ui.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetProfile extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private BottomSheetDialog bottomSheetDialog;


    private LinearLayout linear_profile, linear_history, linear_setting, linear_send_error, linear_info_app;

    public BottomSheetProfile(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_profile, null);
        bottomSheetDialog.setContentView(view);

        Helper.changeNavigationColor(activity, R.color.gray, true);

        initViews(bottomSheetDialog);
        onClick();

        return bottomSheetDialog;
    }

    private void initViews(BottomSheetDialog bottomSheetDialog) {
        linear_profile = bottomSheetDialog.findViewById(R.id.linear_profile);
        linear_history = bottomSheetDialog.findViewById(R.id.linear_history);
        linear_setting = bottomSheetDialog.findViewById(R.id.linear_setting);
    }

    private void onClick() {
        linear_profile.setOnClickListener(view1 -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new ProfileFragment());
            }
            bottomSheetDialog.dismiss();
        });
        linear_history.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new HistoryFragment());
            }
            bottomSheetDialog.dismiss();
        });
    }
}
