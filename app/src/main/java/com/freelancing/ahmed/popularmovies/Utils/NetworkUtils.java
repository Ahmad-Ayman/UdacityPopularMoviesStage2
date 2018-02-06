package com.freelancing.ahmed.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;

import com.freelancing.ahmed.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;

/**
 * Created by ahmed on 1/3/2018.
 */

public class NetworkUtils {
    // API_KEY for TheMoviesDB API
    private static final String API_KEY =  BuildConfig.API_KEY; // PUT YOUR API KEY HERE
    // Link of Json Response for movies sorted by popular
    private static final String MOVIES_LINK_POPULAR = "https://api.themoviedb.org/3/movie/popular";
    // Link of Json Response for movies sorted by top rated
    private static final String MOVIES_LINK_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated";
    // Link of Json Response for movie Video and review
    private static final String MOVIES_OTHER_DATA = "https://api.themoviedb.org/3/movie";


    // the Query Parameter
    private final static String QUERY_PARAM = "api_key";


    public static URL buildUrl(int Condition) {
        Uri builtUri = null;
        URL url = null;
        if (Condition == 0) {
            builtUri = Uri.parse(MOVIES_LINK_POPULAR).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, API_KEY)
                    .build();
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else if (Condition == 1) {
            builtUri = Uri.parse(MOVIES_LINK_TOP_RATED).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, API_KEY)
                    .build();
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}


