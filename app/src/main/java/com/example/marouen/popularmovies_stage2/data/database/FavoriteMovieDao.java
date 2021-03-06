package com.example.marouen.popularmovies_stage2.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.marouen.popularmovies_stage2.model.Movie;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> loadAllFavMovies();

    @Query("SELECT * FROM favorites WHERE movie_id LIKE :movieId")
    LiveData<Movie> findFavById(String movieId);

    @Query("SELECT * FROM favorites WHERE movie_id LIKE :movieId")
    Movie findMovieById(String movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(Movie favoriteMovie);

    @Delete
    void deleteFavoriteMovie(Movie favoriteMovie);
}
