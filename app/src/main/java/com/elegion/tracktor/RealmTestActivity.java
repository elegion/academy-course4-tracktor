package com.elegion.tracktor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

import java.util.Date;
import java.util.List;

public class RealmTestActivity extends AppCompatActivity {

    private RealmRepository mRealmRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_realm_test);

        mRealmRepository = new RealmRepository();

        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track item = mRealmRepository.getItem(getId());
                String text = item == null ? "null" : item.toString();
                Toast.makeText(RealmTestActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.getAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RealmTestActivity.this, show(mRealmRepository.getAll()), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealmRepository.insertItem(getNewTrack());

            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealmRepository.updateItem(getTrack());
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealmRepository.deleteItem(getId());
            }
        });

    }

    private long getId() {
        return Long.parseLong(((EditText) findViewById(R.id.edit)).getText().toString());
    }

    private Track getTrack() {
        Track track = mRealmRepository.getItem(getId());
        track.setDuration(66666);
        return track;
    }

    private Track getNewTrack() {
        Track track = new Track();
        track.setDate(new Date());
        track.setDistance(123.4);
        track.setDuration(13123);
        return track;
    }

    private <T> String show(List<T> list) {
        if (list == null || list.isEmpty()) return "empty";

        StringBuilder stringBuilder = new StringBuilder();

        for (T t : list) {
            stringBuilder.append(t.toString()).append("\n");
        }

        return stringBuilder.toString();

    }
}
