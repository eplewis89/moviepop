package com.udacity.erin.lewis.moviepop.helpers;

import android.content.Context;
import android.net.Uri;

import com.udacity.erin.lewis.moviepop.R;

/**
 * Created by Erin Lewis on 2/17/2017.
 */

public class URLHelper {
    public static String getURL(Context context, String path, String apikey) {
        Uri.Builder builder = new
                Uri.Builder();

        builder.scheme(context.getString(R.string.tmdbscheme))
                .authority(context.getString(R.string.tmdbauthority))
                .appendPath(context.getString(R.string.tmdbversion))
                .appendPath(context.getString(R.string.tmdbendpoint))
                .appendPath(path)
                .appendQueryParameter("api_key", apikey)
                .appendQueryParameter("language", context.getString(R.string.tmdblanguage))
                .appendQueryParameter("page", "1");

        return builder.build().toString();
    }
}
