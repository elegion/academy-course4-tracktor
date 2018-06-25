package com.elegion.tracktor.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.event.ClickStartRouteEvent;
import com.elegion.tracktor.event.ClickStopRouteEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.elegion.tracktor.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CounterViewModel extends ViewModel {

    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();
    private MutableLiveData<String> timeText = new MutableLiveData<>();
    private MutableLiveData<String> distanceText = new MutableLiveData<>();

    public CounterViewModel() {
        EventBus.getDefault().register(this);
    }

    public void startTimer() {
        EventBus.getDefault().post(new ClickStartRouteEvent());
        timeText.postValue("");
        distanceText.postValue("");
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
    }

    public void stopTimer() {
        startEnabled.postValue(true);
        stopEnabled.postValue(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTimer(UpdateTimerEvent event) {
        EventBus.getDefault().post(new ClickStopRouteEvent());
        timeText.postValue(StringUtil.getTimeText(event.getSeconds()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event) {
        distanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
    }

    public MutableLiveData<String> getTimeText() {
        return timeText;
    }

    public MutableLiveData<Boolean> getStartEnabled() {
        return startEnabled;
    }

    public MutableLiveData<Boolean> getStopEnabled() {
        return stopEnabled;
    }

    public MutableLiveData<String> getDistanceText() {
        return distanceText;
    }

    @Override
    protected void onCleared() {
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }
}