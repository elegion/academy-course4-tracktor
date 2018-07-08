package com.elegion.tracktor.event;

public class UpdateTimerEvent {

    private long mSeconds;

    public UpdateTimerEvent(long seconds) {
        mSeconds = seconds;
    }

    public long getSeconds() {
        return mSeconds;
    }
}
