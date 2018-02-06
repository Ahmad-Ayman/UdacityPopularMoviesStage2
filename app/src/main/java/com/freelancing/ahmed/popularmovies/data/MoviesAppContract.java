package com.freelancing.ahmed.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

/**
 * Created by ahmed on 2/2/2018.
 */

public class MoviesAppContract {
    public static final String AUTHORITY = "com.freelancing.ahmed.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAV_MOVIES = "favmovies";

public static final class MoviesEntry implements BaseColumns{
    public static  final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV_MOVIES).build();

    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_MOVIE_NAME = "movieName";
    public static final String COLUMN_MOVIE_POSTER = "moviePoster";
    public static final String COLUMN_MOVIES_BACK_POSTER = "movieBackPoster";
    public static final String COLUMN_MOVIES_ID = "movieID";
    public static final String COLUMN_MOVIES_OVERVIEWS = "movieOverView";
    public static final String COLUMN_MOVIES_RATE="moviesRate";
    public static final String COLUMN_MOVIES_TOTAL_VOTES="moviesTotalVotes";
    public static final String COLUMN_MOVIES_RELEASE_DATE="moviesReleaseDate";
}

}
