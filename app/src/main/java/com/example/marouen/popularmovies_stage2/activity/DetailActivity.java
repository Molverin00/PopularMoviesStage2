package com.example.marouen.popularmovies_stage2.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.marouen.popularmovies_stage2.BuildConfig;
import com.example.marouen.popularmovies_stage2.R;
import com.example.marouen.popularmovies_stage2.adapter.ReviewAdapter;
import com.example.marouen.popularmovies_stage2.adapter.VideoAdapter;
import com.example.marouen.popularmovies_stage2.data.network.ApiClient;
import com.example.marouen.popularmovies_stage2.data.network.ApiService;
import com.example.marouen.popularmovies_stage2.data.database.AppDatabase;
import com.example.marouen.popularmovies_stage2.model.Movie;
import com.example.marouen.popularmovies_stage2.model.Review;
import com.example.marouen.popularmovies_stage2.model.ReviewsResult;
import com.example.marouen.popularmovies_stage2.model.Video;
import com.example.marouen.popularmovies_stage2.model.VideosResult;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private AppDatabase mDb;

    private final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final String POSTER_SIZE = "w185";

    private String mId;
    private String mTitle;
    private String mPosterUrl;
    private String mSynopsis;
    private String mRating;
    private String mReleaseDate;

    private List<Video> videosList;
    private List<Review> reviewsList;

    @BindView(R.id.tv_title) TextView titleTextView;
    @BindView(R.id.tv_user_rating) TextView userRatingTextView;
    @BindView(R.id.tv_release_date) TextView releaseDateTextView;
    @BindView(R.id.tv_synopsis) TextView synopsisTextView;
    @BindView(R.id.iv_detail_poster) ImageView posterImageView;
    @BindView(R.id.tb_favorite) ToggleButton favoriteToggleButton;
    @BindView(R.id.rv_movie_videos) RecyclerView videosRecyclerView;
    @BindView(R.id.rv_movie_reviews) RecyclerView reviewsRecyclerView;

    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        LinearLayoutManager videosLinearLayoutManager = new LinearLayoutManager(this);
        videosRecyclerView.setLayoutManager(videosLinearLayoutManager);
        videosRecyclerView.setHasFixedSize(false);

        LinearLayoutManager reviewsLinearLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
        reviewsRecyclerView.setHasFixedSize(false);


        // Get movie details from intent
        Intent intent = getIntent();
        mId = intent.getStringExtra("movie_id");
        mTitle = intent.getStringExtra("original_title");
        mSynopsis = intent.getStringExtra("synopsis");
        mRating = intent.getStringExtra("user_rating");
        mReleaseDate = intent.getStringExtra("release_date");
        mPosterUrl = intent.getStringExtra("poster_url");

        //Set text views
        titleTextView.setText(mTitle);
        synopsisTextView.setText(mSynopsis);
        userRatingTextView.setText(mRating);
        releaseDateTextView.setText(mReleaseDate);

        // Set image view
        String posterPath = BASE_URL + POSTER_SIZE + mPosterUrl;
        Picasso.with(DetailActivity.this).load(posterPath).into(posterImageView);


        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Fetch movie videos
        loadVideos(apiService);

        // Fetch movie reviews
        loadReviews(apiService);



        // Set Favorite button
        if (isFavorite(mId))

            favoriteToggleButton.setChecked(true);
        else

            favoriteToggleButton.setChecked(false);



        favoriteToggleButton.setOnClickListener(v -> {

            String toastMessage;

            if (favoriteToggleButton.isChecked()) {

                mDb.favoriteMovieDao().insertFavoriteMovie(
                        new Movie(mId, mTitle, mPosterUrl, mSynopsis, mRating, mReleaseDate)
                );

                favoriteToggleButton.setChecked(true);
                toastMessage = "Added to Favorites";
            } else {

                mDb.favoriteMovieDao().deleteFavoriteMovie(mDb.favoriteMovieDao().findFavById(mId));

                favoriteToggleButton.setChecked(false);
                toastMessage = "Removed from Favorites";
            }

            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        });
    }



    /*
    Fetch videos using API
     */
    private void loadVideos(ApiService apiService) {

        Call<VideosResult> videosResultCall = apiService.getMovieVideos(String.valueOf(mId), BuildConfig.ApiKey);
        videosResultCall.enqueue(new Callback<VideosResult>() {
            @Override
            public void onResponse(@NonNull Call<VideosResult> call, @NonNull Response<VideosResult> response) {

                // Get videos list

                if (response.body() != null) {
                    videosList = response.body().getVideosResult();

                    // Populate videos recycler view
                    videoAdapter = new VideoAdapter(DetailActivity.this, videosList);
                    videosRecyclerView.setAdapter(videoAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideosResult> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }



    /*
    Fetch reviews using API
     */
    private void loadReviews(ApiService apiService) {
        Call<ReviewsResult> reviewsResultCall = apiService.getMovieReviews(String.valueOf(mId), BuildConfig.ApiKey);
        reviewsResultCall.enqueue(new Callback<ReviewsResult>() {
            @Override
            public void onResponse(@NonNull Call<ReviewsResult> call, @NonNull Response<ReviewsResult> response) {

                // Get reviews list

                if (response.body() != null) {
                    reviewsList = response.body().getReviewsResult();

                    //Populate reviews recycler view
                    reviewAdapter = new ReviewAdapter(DetailActivity.this, reviewsList);
                    reviewsRecyclerView.setAdapter(reviewAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewsResult> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }



    /*
    Check if current movie is in Favorites local database
     */
    private boolean isFavorite(String movieId) {

        Movie movie = mDb.favoriteMovieDao().findFavById(movieId);

        return movie != null;
    }
}
