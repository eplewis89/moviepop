package com.udacity.erin.lewis.moviepop.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Erin Lewis on 2/16/2017.
 */

public class StarContentProvider extends ContentProvider {
    private StarDbHelper dbHelper;

    public static final int STARS = 100;
    public static final int STAR_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(StarContract.AUTHORITY, StarContract.PATH_STARS, STARS);
        matcher.addURI(StarContract.AUTHORITY, StarContract.PATH_STARS + "/#", STAR_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new StarDbHelper(getContext());

        return true;
    }

    // create
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri returi;

        switch (sUriMatcher.match(uri)) {
            case STARS: {
                long id = db.insert(StarContract.StarEntry.TABLE_NAME, null, values);

                if (id > 0) {
                    returi = ContentUris.withAppendedId(StarContract.StarEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returi;
    }

    // read
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor retcur;

        switch (sUriMatcher.match(uri)) {
            case STARS: {
                retcur = db.query(StarContract.StarEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        retcur.setNotificationUri(getContext().getContentResolver(), uri);

        return retcur;
    }

    // update
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    // delete
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int starsdeleted = 0;

        switch (sUriMatcher.match(uri)) {
            case STAR_WITH_ID: {
                String id = uri.getPathSegments().get(1);

                starsdeleted = db.delete(StarContract.StarEntry.TABLE_NAME, "_id=?", new String[]{id});

                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (starsdeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return starsdeleted;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
