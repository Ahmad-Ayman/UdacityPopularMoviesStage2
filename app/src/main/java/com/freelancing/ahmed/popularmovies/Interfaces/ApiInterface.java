package com.freelancing.ahmed.popularmovies.Interfaces;

import com.freelancing.ahmed.popularmovies.Models.Movies;
import com.freelancing.ahmed.popularmovies.Models.Reviews;
import com.freelancing.ahmed.popularmovies.Models.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ahmed on 1/20/2018.
 */

public interface ApiInterface {

    @GET("movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String id, @Query("api_key") String Key);


    @GET("movie/{id}/videos")
    Call<Videos> getVideos(@Path("id") String id, @Query("api_key") String Key);


    @GET("movie/{sort}")
    Call<Movies> getMovies(@Path("sort") String sortBy, @Query("api_key") String Key);
}
