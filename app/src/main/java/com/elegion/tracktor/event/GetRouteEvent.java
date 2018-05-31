package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class GetRouteEvent {

    private List<LatLng> mRoute;

    public GetRouteEvent(List<LatLng> route) {
        mRoute = route;
    }

    public List<LatLng> getRoute() {
        return mRoute;
    }
}
