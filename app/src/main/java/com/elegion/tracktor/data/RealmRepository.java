package com.elegion.tracktor.data;

import com.elegion.tracktor.data.model.Track;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;

/**
 * @author Azret Magometov
 */
public class RealmRepository implements IRepository<Track> {

    private Realm mRealm;

    private static AtomicLong sPrimaryId;

    public RealmRepository() {
        mRealm = Realm.getDefaultInstance();
        Number max = mRealm.where(Track.class).max("id");
        sPrimaryId = max == null ? new AtomicLong(0) : new AtomicLong(max.longValue());
    }

    @Override
    public Track getItem(long id) {
        Track track = getRealmAssociatedTrack(id);
        return track != null ? mRealm.copyFromRealm(track) : null;
    }

    private Track getRealmAssociatedTrack(long id) {
        return mRealm.where(Track.class).equalTo("id", id).findFirst();
    }

    @Override
    public List<Track> getAll() {
        return mRealm.where(Track.class).findAll();
    }

    @Override
    public long insertItem(Track track) {
        track.setId(sPrimaryId.incrementAndGet());
        mRealm.beginTransaction();
        mRealm.copyToRealm(track);
        mRealm.commitTransaction();
        return sPrimaryId.longValue();
    }

    @Override
    public boolean deleteItem(final long id) {

        boolean isDeleteSuccessful;
        mRealm.beginTransaction();

        Track track = getRealmAssociatedTrack(id);

        if (track != null) {
            track.deleteFromRealm();
            isDeleteSuccessful = true;
        } else {
            isDeleteSuccessful = false;
        }

        mRealm.commitTransaction();

        return isDeleteSuccessful;
    }

    @Override
    public void updateItem(final Track track) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(track);
        mRealm.commitTransaction();
    }

    public long createAndInsertTrackFrom(long duration, double distanse, String base64image) {
        Track track = new Track();

//        mRealm.beginTransaction();
//        Track track = mRealm.createObject(Track.class, sPrimaryId.incrementAndGet());
        track.setDistance(distanse);
        track.setDuration(duration);
        track.setImageBase64(base64image);
        track.setDate(new Date());
//        mRealm.commitTransaction();
//        return sPrimaryId.longValue();

        return insertItem(track);

    }
}
