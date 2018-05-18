package com.elegion.tracktor.model;

import android.location.Location;
import android.util.Base64;

import java.util.Date;
import java.util.List;

/**
 * @author Azret Magometov
 */
public class Track {

    private long mId;

    private Date mDate;

    private long mDuration;

    private Double mDistance;

    private Base64 mImageBase64;

    private List<Location> mLocations;

    public Track() {
        mDate = new Date();
    }

    public long getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public long getDuration() {
        return mDuration;
    }

    public Double getDistance() {
        return mDistance;
    }

    public Base64 getImageBase64() {
        return mImageBase64;
    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setDistance(Double distance) {
        mDistance = distance;
    }

    public void setImageBase64(Base64 imageBase64) {
        mImageBase64 = imageBase64;
    }

    public void setLocations(List<Location> locations) {
        mLocations = locations;
    }
}
