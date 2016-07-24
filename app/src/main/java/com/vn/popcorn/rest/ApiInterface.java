package com.vn.popcorn.rest;

import com.vn.popcorn.model.ReviewsResponse;
import com.vn.popcorn.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by vn on 2/7/16.
 */
public interface ApiInterface {


    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);


    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("id") int id, @Query("api_key") String apiKey);

}
