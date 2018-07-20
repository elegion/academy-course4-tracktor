package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class StopTrackEvent implements Serializable {

    private List<LatLng> mRoute;

    public StopTrackEvent(List<LatLng> route) {
        mRoute = route;
    }

    public List<LatLng> getRoute() {
        return mRoute;
    }
}
