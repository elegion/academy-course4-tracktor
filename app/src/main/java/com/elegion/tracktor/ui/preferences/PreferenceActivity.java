package com.elegion.tracktor.ui.preferences;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.elegion.tracktor.common.SingleFragmentActivity;

public class PreferenceActivity extends SingleFragmentActivity {

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, PreferenceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return MainPreferences.newInstance();
    }
}
