package com.freelancing.ahmed.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.freelancing.ahmed.popularmovies.data.MoviesAppContract.MoviesEntry.TABLE_NAME;

/**
 * Created by ahmed on 2/3/2018.
 */

public class MoviesContentProvider extends ContentProvider {
    private MoviesAppDbHelper mMoviesAppDbHelper;


    // URI MATCHER For the Content Provider
    public final static int MOVIES = 100;
    public final static int MOVIES_WITH_ID =101;

    private static final UriMatcher mMoviesUriMatcher = buildMoviesUriMatcher();
    public static UriMatcher buildMoviesUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Directory Movies
        uriMatcher.addURI(MoviesAppContract.AUTHORITY, MoviesAppContract.PATH_FAV_MOVIES,MOVIES);
        // Single Movie
        uriMatcher.addURI(MoviesAppContract.AUTHORITY,MoviesAppContract.PATH_FAV_MOVIES+"/#",MOVIES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context=getContext();
        mMoviesAppDbHelper = new MoviesAppDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db=mMoviesAppDbHelper.getReadableDatabase();
        int matching = mMoviesUriMatcher.match(uri);
        Cursor moviesCursor;
        switch (matching){
            case MOVIES:
                moviesCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_WITH_ID:
                String idquery = uri.getLastPathSegment();
                // Use selections/selectionArgs to filter for this ID
                moviesCursor = db.query(TABLE_NAME,
                        projection,
                        MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID+"=?",
                        new String[]{idquery},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        moviesCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return moviesCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db=mMoviesAppDbHelper.getWritableDatabase();
        int matching = mMoviesUriMatcher.match(uri);

        Uri resultUri;
        switch (matching){
            case MOVIES:
                long id = db.insert(TABLE_NAME,null,contentValues);
                // if the insert method didn't apply successfully it will return -1 to be stored in id variable
                if ( id > 0){ // means the insert method applied successfully
                    resultUri= ContentUris.withAppendedId(MoviesAppContract.MoviesEntry.CONTENT_URI,id);
                }
                else{
                    throw new SQLException("Failed to insert row into "+uri);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        // notifing the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri,null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db=mMoviesAppDbHelper.getWritableDatabase();
        int matching = mMoviesUriMatcher.match(uri);
        int favDeleted;
        switch (matching){
            case MOVIES_WITH_ID:
                String id = uri.getLastPathSegment();

                // Use selections/selectionArgs to filter for this ID
                favDeleted = db.delete(MoviesAppContract.MoviesEntry.TABLE_NAME, MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID+"=?",new String[]{id});

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);

        }

        if (favDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return favDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
