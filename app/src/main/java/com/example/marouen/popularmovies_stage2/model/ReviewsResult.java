package com.example.marouen.popularmovies_stage2.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResult {

    @SerializedName("results")
    @Expose
    private List<Review> reviewsResult = null;


    public List<Review> getReviewsResult() {
        return reviewsResult;
    }


    @NonNull
    @Override
    public String toString() {
        return "ReviewsResult{" +
                "reviewsResult=" + reviewsResult +
                '}';
    }
}
