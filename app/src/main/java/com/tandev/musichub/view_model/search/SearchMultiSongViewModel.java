package com.tandev.musichub.view_model.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.search.search_multil.SearchMulti;
import com.tandev.musichub.model.search.song.SearchSong;

public class SearchMultiSongViewModel extends ViewModel {
    private MutableLiveData<SearchSong> searchSongMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<SearchSong> getSearchSongMutableLiveData() {
        return searchSongMutableLiveData;
    }

    public void setSearchSongMutableLiveData(SearchSong searchSong) {
        searchSongMutableLiveData.setValue(searchSong);
    }
}
