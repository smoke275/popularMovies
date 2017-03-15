package com.smokescreem.shash.popularmovies.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Shash on 3/9/2017.
 */

@Database(version = MovieDatabase.VERSION,
        packageName = "com.smokescreem.shash.popularmovies.provider"
)
public class MovieDatabase {

    public static final int VERSION = 1;
    @Table(Columns.class)
    public static final String MOVIES = "list";
    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + MOVIES;

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {

    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {

    }
}