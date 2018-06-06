package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

public class SetStartPositionToRouteEvent {

    private LatLng mPosition;

    public SetStartPositionToRouteEvent(LatLng position) {
        mPosition = position;
    }

    public LatLng getPosition() {
        return mPosition;
    }
}
