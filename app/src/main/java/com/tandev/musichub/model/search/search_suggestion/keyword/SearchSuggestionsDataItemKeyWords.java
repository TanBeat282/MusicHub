package com.tandev.musichub.model.search.search_suggestion.keyword;

import com.tandev.musichub.model.search.search_suggestion.SearchSuggestionsDataItem;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchSuggestionsDataItemKeyWords implements SearchSuggestionsDataItem, Serializable {
    ArrayList<SearchSuggestionsDataItemKeyWordsItem> keywords;

    public ArrayList<SearchSuggestionsDataItemKeyWordsItem> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<SearchSuggestionsDataItemKeyWordsItem> keywords) {
        this.keywords = keywords;
    }
}
