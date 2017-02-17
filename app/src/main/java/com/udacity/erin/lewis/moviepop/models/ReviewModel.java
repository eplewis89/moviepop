package com.udacity.erin.lewis.moviepop.models;

/**
 * Created by Erin Lewis on 2/17/2017.
 */

public class ReviewModel {
    public int id;
    public int page;
    public ReviewResult[] results;

    public ReviewModel() {
        id = 0;
        page = 0;
        results = new ReviewResult[0];
    }

    public class ReviewResult {
        public String id;
        public String author;
        public String content;
        public String url;

        public ReviewResult() {
            id = "";
            author = "";
            content = "";
            url = "";
        }
    }
}
