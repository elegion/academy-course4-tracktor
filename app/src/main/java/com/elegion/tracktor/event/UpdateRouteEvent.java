package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class UpdateRouteEvent {

    private List<LatLng> mRoute;
    private double mDistance;

    public UpdateRouteEvent(List<LatLng> route, double distance) {
        mRoute = route;
        mDistance = distance;
    }

    public List<LatLng> getRoute() {
        return mRoute;
    }

    public double getDistance() {
        return mDistance;
    }
}
