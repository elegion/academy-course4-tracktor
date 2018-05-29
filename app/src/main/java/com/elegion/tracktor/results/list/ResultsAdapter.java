package com.elegion.tracktor.results.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elegion.tracktor.R;
import com.elegion.tracktor.data.model.Track;
import com.elegion.tracktor.results.list.ResultsFragment.OnItemClickListener;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultHolder> {

    private final List<Track> mValues;
    private final OnItemClickListener mListener;

    public ResultsAdapter(List<Track> items, ResultsFragment.OnItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.li_track, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        holder.bind(mValues.get(position));
        holder.setListener(mListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
