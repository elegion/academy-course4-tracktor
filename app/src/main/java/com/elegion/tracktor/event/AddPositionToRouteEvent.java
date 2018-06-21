package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

public class AddPositionToRouteEvent {

    private LatLng mLastPosition;
    private LatLng mNewPosition;

    public AddPositionToRouteEvent(LatLng lastPosition, LatLng newPosition) {
        mLastPosition = lastPosition;
        mNewPosition = newPosition;
    }

    public LatLng getLastPosition() {
        return mLastPosition;
    }

    public LatLng getNewPosition() {
        return mNewPosition;
    }
}
