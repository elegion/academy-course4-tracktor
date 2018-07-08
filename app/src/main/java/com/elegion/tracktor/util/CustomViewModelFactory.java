package com.elegion.tracktor.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.ui.results.ResultsViewModel;

/**
 * @author Azret Magometov
 */
public class CustomViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private IRepository mRepository;

    public CustomViewModelFactory(IRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ResultsViewModel.class)) {
            return (T) new ResultsViewModel(mRepository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }

}
