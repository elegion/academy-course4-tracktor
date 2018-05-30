package com.elegion.tracktor.ui;

import android.Manifest;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.elegion.tracktor.ChangeLocationListener;
import com.elegion.tracktor.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        LocationListener {

    public static final int LOCATION_REQUEST_CODE = 99;
    public static final int UPDATE_CURRENT_LOCATION_MIN_TIME_MS = 2000;
    public static final int UPDATE_CURRENT_LOCATION_MIN_DISTANCE = 10;
    public static final int DEFAULT_ZOOM = 15;

    @BindView(R.id.counterContainer) FrameLayout counterContainer;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private String mProvider;
    private LocationListener mLocationListener = new ChangeLocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mMap != null) {
                mMap.clear();
                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title("Current Position"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.counterContainer, new CounterFragment())
                    .commit();
        }
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
            mLocationManager.requestLocationUpdates(
                    mProvider,
                    UPDATE_CURRENT_LOCATION_MIN_TIME_MS,
                    UPDATE_CURRENT_LOCATION_MIN_DISTANCE,
                    mLocationListener);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Запрос разрешений на получение местоположения")
                    .setMessage("Нам необходимо знать Ваше местоположение, чтобы приложение работало")
                    .setPositiveButton("ОК", (dialogInterface, i) ->
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE))
                    .create()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
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

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            Toast toast = Toast.makeText(getApplicationContext(), "Включите Интернет и перезайдите в приложение!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            mProvider = mLocationManager.getBestProvider(new Criteria(), true);
            initMap();
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
