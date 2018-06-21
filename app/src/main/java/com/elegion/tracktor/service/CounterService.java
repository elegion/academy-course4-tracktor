package com.elegion.tracktor.service;

import android.Manifest;
import android.app.IntentService;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetFullRouteEvent;
import com.elegion.tracktor.event.SetStartPositionToRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.elegion.tracktor.util.StringUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CounterService extends IntentService {

    public static final int LOCATION_REQUEST_CODE = 99;
    public static final int UPDATE_INTERVAL = 5000;
    public static final int UPDATE_FASTEST_INTERVAL = 2000;
    public static final int UPDATE_MIN_DISTANCE = 20;
    public static final int DEFAULT_ZOOM = 15;

    public CounterService(String name) {
        super(name);
    }

    private boolean startEnabled;
    private boolean stopEnabled;
    private String timeText = new MutableLiveData<>();
    private String distanceText = new MutableLiveData<>();

    private List<LatLng> route = new ArrayList<>();
    private long time;
    private double distance;
    private Disposable timerDisposable;

    private boolean isRouteStarted;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest = new LocationRequest();
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                Location newLocation = locationResult.getLastLocation();
                LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                if (isRouteStarted && mLastLocation != null) {
                    EventBus.getDefault().post(new AddPositionToRouteEvent(newPosition));
                    LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                } else if (!isRouteStarted) {
                    EventBus.getDefault().post(new SetStartPositionToRouteEvent(newPosition));
                }
                mLastLocation = newLocation;
            }
        }
    };

    private void onTimerUpdate(long totalSeconds) {
        time = totalSeconds;
        timeText.setValue(StringUtil.getTimeText(totalSeconds));
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                .subscribe(this::onTimerUpdate);
    }

    public void stopTimer() {
        EventBus.getDefault().post(new StopRouteEvent(distance, time, new ArrayList<>(route)));
        LatLng lastLocation = route.get(route.size() - 1);
        route.clear();
        route.add(lastLocation);
        distance = 0;
        startEnabled.postValue(true);
        stopEnabled.postValue(false);
        timerDisposable.dispose();
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } else {
            Toast.makeText(this, "Вы не дали разрешения!", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRoute(StartRouteEvent event) {
        EventBus.getDefault().post(new StartRouteEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRoute(StopRouteEvent event) {
        EventBus.getDefault().post(new StopRouteEvent(distance, time, new ArrayList<>(route)));
        LatLng lastLocation = route.get(route.size() - 1);
        route.clear();
        route.add(lastLocation);
        distance = 0;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFullRoute(GetFullRouteEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddPositionToRoute(AddPositionToRouteEvent event) {
        route.add(event.getNewPosition());
        if (route.size() >= 2) {
            LatLng firstPosition = route.get(route.size() - 2);
            LatLng lastPosition = event.getLastPosition();
            double computedDistance = SphericalUtil.computeDistanceBetween(firstPosition, lastPosition);
            distance += computedDistance;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetStartPositionToRoute(SetStartPositionToRouteEvent event) {
        route.clear();
        route.add(event.getPosition());
    }
}
