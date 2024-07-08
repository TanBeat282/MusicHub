package com.tandev.musichub.fragment.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.tandev.musichub.R;

public class ProfileFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NestedScrollView nestedScrollView = view.findViewById(R.id.rootLayout);
        MaterialToolbar toolbar = view.findViewById(R.id.toolBar);
        AppBarLayout appBarLayout = view.findViewById(R.id.topAppBarLayout);

        // Set a listener for the AppBarLayout
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // Check if the toolbar is fully expanded
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Toolbar is collapsed
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorSpotify));
                } else {
                    // Toolbar is expanded
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }
        });
    }
}