package com.example.marouen.popularmovies_stage2.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class FavoritesProvider extends ContentProvider {

    private FavoritesDbHelper mOpenHelper;

    /*
     * These constant will be used to match URIs with the data we are looking for. We will take
     * advantage of the UriMatcher class to make that matching MUCH easier than doing something
     * ourselves, such as using regular expressions.
     */
    public static final int CODE_FAVORITE = 100;
    public static final int CODE_FAVORITES_WITH_ID = 101;

    /* The URI Matcher used by this content provider. */
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     * Creates the UriMatcher that will match each URI to the CODE_FAVORITE and
     * CODE_FAVORITES_WITH_ID constants defined above.
     *
     * UriMatcher does all the hard work for you. You just have to tell it which code to match
     * with which URI, and it does the rest automatically.
     *
     * @return A UriMatcher that correctly matches the constants for CODE_FAVORITE and CODE_FAVORITES_WITH_ID
     */
    public static UriMatcher buildUriMatcher() {

        /*
         * All paths added to the UriMatcher have a corresponding code to return when a match is
         * found. The code passed into the constructor of UriMatcher here represents the code to
         * return for the root URI. It's common to use NO_MATCH as the code for this case.
         */
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        /*
         * For each type of URI you want to add, create a corresponding code. Preferably, these are
         * constant fields in your class so that you can use them throughout the class and you no
         * they aren't going to change. In Favorites, we use CODE_FAVORITE or CODE_FAVORITES_WITH_ID.
         */

        /* This URI is content://com.example.marouen.popularmovies_stage2/favorites/ */
        matcher.addURI(authority, FavoriteMoviesContract.FavoriteEntry.TABLE_NAME, CODE_FAVORITE);

        /*
         * This URI would look something like content://com.example.marouen.popularmovies_stage2/favorites/1
         * The "/#" signifies to the UriMatcher that if TABLE_NAME is followed by ANY number,
         * that it should return the CODE_FAVORITES_WITH_ID code
         */
        matcher.addURI(authority, FavoriteMoviesContract.FavoriteEntry.TABLE_NAME + "/#", CODE_FAVORITES_WITH_ID);

        return matcher;
    }

    /**
     * In onCreate, we initialize our content provider on startup. This method is called for all
     * registered content providers on the application main thread at application launch time.
     * It must not perform lengthy operations, or application startup will be delayed.
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new FavoritesDbHelper(getContext());
        return mOpenHelper != null;
    }


    /**
     * Handles query requests from clients. We will use this method to query for all
     * of our Favorites data as well as to query for the specific Favorite record.
     *
     * @param uri           The URI to query
     * @param projection    The list of columns to put into the cursor. If null, all columns are
     *                      included.
     * @param selection     A selection criteria to apply when filtering rows. If null, then all
     *                      rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the
     *                      selection.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return A Cursor containing the results of the query. In our implementation,
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            /*
             * When sUriMatcher's match method is called with a URI that looks something like this
             *
             *      content://com.example.marouen.popularmovies_stage2/favorites/2
             *
             * sUriMatcher's match method will return the code that indicates to us that we need
             * to return the Favorite for a particular id. The id in this code is encoded in
             * int and is at the very end of the URI (2) and can be accessed
             * programmatically using Uri's getLastPathSegment method.
             *
             * In this case, we want to return a cursor that contains one row of Favorites data for
             * a particular date.
             */
            case CODE_FAVORITES_WITH_ID: {

                /*
                 * In order to determine the id associated with this URI, we look at the last
                 * path segment.
                 */
                String _ID = uri.getLastPathSegment();

                /*
                 * The query method accepts a string array of arguments, as there may be more
                 * than one "?" in the selection statement. Even though in our case, we only have
                 * one "?", we have to create a string array that only contains one element
                 * because this method signature accepts a string array.
                 */
                String[] selectionArguments = new String[]{_ID};

                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        FavoriteMoviesContract.FavoriteEntry.TABLE_NAME,
                        /*
                         * A projection designates the columns we want returned in our Cursor.
                         * Passing null will return all columns of data within the Cursor.
                         * However, if you don't need all the data from the table, it's best
                         * practice to limit the columns returned in the Cursor with a projection.
                         */
                        projection,
                        /*
                         * The URI that matches CODE_TODO_WITH_ID contains a id at the end
                         * of it. We extract that id and use it with these next two lines to
                         * specify the row of Favorite we want returned in the cursor. We use a
                         * question mark here and then designate selectionArguments as the next
                         * argument for performance reasons. Whatever Strings are contained
                         * within the selectionArguments array will be inserted into the
                         * selection statement by SQLite under the hood.
                         */
                        FavoriteMoviesContract.FavoriteEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            /*
             * When sUriMatcher's match method is called with a URI that looks EXACTLY like this
             *
             *      content://com.example.marouen.popularmovies_stage2/favorites
             *
             * sUriMatcher's match method will return the code that indicates to us that we need
             * to return all of the records in our favorites table.
             *
             * In this case, we want to return a cursor that contains every record
             * in our Favorites table.
             */
            case CODE_FAVORITE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:

                long _id = db.insert(FavoriteMoviesContract.FavoriteEntry.TABLE_NAME, null, values);

                /* if _id is equal to -1 insertion failed */
                if (_id != -1) {
                    /*
                     * This will help to broadcast that database has been changed,
                     * and will inform entities to perform automatic update.
                     */
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return FavoriteMoviesContract.FavoriteEntry.buildFavoritesUriWithId(_id);

            default:
                return null;
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        if (selection == null) {
            selection = "1";
        }
        int deleted = database.delete(FavoriteMoviesContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
