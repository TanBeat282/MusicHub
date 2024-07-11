package com.tandev.musichub.fragment.new_release;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.new_release_song.NewReleaseViewPageAdapter;
import com.tandev.musichub.helper.ui.Helper;

public class NewReleaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_release, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.changeStatusBarColor(requireActivity(), R.color.black);
        Helper.changeNavigationColor(requireActivity(), R.color.black, true);

        TabLayout tab_layout_new_release_song = view.findViewById(R.id.tab_layout_new_release_song);
        ViewPager view_pager_new_release_song = view.findViewById(R.id.view_pager_new_release_song);

        NewReleaseViewPageAdapter mViewPagerAdapter = new NewReleaseViewPageAdapter(requireActivity().getSupportFragmentManager());
        view_pager_new_release_song.setAdapter(mViewPagerAdapter);

        tab_layout_new_release_song.setupWithViewPager(view_pager_new_release_song);

        View tool_bar = view.findViewById(R.id.tool_bar);
        ImageView img_back = tool_bar.findViewById(R.id.img_back);
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}