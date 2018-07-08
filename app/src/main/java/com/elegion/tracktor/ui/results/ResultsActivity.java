package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;
import com.elegion.tracktor.util.ScreenshotMaker;


/**
` */
public class ResultsActivity extends SingleFragmentActivity {
    public static final String DISTANCE_KEY = "DISTANCE_KEY";
    public static final String TIME_KEY = "TIME_KEY";
    public static final String SCREENSHOT_KEY = "SCREENSHOT_KEY";

    public static void start(@NonNull Context context, String  distance, String time, Bitmap screenshot){
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(DISTANCE_KEY, distance);
        intent.putExtra(TIME_KEY, time);
        intent.putExtra(SCREENSHOT_KEY, ScreenshotMaker.toBase64(screenshot));

        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return ResultsDetailsFragment.newInstance(getIntent().getExtras());
    }
}
