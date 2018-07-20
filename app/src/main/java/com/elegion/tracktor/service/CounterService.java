package com.elegion.tracktor.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StartTrackEvent;
import com.elegion.tracktor.event.StopBtnClickedEvent;
import com.elegion.tracktor.event.StopTrackEvent;
import com.elegion.tracktor.event.UpdateRouteEvent;
import com.elegion.tracktor.event.UpdateTimerEvent;
import com.elegion.tracktor.ui.map.MainActivity;
import com.elegion.tracktor.util.StringUtil;
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

public class CounterService extends Service {

    public static final String CHANNEL_ID = "counter_service";
    public static final String CHANNEL_NAME = "Counter Service";
    public static final int NOTIFICATION_ID = 101;
    public static final int UPDATE_INTERVAL = 15_000;
    public static final int UPDATE_FASTEST_INTERVAL = 5_000;
    public static final int UPDATE_MIN_DISTANCE = 20;
    public static final int REQUEST_CODE_LAUNCH = 0;

    private double mDistance;
    private Disposable mTimerDisposable;
    private List<LatLng> mRoute = new ArrayList<>();

    private Location mLastLocation;
    private LatLng mLastPosition;

    private NotificationCompat.Builder mNotificationBuilder;
    private long mShutDownDuration;

    private FusedLocationProviderClient mFusedLocationClient;
    private NotificationManager mNotificationManager;
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {

                if (isFirstPoint()) {
                    addPointToRoute(locationResult.getLastLocation());
                    EventBus.getDefault().post(new StartTrackEvent(mLastPosition));

                } else {

                    Location newLocation = locationResult.getLastLocation();
                    LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

                    if (positionChanged(newPosition)) {
                        mRoute.add(newPosition);
                        LatLng prevPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        mDistance += SphericalUtil.computeDistanceBetween(prevPosition, newPosition);
                        EventBus.getDefault().post(new AddPositionToRouteEvent(prevPosition, newPosition, mDistance));
                    }

                    mLastLocation = newLocation;
                    mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }
            }
        }
    };

    private boolean positionChanged(LatLng newPosition) {
        return mLastLocation.getLongitude() != newPosition.longitude || mLastLocation.getLatitude() != newPosition.latitude;
    }

    private void addPointToRoute(Location lastLocation) {
        mLastLocation = lastLocation;
        mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mRoute.add(mLastPosition);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel();
            }

            Notification notification = buildNotification();
            startForeground(NOTIFICATION_ID, notification);

            final LocationRequest locationRequest = new LocationRequest()
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(UPDATE_FASTEST_INTERVAL)
                    .setSmallestDisplacement(UPDATE_MIN_DISTANCE)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);

            startTimer();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            mShutDownDuration = Long.valueOf(preferences.getString(getString(R.string.pref_key_shutdown), "-1"));

        } else {
            Toast.makeText(this, R.string.permissions_denied, Toast.LENGTH_SHORT).show();
        }

    }

    private void startTimer() {
        mTimerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(CounterService.this::onTimerUpdate);
    }

    private void onTimerUpdate(long totalSeconds) {
        EventBus.getDefault().post(new UpdateTimerEvent(totalSeconds, mDistance));

        Notification notification = buildNotification(StringUtil.getTimeText(totalSeconds), StringUtil.getDistanceText(mDistance));
        mNotificationManager.notify(NOTIFICATION_ID, notification);

        if (mShutDownDuration != -1 && totalSeconds == mShutDownDuration) {
            EventBus.getDefault().post(new StopBtnClickedEvent());
            //configure btns state
            //from notification
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().post(new StopTrackEvent(mRoute));

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mTimerDisposable.dispose();

        stopForeground(true);

        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification buildNotification() {
        return buildNotification("", "");
    }

    private Notification buildNotification(String time, String distance) {
        if (mNotificationBuilder == null) {
            configureNotificationBuilder();
        }

        String message = getString(R.string.notify_info, time, distance);

        return mNotificationBuilder
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();

    }

    private void configureNotificationBuilder() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, REQUEST_CODE_LAUNCH, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_white_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.route_active))
                .setVibrate(new long[]{0})
                .setColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (mNotificationManager != null && mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager.createNotificationChannel(chan);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetRoute(GetRouteEvent event) {
        EventBus.getDefault().post(new UpdateRouteEvent(mRoute, mDistance));
    }

    public boolean isFirstPoint() {
        return mRoute.size() == 0 && mLastLocation == null && mLastPosition == null;
    }

}
