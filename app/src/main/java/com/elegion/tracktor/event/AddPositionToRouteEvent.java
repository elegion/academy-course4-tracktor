package com.elegion.tracktor.event;

import com.google.android.gms.maps.model.LatLng;

public class AddPositionToRouteEvent {

    private LatLng mLastPosition;
    private LatLng mNewPosition;
    private double mDistance;

    public AddPositionToRouteEvent(LatLng lastPosition, LatLng newPosition, double distance) {
        mLastPosition = lastPosition;
        mNewPosition = newPosition;
        mDistance = distance;
    }

    public LatLng getLastPosition() {
        return mLastPosition;
    }

    public void setLastPosition(LatLng lastPosition) {
        mLastPosition = lastPosition;
    }

    public LatLng getNewPosition() {
        return mNewPosition;
    }

    public void setNewPosition(LatLng newPosition) {
        mNewPosition = newPosition;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }
}
