package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GetFullRouteEvent {

    private List<LatLng> mRoute;

    public GetFullRouteEvent(List<LatLng> route) {
        mRoute = route;
    }

    public List<LatLng> getRoute() {
        return mRoute;
    }
}
