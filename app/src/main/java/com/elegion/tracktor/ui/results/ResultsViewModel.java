package com.elegion.tracktor.ui.results;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.model.Track;

import java.util.List;

/**
 * @author Azret Magometov
 */
public class ResultsViewModel extends ViewModel {

    private IRepository mRepository;

    private MutableLiveData<List<Track>> mTracks = new MutableLiveData<>();

    public ResultsViewModel(IRepository repository) {
        mRepository = repository;
    }

    public void loadTracks() {
        if (mTracks.getValue() == null || mTracks.getValue().isEmpty()) {
            mTracks.postValue(mRepository.getAll());
        }
    }

    public MutableLiveData<List<Track>> getTracks() {
        return mTracks;
    }
}
