package com.elegion.tracktor.service;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteClickEvent;
import com.elegion.tracktor.event.StopRouteEvent;
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

public class CounterService extends IntentService {

    public static final String CHANNEL_ID = "counter_service";
    public static final String CHANNEL_NAME = "Counter Service";
    public static final int NOTIFICATION_ID = 101;
    public static final int UPDATE_INTERVAL = 5000;
    public static final int UPDATE_FASTEST_INTERVAL = 2000;
    public static final int UPDATE_MIN_DISTANCE = 20;

    public CounterService() {
        super("CounterService");
    }

    public CounterService(String name) {
        super(name);
    }

    private long mTime;
    private double mDistance;
    private Disposable mTimerDisposable;
    private List<LatLng> mRoute = new ArrayList<>();

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

                if (isRouteStarted
                        && mLastLocation != null
                        && (mLastLocation.getLongitude() != newPosition.longitude
                        || mLastLocation.getLatitude() != newPosition.latitude)) {
                    LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mRoute.add(newPosition);
                    mDistance += SphericalUtil.computeDistanceBetween(lastPosition, newPosition);

                    EventBus.getDefault().post(new AddPositionToRouteEvent(lastPosition, newPosition, mDistance));
                    setNotification(StringUtil.getTimeText(mTime), StringUtil.getDistanceText(mDistance));
                }

                mLastLocation = newLocation;
                mLastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }
    };

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        setNotification("", "");
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

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

        onStartRouteClick();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setNotification(String time, String distance) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = time.isEmpty() ? "Мы не следим за вами!" : "Мы следим за вами!\nВремя: " + time + "\nРасстояние: " + distance;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_white_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Маршрут активен!")
                .setContentText("Мы следим за вами!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setVibrate(new long[]{0})
                .setColor(ContextCompat.getColor(this, R.color.colorAccent));

        Notification notification = builder.build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(chan);
    }

    private void onTimerUpdate(long totalSeconds) {
        mTime = totalSeconds;

        EventBus.getDefault().post(new UpdateTimerEvent(totalSeconds));
        setNotification(StringUtil.getTimeText(mTime), StringUtil.getDistanceText(mDistance));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetRoute(GetRouteEvent event) {
        EventBus.getDefault().post(new UpdateRouteEvent(mRoute, mDistance));
    }

    //@Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRouteClick(/*StartRouteClickEvent event*/) {
        isRouteStarted = true;
        if (mLastPosition != null) {
            mRoute.add(mLastPosition);
        }
        EventBus.getDefault().post(new StartRouteEvent(mLastPosition));
        mTimerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onTimerUpdate);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRouteClick(StopRouteClickEvent event) {
        EventBus.getDefault().post(new StopRouteEvent(mDistance, mTime, new ArrayList<>(mRoute)));
        setNotification("", "");
        isRouteStarted = false;
        mRoute.clear();
        mDistance = 0;
        mTimerDisposable.dispose();
        stopSelf();
    }
}
