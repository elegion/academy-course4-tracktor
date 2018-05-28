package com.elegion.tracktor.data.model;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Azret Magometov
 */
public class Track extends RealmObject {

    @PrimaryKey
    private long mId;

    private Date mDate;

    private long mDuration;

    private Double mDistance;

    private String mImageBase64;

    private RealmList<Location> mLocations;

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        mDistance = distance;
    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public void setLocations(RealmList<Location> locations) {
        mLocations = locations;
    }

    public void setImageBase64(String imageBase64) {
        mImageBase64 = imageBase64;
    }

    public String getImageBase64() {
        return mImageBase64;
    }

    @Override
    public String toString() {
        return "Track{" +
                "mId=" + mId +
                ", mDate=" + mDate +
                ", mDuration=" + mDuration +
                ", mDistance=" + mDistance +
                ", mImageBase64=" + mImageBase64 +
                ", mLocations=" + mLocations +
                '}';
    }
}


