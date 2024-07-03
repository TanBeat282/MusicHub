package com.tandev.musichub.view_model.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.search.search_multil.SearchMulti;
import com.tandev.musichub.model.search.search_recommend.SearchRecommend;

public class SearchRecommendViewModel extends ViewModel {
    private MutableLiveData<SearchRecommend> searchRecommendMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<SearchRecommend> getSearchRecommendMutableLiveData() {
        return searchRecommendMutableLiveData;
    }

    public void setSearchRecommendMutableLiveData(SearchRecommend searchRecommend) {
        searchRecommendMutableLiveData.setValue(searchRecommend);
    }
}
