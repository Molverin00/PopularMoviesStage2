package com.example.marouen.popularmovies_stage2.viewModel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.marouen.popularmovies_stage2.data.database.AppDatabase;
import com.example.marouen.popularmovies_stage2.model.Movie;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movieList;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movieList = database.favoriteMovieDao().loadAllFavMovies();
    }


    public LiveData<List<Movie>> getMovieList() {
        return movieList;
    }
}
