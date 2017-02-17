package com.udacity.erin.lewis.moviepop.models;

import java.util.ArrayList;

/**
 * Created by Erin Lewis on 1/30/2017.
 */

public class MovieListModel {
    public int page;
    public ArrayList<MovieModel> results;
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
        results = new ArrayList<MovieModel>();
        total_results = 0;
        total_pages = 0;
    }
}
