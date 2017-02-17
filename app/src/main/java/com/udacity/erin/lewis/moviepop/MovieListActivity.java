package com.udacity.erin.lewis.moviepop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.udacity.erin.lewis.moviepop.adapters.MovieAdapter;
import com.udacity.erin.lewis.moviepop.data.StarContract;
import com.udacity.erin.lewis.moviepop.helpers.URLHelper;
import com.udacity.erin.lewis.moviepop.helpers.WebService;
import com.udacity.erin.lewis.moviepop.models.MovieListModel;
import com.udacity.erin.lewis.moviepop.models.MovieModel;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private static final String BUNDLE_RECYCLER_LAYOUT = "movielistactivity.recycler.layout";

    private MovieAdapter mAdapter;
    private RecyclerView movieRecyclerView;
    private MovieListModel moviedata;
    private RadioButton rButtonTopRated;
    private RadioButton rButtonPopular;
    private RadioButton rButtonFavorite;
    private TextView mLoadingTV;
    private String apikey;

    private enum CurrentSort {
        TOPRATED, POPULAR, FAVORITES
    }

    private CurrentSort currentSort = CurrentSort.TOPRATED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apikey = URLHelper.getAPIKey(this);

        setContentView(R.layout.activity_movie_list);

        mLoadingTV = (TextView) findViewById(R.id.loading_tv);

        setupMovieList();
    }

    private void setupMovieList() {
        if (MovieListModel.getInstance() == null) {
            WebService.getByUrl(URLHelper.getURL(getBaseContext(), getString(R.string.tmdbtoprated), apikey), null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    moviedata = new Gson().fromJson(response.toString(), MovieListModel.class);

                    MovieListModel.setInstance(moviedata);

                    setMovieListAdapter();
                }
            });
        } else {
            if (currentSort == CurrentSort.FAVORITES) {
                setupListFromFavorites();
            } else {
                moviedata = MovieListModel.getInstance();

                setMovieListAdapter();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(currentSort == CurrentSort.FAVORITES) {
            setupListFromFavorites();
        }
    }

    private void setMovieListAdapter() {
        mAdapter = new MovieAdapter(this);
        mAdapter.setMovieData(moviedata);

        int gridcolumns = 0;

        switch (getResources().getConfiguration().orientation) {
            case ORIENTATION_LANDSCAPE:
                gridcolumns = 4;

                break;
            case ORIENTATION_PORTRAIT:
            default:
                gridcolumns = 2;
        }


        movieRecyclerView = (RecyclerView) findViewById(R.id.movielist);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, gridcolumns));
        movieRecyclerView.setAdapter(mAdapter);

        mLoadingTV.setVisibility(View.GONE);
        movieRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        Intent movieDetails = new Intent(this, MovieDetailsActivity.class);

        movieDetails.putExtra("moviedetails", moviedata.results.get(position));

        startActivity(movieDetails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            createSortOptionsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createSortOptionsDialog() {
        LayoutInflater inflater = this.getLayoutInflater();

        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);

        View dialogView = inflater.inflate(R.layout.dialog_sort, null);

        rButtonTopRated = (RadioButton) dialogView.findViewById(R.id.radio_top_rated);
        rButtonPopular = (RadioButton) dialogView.findViewById(R.id.radio_popular);
        rButtonFavorite = (RadioButton) dialogView.findViewById(R.id.radio_favorite);

        switch (currentSort) {
            case POPULAR:
                rButtonPopular.setChecked(true);
                break;
            case TOPRATED:
                rButtonTopRated.setChecked(true);
                break;
            case FAVORITES:
                rButtonFavorite.setChecked(true);
                break;
        }

        builder.setView(dialogView);
        builder.setTitle("Change Sort Order");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mLoadingTV.setVisibility(View.VISIBLE);
                movieRecyclerView.setVisibility(View.GONE);

                switch (currentSort) {
                    case TOPRATED:
                        WebService.getByUrl(URLHelper.getURL(getBaseContext(), getString(R.string.tmdbtoprated), apikey), null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                moviedata = new Gson().fromJson(response.toString(), MovieListModel.class);

                                MovieListModel.setInstance(moviedata);

                                setMovieListAdapter();
                            }
                        });
                        break;
                    case POPULAR:
                        WebService.getByUrl(URLHelper.getURL(getBaseContext(), getString(R.string.tmdbpopular), apikey), null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                moviedata = new Gson().fromJson(response.toString(), MovieListModel.class);

                                MovieListModel.setInstance(moviedata);

                                setMovieListAdapter();
                            }
                        });
                        break;
                    case FAVORITES: {
                        setupListFromFavorites();
                        break;
                    }
                }
            }
        });

        builder.setCancelable(true);
        builder.create().show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_top_rated:
                if (checked) {
                    currentSort = CurrentSort.TOPRATED;
                    rButtonTopRated.setChecked(true);
                }

                break;
            case R.id.radio_popular:
                if (checked) {
                    currentSort = CurrentSort.POPULAR;
                    rButtonPopular.setChecked(true);
                }

                break;
            case R.id.radio_favorite:
                if (checked) {
                    currentSort = CurrentSort.FAVORITES;
                    rButtonFavorite.setChecked(true);
                }

                break;
        }
    }

    // preserve recycler view position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("CURRENT_SORT", currentSort);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, movieRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    // restore recycler view position
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            currentSort = (CurrentSort) savedInstanceState.getSerializable("CURRENT_SORT");

            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);

            movieRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    private void setupListFromFavorites() {
        Cursor cursor = getContentResolver().query(StarContract.StarEntry.CONTENT_URI, null, null, null, null);

        moviedata = new MovieListModel();

        try {
            while (cursor.moveToNext()) {
                String json = cursor.getString(cursor.getColumnIndex(StarContract.StarEntry.COLUMN_MOVIE_JSON));

                MovieModel movie = new Gson().fromJson(json, MovieModel.class);

                moviedata.results.add(movie);
            }
        } catch (Exception ex) {

        } finally {
            cursor.close();
        }

        MovieListModel.setInstance(moviedata);

        setMovieListAdapter();
    }
}
