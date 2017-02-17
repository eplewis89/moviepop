package com.udacity.erin.lewis.moviepop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.erin.lewis.moviepop.models.MovieModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    private MovieModel details;

    @BindView(R.id.movie_details_title)
    TextView mTitle;
    @BindView(R.id.movie_details_date)
    TextView mDate;
    @BindView(R.id.movie_details_voteavg)
    TextView mVoteAvg;
    @BindView(R.id.movie_details_plot)
    TextView mPlot;
    @BindView(R.id.movie_details_poster)
    ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        details = getIntent().getExtras().getParcelable("moviedetails");

        setupDetails();
    }

    private void setupDetails() {
        if (details != null) {
            mTitle.setText(details.title);
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342" + details.poster_path).into(mPoster);
            mDate.setText("Release Date: " + details.release_date);
            mVoteAvg.setText("Vote Average: " + Double.toString(details.vote_average));
            mPlot.setText(details.overview);
        } else {
            mTitle.setText("An error has occurred.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.favorite, menu);

        if (details.isFavorite) {
            menu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_on);
        }

        return true;
    }

    public void getMovieTrailers() {

    }

    public void getMovieReviews() {

    }
}
