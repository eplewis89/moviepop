package com.udacity.erin.lewis.moviepop.models;

/**
 * Created by Erin Lewis on 2/17/2017.
 */

public class TrailerModel {
    public int id;
    public TrailerResult[] results;

    public TrailerModel() {
        id = 0;
        results = new TrailerResult[0];
    }

    public class TrailerResult {
        public String id;
        public String iso_639_1;
        public String iso_3166_1;
        public String key;
        public String name;
        public String site;
        public int size;
        public String type;

        public TrailerResult() {
            id = "";
            iso_639_1 = "";
            iso_3166_1 = "";
            key = "";
            name = "";
            site = "";
            size = 0;
            type = "";
        }
    }
}
