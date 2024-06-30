package com.tandev.musichub.view_model.top100;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tandev.musichub.model.top100.Top100;


public class Top100ViewModel extends ViewModel {
    private MutableLiveData<Top100> top100MutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Top100> getTop100MutableLiveData() {
        return top100MutableLiveData;
    }

    public void setTop100MutableLiveData(Top100 top100) {
        top100MutableLiveData.setValue(top100);
    }
}
