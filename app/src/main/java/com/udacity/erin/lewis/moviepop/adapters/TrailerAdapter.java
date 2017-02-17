package com.udacity.erin.lewis.moviepop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.erin.lewis.moviepop.R;
import com.udacity.erin.lewis.moviepop.models.TrailerModel;

/**
 * Created by Erin Lewis on 2/17/2017.
 */

public class TrailerAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private TrailerModel trailers;
    private TextView trailerTextView;

    public TrailerAdapter(Context context, TrailerModel model) {
        mContext = context;
        trailers = model;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return trailers.results.length;
    }

    @Override
    public Object getItem(int i) {
        return trailers.results[i];
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.list_item_trailer, viewGroup, false);

        trailerTextView = (TextView) rowView.findViewById(R.id.trailer_text_view);
        trailerTextView.setText(trailers.results[i].name);

        return rowView;
    }
}
