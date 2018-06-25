package com.elegion.tracktor.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.StartRouteClickEvent;
import com.elegion.tracktor.event.StopRouteClickEvent;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

    public static final int UPDATE_INTERVAL = 5000;
    public static final int UPDATE_FASTEST_INTERVAL = 2000;
    public static final int UPDATE_MIN_DISTANCE = 20;

    public CounterService(String name) {
        super(name);
    }

    private List<LatLng> mRoute = new ArrayList<>();
    private long mTime;
    private double mDistance;
    private Disposable mTimerDisposable;

    private boolean isRouteStarted;
    private Location mLastLocation;
    private LatLng mLastPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest = new LocationRequest();
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                Location newLocation = locationResult.getLastLocation();
                LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

                if (isRouteStarted && mLastLocation != null) {
                    LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mRoute.add(newPosition);
                    mDistance += SphericalUtil.computeDistanceBetween(lastPosition, newPosition);
                    //if (isAppRunning()) {
                    EventBus.getDefault().post(new AddPositionToRouteEvent(lastPosition, newPosition, mDistance));
                    //}
                }

                mLastLocation = newLocation;
                mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
    };

    private boolean isAppRunning() {
        return true;
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(UPDATE_MIN_DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } else {
            Toast.makeText(this, "Вы не дали разрешения!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetRoute(GetRouteEvent event) {
        if (mRoute.size() >= 2) {
            EventBus.getDefault().post(new UpdateRouteEvent(mRoute, mDistance));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRouteClick(StartRouteClickEvent event) {
        isRouteStarted = true;
        EventBus.getDefault().post(new StartRouteEvent(mLastPosition));
        mTimerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTimerUpdate);
    }

    private void onTimerUpdate(long totalSeconds) {
        mTime = totalSeconds;

        EventBus.getDefault().post(new UpdateTimerEvent(totalSeconds));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRouteClick(StopRouteClickEvent event) {
        EventBus.getDefault().post(new StopRouteEvent(mDistance, mTime, new ArrayList<>(mRoute)));
        isRouteStarted = false;
        mRoute.clear();
        mDistance = 0;
        mTimerDisposable.dispose();
    }
}
