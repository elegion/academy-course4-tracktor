package com.elegion.tracktor.ui.results;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.elegion.tracktor.common.SingleFragmentActivity;


/**
 * @author Azret Magometov
 */
public class ResultsActivity extends SingleFragmentActivity {

    public static void start(@NonNull Context context){
        Intent intent = new Intent(context, ResultsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return ResultsDetailsFragment.newInstance();
    }
}
