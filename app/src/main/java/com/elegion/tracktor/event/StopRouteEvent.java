package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class StopRouteEvent {

    private double mDistance;
    private long mTime;
    private ArrayList<LatLng> mRoute;

    public StopRouteEvent(double distance, long time, ArrayList<LatLng> route) {
        mDistance = distance;
        mTime = time;
        mRoute = route;
    }

    public double getDistance() {
        return mDistance;
    }

    public long getTime() {
        return mTime;
    }

    public ArrayList<LatLng> getRoute() {
        return mRoute;
    }
}
