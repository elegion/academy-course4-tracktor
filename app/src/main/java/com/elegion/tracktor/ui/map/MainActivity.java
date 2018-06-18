package com.elegion.tracktor.ui.map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.AddPositionToRouteEvent;
import com.elegion.tracktor.event.SetStartPositionToRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.elegion.tracktor.ui.results.ResultsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    public static final int LOCATION_REQUEST_CODE = 99;
    public static final int UPDATE_INTERVAL = 5000;
    public static final int UPDATE_FASTEST_INTERVAL = 2000;
    public static final int UPDATE_MIN_DISTANCE = 20;
    public static final int DEFAULT_ZOOM = 15;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private boolean isRouteStarted;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest = new LocationRequest();
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null && mMap != null) {
                Location newLocation = locationResult.getLastLocation();
                LatLng newPosition = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                if (isRouteStarted && mLastLocation != null) {
                    EventBus.getDefault().post(new AddPositionToRouteEvent(newPosition));
                    LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addPolyline(new PolylineOptions().add(lastPosition, newPosition));
                } else if (!isRouteStarted) {
                    EventBus.getDefault().post(new SetStartPositionToRouteEvent(newPosition));
                }
                mLastLocation = newLocation;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPosition, DEFAULT_ZOOM));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(UPDATE_MIN_DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (savedInstanceState == null) {
            mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMapFragment.setRetainInstance(true);
            mMapFragment.getMapAsync(this);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.counterContainer, new CounterFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRoute(StartRouteEvent event) {
        mMap.clear();
        LatLng lastPosition = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(lastPosition).title(getString(R.string.start)));
        isRouteStarted = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRoute(StopRouteEvent event) {
        isRouteStarted = false;

        List<LatLng> route = event.getRoute();
        mMap.addMarker(new MarkerOptions().position(route.get(route.size() - 1)).title(getString(R.string.end)));

        takeMapScreenshot(route, bitmap -> ResultsActivity.start(this, event.getDistance(), event.getTime(), bitmap));
    }

    private void takeMapScreenshot(List<LatLng> route, GoogleMap.SnapshotReadyCallback snapshotCallback) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : route) {
            builder.include(point);
        }
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
        mMap.moveCamera(cu);

        mMap.snapshot(snapshotCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionStatistic:
                //todo add logic
                break;
            case R.id.actionSettings:
                //todo add logic
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Запрос разрешений на получение местоположения")
                    .setMessage("Нам необходимо знать Ваше местоположение, чтобы приложение работало")
                    .setPositiveButton("ОК", (dialogInterface, i) ->
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, LOCATION_REQUEST_CODE))
                    .create()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 2 &&
                    permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Вы не дали разрешения!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
