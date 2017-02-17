package com.udacity.erin.lewis.moviepop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erin Lewis on 2/16/2017.
 */

public class StarDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "starsdb.db";
    private static final int DATABASE_VERSION = 1;

    StarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_QUERY = "CREATE TABLE " + StarContract.StarEntry.TABLE_NAME + " (" +
                StarContract.StarEntry._ID          + " INTEGER PRIMARY KEY, " +
                StarContract.StarEntry.COLUMN_MOVIE_ID          + " INTEGER NOT NULL, " +
                StarContract.StarEntry.COLUMN_MOVIE_JSON          + " TEXT NOT NULL" +
                ");";

        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + StarContract.StarEntry.TABLE_NAME + ";");
        onCreate(db);
    }
}
