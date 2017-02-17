package com.udacity.erin.lewis.moviepop.models;

import java.util.List;

/**
 * Created by Erin Lewis on 1/30/2017.
 */

public class MovieListModel {
    public int page;
    public List<MovieModel> results;
    public int total_results;
    public int total_pages;

    private static MovieListModel instance = null;

    public static MovieListModel getInstance() {
        return instance;
    }

    public static void setInstance(MovieListModel model) {
        instance = model;
    }

    public MovieListModel() {
        page = 0;
        results = null;
        total_results = 0;
        total_pages = 0;
    }
}
