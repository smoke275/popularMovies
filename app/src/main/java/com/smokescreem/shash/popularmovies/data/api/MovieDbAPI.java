package com.smokescreem.shash.popularmovies.data.api;

import com.smokescreem.shash.popularmovies.data.models.Movie;
import com.smokescreem.shash.popularmovies.data.models.Review;
import com.smokescreem.shash.popularmovies.data.models.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Shash on 11/3/2016.
 */

public interface MovieDbAPI {
    @GET("movie/{sort_criteria}")
    Call<Movie.Response> getMovies(@Path("sort_criteria") String sortCriteria, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<Review.Response> getReviews(@Path("id") Long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Video.Response> getTrailer(@Path("id") Long id, @Query("api_key") String apiKey);
}
