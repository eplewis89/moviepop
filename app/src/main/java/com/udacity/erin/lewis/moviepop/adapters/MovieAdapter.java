package com.udacity.erin.lewis.moviepop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.erin.lewis.moviepop.models.MovieListModel;
import com.udacity.erin.lewis.moviepop.models.MovieModel;
import com.udacity.erin.lewis.moviepop.R;

import java.util.List;

/**
 * Created by Erin Lewis on 1/9/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private final MovieAdapterOnClickHandler clickHandler;
    private List<MovieModel> movies;
    private Context context;

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View movieView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new MovieHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.movieItem = movies.get(position);

        Picasso.with(this.context).load("http://image.tmdb.org/t/p/w342" + holder.movieItem.poster_path).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        if (this.movies != null) {
            return this.movies.size();
        } else {
            return 0;
        }
    }

    public void setMovieData(MovieListModel movielist) {
        if (this.movies != null) {
            this.movies.clear();
            this.movies.addAll(movielist.results);
        } else {
            this.movies = movielist.results;
        }

        notifyDataSetChanged();
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView movieImage;
        public MovieModel movieItem;

        public MovieHolder(View v) {
            super(v);

            movieImage = (ImageView) v.findViewById(R.id.movie_item);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(getAdapterPosition());
        }
    }
}
