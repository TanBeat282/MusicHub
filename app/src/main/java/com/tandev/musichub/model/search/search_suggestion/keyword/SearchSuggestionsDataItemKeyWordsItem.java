package com.tandev.musichub.model.search.search_suggestion.keyword;

import java.io.Serializable;

public class SearchSuggestionsDataItemKeyWordsItem implements Serializable {
    private int type;
    private String keyword;
    private String suggestType;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSuggestType() {
        return suggestType;
    }

    public void setSuggestType(String suggestType) {
        this.suggestType = suggestType;
    }
}
