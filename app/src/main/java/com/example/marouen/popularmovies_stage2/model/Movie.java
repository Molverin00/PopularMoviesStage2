package com.example.marouen.popularmovies_stage2.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favorites")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    private String movieId;

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String originalTitle;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = "synopsis")
    @SerializedName("overview")
    private String synopsis;

    @ColumnInfo(name = "user_rating")
    @SerializedName("vote_average")
    private String userRating;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;


    public Movie(int id, String movieId, String originalTitle, String posterPath, String synopsis, String userRating, String releaseDate) {
        this._id = id;
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }


    @Ignore
    public Movie(String movieId, String originalTitle, String posterPath, String synopsis, String userRating, String releaseDate) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }


    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + _id +
                ", movieId='" + movieId + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", userRating='" + userRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
