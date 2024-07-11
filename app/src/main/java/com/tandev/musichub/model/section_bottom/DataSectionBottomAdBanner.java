package com.tandev.musichub.model.section_bottom;

import com.tandev.musichub.model.playlist.DataPlaylist;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSectionBottomAdBanner implements DataSectionBottom, Serializable {
    private static final long serialVersionUID = 1L;
    private String sectionType;
    private String adId;
    private String pageType;

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }
}
