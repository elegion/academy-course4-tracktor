package com.elegion.tracktor.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.elegion.tracktor.BuildConfig;
import com.elegion.tracktor.api.DistanceMatrixApi;
import com.elegion.tracktor.api.DistanceMatrixResponse;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetFullRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.google.android.gms.maps.model.LatLng;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CounterViewModel extends ViewModel {

    private MutableLiveData<Boolean> startEnabled = new MutableLiveData<>();
    private MutableLiveData<Boolean> stopEnabled = new MutableLiveData<>();
    private MutableLiveData<String> timeText = new MutableLiveData<>();
    private MutableLiveData<String> distanceText = new MutableLiveData<>();

    private Disposable timerDisposable;
    private Disposable distanceDisposable;
    private List<LatLng> route = new ArrayList<>();
    private DistanceMatrixApi api;

    public CounterViewModel(DistanceMatrixApi api) {
        EventBus.getDefault().register(this);
        this.api = api;
    }

    public void startTimer() {
        EventBus.getDefault().post(new StartRouteEvent());
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(seconds -> onTimerUpdate(seconds.intValue()));
    }

    public void stopTimer() {
        EventBus.getDefault().post(new StopRouteEvent());
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
        api.getDistanceForRoute(getFormatedRoute(route), getFormatedLastPosition(route.get(route.size() - 1)), BuildConfig.GOOGLE_MAPS_KEY)
                .enqueue(new Callback<DistanceMatrixResponse>() {
                    @Override
                    public void onResponse(Call<DistanceMatrixResponse> call, Response<DistanceMatrixResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<DistanceMatrixResponse> call, Throwable t) {

                    }
                });
    }

    private String getFormatedRoute(List<LatLng> route) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < route.size(); i++) {
            LatLng position = route.get(i);
            result.append(String.valueOf(position.latitude)).append("|").append(String.valueOf(position.longitude));
            if (i != route.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    private String getFormatedLastPosition(LatLng position) {
        return String.valueOf(position.latitude) + "|" + String.valueOf(position.longitude);
    }
}