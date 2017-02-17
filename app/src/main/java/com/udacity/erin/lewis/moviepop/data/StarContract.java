package com.udacity.erin.lewis.moviepop.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Erin Lewis on 2/16/2017.
 */

public class StarContract {
    public static final String AUTHORITY = "com.udacity.erin.lewis.moviepop";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_STARS= "stars";

    public static final class StarEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STARS).build();

        public static final String TABLE_NAME = "stars";

        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_MOVIE_JSON = "moviejson";
    }
}
