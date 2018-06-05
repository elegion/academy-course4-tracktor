package com.elegion.tracktor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.elegion.tracktor.R;

/**
 * @author Denis Anisimov.
 */
public class BaseActivity extends AppCompatActivity {
    protected void changeFragment(Fragment fragment) {

        boolean shouldAddToBackStack = getSupportFragmentManager().findFragmentById(R.id.container) != null;

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment);

        if (shouldAddToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }
}
