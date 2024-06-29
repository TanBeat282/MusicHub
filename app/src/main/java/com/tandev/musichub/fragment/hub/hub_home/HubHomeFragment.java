package com.tandev.musichub.fragment.hub.hub_home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tandev.musichub.R;
import com.tandev.musichub.adapter.hub.hub_home_banner.HubHomeBannerAdapter;
import com.tandev.musichub.adapter.hub.hub_home.HubHomeAllAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.HubCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.hub.hub_home.HubHome;
import com.tandev.musichub.model.hub.hub_home.banner.HubHomeBanner;
import com.tandev.musichub.model.hub.hub_home.featured.HubHomeFeaturedItems;
import com.tandev.musichub.model.hub.hub_home.genre.HubHomeGenre;
import com.tandev.musichub.model.hub.hub_home.nations.HubHomeNations;
import com.tandev.musichub.model.hub.hub_home.topic.HubHomeTopic;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HubHomeFragment extends Fragment {
    private RelativeLayout relative_header;
    private ImageView img_back;
    private TextView txt_name_artist;
    private TextView txt_view;
    private ImageView img_more;

    private NestedScrollView nested_scroll;
    private RelativeLayout relative_loading;
    private TextView txt_hub_home;
    private SearchView searchView;

    private ViewPager2 view_pager_banner;
    private final Handler bannerHandler = new Handler();

    private TextView txt_title_noi_bat;
    private RecyclerView rv_noi_bat;
    private ArrayList<HubHomeFeaturedItems> hubHomeFeaturedItems = new ArrayList<>();
    private HubHomeAllAdapter hubHomeAllAdapter;


    private TextView txt_title_topic;
    private RecyclerView rv_topic;
    private ArrayList<HubHomeTopic> hubHomeTopics = new ArrayList<>();
    private HubHomeAllAdapter hubHomeTopicAdapter;

    private TextView txt_title_nation;
    private RecyclerView rv_nation;
    private ArrayList<HubHomeNations> hubHomeNations = new ArrayList<>();
    private HubHomeAllAdapter hubHomeNationAdapter;

    private TextView txt_title_genre;
    private RecyclerView rv_genre;
    private ArrayList<HubHomeGenre> hubHomeGenres = new ArrayList<>();
    private HubHomeAllAdapter hubHomeGenreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hub_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initAdapter();
        conFigViews();
        onClick();


        getHubHome();
    }

    private void initViews(View view) {
        relative_header = view.findViewById(R.id.relative_header);
        img_back = view.findViewById(R.id.img_back);
        txt_name_artist = view.findViewById(R.id.txt_name_artist);
        txt_view = view.findViewById(R.id.txt_view);
        img_more = view.findViewById(R.id.img_more);

        nested_scroll = view.findViewById(R.id.nested_scroll);
        relative_loading = view.findViewById(R.id.relative_loading);
        txt_hub_home = view.findViewById(R.id.txt_hub_home);
        searchView = view.findViewById(R.id.searchView);
        view_pager_banner = view.findViewById(R.id.view_pager_banner);

        txt_title_noi_bat = view.findViewById(R.id.txt_title_noi_bat);
        rv_noi_bat = view.findViewById(R.id.rv_noi_bat);

        txt_title_topic = view.findViewById(R.id.txt_title_topic);
        rv_topic = view.findViewById(R.id.rv_topic);

        txt_title_nation = view.findViewById(R.id.txt_title_nation);
        rv_nation = view.findViewById(R.id.rv_nation);

        txt_title_genre = view.findViewById(R.id.txt_title_genre);
        rv_genre = view.findViewById(R.id.rv_genre);
    }

    private void initAdapter() {
        rv_noi_bat.setLayoutManager(new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false));
        hubHomeAllAdapter = new HubHomeAllAdapter(hubHomeFeaturedItems, requireActivity(), requireContext());
        rv_noi_bat.setAdapter(hubHomeAllAdapter);

        rv_topic.setLayoutManager(new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false));
        hubHomeTopicAdapter = new HubHomeAllAdapter(hubHomeTopics, requireActivity(), requireContext());
        rv_topic.setAdapter(hubHomeTopicAdapter);

        rv_nation.setLayoutManager(new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false));
        hubHomeNationAdapter = new HubHomeAllAdapter(hubHomeNations, requireActivity(), requireContext());
        rv_nation.setAdapter(hubHomeNationAdapter);

        rv_genre.setLayoutManager(new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false));
        hubHomeGenreAdapter = new HubHomeAllAdapter(hubHomeGenres, requireActivity(), requireContext());
        rv_genre.setAdapter(hubHomeGenreAdapter);
    }

    private void conFigViews() {
        nested_scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @SuppressLint({"ObsoleteSdkInt", "SetTextI18n"})
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Kiểm tra nếu người dùng đã cuộn đến đầu trang
                if (scrollY <= 600) {
                    // Ẩn TextView khi người dùng cuộn trở lại đầu trang
                    txt_name_artist.setVisibility(View.GONE);
                    txt_view.setVisibility(View.VISIBLE);
                    relative_header.setBackgroundResource(android.R.color.transparent);
                    // Make the content appear under the status barr
                    Helper.changeStatusBarTransparent(requireActivity());

                } else if (scrollY >= 800) {
                    // Hiển thị TextView khi người dùng cuộn xuống khỏi đầu trang
                    txt_name_artist.setVisibility(View.VISIBLE);
                    txt_view.setVisibility(View.GONE);
                    txt_name_artist.setText("Chủ đề & thể loại");
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

    private void getHubHome() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    HubCategories hubCategories = new HubCategories();
                    Map<String, String> map = hubCategories.getHubHome();

                    retrofit2.Call<HubHome> call = service.HUB_HOME_CALL(map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<HubHome>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<HubHome> call, @NonNull Response<HubHome> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getHubHome " + call.request().url());
                            if (response.isSuccessful()) {
                                HubHome hubHome = response.body();
                                if (hubHome != null) {
                                    requireActivity().runOnUiThread(() -> {
                                        setUpViewPageBanner(hubHome.getData().getBanners());

                                        txt_title_noi_bat.setText(hubHome.getData().getFeatured().getTitle());
                                        hubHomeFeaturedItems = hubHome.getData().getFeatured().getItems();

                                        txt_title_nation.setText("Quốc gia");
                                        hubHomeNations = hubHome.getData().getNations();

                                        txt_title_topic.setText("Hot");
                                        hubHomeTopics = hubHome.getData().getTopic();

                                        txt_title_genre.setText("Thể loại");
                                        hubHomeGenres = hubHome.getData().getGenre();

                                        hubHomeAllAdapter.setFilterList(hubHomeFeaturedItems);
                                        hubHomeNationAdapter.setFilterList(hubHomeNations);
                                        hubHomeTopicAdapter.setFilterList(hubHomeTopics);
                                        hubHomeGenreAdapter.setFilterList(hubHomeGenres);

                                        relative_loading.setVisibility(View.GONE);
                                        nested_scroll.setVisibility(View.VISIBLE);
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<HubHome> call, @NonNull Throwable throwable) {

                        }
                    });
                } catch (
                        Exception e) {
                    Log.e("TAG", "Error: " + e.getMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("TAG", "Service creation error: " + e.getMessage(), e);
            }
        });
    }

    private void setUpViewPageBanner(ArrayList<HubHomeBanner> hubHomeBannerArrayList) {
        HubHomeBannerAdapter adapter = new HubHomeBannerAdapter(hubHomeBannerArrayList, view_pager_banner, requireContext(), requireActivity());

        view_pager_banner.setAdapter(adapter);
        view_pager_banner.setClipToPadding(false);
        view_pager_banner.setClipChildren(false);
        view_pager_banner.setOffscreenPageLimit(3);
        view_pager_banner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(30));
        transformer.addTransformer((page, position) -> {
            float scaleFactor = 0.7f + (1 - Math.abs(position)) * 0.2f;
            page.setScaleY(scaleFactor);
        });
        view_pager_banner.setPageTransformer(transformer);

        view_pager_banner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bannerHandler.removeCallbacks(bannerRunnable);
                bannerHandler.postDelayed(bannerRunnable, 3000);
            }
        });
    }

    private final Runnable bannerRunnable = () -> view_pager_banner.setCurrentItem(view_pager_banner.getCurrentItem() + 1);

    @Override
    public void onResume() {
        super.onResume();
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bannerHandler.removeCallbacks(bannerRunnable);
    }
}