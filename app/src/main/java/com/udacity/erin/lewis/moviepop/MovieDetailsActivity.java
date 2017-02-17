package com.udacity.erin.lewis.moviepop;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.udacity.erin.lewis.moviepop.adapters.TrailerAdapter;
import com.udacity.erin.lewis.moviepop.data.StarContract;
import com.udacity.erin.lewis.moviepop.helpers.URLHelper;
import com.udacity.erin.lewis.moviepop.helpers.WebService;
import com.udacity.erin.lewis.moviepop.models.MovieModel;
import com.udacity.erin.lewis.moviepop.models.ReviewModel;
import com.udacity.erin.lewis.moviepop.models.TrailerModel;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {
    private MovieModel details;
    private TrailerModel trailers;
    private ReviewModel reviews;
    private String apikey;
    private Menu mOptionsMenu;
    private Toast mToast;

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
    @BindView(R.id.trailers_loading_tv)
    TextView mTrailersLoading;
    @BindView(R.id.reviews_loading_tv)
    TextView mReviewsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        apikey = URLHelper.getAPIKey(this);
        details = getIntent().getExtras().getParcelable("moviedetails");

        checkIfFavorite();
        setupDetails();
        getMovieTrailers();
        getMovieReviews();
    }

    private void checkIfFavorite() {
        Cursor cursor = getContentResolver().query(StarContract.StarEntry.CONTENT_URI, null, StarContract.StarEntry.COLUMN_MOVIE_ID + "=?", new String[]{Integer.toString(details.id)}, null);

        if(cursor.getCount() > 0) {
            details.isFavorite = true;
        }
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

        mOptionsMenu = menu;

        getMenuInflater().inflate(R.menu.favorite, menu);

        if (details.isFavorite) {
            menu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_on);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            addMovieAsFavorite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addMovieAsFavorite() {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = null;

        if (details.isFavorite) {
            int deleted = getContentResolver().delete(StarContract.StarEntry.CONTENT_URI, "movieid=?", new String[]{Integer.toString(details.id)});

            if (deleted > 0) {
                mOptionsMenu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_off);
                mToast = Toast.makeText(this, details.title + " has been unfavorited", Toast.LENGTH_SHORT);
                details.isFavorite = false;
            }
        } else {
            ContentValues values = new ContentValues();

            values.put(StarContract.StarEntry.COLUMN_MOVIE_ID, details.id);
            values.put(StarContract.StarEntry.COLUMN_MOVIE_JSON, new Gson().toJson(details));

            Uri uri = getContentResolver().insert(StarContract.StarEntry.CONTENT_URI, values);

            if (uri != null) {
                mOptionsMenu.findItem(R.id.action_favorite).setIcon(android.R.drawable.btn_star_big_on);
                mToast = Toast.makeText(this, details.title + " has been favorited", Toast.LENGTH_SHORT);
                details.isFavorite = true;
            }
        }

        if (mToast != null) {
            mToast.show();
        }
    }

    private void getMovieTrailers() {
        String[] paths = {Integer.toString(details.id), getString(R.string.tmdbtrailers)};

        WebService.getByUrl(URLHelper.getURL(getBaseContext(), paths, apikey), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                trailers = new Gson().fromJson(response.toString(), TrailerModel.class);

                addMovieTrailers();
            }
        });
    }

    private void addMovieTrailers() {
        if (trailers.results.length > 0) {
            mTrailersLoading.setVisibility(View.GONE);

            for (final TrailerModel.TrailerResult result : trailers.results) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.trailers_ll);

                View trailerItem = View.inflate(this, R.layout.list_item_trailer, null);

                TextView trailerTextView = (TextView) trailerItem.findViewById(R.id.trailer_text_view);

                trailerTextView.setText(result.name);

                trailerItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + result.key)));
                    }
                });

                layout.addView(trailerItem);
            }
        } else {
            mTrailersLoading.setText("No Trailers");
        }
    }

    private void getMovieReviews() {
        String[] paths = {Integer.toString(details.id), getString(R.string.tmdbreviews)};

        WebService.getByUrl(URLHelper.getURL(getBaseContext(), paths, apikey), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                reviews = new Gson().fromJson(response.toString(), ReviewModel.class);

                addMovieReviews();
            }
        });
    }

    private void addMovieReviews() {
        if (reviews.results.length > 0) {
            mReviewsLoading.setVisibility(View.GONE);

            for (final ReviewModel.ReviewResult result : reviews.results) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.reviews_ll);

                View reviewItem = View.inflate(this, R.layout.list_item_review, null);

                TextView reviewAuthorTextView = (TextView) reviewItem.findViewById(R.id.review_author_tv);
                TextView reviewContentTextView = (TextView) reviewItem.findViewById(R.id.review_content_tv);

                reviewAuthorTextView.setText(result.author);
                reviewContentTextView.setText(result.content);

                layout.addView(reviewItem);
            }
        } else {
            mReviewsLoading.setText("No Reviews");
        }
    }
}
