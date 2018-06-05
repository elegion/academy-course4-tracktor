package com.elegion.tracktor.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elegion.tracktor.BaseActivity;
import com.elegion.tracktor.R;

public class PreferenceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        if (savedInstanceState == null) {
            changeFragment(MainPreferences.newInstance());
        }
    }

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, PreferenceActivity.class);
        activity.startActivity(intent);
    }
}
