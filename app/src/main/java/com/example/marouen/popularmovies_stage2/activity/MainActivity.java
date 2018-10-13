package com.example.marouen.popularmovies_stage2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marouen.popularmovies_stage2.BuildConfig;
import com.example.marouen.popularmovies_stage2.R;
import com.example.marouen.popularmovies_stage2.adapter.PosterAdapter;
import com.example.marouen.popularmovies_stage2.data.network.ApiClient;
import com.example.marouen.popularmovies_stage2.data.network.ApiService;
import com.example.marouen.popularmovies_stage2.data.database.AppDatabase;
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

    private AppDatabase mDb;

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

        mDb = AppDatabase.getInstance(getApplicationContext());

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

    private void loadFavoritesMovies() {

        moviesList = mDb.favoriteMovieDao().loadAllFavMovies();

        for(Movie movie: moviesList) {
            Log.d(LOG_TAG, movie.toString());
        }

        // Populate movies grid view
        posterAdapter = new PosterAdapter(MainActivity.this, moviesList);
        moviesRecyclerView.setAdapter(posterAdapter);
    }


    private void refreshMainActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
    }
}
