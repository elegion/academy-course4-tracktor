package com.elegion.tracktor.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.elegion.tracktor.api.DistanceMatrixApi;

import javax.inject.Inject;

public class CounterViewModelFactory implements ViewModelProvider.Factory {

    private DistanceMatrixApi api;

    public CounterViewModelFactory(DistanceMatrixApi api) {
        this.api = api;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CounterViewModel.class)) {
            return (T) new CounterViewModel(api);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}