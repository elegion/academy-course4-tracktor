package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

public class AddPositionToRouteEvent {

    private LatLng mPosition;

    public AddPositionToRouteEvent(LatLng position) {
        mPosition = position;
    }

    public LatLng getPosition() {
        return mPosition;
    }
}
