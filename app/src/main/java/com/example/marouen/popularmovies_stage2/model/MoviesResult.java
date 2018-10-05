package com.example.marouen.popularmovies_stage2.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResult {

    @SerializedName("results")
    @Expose
    private List<Movie> MoviesList = null;

    public List<Movie> getMoviesList() {
        return MoviesList;
    }

    @NonNull
    @Override
    public String toString() {
        return "MoviesResult{" +
                "MoviesList=" + MoviesList +
                '}';
    }
}
