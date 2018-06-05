package com.elegion.tracktor;

import android.app.Application;

import io.realm.Realm;

/**
 * @author Azret Magometov
 */
public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
