package com.example.marouen.popularmovies_stage2.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.marouen.popularmovies_stage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    /* Inner class that defines the table contents of the Favorite table */
    public static final class FavoriteEntry implements BaseColumns {
        /* Table name used by our database*/
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "original_title";
        public static final String COLUMN_MOVIE_POSTER_URL = "poster_url";
        public static final String COLUMN_MOVIE_SYNOPSIS = "synopsis";
        public static final String COLUMN_MOVIE_USER_RATING = "user_rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";


        /* The base CONTENT_URI used to query the Favorites table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        /**
         * Builds a URI that adds the task _ID to the end of the Favorites content URI path.
         * This is used to query details about a single Favorite entry by _ID. This is what we
         * use for the detail view query.
         *
         * @param id Unique id pointing to that row
         * @return Uri to query details about a single Favorite entry
         */
        public static Uri buildFavoritesUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
