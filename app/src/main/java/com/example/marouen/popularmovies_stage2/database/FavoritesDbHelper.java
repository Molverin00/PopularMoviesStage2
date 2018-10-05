package com.example.marouen.popularmovies_stage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our Favorites data.
         */
        final String SQL_CREATE_TODO_TABLE =

                "CREATE TABLE " + FavoriteMoviesContract.FavoriteEntry.TABLE_NAME + " (" +

                        FavoriteMoviesContract.FavoriteEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_ID       + " TEXT NOT NULL, "                 +

                        FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_TITLE       + " TEXT NOT NULL, "                 +

                        FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_POSTER_URL       + " TEXT NOT NULL, "                 +

                        FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_SYNOPSIS       + " TEXT NOT NULL, "                 +

                        FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_USER_RATING       + " TEXT NOT NULL, "                 +

                        FavoriteMoviesContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE     + " TEXT NOT NULL);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
