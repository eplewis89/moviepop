package com.udacity.erin.lewis.moviepop.helpers;

import com.loopj.android.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Erin Lewis on 1/27/2017.
 */

public class WebService {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }
}
