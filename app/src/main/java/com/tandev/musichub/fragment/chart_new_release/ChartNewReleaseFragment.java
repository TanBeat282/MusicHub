package com.tandev.musichub.fragment.chart_new_release;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.bxh_song.BXHSongAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.ChartCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.new_release.NewRelease;
import com.tandev.musichub.view_model.chart_new_release.ChartNewReleaseViewModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartNewReleaseFragment extends Fragment {
    private ChartNewReleaseViewModel chartNewReleaseViewModel;

    //tool bar
    private View tool_bar;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;
    private ImageView img_play;

    private RelativeLayout relative_loading;
    private NestedScrollView nested_scroll;
    private TextView txt_new_release;
    private RecyclerView rv_new_release_song;
    private BXHSongAdapter newReleaseChartAdapter;
    private ArrayList<Items> itemsArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chartNewReleaseViewModel = new ViewModelProvider(this).get(ChartNewReleaseViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_new_release, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initAdapter();
        conFigViews();
        initOnClick();
        initViewModel();
    }

    private void initViewModel() {
        chartNewReleaseViewModel.getNewReleaseMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getNewReleaseChart();
            }
        });

        if (chartNewReleaseViewModel.getNewReleaseMutableLiveData().getValue() == null) {
            getNewReleaseChart();
        }
    }

    private void initViews(View view) {
        tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);

        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        txt_new_release = view.findViewById(R.id.txt_new_release);
        img_play = view.findViewById(R.id.img_play);

        rv_new_release_song = view.findViewById(R.id.rv_new_release_song);
    }

    private void initAdapter() {
        itemsArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_new_release_song.setLayoutManager(layoutManager);

        newReleaseChartAdapter = new BXHSongAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_new_release_song.setAdapter(newReleaseChartAdapter);

    }

    private void conFigViews() {
        Helper.changeStatusBarColor(requireActivity(), R.color.bg);


        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 200) {
                    txt_title_toolbar.setText("");
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 300) {
                    txt_title_toolbar.setText(chartNewReleaseViewModel.getNewReleaseMutableLiveData().getValue().getData().getTitle());
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg));
                    Helper.changeStatusBarColor(requireActivity(), R.color.bg);
                }
            }
        });
    }

    private void initOnClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getNewReleaseChart() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    ChartCategories chartCategories = new ChartCategories();
                    Map<String, String> map = chartCategories.getNewReleaseChart();

                    retrofit2.Call<NewRelease> call = service.CHART_NEW_RELEASE_CALL(map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<NewRelease>() {
                        @Override
                        public void onResponse(@NonNull Call<NewRelease> call, @NonNull Response<NewRelease> response) {
                            if (response.isSuccessful()) {
                                Log.d(">>>>>>>>>>>>>>>>>>", "getNewReleaseChart " + call.request().url());
                                NewRelease newRelease = response.body();
                                if (newRelease != null && newRelease.getErr() == 0) {
                                    chartNewReleaseViewModel.setNewReleaseMutableLiveData(newRelease);
                                } else {
                                    Log.d("TAG", "Error: ");
                                }
                            } else {
                                Log.d("TAG", "Failed to retrieve data: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<NewRelease> call, @NonNull Throwable throwable) {

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

    private void updateUI(NewRelease newRelease) {
        ArrayList<Items> items = newRelease.getData().getItems();
        txt_new_release.setText(newRelease.getData().getTitle());

        itemsArrayList = items;
        newReleaseChartAdapter.setFilterList(itemsArrayList);
        relative_loading.setVisibility(View.GONE);
        nested_scroll.setVisibility(View.VISIBLE);
    }
}