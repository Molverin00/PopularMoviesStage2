package com.example.marouen.popularmovies_stage2.api;

import com.example.marouen.popularmovies_stage2.model.Movie;
import com.example.marouen.popularmovies_stage2.model.MoviesResult;
import com.example.marouen.popularmovies_stage2.model.ReviewsResult;
import com.example.marouen.popularmovies_stage2.model.VideosResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("{category}")
    Call<MoviesResult> getMovies(
            @Path("category") String category,
            @Query("api_key") String apiKey
    );

    @GET("{movie_id}/videos")
    Call<VideosResult> getMovieVideos(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET("{movie_id}/reviews")
    Call<ReviewsResult> getMovieReviews(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );
}
