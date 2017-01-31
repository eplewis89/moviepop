package com.udacity.erin.lewis.moviepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {
    private MovieModel details;
    private TextView mTitle;
    private TextView mDate;
    private TextView mVoteAvg;
    private TextView mPlot;
    private ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        details = getIntent().getExtras().getParcelable("moviedetails");

        setupView();

        setupDetails();
    }

    private void setupView() {
        mTitle = (TextView) findViewById(R.id.movie_details_title);
        mDate = (TextView) findViewById(R.id.movie_details_date);
        mVoteAvg = (TextView) findViewById(R.id.movie_details_voteavg);
        mPlot = (TextView) findViewById(R.id.movie_details_plot);
        mPoster = (ImageView) findViewById(R.id.movie_details_poster);
    }

    private void setupDetails() {
        if(details != null) {
            mTitle.setText(details.title);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342" + details.poster_path).into(mPoster);
            mDate.setText("Release Date: " + details.release_date);
            mVoteAvg.setText("Vote Average: " + Double.toString(details.vote_average));
            mPlot.setText(details.overview);
        } else {
            mTitle.setText("An error has occurred.");
        }
    }
}
