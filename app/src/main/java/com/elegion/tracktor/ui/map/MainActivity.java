package com.elegion.tracktor.ui.map;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.StartBtnClickedEvent;
import com.elegion.tracktor.event.StopBtnClickedEvent;
import com.elegion.tracktor.service.CounterService;
import com.elegion.tracktor.ui.preferences.PreferenceActivity;
import com.elegion.tracktor.ui.results.ResultsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_STORAGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_DENIED) {
            showRequestRationaleDialog();
        } else {
            configureMap();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartBtnClicked(@NonNull StartBtnClickedEvent event) {
        Intent serviceIntent = getServiceIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopBtnClicked(@NonNull StopBtnClickedEvent event) {
        stopService(getServiceIntent());
    }

    private void showRequestRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.permissions_request_title)
                .setMessage(R.string.permissions_request_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) ->
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_LOCATION_STORAGE))
                .create()
                .show();
    }

    private void configureMap() {
        TrackMapFragment map = getTrackMapFragment();
        map.configure();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_STORAGE) {
            if (permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PERMISSION_DENIED) {
                Toast.makeText(this, R.string.back_off, Toast.LENGTH_LONG).show();
                finish();
            } else {
                configureMap();
            }
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
                ResultsActivity.start(this, ResultsActivity.LIST_ID);
                return true;
            case R.id.actionSettings:
                PreferenceActivity.start(this);
                return true;
            case R.id.actionAbout:
                launchAboutScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAboutScreen() {
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowTitle(true)
                .enableUrlBarHiding()
                .build();

        intent.launchUrl(this, Uri.parse("https://www.e-legion.com"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "from notify", Toast.LENGTH_SHORT).show();
    }

    @NonNull
    private Intent getServiceIntent() {
        return new Intent(this, CounterService.class);
    }

    private TrackMapFragment getTrackMapFragment() {
        return (TrackMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

}
