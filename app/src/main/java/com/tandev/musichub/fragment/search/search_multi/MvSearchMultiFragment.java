package com.tandev.musichub.fragment.search.search_multi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tandev.musichub.R;

public class MvSearchMultiFragment extends Fragment {
    private static final String ARG_DATA = "query";
    private String query;
    public static MvSearchMultiFragment newInstance(String data) {
        MvSearchMultiFragment fragment = new MvSearchMultiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_DATA);
//            searchMulti(query);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mv_search_multi, container, false);
    }
}