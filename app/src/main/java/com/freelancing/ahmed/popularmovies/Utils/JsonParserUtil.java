package com.freelancing.ahmed.popularmovies.Utils;

import android.content.Context;

import com.freelancing.ahmed.popularmovies.Models.Movies;
import com.freelancing.ahmed.popularmovies.Models.Reviews;
import com.freelancing.ahmed.popularmovies.Models.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ahmed on 1/3/2018.
 */

public class JsonParserUtil {

    /**
     * @param context
     * @param MoviesJsonStr
     * @return
     * @throws JSONException
     */
    public static ArrayList<Movies> getMoviesStringsFromJson(Context context, String MoviesJsonStr)
            throws JSONException {
        /* Movies Object to store Data */
        ArrayList<Movies> MoviesListingArray = new ArrayList<>();
        /* Movies information. Each result is an element of the "results" array */
        final String RESULT = "results";

        /* vote counter >> Integer */
        final String VOTES_COUNTER = "vote_count";

        /* vote Average >> Dobule */
        final String VOTE_AVG = "vote_average";
        /*back image */
        final String BACK_IMG = "backdrop_path";
        /* poster path >> String */
        final String POSTER_PATH = "poster_path";
        /* title of the movie >> String */
        final String ORIGINAL_TITLE = "original_title";
        /* overview of the movie >> String */
        final String OVERVIEW = "overview";
        /* Release date of the movie >> String */
        final String RELEASE = "release_date";
        /* ID of the movie >> Integer */
        final String ID = "id";

        /* root Json Object*/
        JSONObject MoviesJson = new JSONObject(MoviesJsonStr);

        /* Is there an error? */

        JSONArray MoviesResultArray = MoviesJson.getJSONArray(RESULT);

        for (int i = 0; i < MoviesResultArray.length(); i++) {
            /* These are the values that will be collected */
            int id;
            int voteCounter;
            double voteavg;
            String posterPath;
            String title;
            String overview;
            String releaseDate;
            String backimg;
            /* Get the JSON object representing the results at each index*/
            JSONObject MoviesList = MoviesResultArray.getJSONObject(i);

            /* GET the Value of each Key from the Json */
            voteCounter = MoviesList.getInt(VOTES_COUNTER);
            voteavg = MoviesList.getDouble(VOTE_AVG);
            posterPath = MoviesList.getString(POSTER_PATH);
            title = MoviesList.getString(ORIGINAL_TITLE);
            overview = MoviesList.getString(OVERVIEW);
            releaseDate = MoviesList.getString(RELEASE);
            backimg = MoviesList.getString(BACK_IMG);
            id = MoviesList.getInt(ID);
            MoviesListingArray.add(new Movies(id, voteCounter, voteavg, posterPath, title, overview, releaseDate, backimg));

        }
        return MoviesListingArray;

    }
//    public static ArrayList<Reviews> getReviewsStringsFromJson(Context context, String ReviewJsonStr)
//            throws JSONException {
//        /* Movies Object to store Data */
//        ArrayList<Reviews> ReviewsListingArray = new ArrayList<>();
//        /* Movies information. Each result is an element of the "results" array */
//        final String RESULT = "results";
//
//        /* vote counter >> Integer */
//        final String AUTHOR = "author";
//
//        final String IDRev= "id";
//        /* vote Average >> Dobule */
//        final String CONTENT = "content";
//        /* root Json Object*/
//        JSONObject ReviewJson = new JSONObject(ReviewJsonStr);
//
//        /* Is there an error? */
//
//        JSONArray ReviewResultArray = ReviewJson.getJSONArray(RESULT);
//        if(ReviewResultArray.length()==0){
//            ReviewsListingArray=null;
//        }else {
//            for (int i = 0; i < ReviewResultArray.length(); i++) {
//            /* These are the values that will be collected */
//                String author, content, id;
//                int noOfResults;
//            /* Get the JSON object representing the results at each index*/
//                JSONObject ReviewsList = ReviewResultArray.getJSONObject(i);
//
//            /* GET the Value of each Key from the Json */
//                author = ReviewsList.getString(AUTHOR);
//                content = ReviewsList.getString(CONTENT);
//                id = ReviewsList.getString(IDRev);
//                ReviewsListingArray.add(new Reviews(id, author, content));
//
//            }
//
//        }
//        return ReviewsListingArray;
//    }

//    public static ArrayList<Videos> getVideosStringsFromJson(Context context, String VideosJsonStr)
//            throws JSONException {
//        /* Movies Object to store Data */
//        ArrayList<Videos> VideossListingArray = new ArrayList<>();
//        /* Movies information. Each result is an element of the "results" array */
//        final String RESULT = "results";
//
//        /* vote counter >> Integer */
//        final String KEY = "key";
//
//        final String IDVid= "id";
//        /* vote Average >> Dobule */
//        final String NAME = "name";
//        /* root Json Object*/
//        JSONObject VideosJson = new JSONObject(VideosJsonStr);
//
//        /* Is there an error? */
//
//        JSONArray VideosResultArray = VideosJson.getJSONArray(RESULT);
//        if(VideosResultArray.length()==0){
//            VideossListingArray=null;
//        } else {
//            for (int i = 0; i < VideosResultArray.length(); i++) {
//            /* These are the values that will be collected */
//                String id, key, namevideo;
//            /* Get the JSON object representing the results at each index*/
//                JSONObject VideosList = VideosResultArray.getJSONObject(i);
//
//            /* GET the Value of each Key from the Json */
//                id = VideosList.getString(IDVid);
//                key = VideosList.getString(KEY);
//                namevideo = VideosList.getString(NAME);
//                VideossListingArray.add(new Videos(id, key, namevideo));
//
//            }
//        }
//        return  VideossListingArray;
//    }
}
