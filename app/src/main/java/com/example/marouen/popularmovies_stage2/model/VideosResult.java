package com.example.marouen.popularmovies_stage2.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> videosResult = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getVideosResult() {
        return videosResult;
    }

    @NonNull
    @Override
    public String toString() {
        return "VideosResult{" +
                "id=" + id +
                ", videosResult=" + videosResult +
                '}';
    }
}
