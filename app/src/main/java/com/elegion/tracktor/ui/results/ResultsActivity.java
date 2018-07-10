package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;

/**
 * `
 */
public class ResultsActivity extends SingleFragmentActivity implements ResultsFragment.OnItemClickListener {
    public static final String RESULT_ID = "RESULT_ID";
    public static final long LIST_ID = -1L;

    public static void start(@NonNull Context context, long resultId) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(RESULT_ID, resultId);

        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        long resultId = getIntent().getLongExtra(RESULT_ID, LIST_ID);

        if (resultId != LIST_ID)
            return ResultsDetailsFragment.newInstance(resultId);
        else
            return ResultsFragment.newInstance();
    }

    //eventbus ?
    @Override
    public void onClick(long trackId) {
        changeFragment(ResultsDetailsFragment.newInstance(trackId));
    }
}
