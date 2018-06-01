package com.elegion.tracktor.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elegion.tracktor.NetworkUtils;
import com.elegion.tracktor.R;
import com.elegion.tracktor.viewmodel.CounterViewModel;
import com.elegion.tracktor.viewmodel.CounterViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CounterFragment extends Fragment {

    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvDistance) TextView tvDistance;
    @BindView(R.id.buttonStart) Button buttonStart;
    @BindView(R.id.buttonStop) Button buttonStop;

    private CounterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_counter, container, false);
        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this, new CounterViewModelFactory(NetworkUtils.MATRIX_API)).get(CounterViewModel.class);
        viewModel.getTimeText().observe(this, s -> tvTime.setText(s));
        viewModel.getDistanceText().observe(this, s -> tvDistance.setText(s));
        viewModel.getStartEnabled().observe(this, buttonStart::setEnabled);
        viewModel.getStopEnabled().observe(this, buttonStop::setEnabled);

        return view;
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.buttonStart)
    void onStartClick() {
        viewModel.startTimer();
    }

    @OnClick(R.id.buttonStop)
    void onStopClick() {
        viewModel.stopTimer();
    }
}
