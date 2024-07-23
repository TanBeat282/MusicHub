package com.tandev.musichub.fragment.chart_home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.chart_home.ChartHomeMoreAdapter;
import com.tandev.musichub.adapter.chart_home.WeekChartHomeAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.ChartCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.fragment.week_chart.WeekChartFragment;
import com.tandev.musichub.helper.ui.ChartInfoView;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.ui.MusicHelper;
import com.tandev.musichub.model.chart.chart_home.ChartHome;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.service.MyService;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;
import com.tandev.musichub.view_model.chart_home.ChartHomeViewModel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartHomeFragment extends Fragment {
    ChartHomeViewModel chartHomeViewModel;
    private RelativeLayout relative_header;
    private TextView txt_title_toolbar;
    private ImageView img_back;
    private ImageView imageBackground;
    private RelativeLayout relative_chart_home;

    private ImageView img_play;
    private TextView txt_time_chart;

    //chart
    private LineChart chart;
    private ChartInfoView chartInfoView;
    private RelativeLayout relative_loading;
    private NestedScrollView nested_scroll;
    private RecyclerView rv_chart_home;
    private ArrayList<Items> itemsArrayList;
    private ChartHomeMoreAdapter chartHomeMoreAdapter;

    //week chart
    private LinearLayout linear_week_chart_vn;
    private RoundedImageView rounded_image_week_chart_vn;
    private TextView txt_title_week_chart_vn;
    private RecyclerView rv_week_chart_vn;
    private ArrayList<Items> itemsArrayList_week_chart_vn;
    private WeekChartHomeAdapter weekChartMoreAdapter_vn;

    private LinearLayout linear_week_chart_us;
    private RoundedImageView rounded_image_week_chart_us;
    private TextView txt_title_week_chart_us;
    private RecyclerView rv_week_chart_us;
    private ArrayList<Items> itemsArrayList_week_chart_us;
    private WeekChartHomeAdapter weekChartMoreAdapter_us;


    private LinearLayout linear_week_chart_korea;
    private RoundedImageView rounded_image_week_chart_korea;
    private TextView txt_title_week_chart_korea;
    private RecyclerView rv_week_chart_korea;
    private ArrayList<Items> itemsArrayList_week_chart_korea;
    private WeekChartHomeAdapter weekChartMoreAdapter_korea;

    private MusicHelper musicHelper;
    private SharedPreferencesManager sharedPreferencesManager;


    public BroadcastReceiver createBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    return;
                }
                Items items = (Items) bundle.get("object_song");
                musicHelper.checkIsPlayingPlaylist(items, itemsArrayList, chartHomeMoreAdapter);

            }
        };
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chartHomeViewModel = new ViewModelProvider(this).get(ChartHomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chartInfoView = new ChartInfoView(requireContext());
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        musicHelper = new MusicHelper(requireContext(), sharedPreferencesManager);

        initViews(view);
        conFigViews();
        initAdapter();
        setupChart();
        onClick();
        initViewModel();

    }

    private void initViewModel() {
        chartHomeViewModel.getChartHomeMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUiChartHome(artistDetail);
                updateUiWeekChart(artistDetail);
                updateChart(artistDetail);
            } else {
                getChartHome();
            }
        });

        if (chartHomeViewModel.getChartHomeMutableLiveData().getValue() == null) {
            getChartHome();
        }
    }

    private void initViews(View view) {
        //tool bar
        View tool_bar = view.findViewById(R.id.tool_bar);
        relative_header = tool_bar.findViewById(R.id.relative_header);
        img_back = tool_bar.findViewById(R.id.img_back);
        txt_title_toolbar = tool_bar.findViewById(R.id.txt_title_toolbar);

        imageBackground = view.findViewById(R.id.imageBackground);
        img_play = view.findViewById(R.id.img_play);
        txt_time_chart = view.findViewById(R.id.txt_time_chart);
        relative_chart_home = view.findViewById(R.id.relative_chart_home);
        chart = view.findViewById(R.id.chart);
        relative_loading = view.findViewById(R.id.relative_loading);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        rv_chart_home = view.findViewById(R.id.rv_chart_home);

        linear_week_chart_vn = view.findViewById(R.id.linear_week_chart_vn);
        rounded_image_week_chart_vn = view.findViewById(R.id.rounded_image_week_chart_vn);
        txt_title_week_chart_vn = view.findViewById(R.id.txt_title_week_chart_vn);
        rv_week_chart_vn = view.findViewById(R.id.rv_week_chart_vn);

        linear_week_chart_us = view.findViewById(R.id.linear_week_chart_us);
        rounded_image_week_chart_us = view.findViewById(R.id.rounded_image_week_chart_us);
        txt_title_week_chart_us = view.findViewById(R.id.txt_title_week_chart_us);
        rv_week_chart_us = view.findViewById(R.id.rv_week_chart_us);

        linear_week_chart_korea = view.findViewById(R.id.linear_week_chart_korea);
        rounded_image_week_chart_korea = view.findViewById(R.id.rounded_image_week_chart_korea);
        txt_title_week_chart_korea = view.findViewById(R.id.txt_title_week_chart_korea);
        rv_week_chart_korea = view.findViewById(R.id.rv_week_chart_korea);
    }

    private void initAdapter() {
        itemsArrayList = new ArrayList<>();
        itemsArrayList_week_chart_vn = new ArrayList<>();
        itemsArrayList_week_chart_us = new ArrayList<>();
        itemsArrayList_week_chart_korea = new ArrayList<>();

        rv_chart_home.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        chartHomeMoreAdapter = new ChartHomeMoreAdapter(itemsArrayList, requireActivity(), requireContext());
        rv_chart_home.setAdapter(chartHomeMoreAdapter);

        //week chart
        rv_week_chart_vn.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        weekChartMoreAdapter_vn = new WeekChartHomeAdapter(itemsArrayList_week_chart_vn, requireActivity(), requireContext());
        rv_week_chart_vn.setAdapter(weekChartMoreAdapter_vn);

        rv_week_chart_us.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        weekChartMoreAdapter_us = new WeekChartHomeAdapter(itemsArrayList_week_chart_us, requireActivity(), requireContext());
        rv_week_chart_us.setAdapter(weekChartMoreAdapter_us);

        rv_week_chart_korea.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        weekChartMoreAdapter_korea = new WeekChartHomeAdapter(itemsArrayList_week_chart_korea, requireActivity(), requireContext());
        rv_week_chart_korea.setAdapter(weekChartMoreAdapter_korea);
    }

    private void conFigViews() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 600) {
                    txt_title_toolbar.setText("");
                } else if (scrollY >= 4500) {
                    txt_title_toolbar.setText("Bảng xếp hạng tuần");
                } else {
                    txt_title_toolbar.setText("Chart home");
                }
                relative_header.setBackgroundResource(android.R.color.transparent);
            }
        });
    }

    private void onClick() {
        img_back.setOnClickListener(view1 -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img_play.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), MyService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_song", itemsArrayList.get(0));
            bundle.putInt("position_song", 0);
            bundle.putSerializable("song_list", itemsArrayList);
            intent.putExtras(bundle);

            requireContext().startService(intent);
        });
        linear_week_chart_vn.setOnClickListener(view -> {
            WeekChartFragment weekChartFragment = new WeekChartFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("position_slide", 1);

            if (requireContext() instanceof MainActivity) {
                ((MainActivity) requireContext()).replaceFragmentWithBundle(weekChartFragment, bundle);
            }
        });
        linear_week_chart_us.setOnClickListener(view -> {
            WeekChartFragment weekChartFragment = new WeekChartFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("position_slide", 0);

            if (requireContext() instanceof MainActivity) {
                ((MainActivity) requireContext()).replaceFragmentWithBundle(weekChartFragment, bundle);
            }
        });
        linear_week_chart_korea.setOnClickListener(view -> {
            WeekChartFragment weekChartFragment = new WeekChartFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("position_slide", 2);

            if (requireContext() instanceof MainActivity) {
                ((MainActivity) requireContext()).replaceFragmentWithBundle(weekChartFragment, bundle);
            }
        });
    }

    private void getChartHome() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    ChartCategories chartCategories = new ChartCategories();
                    Map<String, String> map = chartCategories.getChartHome();

                    retrofit2.Call<ChartHome> call = service.CHART_HOME_CALL(map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ChartHome>() {
                        @Override
                        public void onResponse(@NonNull Call<ChartHome> call, @NonNull Response<ChartHome> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getChartHome " + call.request().url());
                            if (response.isSuccessful()) {
                                ChartHome chartHome = response.body();
                                if (chartHome != null) {
                                    chartHomeViewModel.setChartHomeMutableLiveData(chartHome);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ChartHome> call, @NonNull Throwable throwable) {

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

    private void updateUiChartHome(ChartHome chartHome) {
        itemsArrayList = chartHome.getData().getRTChart().getItems();
        chartHomeMoreAdapter.setFilterList(itemsArrayList);
        musicHelper.checkIsPlayingPlaylist(sharedPreferencesManager.restoreSongState(), itemsArrayList, chartHomeMoreAdapter);

        loadImage(itemsArrayList.get(0).getThumbnailM());
    }

    @SuppressLint("SetTextI18n")
    private void updateUiWeekChart(ChartHome chartHome) {
        itemsArrayList_week_chart_vn = chartHome.getData().getWeekChart().getVn().getItems();
        weekChartMoreAdapter_vn.setFilterList(itemsArrayList_week_chart_vn);
        txt_title_week_chart_vn.setText("V-POP");
        Glide.with(requireContext()).load(itemsArrayList_week_chart_vn.get(0).getThumbnailM()).into(rounded_image_week_chart_vn);

        itemsArrayList_week_chart_us = chartHome.getData().getWeekChart().getUs().getItems();
        weekChartMoreAdapter_us.setFilterList(itemsArrayList_week_chart_us);
        txt_title_week_chart_us.setText("US-UK");
        Glide.with(requireContext()).load(itemsArrayList_week_chart_us.get(0).getThumbnailM()).into(rounded_image_week_chart_us);

        itemsArrayList_week_chart_korea = chartHome.getData().getWeekChart().getKorea().getItems();
        weekChartMoreAdapter_korea.setFilterList(itemsArrayList_week_chart_korea);
        txt_title_week_chart_korea.setText("K-POP");
        Glide.with(requireContext()).load(itemsArrayList_week_chart_korea.get(0).getThumbnailM()).into(rounded_image_week_chart_korea);
    }

    private void setupChart() {
        // Cài đặt các thuộc tính cơ bản của biểu đồ
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true); // Cho phép chạm vào biểu đồ
        chart.setDragEnabled(false); // Tắt di chuyển biểu đồ
        chart.setScaleEnabled(false); // Tắt phóng to và thu nhỏ biểu đồ
        chart.setPinchZoom(false); // Tắt phóng to và thu nhỏ bằng cách kéo hai ngón tay (pinch zoom)

        // Cài đặt Legend
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE); // Đổi màu chữ chú thích thành trắng
        legend.setEnabled(false); // Ẩn toàn bộ chú thích (legend)

        // Cài đặt trục X
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE); // Đổi màu chữ trục X thành trắng
        xAxis.setGranularity(1f); // Chắc chắn rằng các nhãn trục X sẽ được hiển thị đủ

        // Đảm bảo trục Y bên phải không được hiển thị
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
    }

    private void updateChart(ChartHome chartHome) {
        JsonObject chartData = chartHome.getData().getRTChart().getChart();
        JsonArray times = chartData.getAsJsonArray("times");
        JsonObject items = chartData.getAsJsonObject("items");

        // Lấy 12 thời điểm cuối cùng
        List<String> xLabels = new ArrayList<>();
        int start = Math.max(times.size() - 12, 0);
        for (int i = start; i < times.size(); i++) {
            JsonObject timeObject = times.get(i).getAsJsonObject();
            String hour = timeObject.get("hour").getAsString();
            xLabels.add(hour + ":00");
        }

        // Tạo map cho các điểm dữ liệu
        Map<String, List<Entry>> entryMap = new HashMap<>();
        Map<String, Float> maxValues = new HashMap<>();

        for (String key : items.keySet()) {
            List<Entry> entries = new ArrayList<>();
            JsonArray dataArray = items.getAsJsonArray(key);

            // Đảm bảo điểm bắt đầu là (0, 0)
            entries.add(new Entry(0, 0));

            // Lấy dữ liệu cho 12 thời điểm cuối cùng
            float max = 0; // Khởi tạo giá trị tối đa cho line hiện tại
            for (int i = Math.max(dataArray.size() - 12, 0); i < dataArray.size(); i++) {
                JsonObject dataPoint = dataArray.get(i).getAsJsonObject();
                float counter = dataPoint.get("counter").getAsFloat();
                entries.add(new Entry(i - Math.max(dataArray.size() - 12, 0) + 1, counter));
                if (counter > max) {
                    max = counter; // Cập nhật giá trị tối đa
                }
            }

            entryMap.put(key, entries);
            maxValues.put(key, max); // Lưu giá trị tối đa cho line
            setTimeChartHome(dataArray);
        }

        // Sắp xếp các dòng theo giá trị tối đa
        List<Map.Entry<String, Float>> sortedEntries = new ArrayList<>(maxValues.entrySet());
        sortedEntries.sort((e1, e2) -> Float.compare(e2.getValue(), e1.getValue())); // Sắp xếp giảm dần

        // Tạo các LineDataSet và thêm vào biểu đồ
        List<ILineDataSet> dataSets = new ArrayList<>();
        int[] colors = {Color.BLUE, Color.GREEN, Color.RED}; // Màu sắc cho các line

        for (int i = 0; i < sortedEntries.size(); i++) {
            String key = sortedEntries.get(i).getKey();
            List<Entry> entries = entryMap.get(key);

            LineDataSet dataSet = new LineDataSet(entries, key);
            dataSet.setColor(colors[i % colors.length]); // Gán màu theo thứ tự
            dataSet.setLineWidth(2f);
            dataSet.setValueTextColor(Color.WHITE); // Ẩn giá trị trên line
            dataSet.setDrawValues(false); // Không hiển thị giá trị trên line
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawCircles(false);
            dataSet.setHighlightEnabled(true); // Ẩn highlight
            dataSet.setDrawHighlightIndicators(false); // Ẩn highlight indicators
            dataSets.add(dataSet);
        }

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        // Cài đặt nhãn cho trục X
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);  // Đổi màu chữ trục X thành trắng

        // Ẩn các giá trị trên trục Y
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawLabels(false); // Ẩn nhãn trên trục Y bên trái
        leftAxis.setDrawGridLines(false); // Ẩn các đường lưới trên trục Y bên trái

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false); // Ẩn nhãn trên trục Y bên phải
        rightAxis.setDrawGridLines(false); // Ẩn các đường lưới trên trục Y bên phải

        // Thiết lập sự kiện chạm vào điểm dữ liệu
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = h.getXPx();
                float y = h.getYPx();

                int dataSetIndex = h.getDataSetIndex();
                LineDataSet selectedDataSet = (LineDataSet) chart.getData().getDataSetByIndex(dataSetIndex);
                String label = selectedDataSet.getLabel();
                int lineOrder = dataSetIndex + 1;

                chartInfoView.show((ViewGroup) chart.getParent(), x, y, lineOrder, label);
            }

            @Override
            public void onNothingSelected() {
                chartInfoView.hide();
            }
        });

        chart.invalidate(); // Refresh chart

        nested_scroll.setVisibility(View.VISIBLE);
        relative_loading.setVisibility(View.GONE);
    }

    private void setTimeChartHome(JsonArray dataArray) {
        JsonObject timeObject = dataArray.get(dataArray.size() - 1).getAsJsonObject();
        long time = timeObject.get("time").getAsLong();
        txt_time_chart.setText(Helper.convertTimestampToDate(time));
    }


    private void loadImage(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.holder_video)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transform(new BitmapTransformation() {
                    @Override
                    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                        Palette p = Palette.from(toTransform).generate();
                        int defaultColor = 0x000000;
                        int startColor = p.getDarkVibrantColor(defaultColor);

                        if (startColor == defaultColor) {
                            startColor = p.getDarkMutedColor(defaultColor);
                            if (startColor == defaultColor) {
                                startColor = p.getVibrantColor(defaultColor);
                                if (startColor == defaultColor) {
                                    startColor = p.getMutedColor(defaultColor);
                                    if (startColor == defaultColor) {
                                        startColor = p.getLightVibrantColor(defaultColor);
                                        if (startColor == defaultColor) {
                                            startColor = p.getLightMutedColor(defaultColor);
                                        }
                                    }
                                }
                            }
                        }

                        int endColor = getResources().getColor(R.color.gray, null);
                        startColor = ColorUtils.setAlphaComponent(startColor, 150);

                        GradientDrawable gd = new GradientDrawable(
                                GradientDrawable.Orientation.TOP_BOTTOM,
                                new int[]{startColor, endColor}
                        );
                        gd.setCornerRadius(0f);
                        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                        gd.setGradientRadius(0.2f);

                        int finalStartColor = startColor;
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Thay đổi màu nền cho RelativeLayout
                                Drawable backgroundDrawable = relative_chart_home.getBackground();

                                if (backgroundDrawable instanceof GradientDrawable) {
                                    GradientDrawable drawable = (GradientDrawable) backgroundDrawable;
                                    // Thiết lập màu nền
                                    drawable.setColor(finalStartColor); // finalStartColor là màu bạn muốn sử dụng
                                } else {
                                    // Nếu drawable không phải là GradientDrawable, bạn có thể tạo một cái mới
                                    GradientDrawable drawable = new GradientDrawable();
                                    drawable.setShape(GradientDrawable.RECTANGLE);
                                    drawable.setColor(finalStartColor); // Màu nền
                                    relative_chart_home.setBackground(drawable);
                                }

                                // Thay đổi màu nền cho ImageView
                                GradientDrawable gd = new GradientDrawable();
                                gd.setShape(GradientDrawable.RECTANGLE);
                                gd.setColor(finalStartColor); // Màu nền

                                imageBackground.setBackground(gd);
                                relative_header.setBackgroundResource(android.R.color.transparent);
                            }
                        });

                        return toTransform;
                    }

                    @Override
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
                        messageDigest.update("paletteTransformer".getBytes());
                    }
                })
                .submit();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(createBroadcastReceiver(), new IntentFilter("send_data_to_activity"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(createBroadcastReceiver());
    }
}