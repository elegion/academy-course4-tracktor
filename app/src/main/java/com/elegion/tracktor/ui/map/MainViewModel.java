package com.elegion.tracktor.ui.map;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();

    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mDistanceText = new MutableLiveData<>();

    private long mDurationRaw;
    private double mDistanceRaw;

    private RealmRepository mRealmRepository;

    public MainViewModel() {
        EventBus.getDefault().register(this);
        startEnabled.setValue(true);
        stopEnabled.setValue(false);
        mRealmRepository = new RealmRepository();
    }

    public void switchButtons() {
        startEnabled.setValue(!startEnabled.getValue());
        stopEnabled.setValue(!stopEnabled.getValue());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTimer(UpdateTimerEvent event) {
        mTimeText.postValue(StringUtil.getTimeText(event.getSeconds()));
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
        mDurationRaw = event.getSeconds();
        mDistanceRaw = event.getDistance();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event) {
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
        mDistanceRaw = event.getDistance();

        startEnabled.postValue(false);
        stopEnabled.postValue(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {
        mDistanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
    }

    public MutableLiveData<String> getTimeText() {
        return mTimeText;
    }

    public MutableLiveData<Boolean> getStartEnabled() {
        return startEnabled;
    }

    public MutableLiveData<Boolean> getStopEnabled() {
        return stopEnabled;
    }

    public MutableLiveData<String> getDistanceText() {
        return mDistanceText;
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }

    public void clear() {
        mTimeText.setValue("");
        mDistanceText.setValue("");
    }

    public long saveResults(String base54image) {

        return mRealmRepository.createAndInsertTrackFrom(mDurationRaw, mDistanceRaw, base54image);
    }
}