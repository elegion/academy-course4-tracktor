package com.elegion.tracktor.data.model;

import io.realm.RealmObject;

/**
 * @author Azret Magometov
 */
public class Location extends RealmObject {

    private double mLatitude;
    private double mLongitude;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
