package com.tandev.musichub.fragment.top100;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.top100.Top100Adapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.ChartCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.top100.DataTop100;
import com.tandev.musichub.model.top100.Top100;
import com.tandev.musichub.view_model.top100.Top100ViewModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Top100Fragment extends Fragment {
    private Top100ViewModel top100ViewModel;

    private RelativeLayout relative_header;
    private ImageView img_back;
    private TextView txt_name_playlist, txt_view;
    private NestedScrollView nested_scroll;
    private RecyclerView rv_playlist_vertical;
    private ImageView img_playlist;
    private ProgressBar progress_image;

    private ArrayList<DataTop100> dataTop100ArrayList;
    private Top100Adapter top100Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        top100ViewModel = new ViewModelProvider(this).get(Top100ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top100, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initAdapter();
        conFigViews();
        onClick();
        initViewModel();
    }

    private void initViewModel() {
        top100ViewModel.getTop100MutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUI(artistDetail);
            } else {
                getTop100();
            }
        });

        if (top100ViewModel.getTop100MutableLiveData().getValue() == null) {
            getTop100();
        }
    }

    private void initViews(View view) {
        relative_header = view.findViewById(R.id.relative_header);
        img_back = view.findViewById(R.id.img_back);
        txt_name_playlist = view.findViewById(R.id.txt_name_playlist);
        txt_view = view.findViewById(R.id.txt_view);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        rv_playlist_vertical = view.findViewById(R.id.rv_playlist_vertical);

        img_playlist = view.findViewById(R.id.img_playlist);
        progress_image = view.findViewById(R.id.progress_image);

    }

    private void initAdapter() {
        dataTop100ArrayList = new ArrayList<>();
        rv_playlist_vertical.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        top100Adapter = new Top100Adapter(dataTop100ArrayList, requireActivity(), requireContext());
        rv_playlist_vertical.setAdapter(top100Adapter);
    }

    private void conFigViews() {
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_name_playlist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status bar
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                        requireActivity().getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
                    }

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    txt_name_playlist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_playlist.setText("Top 100");
                    relative_header.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray));
                    Helper.changeStatusBarColor(requireActivity(), R.color.gray);
                }
            }
        });
    }

    private void onClick() {
        img_back.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void getTop100() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    ChartCategories chartCategories = new ChartCategories();
                    Map<String, String> map = chartCategories.getTop100();

                    retrofit2.Call<Top100> call = service.TOP100_CALL(map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<Top100>() {
                        @Override
                        public void onResponse(@NonNull Call<Top100> call, @NonNull Response<Top100> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getTop100 " + call.request().url());
                            if (response.isSuccessful() && response.body() != null) {
                                Top100 top100 = response.body();
                                if (top100.getErr() == 0) {
                                    top100ViewModel.setTop100MutableLiveData(top100);
                                }
                            } else {
                                Log.d("TAG", "Response unsuccessful or empty body");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Top100> call, @NonNull Throwable throwable) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getHub1111 " + call.request().url());
                        }
                    });

                } catch (Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }

    private void updateUI(Top100 top100) {
        ArrayList<DataTop100> arrayList = top100.getData();
        Glide.with(requireContext()).load("https://photo-zmp3.zmdcdn.me/cover/d/8/3/2/d8326486b929ec525e44f7059ca3ebca.jpg").placeholder(R.drawable.holder_video).into(img_playlist);
        if (!arrayList.isEmpty()) {
            dataTop100ArrayList = arrayList;
            top100Adapter.setFilterList(dataTop100ArrayList);
            progress_image.setVisibility(View.GONE);
            img_playlist.setVisibility(View.VISIBLE);
        }
    }
}