package com.udacity.erin.lewis.moviepop;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.udacity.erin.lewis.moviepop.adapters.MovieAdapter;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private View currentView;
    private MovieAdapter mAdapter;
    private RecyclerView movieRecyclerView;
    private MovieListModel moviedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);

        setupMovieList();
    }

    private void setupMovieList() {
        WebService.getByUrl(getPopularURL(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                moviedata = new Gson().fromJson(response.toString(), MovieListModel.class);

                setMovieListAdapter();
            }
        });
    }

    private void setMovieListAdapter() {
        mAdapter = new MovieAdapter(this);
        mAdapter.setMovieData(moviedata);

        movieRecyclerView = (RecyclerView) findViewById(R.id.movielist);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieRecyclerView.setAdapter(mAdapter);
    }

    private String getTopRatedURL() {
        Uri.Builder builder = new
                Uri.Builder();

        builder.scheme(getString(R.string.tmdbscheme))
                .authority(getString(R.string.tmdbauthority))
                .appendPath(getString(R.string.tmdbversion))
                .appendPath(getString(R.string.tmdbendpoint))
                .appendPath(getString(R.string.tmdbtoprated))
                .appendQueryParameter("api_key", getString(R.string.tmdbapikey))
                .appendQueryParameter("language", getString(R.string.tmdblanguage))
                .appendQueryParameter("page", "1");

        return builder.build().toString();
    }

    private String getPopularURL() {
        Uri.Builder builder = new
                Uri.Builder();

        builder.scheme(getString(R.string.tmdbscheme))
                .authority(getString(R.string.tmdbauthority))
                .appendPath(getString(R.string.tmdbversion))
                .appendPath(getString(R.string.tmdbendpoint))
                .appendPath(getString(R.string.tmdbpopular))
                .appendQueryParameter("api_key", getString(R.string.tmdbapikey))
                .appendQueryParameter("language", getString(R.string.tmdblanguage))
                .appendQueryParameter("page", "1");

        return builder.build().toString();
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

        builder.setView(inflater.inflate(R.layout.dialog_sort, null));
        builder.setTitle("Change Sort Order");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setCancelable(true);
        builder.create().show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_top_rated:
                if (checked)

                    break;
            case R.id.radio_popular:
                if (checked)

                    break;
        }
    }
}
