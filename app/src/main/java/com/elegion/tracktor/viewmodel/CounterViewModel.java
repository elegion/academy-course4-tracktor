package com.elegion.tracktor.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetFullRouteEvent;
import com.elegion.tracktor.event.SetStartPositionToRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.elegion.tracktor.util.StringUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CounterViewModel extends ViewModel {

    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();
    private MutableLiveData<String> timeText = new MutableLiveData<>();
    private MutableLiveData<String> distanceText = new MutableLiveData<>();

    private Disposable timerDisposable;
    private List<LatLng> route = new ArrayList<>();
    private double distance;

    public CounterViewModel() {
        EventBus.getDefault().register(this);
    }

    public void startTimer() {
        EventBus.getDefault().post(new StartRouteEvent());
        timeText.postValue("");
        distanceText.postValue("");
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(seconds -> onTimerUpdate(seconds.intValue()));
    }

    public void stopTimer() {
        EventBus.getDefault().post(new StopRouteEvent());
        LatLng lastLocation = route.get(route.size() - 1);
        route.clear();
        route.add(lastLocation);
        distance = 0;
        startEnabled.postValue(true);
        stopEnabled.postValue(false);
        timerDisposable.dispose();
    }

    private void onTimerUpdate(int totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        timeText.setValue(String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds));
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
        route.clear();
        distance = 0.0;
        timerDisposable.dispose();
        timerDisposable = null;
        EventBus.getDefault().unregister(this);
        super.onCleared();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFullRoute(GetFullRouteEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {
        route.add(event.getPosition());
        if (route.size() >= 2) {
            LatLng firstPosition = route.get(route.size() - 2);
            LatLng lastPosition = event.getPosition();
            double computedDistance = SphericalUtil.computeDistanceBetween(firstPosition, lastPosition);
            distance += computedDistance;
            distanceText.postValue(StringUtil.getDistanceText(distance));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetStartPositionToRoute(SetStartPositionToRouteEvent event) {
        route.clear();
        route.add(event.getPosition());
    }
}