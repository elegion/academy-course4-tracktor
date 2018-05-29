package com.elegion.tracktor.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.elegion.tracktor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int LOCATION_REQUEST_CODE = 99;

    @BindView(R.id.mapContainer) FrameLayout mapContainer;
    @BindView(R.id.counterContainer) FrameLayout counterContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.counterContainer, new com.elegion.tracktor.ui.CounterFragment())
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

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);
            Toast.makeText(this, "Разрешения уже получены!", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Запрос разрешений на получение местоположения")
                    .setMessage("Нам необходимо знать Ваше местоположение, чтобы приложение работало.")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_REQUEST_CODE);
                        }
                    })
                    .create()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //mMap.setMyLocationEnabled(true);
                Toast.makeText(this, "Вы дали разрешения!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(this, "Вы не дали разрешения!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
