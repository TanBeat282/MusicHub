package com.tandev.musichub.fragment.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tandev.musichub.MainActivity;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.album.AlbumMoreAdapter;
import com.tandev.musichub.adapter.banner.BannerSlideAdapter;
import com.tandev.musichub.adapter.home.HomePlaylistAdapter;
import com.tandev.musichub.adapter.hub.HubHomeAdapter;
import com.tandev.musichub.adapter.playlist.PlaylistMoreAdapter;
import com.tandev.musichub.adapter.radio.RadioMoreAdapter;
import com.tandev.musichub.adapter.song.SongMoreAdapter;
import com.tandev.musichub.adapter.week_chart.WeekChartSlideAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.ChartCategories;
import com.tandev.musichub.api.categories.HubCategories;
import com.tandev.musichub.api.categories.RadioCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.type_adapter_Factory.home.HomeDataItemTypeAdapter;
import com.tandev.musichub.bottomsheet.BottomSheetProfile;
import com.tandev.musichub.fragment.chart_home.ChartHomeFragment;
import com.tandev.musichub.fragment.chart_new_release.ChartNewReleaseFragment;
import com.tandev.musichub.fragment.history.HistoryFragment;
import com.tandev.musichub.fragment.hub.hub_home.HubHomeFragment;
import com.tandev.musichub.fragment.new_release.NewReleaseFragment;
import com.tandev.musichub.fragment.search.SearchFragment;
import com.tandev.musichub.fragment.top100.Top100Fragment;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.album.DataAlbum;
import com.tandev.musichub.model.chart.chart_home.Items;
import com.tandev.musichub.model.chart.home.home_new.Home;
import com.tandev.musichub.model.chart.home.home_new.album.HomeDataItemPlaylistAlbum;
import com.tandev.musichub.model.chart.home.home_new.banner.HomeDataItemBanner;
import com.tandev.musichub.model.chart.home.home_new.banner.HomeDataItemBannerItem;
import com.tandev.musichub.model.chart.home.home_new.editor_theme.HomeDataItemPlaylistEditorTheme;
import com.tandev.musichub.model.chart.home.home_new.editor_theme_3.HomeDataItemPlaylistEditorTheme3;
import com.tandev.musichub.model.chart.home.home_new.item.HomeDataItem;
import com.tandev.musichub.model.chart.home.home_new.new_release.HomeDataItemNewRelease;
import com.tandev.musichub.model.chart.home.home_new.new_release_chart.HomeDataItemNewReleaseChart;
import com.tandev.musichub.model.chart.home.home_new.radio.HomeDataItemRadio;
import com.tandev.musichub.model.chart.home.home_new.radio.HomeDataItemRadioItem;
import com.tandev.musichub.model.chart.home.home_new.rt_chart.HomeDataItemRTChart;
import com.tandev.musichub.model.chart.home.home_new.season_theme.HomeDataItemPlaylistSeasonTheme;
import com.tandev.musichub.model.chart.home.home_new.top100.HomeDataItemPlaylistTop100;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChart;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChartItem;
import com.tandev.musichub.model.hub.hub_home.HubHome;
import com.tandev.musichub.model.hub.hub_home.featured.HubHomeFeatured;
import com.tandev.musichub.model.hub.hub_home.featured.HubHomeFeaturedItems;
import com.tandev.musichub.model.hub.hub_home.nations.HubHomeNations;
import com.tandev.musichub.model.playlist.DataPlaylist;
import com.tandev.musichub.model.user_active_radio.DataUserActiveRadio;
import com.tandev.musichub.model.user_active_radio.UserActiveRadio;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private Handler mHandler;
    private static final int INTERVAL = 15000;
    private Home home;
    private SharedPreferencesManager sharedPreferencesManager;

    private ImageView img_icon_app;
    private TextView txt_name_app;

    //view
    private ViewPager2 view_pager_banner;
    private final Handler bannerHandler = new Handler();

    private NestedScrollView nested_scroll;
    private RelativeLayout relative_loading;


    private LinearLayout btn_tat_ca, btn_viet_nam, btn_quoc_te;
    private LinearLayout linear_new_release_song, linear_bxh_new_release_song;
    private ImageView img_history, img_search, img_account;


    //init_view
    // rv_new_release_song
    private HomeDataItemNewRelease homeDataItemNewRelease;
    private RecyclerView rv_new_release_song;
    private SongMoreAdapter new_release_songAdapter;
    private ArrayList<Items> new_release_songArrayList = new ArrayList<>();


    // bxh_new_release_song
    private RecyclerView rv_bxh_new_release_song;
    private SongMoreAdapter bxh_new_release_songAdapter;
    private ArrayList<Items> bxh_new_release_songArrayList = new ArrayList<>();


    // bxh nhac
    private LinearLayout linear_chart_home;
    private RecyclerView rv_bang_xep_hang;
    private SongMoreAdapter bang_xep_hangAdapter;
    private ArrayList<Items> bang_xep_hangArrayList = new ArrayList<>();

    // playlist
    private RecyclerView rv_playlist;
    private final ArrayList<HomeDataItem> homeDataItems = new ArrayList<>();
    private HomePlaylistAdapter homePlaylistAdapter;

    // week chart
    private ViewPager2 view_pager_week_chart;
    private final ArrayList<HomeDataItemWeekChartItem> homeDataItemWeekChartItems = new ArrayList<>();

    //hub_home
    private LinearLayout linear_hub_home;
    private TextView txt_hub_home;
    private RecyclerView rv_hub_home;
    private final ArrayList<HubHomeFeaturedItems> hubHomeFeaturedItemsArrayList = new ArrayList<>();
    private final ArrayList<HubHomeNations> hubHomeNationsArrayList = new ArrayList<>();
    private HubHomeAdapter hubHomeAdapter;

    //top100
    private LinearLayout linear_top100;
    private RecyclerView rv_top100;
    private TextView txt_title_top100;
    private PlaylistMoreAdapter playlistMoreAdapter;
    private final ArrayList<DataPlaylist> dataPlaylistArrayListTop100 = new ArrayList<>();

    private TextView txt_title_album;
    private RecyclerView rv_album;
    private final ArrayList<DataAlbum> dataAlbumArrayList = new ArrayList<>();
    private AlbumMoreAdapter albumMoreAdapter;

    private TextView txt_title_radio;
    private RecyclerView rv_radio;
    private final ArrayList<HomeDataItemRadioItem> homeDataItemRadioItemArrayList = new ArrayList<>();
    private RadioMoreAdapter radioMoreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        requireActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        initData();
        initViews(view);
        initRecyclerView();
        initAdapter();
        onClick();

        //get data
        getHome();
        getHubHome();

        mHandler = new Handler();
    }

    private void initData() {
        Helper.changeStatusBarColor(requireActivity(), R.color.black);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);

        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
    }

    private void initViews(View view) {
        img_icon_app = view.findViewById(R.id.img_icon_app);
        txt_name_app = view.findViewById(R.id.txt_name_app);

        nested_scroll = view.findViewById(R.id.nested_scroll);
        relative_loading = view.findViewById(R.id.relative_loading);
        rv_new_release_song = view.findViewById(R.id.rv_new_release_song);
        rv_bxh_new_release_song = view.findViewById(R.id.rv_bxh_new_release_song);

        linear_chart_home = view.findViewById(R.id.linear_chart_home);
        rv_bang_xep_hang = view.findViewById(R.id.rv_bang_xep_hang);

        view_pager_week_chart = view.findViewById(R.id.view_pager_week_chart);
        linear_new_release_song = view.findViewById(R.id.linear_new_release_song);
        linear_bxh_new_release_song = view.findViewById(R.id.linear_bxh_new_release_song);

        img_history = view.findViewById(R.id.img_history);
        view_pager_banner = view.findViewById(R.id.view_pager_banner);

        img_search = view.findViewById(R.id.img_search);
        img_account = view.findViewById(R.id.img_account);

        //btn nhac moi phat hanh
        btn_tat_ca = view.findViewById(R.id.btn_tat_ca);
        btn_viet_nam = view.findViewById(R.id.btn_viet_nam);
        btn_quoc_te = view.findViewById(R.id.btn_quoc_te);

        //playlist
        rv_playlist = view.findViewById(R.id.rv_playlist);

        //hub_home
        linear_hub_home = view.findViewById(R.id.linear_hub_home);
        txt_hub_home = view.findViewById(R.id.txt_hub_home);
        rv_hub_home = view.findViewById(R.id.rv_hub_home);

        //top100
        linear_top100 = view.findViewById(R.id.linear_top100);
        txt_title_top100 = view.findViewById(R.id.txt_title_top100);
        rv_top100 = view.findViewById(R.id.rv_top100);

        txt_title_album = view.findViewById(R.id.txt_title_album);
        rv_album = view.findViewById(R.id.rv_album);

        txt_title_radio = view.findViewById(R.id.txt_title_radio);
        rv_radio = view.findViewById(R.id.rv_radio);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManagerNewReleaseSong = new GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false);
        rv_new_release_song.setLayoutManager(layoutManagerNewReleaseSong);

        GridLayoutManager layoutManagerBXHNewReleaseSong = new GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false);
        rv_bxh_new_release_song.setLayoutManager(layoutManagerBXHNewReleaseSong);

        GridLayoutManager layoutManagerBangXepHang = new GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false);
        rv_bang_xep_hang.setLayoutManager(layoutManagerBangXepHang);


        LinearLayoutManager layoutManagerPlaylist = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rv_playlist.setLayoutManager(layoutManagerPlaylist);

        LinearLayoutManager layoutManagerHubHome = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_hub_home.setLayoutManager(layoutManagerHubHome);


        LinearLayoutManager layoutManagerTop100 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_top100.setLayoutManager(layoutManagerTop100);
        LinearLayoutManager layoutManagerAlbum = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_album.setLayoutManager(layoutManagerAlbum);
        LinearLayoutManager layoutManagerRadio = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_radio.setLayoutManager(layoutManagerRadio);
    }

    private void initAdapter() {
        new_release_songAdapter = new SongMoreAdapter(new_release_songArrayList, 0, requireActivity(), requireContext());
        rv_new_release_song.setAdapter(new_release_songAdapter);

        bxh_new_release_songAdapter = new SongMoreAdapter(bxh_new_release_songArrayList, 1, requireActivity(), requireContext());
        rv_bxh_new_release_song.setAdapter(bxh_new_release_songAdapter);

        bang_xep_hangAdapter = new SongMoreAdapter(bang_xep_hangArrayList, 2, requireActivity(), requireContext());
        rv_bang_xep_hang.setAdapter(bang_xep_hangAdapter);

        homePlaylistAdapter = new HomePlaylistAdapter(requireContext(), requireActivity(), homeDataItems);
        rv_playlist.setAdapter(homePlaylistAdapter);

        hubHomeAdapter = new HubHomeAdapter(hubHomeFeaturedItemsArrayList, hubHomeNationsArrayList, requireActivity(), requireContext());
        rv_hub_home.setAdapter(hubHomeAdapter);

        playlistMoreAdapter = new PlaylistMoreAdapter(dataPlaylistArrayListTop100, requireActivity(), requireContext());
        rv_top100.setAdapter(playlistMoreAdapter);

        albumMoreAdapter = new AlbumMoreAdapter(dataAlbumArrayList, requireActivity(), requireContext());
        rv_album.setAdapter(albumMoreAdapter);
        radioMoreAdapter = new RadioMoreAdapter(homeDataItemRadioItemArrayList, requireActivity(), requireContext());
        rv_radio.setAdapter(radioMoreAdapter);
    }

    private void onClick() {
        img_icon_app.setOnClickListener(view -> {
            nested_scroll.smoothScrollTo(0, 0);
        });
        txt_name_app.setOnClickListener(view -> {
            nested_scroll.smoothScrollTo(0, 0);
        });
        img_search.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new SearchFragment());
            }
        });
        img_history.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new HistoryFragment());
            }
        });
        img_account.setOnClickListener(view -> {
            BottomSheetProfile bottomSheetProfile = new BottomSheetProfile(requireContext(), requireActivity());
            bottomSheetProfile.show(requireActivity().getSupportFragmentManager(), bottomSheetProfile.getTag());
        });
        linear_new_release_song.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new NewReleaseFragment());
            }
        });
        linear_bxh_new_release_song.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new ChartNewReleaseFragment());
            }
        });
        linear_chart_home.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new ChartHomeFragment());
            }
        });
        linear_hub_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).replaceFragment(new HubHomeFragment());
                }
            }
        });
        linear_top100.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new Top100Fragment());
            }
        });

        //btn nhac moi phat hanh
        btn_viet_nam.setOnClickListener(view -> checkCategoriesNewReleaseSong(1));
        btn_quoc_te.setOnClickListener(view -> checkCategoriesNewReleaseSong(2));
        btn_tat_ca.setOnClickListener(view -> checkCategoriesNewReleaseSong(0));
    }

    private void getHome() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    ChartCategories chartCategories = new ChartCategories();
                    Map<String, String> map = chartCategories.getHome(1, 30);

                    retrofit2.Call<ResponseBody> call = service.HOME_CALL(map.get("page"), map.get("count"), map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getHome " + call.request().url());
                            if (response.isSuccessful()) {
                                try {
                                    assert response.body() != null;
                                    String jsonData = response.body().string();
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    gsonBuilder.registerTypeAdapter(HomeDataItem.class, new HomeDataItemTypeAdapter());
                                    Gson gson = gsonBuilder.create();

                                    home = gson.fromJson(jsonData, Home.class);

                                    if (home != null && home.getData() != null && home.getData().getItems() != null) {
                                        requireActivity().runOnUiThread(() -> {
                                            getBanner();
                                            getNewReleaseSong();
                                            getNewReleaseSongChart();
                                            getRTChart();
                                            getWeekChart();
                                            getPlaylist();
                                            getRadioLive();
                                            relative_loading.setVisibility(View.GONE);
                                            nested_scroll.setVisibility(View.VISIBLE);
                                        });
                                    }

                                } catch (Exception e) {
                                    Log.e("TAG", "Error: " + e.getMessage(), e);
                                }
                            } else {
                                Log.d("TAG", "Failed to retrieve data: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
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

    private void getHubHome() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    HubCategories hubCategories = new HubCategories();
                    Map<String, String> map = hubCategories.getHubHome();

                    retrofit2.Call<HubHome> call = service.HUB_HOME_CALL(map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<HubHome>() {
                        @Override
                        public void onResponse(Call<HubHome> call, Response<HubHome> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getHubHome " + call.request().url());
                            if (response.isSuccessful()) {
                                HubHome hubHome = response.body();
                                if (hubHome != null) {
                                    requireActivity().runOnUiThread(() -> {
                                        if (hubHome.getData() != null) {
                                            HubHomeFeatured hubHomeFeatured = hubHome.getData().getFeatured();
                                            for (int i = 0; i < Math.min(2, hubHomeFeatured.getItems().size()); i++) {
                                                hubHomeFeaturedItemsArrayList.add(hubHomeFeatured.getItems().get(i));
                                            }
                                            hubHomeNationsArrayList.addAll(hubHome.getData().getNations());

                                            hubHomeAdapter.setFilterList(hubHomeFeaturedItemsArrayList, hubHomeNationsArrayList);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<HubHome> call, Throwable throwable) {

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


    // banner slider
    private void getBanner() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();
            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemBanner) {
                    HomeDataItemBanner homeBanner = (HomeDataItemBanner) item;
                    setUpViewPageBanner(homeBanner);
                }
            }
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }

    //new release song
    @SuppressLint("NotifyDataSetChanged")
    private void checkCategoriesNewReleaseSong(int categories_nhac_moi) {
        new_release_songArrayList.clear();
        switch (categories_nhac_moi) {
            case 1:
                // viet nam
                btn_tat_ca.setBackgroundResource(R.drawable.background_button_categories);
                btn_viet_nam.setBackgroundResource(R.drawable.background_button_categories_check);
                btn_quoc_te.setBackgroundResource(R.drawable.background_button_categories);
                new_release_songArrayList.addAll(homeDataItemNewRelease.getItems().getvPop());

                break;
            case 2:
                // quoc te
                btn_tat_ca.setBackgroundResource(R.drawable.background_button_categories);
                btn_viet_nam.setBackgroundResource(R.drawable.background_button_categories);
                btn_quoc_te.setBackgroundResource(R.drawable.background_button_categories_check);
                new_release_songArrayList.addAll(homeDataItemNewRelease.getItems().getOthers());
                break;
            default:
                // tat ca
                btn_tat_ca.setBackgroundResource(R.drawable.background_button_categories_check);
                btn_viet_nam.setBackgroundResource(R.drawable.background_button_categories);
                btn_quoc_te.setBackgroundResource(R.drawable.background_button_categories);
                new_release_songArrayList.addAll(homeDataItemNewRelease.getItems().getAll());
                break;
        }
        new_release_songAdapter.setFilterList(new_release_songArrayList);
        rv_new_release_song.scrollToPosition(0);
    }

    // new release song
    private void getNewReleaseSong() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();
            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemNewRelease) {
                    homeDataItemNewRelease = (HomeDataItemNewRelease) item;
                    new_release_songArrayList = homeDataItemNewRelease.getItems().getAll();
                    new_release_songAdapter.setFilterList(new_release_songArrayList);
                }
            }
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }

    // playlist
    private void getPlaylist() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();

            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemPlaylistSeasonTheme) {
                    // hSeasonTheme
                    //playlist
                    HomeDataItemPlaylistSeasonTheme homeDataItemPlaylistSeasonTheme = (HomeDataItemPlaylistSeasonTheme) item;
                    homeDataItems.add(homeDataItemPlaylistSeasonTheme);
                } else if (item instanceof HomeDataItemPlaylistEditorTheme) {
                    // hEditorTheme
                    HomeDataItemPlaylistEditorTheme homeDataItemPlaylistEditorTheme = (HomeDataItemPlaylistEditorTheme) item;
                    homeDataItems.add(homeDataItemPlaylistEditorTheme);
                } else if (item instanceof HomeDataItemPlaylistEditorTheme3) {
                    // hEditorTheme3
                    HomeDataItemPlaylistEditorTheme3 homeDataItemPlaylistEditorTheme3 = (HomeDataItemPlaylistEditorTheme3) item;
                    homeDataItems.add(homeDataItemPlaylistEditorTheme3);
                } else if (item instanceof HomeDataItemPlaylistTop100) {
                    // hTop100
                    HomeDataItemPlaylistTop100 homeDataItemPlaylistTop100 = (HomeDataItemPlaylistTop100) item;
                    txt_title_top100.setText(homeDataItemPlaylistTop100.getTitle());
                    dataPlaylistArrayListTop100.addAll(homeDataItemPlaylistTop100.getItems());
                    playlistMoreAdapter.setFilterList(homeDataItemPlaylistTop100.getItems());
                } else if (item instanceof HomeDataItemPlaylistAlbum) {
                    // hAlbum
                    HomeDataItemPlaylistAlbum homeDataItemPLaylistAlbum = (HomeDataItemPlaylistAlbum) item;
                    txt_title_album.setText(homeDataItemPLaylistAlbum.getTitle());
                    dataAlbumArrayList.addAll(homeDataItemPLaylistAlbum.getItems());
                    albumMoreAdapter.setFilterList(homeDataItemPLaylistAlbum.getItems());
                } else {
                    Log.d("TAG", "Unknown HomeDataItem type: " + item.getClass().getSimpleName());
                }
            }
            homePlaylistAdapter.setFilterList(homeDataItems);
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }


    // bxh new release song
    private void getNewReleaseSongChart() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();
            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemNewReleaseChart) {
                    HomeDataItemNewReleaseChart homeDataItemNewReleaseChart = (HomeDataItemNewReleaseChart) item;
                    bxh_new_release_songArrayList = homeDataItemNewReleaseChart.getItems();
                    bxh_new_release_songAdapter.setFilterList(homeDataItemNewReleaseChart.getItems());
                }
            }
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }


    // rt chart
    private void getRTChart() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();
            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemRTChart) {
                    HomeDataItemRTChart homeDataItemRTChart = (HomeDataItemRTChart) item;
                    bang_xep_hangArrayList = homeDataItemRTChart.getItems();
                    bang_xep_hangAdapter.setFilterList(homeDataItemRTChart.getItems());
                }
            }
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }


    // week chart
    private void getWeekChart() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();
            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemWeekChart) {
                    HomeDataItemWeekChart homeDataItemWeekChart = (HomeDataItemWeekChart) item;
                    setUpViewPageWeekChart(homeDataItemWeekChart);
                }
            }
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }

    // new release song
    private void getRadioLive() {
        if (home != null && home.getData() != null && home.getData().getItems() != null) {
            ArrayList<HomeDataItem> items = home.getData().getItems();
            for (HomeDataItem item : items) {
                if (item instanceof HomeDataItemRadio) {
                    HomeDataItemRadio homeDataItemRadio = (HomeDataItemRadio) item;
                    txt_title_radio.setText(homeDataItemRadio.getTitle());
                    homeDataItemRadioItemArrayList.addAll(homeDataItemRadio.getItems());
                    radioMoreAdapter.setFilterList(homeDataItemRadio.getItems());
                    // Sử dụng mHandler để lập lịch gọi hàm getUserActiveRadio(homeDataItemRadioItemArrayList)
                    startRepeatingTask();
                }
            }
        } else {
            Log.d("TAG", "No data found in JSON");
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                // Gọi hàm getUserActiveRadio(homeDataItemRadioItemArrayList)
                getUserActiveRadio(homeDataItemRadioItemArrayList);
            } finally {
                // Lập lịch gọi lại chính nó sau 3 giây
                mHandler.postDelayed(mStatusChecker, INTERVAL);
            }
        }
    };

    private void startRepeatingTask() {
        // Bắt đầu lập lịch gọi lại phương thức mStatusChecker
        mHandler.postDelayed(mStatusChecker, INTERVAL);
    }

    private void setUpViewPageWeekChart(HomeDataItemWeekChart homeDataItemWeekChart) {
        homeDataItemWeekChartItems.add(homeDataItemWeekChart.getItems().get(1));
        homeDataItemWeekChartItems.add(homeDataItemWeekChart.getItems().get(0));
        homeDataItemWeekChartItems.add(homeDataItemWeekChart.getItems().get(2));

        view_pager_week_chart.setAdapter(new WeekChartSlideAdapter(homeDataItemWeekChartItems, view_pager_week_chart, requireContext(), requireActivity()));
        view_pager_week_chart.setClipToPadding(false);
        view_pager_week_chart.setClipChildren(false);
        view_pager_week_chart.setOffscreenPageLimit(3);
        view_pager_week_chart.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        view_pager_week_chart.setPageTransformer(compositePageTransformer);
        view_pager_week_chart.setCurrentItem(1, false);
    }

    private void setUpViewPageBanner(HomeDataItemBanner homeDataItemBanner) {
        ArrayList<HomeDataItemBannerItem> bannerItems = homeDataItemBanner.getItems();
        BannerSlideAdapter adapter = new BannerSlideAdapter(bannerItems, view_pager_banner, requireContext(), requireActivity());

        view_pager_banner.setAdapter(adapter);
        view_pager_banner.setClipToPadding(false);
        view_pager_banner.setClipChildren(false);
        view_pager_banner.setOffscreenPageLimit(3);
        view_pager_banner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float scaleFactor = 0.8f + (1 - Math.abs(position)) * 0.15f;
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

    private void getUserActiveRadio(ArrayList<HomeDataItemRadioItem> homeDataItemRadioItemArrayList) {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    RadioCategories radioCategories = new RadioCategories();
                    StringBuilder idsBuilder = new StringBuilder();
                    for (int i = 0; i < homeDataItemRadioItemArrayList.size(); i++) {
                        HomeDataItemRadioItem item = homeDataItemRadioItemArrayList.get(i);
                        idsBuilder.append(item.getEncodeId());
                        if (i < homeDataItemRadioItemArrayList.size() - 1) {
                            idsBuilder.append(",");
                        }
                    }
                    String ids = idsBuilder.toString();

                    Map<String, String> map = radioCategories.getUserActiveRadio(ids);
                    retrofit2.Call<UserActiveRadio> call = service.USER_ACTIVE_RADIO_CALL(ids, map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));

                    call.enqueue(new Callback<UserActiveRadio>() {
                        @Override
                        public void onResponse(@NonNull Call<UserActiveRadio> call, @NonNull Response<UserActiveRadio> response) {
                            Log.d(">>>>>>>>>>>>>>>>>>", "getUserActiveRadio " + call.request().url());
                            if (response.isSuccessful()) {
                                UserActiveRadio userActiveRadio = response.body();
                                if (userActiveRadio != null) {
                                    for (DataUserActiveRadio dataUserActiveRadio : userActiveRadio.getData()) {
                                        for (HomeDataItemRadioItem homeDataItemRadioItem : homeDataItemRadioItemArrayList) {
                                            if (homeDataItemRadioItem.getEncodeId().equals(dataUserActiveRadio.getEncodeId())) {
                                                homeDataItemRadioItem.setActiveUsers(dataUserActiveRadio.getActiveUsers());
                                            }
                                        }
                                    }
                                    radioMoreAdapter.setFilterList(homeDataItemRadioItemArrayList);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserActiveRadio> call, @NonNull Throwable throwable) {
                            Log.e("TAG", "API call failed: " + throwable.getMessage(), throwable);
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


    @Override
    public void onResume() {
        super.onResume();
        bannerHandler.postDelayed(bannerRunnable, 3000);
        startRepeatingTask();
    }

    @Override
    public void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mStatusChecker);
    }
}