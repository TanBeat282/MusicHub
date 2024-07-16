package com.tandev.musichub.fragment.search;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.search.search_history.SearchHistoryAdapter;
import com.tandev.musichub.adapter.search.search_multi.SearchMultiViewPageAdapter;
import com.tandev.musichub.adapter.search.search_recommend.SearchRecommendAdapter;
import com.tandev.musichub.adapter.search.search_suggestion.SearchSuggestionAdapter;
import com.tandev.musichub.api.ApiService;
import com.tandev.musichub.api.categories.SearchCategories;
import com.tandev.musichub.api.service.ApiServiceFactory;
import com.tandev.musichub.api.service.RetrofitClient;
import com.tandev.musichub.api.type_adapter_Factory.search.SearchTypeAdapter;
import com.tandev.musichub.constants.Constants;
import com.tandev.musichub.constants.PermissionConstants;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.helper.uliti.PermissionUtils;
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
import com.tandev.musichub.view_model.search.SearchRecommendViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchSuggestionAdapter.KeyWordItemClickListener, SearchRecommendAdapter.SearchRecommendClickListener, SearchHistoryAdapter.SearchRecommendClickListener, PermissionUtils.PermissionCallback {
    private PermissionUtils permissionUtils;

    private SharedPreferencesManager sharedPreferencesManager;
    private SearchRecommendViewModel searchRecommendViewModel;

    private SearchView searchView;
    private ImageView img_mic;
    private ImageView img_back;

    //search recommend
    private LinearLayout linear_search_recommend;
    private RecyclerView rv_search_recommend;
    private SearchRecommendAdapter searchRecommendAdapter;
    private ArrayList<DataSearchRecommend> dataSearchRecommendArrayList;


    //search suggestion
    private RelativeLayout relative_search_suggestion;
    private RecyclerView rv_search_suggestion;

    //search history
    private View view_search_history;
    private TextView txt_search_history;
    private TextView txt_delete_search_history;
    private RecyclerView rv_search_history;
    private SearchHistoryAdapter searchHistoryAdapter;
    private ArrayList<DataSearchRecommend> dataSearchHistoryArrayList;

    //search multi
    private RelativeLayout relative_search_multi;
    private TabLayout tab_layout_search_multi;
    private ViewPager view_pager_search_multi;
    private static final String allowCorrect = "1";

    //no data
    private ProgressBar progress_bar_loading;


    private SearchSuggestionAdapter searchSuggestionAdapter;
    private ArrayList<SearchSuggestionsDataItemKeyWordsItem> searchSuggestionsDataItemKeyWordsItems;
    private ArrayList<SearchSuggestionsDataItemSuggestionsArtist> searchSuggestionsDataItemSuggestionsArtists;
    private ArrayList<SearchSuggestionsDataItemSuggestionsSong> searchSuggestionsDataItemSuggestionsSongs;
    private ArrayList<SearchSuggestionsDataItemSuggestionsPlaylist> searchSuggestionsDataItemSuggestionsPlaylists;
    private Handler handler;
    private static final int DELAY = 500;
    private Runnable searchRunnable;

    private ApiService apiService;

    private SpeechRecognizer speechRecognizer;


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
        searchRecommendViewModel = new ViewModelProvider(this).get(SearchRecommendViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.changeNavigationColor(requireActivity(), R.color.gray, true);
        Helper.changeStatusBarColor(requireActivity(), R.color.black);
        apiService = RetrofitClient.getSearchSuggestion().create(ApiService.class);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        permissionUtils = new PermissionUtils(requireContext(), this);

        initViewsSearchSuggestion(view);
        conFigViewSearchSuggestion();

        initViewsSearchHistory(view);


        initViewsSearchMulti(view);

        initAdapter();
        onClick();
        getSearchHistory();
        initViewModel();
        handler = new Handler();

        searchRunnable = () -> {
            try {
                searchSuggestion(searchView.getQuery().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void initViewModel() {
        searchRecommendViewModel.getSearchRecommendMutableLiveData().observe(getViewLifecycleOwner(), artistDetail -> {
            if (artistDetail != null) {
                updateUISearchRecommend(artistDetail);
            } else {
                getSearchRecommend();
            }
        });

        if (searchRecommendViewModel.getSearchRecommendMutableLiveData().getValue() == null) {
            getSearchRecommend();
        }
    }

    private void initViewsSearchSuggestion(View view) {
        searchView = view.findViewById(R.id.searchView);
        img_mic = view.findViewById(R.id.img_mic);
        relative_search_suggestion = view.findViewById(R.id.relative_search_suggestion);
        rv_search_suggestion = view.findViewById(R.id.rv_search_suggestion);
        linear_search_recommend = view.findViewById(R.id.linear_search_recommend);
        rv_search_recommend = view.findViewById(R.id.rv_search_recommend);
        progress_bar_loading = view.findViewById(R.id.progress_bar_loading);
        img_back = view.findViewById(R.id.img_back);
    }

    private void initViewsSearchHistory(View view) {
        view_search_history = view.findViewById(R.id.view_search_history);
        txt_search_history = view.findViewById(R.id.txt_search_history);
        txt_delete_search_history = view.findViewById(R.id.txt_delete_search_history);
        rv_search_history = view.findViewById(R.id.rv_search_history);

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
                saveSearchHistory(query);

                // Close the keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                // Clear focus from the SearchView to prevent the keyboard from reopening
                searchView.clearFocus();

                relative_search_suggestion.setVisibility(View.GONE);
                relative_search_multi.setVisibility(View.VISIBLE);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                relative_search_suggestion.setVisibility(View.VISIBLE);
                relative_search_multi.setVisibility(View.GONE);

                if (newText == null || newText.isEmpty()) {
                    initViewModel();
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
        searchSuggestionsDataItemKeyWordsItems = new ArrayList<>();
        searchSuggestionsDataItemSuggestionsArtists = new ArrayList<>();
        searchSuggestionsDataItemSuggestionsSongs = new ArrayList<>();
        searchSuggestionsDataItemSuggestionsPlaylists = new ArrayList<>();
        dataSearchRecommendArrayList = new ArrayList<>();
        dataSearchHistoryArrayList = new ArrayList<>();


        rv_search_suggestion.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchSuggestionAdapter = new SearchSuggestionAdapter(requireContext(), requireActivity(), dataSearchHistoryArrayList, searchSuggestionsDataItemKeyWordsItems, searchSuggestionsDataItemSuggestionsArtists, searchSuggestionsDataItemSuggestionsPlaylists, searchSuggestionsDataItemSuggestionsSongs);
        rv_search_suggestion.setAdapter(searchSuggestionAdapter);
        searchSuggestionAdapter.setListener(this);
        rv_search_recommend.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRecommendAdapter = new SearchRecommendAdapter(dataSearchRecommendArrayList, requireActivity(), requireContext());
        rv_search_recommend.setAdapter(searchRecommendAdapter);
        searchRecommendAdapter.setListener(this);

        rv_search_history.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        searchHistoryAdapter = new SearchHistoryAdapter(dataSearchHistoryArrayList, requireActivity(), requireContext());
        rv_search_history.setAdapter(searchHistoryAdapter);
        searchHistoryAdapter.setListener(this);

    }

    private void onClick() {
        img_back.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        img_mic.setOnClickListener(view -> {
            permissionStorage();
        });
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    searchView.setQuery(matches.get(0), true);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });
        txt_delete_search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo dialog xác nhận
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn xóa tất cả lịch sử tìm kiếm không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sharedPreferencesManager.deleteSearchHistory();
                                getSearchHistory();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void permissionStorage() {
        permissionUtils.checkAndRequestPermissions(PermissionConstants.REQUEST_CODE_RECORD_AUDIO,
                android.Manifest.permission.RECORD_AUDIO);
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
                        public void onResponse(@NonNull Call<SearchRecommend> call, @NonNull Response<SearchRecommend> response) {
                            LogUtil.d(Constants.TAG, "getSearchRecommend: " + call.request().url());
                            if (response.isSuccessful()) {
                                SearchRecommend searchRecommend = response.body();
                                if (searchRecommend != null) {
                                    searchRecommendViewModel.setSearchRecommendMutableLiveData(searchRecommend);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<SearchRecommend> call, @NonNull Throwable throwable) {

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

    private void getSearchHistory() {
        // Lấy danh sách lịch sử tìm kiếm hiện tại
        ArrayList<DataSearchRecommend> searchHistory = sharedPreferencesManager.restoreSearchHistory();

        // Kiểm tra nếu danh sách lịch sử tìm kiếm là null
        if (searchHistory == null) {
            view_search_history.setVisibility(View.GONE);
            txt_search_history.setVisibility(View.GONE);
            rv_search_history.setVisibility(View.GONE);
        } else {
            dataSearchHistoryArrayList = searchHistory;
            searchHistoryAdapter.setFilterList(dataSearchHistoryArrayList);
            view_search_history.setVisibility(View.VISIBLE);
            txt_search_history.setVisibility(View.VISIBLE);
            rv_search_history.setVisibility(View.VISIBLE);
        }
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
                        if (searchSuggestions != null) {
                            updateUISearchSuggestion(searchSuggestions, query);
                        }

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

    private void updateUISearchRecommend(SearchRecommend searchRecommend) {
        ArrayList<DataSearchRecommend> data = searchRecommend.getData();
        if (!data.isEmpty()) {
            dataSearchRecommendArrayList = data;
            searchRecommendAdapter.setFilterList(dataSearchRecommendArrayList);
        }
    }

    private void updateUISearchSuggestion(SearchSuggestions searchSuggestions, String keyword) {
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
        ArrayList<DataSearchRecommend> filteredData = new ArrayList<>();
        String normalizedKeyword = Helper.normalizeString(keyword);
        for (DataSearchRecommend dataSearchRecommend : dataSearchHistoryArrayList) {
            if (Helper.normalizeString(dataSearchRecommend.getKeyword()).contains(normalizedKeyword)) {
                filteredData.add(dataSearchRecommend);
            }
        }

        searchSuggestionAdapter.setFilterList(filteredData, searchSuggestionsDataItemKeyWordsItems, searchSuggestionsDataItemSuggestionsArtists, searchSuggestionsDataItemSuggestionsPlaylists, searchSuggestionsDataItemSuggestionsSongs);

        linear_search_recommend.setVisibility(View.GONE);
        progress_bar_loading.setVisibility(View.GONE);
        rv_search_suggestion.setVisibility(View.VISIBLE);
    }

    private void saveSearchHistory(String keyword) {
        sharedPreferencesManager.saveSearchHistory(keyword);
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
        saveSearchHistory(keyword);

        relative_search_suggestion.setVisibility(View.GONE);
        relative_search_multi.setVisibility(View.VISIBLE);
        // Close the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
        saveSearchHistory(keyword);
        searchView.setQuery(keyword, true);
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
        saveSearchHistory(keyword);
        searchView.setQuery(keyword, true);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đó...");

        try {
            startActivityForResult(intent, PermissionConstants.REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PermissionConstants.REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    saveSearchHistory(result.get(0));
                    searchView.setQuery(result.get(0), true);
                }
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PermissionConstants.REQUEST_CODE_RECORD_AUDIO) {
            promptSpeechInput();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PermissionConstants.REQUEST_CODE_RECORD_AUDIO) {
            Toast.makeText(requireContext(), "Quyền bị từ chối. Không thể lắng nghe.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSearchHistory();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiverTabLayout, new IntentFilter("tab_layout_position"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiverTabLayout);
    }
}