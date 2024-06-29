package com.tandev.musichub.fragment.search;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.search.search_multi.SearchMultiViewPageAdapter;
import com.tandev.musichub.adapter.search.search_recommend.SearchRecommendAdapter;
import com.tandev.musichub.adapter.search.search_suggestion.SearchSuggestionAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SearchCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.service.RetrofitClient;
import com.tandev.musichub.api.type_adapter_Factory.search.SearchTypeAdapter;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.log.LogUtil;
import com.tandev.musichub.model.search.search_recommend.DataSearchRecommend;
import com.tandev.musichub.model.search.search_recommend.SearchRecommend;
import com.tandev.musichub.model.search.search_suggestion.SearchSuggestions;
import com.tandev.musichub.model.search.search_suggestion.SearchSuggestionsDataItem;
import com.tandev.musichub.model.search.search_suggestion.keyword.SearchSuggestionsDataItemKeyWords;
import com.tandev.musichub.model.search.search_suggestion.keyword.SearchSuggestionsDataItemKeyWordsItem;
import com.tandev.musichub.model.search.search_suggestion.playlist.SearchSuggestionsDataItemSuggestionsPlaylist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestions;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsArtist;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsItem;
import com.tandev.musichub.model.search.search_suggestion.suggestion.SearchSuggestionsDataItemSuggestionsSong;
import com.tandev.musichub.sharedpreferences.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchSuggestionAdapter.KeyWordItemClickListener, SearchRecommendAdapter.SearchRecommendClickListener {
    private SearchView searchView;
    private ImageView img_back;

    //search suggestion
    private RelativeLayout relative_search_suggestion;
    private RecyclerView rv_search_suggestion;

    //search multi
    private RelativeLayout relative_search_multi;
    private TabLayout tab_layout_search_multi;
    private ViewPager view_pager_search_multi;
    private static final String allowCorrect = "1";

    //no data
    private LinearLayout linear_search_recommend;
    private RecyclerView rv_search_recommend;
    private ProgressBar progress_bar_loading;
    private SearchRecommendAdapter searchRecommendAdapter;
    private ArrayList<DataSearchRecommend> dataSearchRecommendArrayList = new ArrayList<>();


    private SearchSuggestionAdapter searchSuggestionAdapter;
    private final ArrayList<SearchSuggestionsDataItemKeyWordsItem> searchSuggestionsDataItemKeyWordsItems = new ArrayList<>();
    private final ArrayList<SearchSuggestionsDataItemSuggestionsArtist> searchSuggestionsDataItemSuggestionsArtists = new ArrayList<>();
    private final ArrayList<SearchSuggestionsDataItemSuggestionsSong> searchSuggestionsDataItemSuggestionsSongs = new ArrayList<>();
    private final ArrayList<SearchSuggestionsDataItemSuggestionsPlaylist> searchSuggestionsDataItemSuggestionsPlaylists = new ArrayList<>();
    private SharedPreferencesManager sharedPreferencesManager;
    private final Handler handler = new Handler();
    private static final int DELAY = 500;
    private Runnable searchRunnable;

    private ApiService apiService;

    private final BroadcastReceiver broadcastReceiverTabLayout = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            int tab_layout_position = bundle.getInt("position");
            tab_layout_search_multi.getTabAt(tab_layout_position).select();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);
        Helper.changeStatusBarColor(requireActivity(), R.color.black);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initViewsSearchMulti(view);
        initViewsSearchSuggestion(view);
        conFigViewSearchSuggestion();


        initAdapter();
        onClick();
        getSearchRecommend();

        searchRunnable = () -> {
            try {
                searchSuggestion(searchView.getQuery().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
    private void initViewsSearchSuggestion(View view) {
        searchView = view.findViewById(R.id.searchView);
        relative_search_suggestion = view.findViewById(R.id.relative_search_suggestion);
        rv_search_suggestion = view.findViewById(R.id.rv_search_suggestion);
        linear_search_recommend = view.findViewById(R.id.linear_search_recommend);
        rv_search_recommend = view.findViewById(R.id.rv_search_recommend);
        progress_bar_loading = view.findViewById(R.id.progress_bar_loading);
        img_back = view.findViewById(R.id.img_back);
    }

    private void initViewsSearchMulti(View view) {
        relative_search_multi = view.findViewById(R.id.relative_search_multi);

        tab_layout_search_multi = view.findViewById(R.id.tab_layout_search_multi);
        view_pager_search_multi = view.findViewById(R.id.view_pager_search_multi);
    }

    private void initViewPager(String query) {
        SearchMultiViewPageAdapter mSearchMultiViewPageAdapter = new SearchMultiViewPageAdapter(requireActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, query);
        view_pager_search_multi.setAdapter(mSearchMultiViewPageAdapter);
        tab_layout_search_multi.setupWithViewPager(view_pager_search_multi);
    }

    @SuppressLint("SetTextI18n")
    private void conFigViewSearchSuggestion() {
        linear_search_recommend.setVisibility(View.VISIBLE);
        progress_bar_loading.setVisibility(View.GONE);
        rv_search_suggestion.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initViewPager(query);
                sendBroadcast(query);
                relative_search_suggestion.setVisibility(View.GONE);
                relative_search_multi.setVisibility(View.VISIBLE);

                // Close the keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                relative_search_suggestion.setVisibility(View.VISIBLE);
                relative_search_multi.setVisibility(View.GONE);

                if (newText == null || newText.isEmpty()) {
                    linear_search_recommend.setVisibility(View.VISIBLE);
                    progress_bar_loading.setVisibility(View.GONE);
                    rv_search_suggestion.setVisibility(View.GONE);
                } else {
                    linear_search_recommend.setVisibility(View.GONE);
                    progress_bar_loading.setVisibility(View.VISIBLE);
                    rv_search_suggestion.setVisibility(View.GONE);
                    handler.removeCallbacks(searchRunnable);
                    handler.postDelayed(searchRunnable, DELAY);
                }

                return true;
            }
        });
    }

    private void initAdapter() {
        rv_search_suggestion.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchSuggestionAdapter = new SearchSuggestionAdapter(requireContext(), requireActivity(), searchSuggestionsDataItemKeyWordsItems, searchSuggestionsDataItemSuggestionsArtists, searchSuggestionsDataItemSuggestionsPlaylists, searchSuggestionsDataItemSuggestionsSongs);
        rv_search_suggestion.setAdapter(searchSuggestionAdapter);
        searchSuggestionAdapter.setListener(this);

        rv_search_recommend.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRecommendAdapter = new SearchRecommendAdapter(dataSearchRecommendArrayList, requireActivity(), requireContext());
        rv_search_recommend.setAdapter(searchRecommendAdapter);
        searchRecommendAdapter.setListener(this);
    }

    private void onClick() {
        img_back.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void getSearchRecommend() {
        ApiServiceFactory.createServiceAsync(new ApiServiceFactory.ApiServiceCallback() {
            @Override
            public void onServiceCreated(ApiService service) {
                try {
                    SearchCategories searchCategories = new SearchCategories();
                    Map<String, String> map = searchCategories.getRecommendKeyword();

                    retrofit2.Call<SearchRecommend> call = service.SEARCH_RECOMMEND_CALL(map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));
                    call.enqueue(new Callback<SearchRecommend>() {
                        @Override
                        public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                            LogUtil.d(Constants.TAG, "getSearchRecommend: " + call.request().url());
                            if (response.isSuccessful()) {
                                SearchRecommend searchRecommend = response.body();
                                if (searchRecommend != null) {
                                    ArrayList<DataSearchRecommend> data = searchRecommend.getData();
                                    if (!data.isEmpty()) {
                                        requireActivity().runOnUiThread(() -> {
                                            dataSearchRecommendArrayList = data;
                                            searchRecommendAdapter.setFilterList(dataSearchRecommendArrayList);
                                        });
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchRecommend> call, Throwable throwable) {

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


    private void searchSuggestion(String query) throws Exception {
        // Clear previous data before making a new search request
        searchSuggestionsDataItemKeyWordsItems.clear();
        searchSuggestionsDataItemSuggestionsArtists.clear();
        searchSuggestionsDataItemSuggestionsSongs.clear();
        searchSuggestionsDataItemSuggestionsPlaylists.clear();

        // Gọi phương thức trong ApiService
        SearchCategories searchCategories = new SearchCategories();
        Map<String, String> map = searchCategories.getSearchSuggestion();

        Call<ResponseBody> call = apiService.SEARCH_SUGGESTIONS_CALL("10", query, "vi", map.get("sig"), map.get("ctime"), map.get("version"), map.get("apiKey"));

        // Thực hiện cuộc gọi bất đồng bộ
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                LogUtil.d(Constants.TAG, "searchSuggestion: " + call.request().url());
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        String jsonData = response.body().string();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(SearchSuggestionsDataItem.class, new SearchTypeAdapter());
                        Gson gson = gsonBuilder.create();

                        SearchSuggestions searchSuggestions = gson.fromJson(jsonData, SearchSuggestions.class);

                        requireActivity().runOnUiThread(() -> {
                            ArrayList<SearchSuggestionsDataItem> items = searchSuggestions.getData().getItems();
                            for (SearchSuggestionsDataItem item : items) {
                                if (item instanceof SearchSuggestionsDataItemKeyWords) {
                                    SearchSuggestionsDataItemKeyWords searchSuggestionsDataItemKeyWords = (SearchSuggestionsDataItemKeyWords) item;
                                    searchSuggestionsDataItemKeyWordsItems.addAll(searchSuggestionsDataItemKeyWords.getKeywords());

                                } else if (item instanceof SearchSuggestionsDataItemSuggestions) {
                                    SearchSuggestionsDataItemSuggestions searchSuggestionsDataItemSuggestions = (SearchSuggestionsDataItemSuggestions) item;

                                    ArrayList<SearchSuggestionsDataItemSuggestionsItem> suggestions = searchSuggestionsDataItemSuggestions.getSuggestions();
                                    for (SearchSuggestionsDataItemSuggestionsItem item2 : suggestions) {
                                        if (item2 instanceof SearchSuggestionsDataItemSuggestionsArtist) {
                                            SearchSuggestionsDataItemSuggestionsArtist searchSuggestionsDataItemSuggestionsArtist = (SearchSuggestionsDataItemSuggestionsArtist) item2;
                                            searchSuggestionsDataItemSuggestionsArtists.add(searchSuggestionsDataItemSuggestionsArtist);
                                        } else if (item2 instanceof SearchSuggestionsDataItemSuggestionsSong) {
                                            SearchSuggestionsDataItemSuggestionsSong searchSuggestionsDataItemSuggestionsSong = (SearchSuggestionsDataItemSuggestionsSong) item2;
                                            searchSuggestionsDataItemSuggestionsSongs.add(searchSuggestionsDataItemSuggestionsSong);
                                        } else if (item2 instanceof SearchSuggestionsDataItemSuggestionsPlaylist) {
                                            SearchSuggestionsDataItemSuggestionsPlaylist searchSuggestionsDataItemSuggestionsPlaylist = (SearchSuggestionsDataItemSuggestionsPlaylist) item2;
                                            searchSuggestionsDataItemSuggestionsPlaylists.add(searchSuggestionsDataItemSuggestionsPlaylist);
                                        }
                                    }
                                }
                            }
                            linear_search_recommend.setVisibility(View.GONE);
                            progress_bar_loading.setVisibility(View.GONE);
                            rv_search_suggestion.setVisibility(View.VISIBLE);
                            searchSuggestionAdapter.setFilterList(searchSuggestionsDataItemKeyWordsItems, searchSuggestionsDataItemSuggestionsArtists, searchSuggestionsDataItemSuggestionsPlaylists, searchSuggestionsDataItemSuggestionsSongs);
                        });

                    } catch (Exception e) {
                        Log.e("TAG", "Error: " + e.getMessage(), e);
                    }
                } else {
                    Log.d("TAG", "Failed to retrieve data: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                // Xử lý lỗi trong quá trình gọi API
                Log.e("SearchActivity", "Error fetching search results: " + t.getMessage());
            }
        });
    }

    private void sendBroadcast(String query) {
        Intent intent = new Intent("search_query");
        Bundle bundle = new Bundle();
        bundle.putString("query", query);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
        LogUtil.d(Constants.TAG, "query1 " + query);
    }

    @Override
    public void onKeyWordItemClick(String keyword) {
        initViewPager(keyword);
        sendBroadcast(keyword);
        relative_search_suggestion.setVisibility(View.GONE);
        relative_search_multi.setVisibility(View.VISIBLE);
        // Close the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }

    @Override
    public void onSearchRecommendClickListener(String keyword) {
        initViewPager(keyword);
        sendBroadcast(keyword);
        relative_search_suggestion.setVisibility(View.GONE);
        relative_search_multi.setVisibility(View.VISIBLE);
        // Close the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
        searchView.setQuery(keyword, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiverTabLayout, new IntentFilter("tab_layout_position"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiverTabLayout);
    }
}