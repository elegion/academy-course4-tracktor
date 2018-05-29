package com.elegion.tracktor.results;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.results.list.ResultsFragment;

import java.util.Date;

public class ResultsActivity extends AppCompatActivity implements ResultsFragment.OnItemClickListener {

    public static void start(AppCompatActivity activity) {
        Intent intent = new Intent(activity, ResultsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

//        generateStubs();

        if (savedInstanceState == null) {
            changeFragment(ResultsFragment.newInstance());
        }

    }

    // TODO: 29.05.2018 delete after getting actual track data
    private void generateStubs() {
        RealmRepository realmRepository = new RealmRepository();

        for (int i = 0; i < 10; i++) {
            Track track = new Track();
            track.setDate(new Date());
            track.setDistance(i * 123.4);
            track.setDuration(i * 131);
            realmRepository.insertItem(track);
        }

    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onClick(long trackId) {
        Toast.makeText(this, String.valueOf(trackId), Toast.LENGTH_SHORT).show();
    }
}
