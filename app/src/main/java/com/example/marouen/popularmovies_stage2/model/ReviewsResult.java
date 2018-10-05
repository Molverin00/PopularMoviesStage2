package com.example.marouen.popularmovies_stage2.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Review> reviewsResult = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Review> getReviewsResult() {
        return reviewsResult;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReviewsResult{" +
                "id=" + id +
                ", reviewsResult=" + reviewsResult +
                '}';
    }
}
