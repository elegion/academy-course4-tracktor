package com.elegion.tracktor.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

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
    private Disposable timerDisposable;

    public void startTimer() {
        startEnabled.postValue(false);
        stopEnabled.postValue(true);
        timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(seconds -> onTimerUpdate(seconds.intValue()));
    }

    public void stopTimer() {
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

    @Override
    protected void onCleared() {
        timerDisposable.dispose();
        timerDisposable = null;
        super.onCleared();
    }
}