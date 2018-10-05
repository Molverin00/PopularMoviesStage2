package com.example.marouen.popularmovies_stage2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marouen.popularmovies_stage2.BuildConfig;
import com.example.marouen.popularmovies_stage2.R;
import com.example.marouen.popularmovies_stage2.adapter.PosterAdapter;
import com.example.marouen.popularmovies_stage2.api.ApiClient;
import com.example.marouen.popularmovies_stage2.api.ApiService;
import com.example.marouen.popularmovies_stage2.database.FavoriteMoviesContract;
import com.example.marouen.popularmovies_stage2.model.Movie;
import com.example.marouen.popularmovies_stage2.model.MoviesResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private String sort_type;

    private List<Movie> moviesList;

    @BindView(R.id.rv_movies) RecyclerView moviesRecyclerView;
    private PosterAdapter posterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager moviesGridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(moviesGridLayoutManager);
        moviesRecyclerView.setHasFixedSize(true);


        sharedPreferences = getSharedPreferences("popular_movies",MODE_PRIVATE);
        sort_type = sharedPreferences.getString("sort_type", "popular");

        moviesList = new ArrayList<>();

        switch (sort_type) {

            case "popular":
                setTitle(R.string.activity_label_popular);
                loadMovies();
                break;

            case "top_rated":
                setTitle(R.string.activity_label_top_rated);
                loadMovies();
                break;

            case "favorites":
                setTitle(R.string.activity_label_favorites);
                loadFavoritesMovies();
                break;
        }

        /*
        if(sort_type.equals("popular"))
            setTitle(R.string.activity_label_popular);
        else if(sort_type.equals("top_rated"))
            setTitle(R.string.activity_label_top_rated);
        else if(sort_type.equals("favorites"))
            setTitle(R.string.activity_label_favorites);

        moviesList = new ArrayList<>();

        if(sort_type.equals("favorites"))

            loadFavoritesMovies();
        else

            loadMovies(); */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (itemId) {
            case R.id.menu_main_item_popular:

                // Update sharedPreferences
                editor.putString("sort_type", "popular");
                editor.apply();

                //refresh main activity
                refreshMainActivity();
                break;

            case R.id.menu_main_item_top_rated:

                // Update sharedPreferences
                editor.putString("sort_type", "top_rated");
                editor.apply();

                //refresh main activity
                refreshMainActivity();
                break;

            case R.id.menu_main_item_favorites:

                // Update sharedPreferences
                editor.putString("sort_type", "favorites");
                editor.apply();

                //refresh main activity
                refreshMainActivity();

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    /*
    Fetch movies from internet
     */
    private void loadMovies() {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<MoviesResult> moviesResultCall = apiService.getMovies(sort_type, BuildConfig.ApiKey);
        moviesResultCall.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResult> call, @NonNull Response<MoviesResult> response) {

                // Get movies list
                if (response.body() != null) {
                    moviesList = response.body().getMoviesList();

                    // Populate movies grid view
                    posterAdapter = new PosterAdapter(MainActivity.this, moviesList);
                    moviesRecyclerView.setAdapter(posterAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResult> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

    }



    /*
    Fetch favorites movies from local database
     */
    private void loadFavoritesMovies() {

        /* To query all records */
        Cursor cursor = getContentResolver()
                .query(FavoriteMoviesContract.FavoriteEntry.CONTENT_URI,null,null,null,null);


        if (cursor != null && cursor.moveToFirst()) { //If there are existing favorite items

            moviesList = new ArrayList<>();
            Movie movie;

            Log.d(LOG_TAG, String.valueOf(cursor.getCount()));

            while (!cursor.isAfterLast()) {

                movie = new Movie(

                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID))),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_POSTER_URL)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_SYNOPSIS)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_USER_RATING)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE))
                );

                Log.d(LOG_TAG, movie.toString());

                moviesList.add(movie);

                cursor.moveToNext();
            }

            cursor.close();

            // Populate movies grid view
            posterAdapter = new PosterAdapter(MainActivity.this, moviesList);
            moviesRecyclerView.setAdapter(posterAdapter);


        } else {
            Toast.makeText(this, "Favorites is empty", Toast.LENGTH_SHORT).show();
        }

    }



    private void refreshMainActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
    }
}
