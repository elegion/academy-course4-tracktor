package com.elegion.tracktor.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.StopRouteClickEvent;
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
        //EventBus.getDefault().post(new StartRouteClickEvent());
        timeText.postValue("");
        distanceText.postValue("");
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
    }

    public void stopTimer() {
        EventBus.getDefault().post(new StopRouteClickEvent());
        startEnabled.postValue(true);
        stopEnabled.postValue(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRouteClickTimer(StopRouteClickEvent event) {
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateTimer(UpdateTimerEvent event) {
        timeText.postValue(StringUtil.getTimeText(event.getSeconds()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateRoute(UpdateRouteEvent event) {
        distanceText.postValue(StringUtil.getDistanceText(event.getDistance()));
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {
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