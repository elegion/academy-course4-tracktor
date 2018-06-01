package com.elegion.tracktor.ui.map;

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

import com.elegion.tracktor.R;
import com.elegion.tracktor.event.GetRouteEvent;
import com.elegion.tracktor.event.StartRouteEvent;
import com.elegion.tracktor.event.StopRouteEvent;
import com.elegion.tracktor.viewmodel.CounterViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

        /*tvTime.setText("Временный текст");
        tvDistance.setText("Временный текст");*/

        viewModel = ViewModelProviders.of(this).get(CounterViewModel.class);
        viewModel.getTimeText().observe(this, s -> tvTime.setText(s));
        viewModel.getStartEnabled().observe(this, buttonStart::setEnabled);
        viewModel.getStopEnabled().observe(this, buttonStop::setEnabled);

        return view;
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetRoute(GetRouteEvent event) {
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.buttonStart)
    void onStartClick() {
        EventBus.getDefault().post(new StartRouteEvent());
        viewModel.startTimer();
    }

    @OnClick(R.id.buttonStop)
    void onStopClick() {
        EventBus.getDefault().post(new StopRouteEvent());
        viewModel.stopTimer();
    }
}
