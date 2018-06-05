package com.elegion.tracktor.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.elegion.tracktor.R;
import com.elegion.tracktor.preferences.PreferenceActivity;
import com.elegion.tracktor.results.ResultsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.tr_pref, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_results:
                ResultsActivity.start(this);
                return true;
            case R.id.mi_preferences:
                PreferenceActivity.start(this);
                return true;
            case R.id.mi_about:
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
}
