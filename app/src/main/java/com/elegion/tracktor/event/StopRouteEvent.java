package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class StopRouteEvent implements Serializable {

    private double mDistance;
    private long mTime;
    private List<LatLng> mRoute;

    public StopRouteEvent(double distance, long time, List<LatLng> route) {
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

    public List<LatLng> getRoute() {
        return mRoute;
    }
}
