package com.projects.melih.popularmovies.repository.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class FavoritesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoritesDb.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String REAL_NOT_NULL = " REAL NOT NULL";
    private static final String COMMA_AND_SPACE = ", ";

    FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavoritesContract.FavoriteEntry.TABLE_NAME + " (" +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_TITLE + TEXT_NOT_NULL + COMMA_AND_SPACE +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_SYNOPSIS + TEXT_NOT_NULL + COMMA_AND_SPACE +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE + TEXT_NOT_NULL + COMMA_AND_SPACE +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_AVERAGE_RATE + REAL_NOT_NULL + COMMA_AND_SPACE +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_POSTER_PATH + TEXT_NOT_NULL + COMMA_AND_SPACE +
                FavoritesContract.FavoriteEntry.COLUMN_MOVIE_BACKDROP_PATH + TEXT_NOT_NULL + ");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}