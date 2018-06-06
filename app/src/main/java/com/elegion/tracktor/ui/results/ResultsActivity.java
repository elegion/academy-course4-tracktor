package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


/**
 * @author Azret Magometov
 */
public class ResultsActivity extends SingleFragmentActivity {
    public static final String DISTANCE_KEY = "DISTANCE_KEY";
    public static final String TIME_KEY = "TIME_KEY";
    public static final String ROUTE_KEY = "ROUTE_KEY";

    public static void start(@NonNull Context context, double distance, long time, ArrayList<LatLng> route){
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(DISTANCE_KEY, distance);
        intent.putExtra(TIME_KEY, time);
        intent.putExtra(ROUTE_KEY, route);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return ResultsDetailsFragment.newInstance(getIntent().getExtras());
    }
}
