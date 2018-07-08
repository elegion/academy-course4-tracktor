package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;

/**
 * `
 */
public class ResultsActivity extends SingleFragmentActivity {
    public static final String RESULT_ID = "RESULT_ID";

    public static void start(@NonNull Context context, long resultId) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(RESULT_ID, resultId);

        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return ResultsDetailsFragment.newInstance(getIntent().getExtras());
    }
}
