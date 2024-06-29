package com.tandev.musichub.model.chart.home.home_new.option;

import java.io.Serializable;

public class HomeOption implements Serializable {
    private boolean hideTitle;
    private boolean autoSlider;
    private boolean hideArrow;

    public boolean isHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(boolean hideTitle) {
        this.hideTitle = hideTitle;
    }

    public boolean isAutoSlider() {
        return autoSlider;
    }

    public void setAutoSlider(boolean autoSlider) {
        this.autoSlider = autoSlider;
    }

    public boolean isHideArrow() {
        return hideArrow;
    }

    public void setHideArrow(boolean hideArrow) {
        this.hideArrow = hideArrow;
    }
}
