package com.elegion.tracktor.ui.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elegion.tracktor.R;
import com.elegion.tracktor.util.ScreenshotMaker;
import com.elegion.tracktor.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.elegion.tracktor.ui.results.ResultsActivity.DISTANCE_KEY;
import static com.elegion.tracktor.ui.results.ResultsActivity.SCREENSHOT_KEY;
import static com.elegion.tracktor.ui.results.ResultsActivity.TIME_KEY;

/**
 * @author Azret Magometov
 */
public class ResultsDetailsFragment extends Fragment {

    @BindView(R.id.tvTime) TextView mTimeText;
    @BindView(R.id.tvDistance) TextView mDistanceText;
    @BindView(R.id.ivScreenshot) ImageView mScreenshotImage;

    public static ResultsDetailsFragment newInstance(Bundle bundle) {
        Bundle args = new Bundle();
        args.putAll(bundle);

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

        double distance = getArguments().getDouble(DISTANCE_KEY, 0.0);
        long time = getArguments().getLong(TIME_KEY, 0);

        mTimeText.setText(StringUtil.getTimeText(time));
        mDistanceText.setText(StringUtil.getDistanceText(distance));
        mScreenshotImage.setImageBitmap(ScreenshotMaker.fromBase64(getArguments().getString(SCREENSHOT_KEY)));
    }
}
