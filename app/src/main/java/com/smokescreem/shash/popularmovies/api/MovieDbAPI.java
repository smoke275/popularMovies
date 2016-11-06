package com.smokescreem.shash.popularmovies.api;

import com.smokescreem.shash.popularmovies.models.MovieData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Shash on 11/3/2016.
 */

public interface MovieDbAPI {
    @GET("movie/{sort_criteria}")
    Call<MovieData> getMovies(@Path("sort_criteria") String sortCriteria,@Query("api_key") String apiKey);
}
