package com.elegion.tracktor.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

import com.elegion.tracktor.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class TrPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = TrPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.tr_pref, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public static TrPreferenceFragment newInstance() {
        TrPreferenceFragment fragment = new TrPreferenceFragment();
        return fragment;
    }
}
