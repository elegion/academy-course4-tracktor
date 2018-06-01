package com.elegion.tracktor.ui.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elegion.tracktor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Azret Magometov
 */
public class ResultsDetailsFragment extends Fragment {

    @BindView(R.id.tv_time)
    TextView mTimeText;

    @BindView(R.id.tv_distance)
    TextView mDistanceText;

    public static ResultsDetailsFragment newInstance() {

        Bundle args = new Bundle();

        ResultsDetailsFragment fragment = new ResultsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
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

        mTimeText.setText("Stub time");
        mDistanceText.setText("Stub distance");
    }
}
