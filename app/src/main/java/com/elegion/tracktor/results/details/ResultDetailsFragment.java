package com.elegion.tracktor.results.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * @author Azret Magometov
 */
public class ResultDetailsFragment extends Fragment {

    private static final String TRACK_ID = "id";
    private long mTrackId;

    public static ResultDetailsFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(TRACK_ID, id);
        ResultDetailsFragment fragment = new ResultDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrackId = getArguments().getLong(TRACK_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), String.valueOf(mTrackId), Toast.LENGTH_SHORT).show();
    }
}
