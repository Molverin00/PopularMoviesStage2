package com.example.marouen.popularmovies_stage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.marouen.popularmovies_stage2.R;
import com.example.marouen.popularmovies_stage2.activity.DetailActivity;
import com.example.marouen.popularmovies_stage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.MovieViewHolder> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> movieList;

    private final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final String POSTER_SIZE = "w185";


    public PosterAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_grid_movie_item, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {

        Movie movie = movieList.get(i);
        String posterPath = BASE_URL + POSTER_SIZE + movie.getPosterPath();

        // Set movie poster
        Picasso.with(mContext).load(posterPath).into(movieViewHolder.posterImageView);

        // Set OnClickListener on image
        movieViewHolder.posterImageView.setOnClickListener(v -> {

            Log.d(LOG_TAG, movie.getMovieId() + " : " + movie.getOriginalTitle());

            // Go to movie details activity
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("movie_id", movie.getMovieId());
            intent.putExtra("poster_url", movie.getPosterPath());
            intent.putExtra("original_title", movie.getOriginalTitle());
            intent.putExtra("synopsis",movie.getSynopsis());
            intent.putExtra("user_rating", movie.getUserRating());
            intent.putExtra("release_date", movie.getReleaseDate());

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            posterImageView = itemView.findViewById(R.id.iv_poster);
        }
    }
}
