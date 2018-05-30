package com.elegion.tracktor.results.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_result_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_delete_result)
    public void onDeleteResultClick() {
        RealmRepository realmRepository = new RealmRepository();
        if (realmRepository.deleteItem(mTrackId)) {
            getActivity().onBackPressed();
        }
    }
}
