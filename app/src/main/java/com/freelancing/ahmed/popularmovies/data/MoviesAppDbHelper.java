package com.freelancing.ahmed.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmed on 2/2/2018.
 */

public class MoviesAppDbHelper extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "movies.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MoviesAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesAppContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesAppContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesAppContract.MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                MoviesAppContract.MoviesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_BACK_POSTER + " TEXT NOT NULL, "+
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID +" INTEGER NOT NULL, "+
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_OVERVIEWS+ " TEXT NOT NULL, "+
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_RATE+" REAL NOT NULL, "+
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_TOTAL_VOTES+" INTEGER NOT NULL, "+
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_RELEASE_DATE+" TEXT NOT NULL"+
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesAppContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
