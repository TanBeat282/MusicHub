package com.tandev.musichub.fragment.week_chart.vn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.bxh_song.BXHSongAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.ChartCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.weekchart.WeekChart;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;
import com.tandev.musichub.view_model.week_chart.WeekChartVnViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VnFragment extends Fragment {
    private WeekChartVnViewModel weekChartVnViewModel;

    private NestedScrollView nested_scroll_week_chart;
    private RelativeLayout relative_loading;
    private RecyclerView recycler_week_chart;
    private ArrayList<Items> itemsArrayList;
    private BXHSongAdapter bxhSongAdapter;
    private String week;
    private String year;
    private static final String VN_CATEGORY_ID = "IWZ9Z08I";

    public BroadcastReceiver WeekYearBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                week = bundle.getString("week_chart");
                year = bundle.getString("year_chart");
                relative_loading.setVisibility(View.VISIBLE);
                nested_scroll_week_chart.setVisibility(View.GONE);
                getWeekChart(VN_CATEGORY_ID, week, year);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weekChartVnViewModel = new ViewModelProvider(this).get(WeekChartVnViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        initViews(view);
        initAdapter();

        initViewModel();

    }

    private void initViewModel() {
        weekChartVnViewModel.getWeekChartMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getWeekChart(VN_CATEGORY_ID, String.valueOf(getWeekChartPosition()), "2024");
            }
        });

        if (weekChartVnViewModel.getWeekChartMutableLiveData().getValue() == null) {
            getWeekChart(VN_CATEGORY_ID, String.valueOf(getWeekChartPosition()), "2024");
        }
    }

    private void initViews(View view) {
        nested_scroll_week_chart = view.findViewById(R.id.nested_scroll_week_chart);
        relative_loading = view.findViewById(R.id.relative_loading);
        recycler_week_chart = view.findViewById(R.id.recycler_week_chart);
    }

    private void initAdapter() {
        itemsArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler_week_chart.setLayoutManager(linearLayoutManager);
        bxhSongAdapter = new BXHSongAdapter(itemsArrayList, requireActivity(), requireContext());
        recycler_week_chart.setAdapter(bxhSongAdapter);
    }

    private int getWeekChartPosition() {
        Calendar calendar = Calendar.getInstance();
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        return (weekNumber - 1);
    }

    private void getWeekChart(String encodeId, String week, String year) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    ChartCategories chartCategories = new ChartCategories();
                    Map<String, String> map = chartCategories.getWeekChart(encodeId);

                    retrofit2.Call<WeekChart> call = service.WEEK_CHART_CALL(encodeId, week, year, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<WeekChart>() {
                        @Override
                        public void onResponse(Call<WeekChart> call, Response<WeekChart> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>>", "getWeekChart " + call.request().url());
                            if (response.isSuccessful()) {
                                WeekChart weekChart = response.body();
                                if (weekChart != null && weekChart.getErr() == 0) {
                                    weekChartVnViewModel.setWeekChartMutableLiveData(weekChart);
                                } else {
                                    Log.d("TAG", "Error: ");
                                }
                            } else {
                                Log.d("TAG", "Failed to retrieve data: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<WeekChart> call, Throwable throwable) {

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

    private void updateUI(WeekChart weekChart) {
        ArrayList<Items> arrayList = weekChart.getData().getItems();
        if (arrayList != null && !arrayList.isEmpty()) {
            itemsArrayList = arrayList;
            bxhSongAdapter.setFilterList(arrayList);
            relative_loading.setVisibility(View.GONE);
            nested_scroll_week_chart.setVisibility(View.VISIBLE);
        } else {
            Log.d("TAG", "Items list is empty");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(WeekYearBroadcastReceiver(), new IntentFilter("send_week_year_to_fragment"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(WeekYearBroadcastReceiver());
    }
}
