package com.udacity.erin.lewis.moviepop;

import java.util.List;

/**
 * Created by Erin Lewis on 1/30/2017.
 */

public class MovieListModel {
    public int page;
    public List<MovieModel> results;
    public int total_results;
    public int total_pages;

    public MovieListModel() {
        page = 0;
        results = null;
        total_results = 0;
        total_pages = 0;
    }
}
