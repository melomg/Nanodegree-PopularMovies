package com.projects.melih.popularmovies.repository.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import timber.log.Timber;

import static com.projects.melih.popularmovies.repository.local.FavoritesContract.FavoriteEntry.COLUMN_MOVIE_ID;
import static com.projects.melih.popularmovies.repository.local.FavoritesContract.FavoriteEntry.TABLE_NAME;

public class FavoritesContentProvider extends ContentProvider {
    @SuppressWarnings("WeakerAccess")
    public static final int FAVORITES = 100;
    @SuppressWarnings("WeakerAccess")
    public static final int FAVORITE_WITH_ID = 101;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    private FavoritesDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;
        switch (URI_MATCHER.match(uri)) {
            // Query for the tasks directory
            case FAVORITES:
                retCursor = dbHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_WITH_ID:
                retCursor = dbHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context context = getContext();
        if (context != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (URI_MATCHER.match(uri)) {
            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoriteEntry.CONTENT_URI, id);
                } else {
                    Timber.e("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int numRowsDeleted;
        switch (match) {
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                numRowsDeleted = db.delete(TABLE_NAME, COLUMN_MOVIE_ID + " = ?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            Context context = getContext();
            if (context != null) {
                context.getContentResolver().notifyChange(uri, null);
            }
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, FavoritesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(authority, FavoritesContract.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);
        return uriMatcher;
    }
}
