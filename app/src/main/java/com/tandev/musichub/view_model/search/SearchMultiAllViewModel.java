package com.tandev.musichub.view_model.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.playlist.Playlist;
import com.tandev.musichub.model.search.search_multil.SearchMulti;

public class SearchMultiAllViewModel extends ViewModel {
    private MutableLiveData<SearchMulti> searchMultiMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<SearchMulti> getSearchMultiMutableLiveData() {
        return searchMultiMutableLiveData;
    }

    public void setSearchMultiMutableLiveData(SearchMulti searchMulti) {
        searchMultiMutableLiveData.setValue(searchMulti);
    }
}
