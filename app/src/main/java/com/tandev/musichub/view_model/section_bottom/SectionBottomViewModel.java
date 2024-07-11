package com.tandev.musichub.view_model.section_bottom;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.section_bottom.SectionBottom;
import com.tandev.musichub.model.top100.Top100;


public class SectionBottomViewModel extends ViewModel {
    private MutableLiveData<SectionBottom> sectionBottomMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<SectionBottom> getSectionBottomMutableLiveData() {
        return sectionBottomMutableLiveData;
    }

    public void setSectionBottomMutableLiveData(SectionBottom sectionBottom) {
        sectionBottomMutableLiveData.setValue(sectionBottom);
    }
}
