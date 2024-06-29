package com.tandev.musichub.model.search.search_suggestion.suggestion;

import com.tandev.musichub.model.search.search_suggestion.SearchSuggestionsDataItem;

import java.io.Serializable;
import java.util.ArrayList;

public class  SearchSuggestionsDataItemSuggestions implements SearchSuggestionsDataItem,  Serializable{
    ArrayList<SearchSuggestionsDataItemSuggestionsItem> suggestions;

    public ArrayList<SearchSuggestionsDataItemSuggestionsItem> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<SearchSuggestionsDataItemSuggestionsItem> suggestions) {
        this.suggestions = suggestions;
    }
}
