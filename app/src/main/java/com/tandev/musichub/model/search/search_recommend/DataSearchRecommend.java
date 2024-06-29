package com.tandev.musichub.model.search.search_recommend;

import java.io.Serializable;

public class DataSearchRecommend implements Serializable {
    private String keyword;
    private String link;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
