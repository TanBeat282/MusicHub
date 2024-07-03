package com.tandev.musichub.model.search.search_recommend;

import java.io.Serializable;
import java.util.Objects;

public class DataSearchRecommend implements Serializable {
    private String keyword;
    private String link;


    public DataSearchRecommend(String keyword, String link) {
        this.keyword = keyword;
        this.link = link;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DataSearchRecommend that = (DataSearchRecommend) obj;
        return Objects.equals(keyword, that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyword);
    }
}
