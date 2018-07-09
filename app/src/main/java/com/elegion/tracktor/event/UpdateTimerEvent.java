package com.elegion.tracktor.event;

public class UpdateTimerEvent {

    private long mSeconds;
    private double mDistance;

    public UpdateTimerEvent(long seconds, double distance) {
        mSeconds = seconds;
        mDistance = distance;
    }

    public long getSeconds() {
        return mSeconds;
    }

    public double getDistance() {
        return mDistance;
    }
}
