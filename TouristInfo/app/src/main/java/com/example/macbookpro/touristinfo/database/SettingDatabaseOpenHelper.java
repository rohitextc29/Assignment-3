package com.example.macbookpro.touristinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by macbookpro on 13/04/17.
 */

public class SettingDatabaseOpenHelper extends SQLiteOpenHelper{

    private final static int DATABASE_VERSION=1;
    private final static String DATABASE_NAME="Setting.db";

    public final static String TABLE_NAME="user_setting";
    public final static String COLUMNNAME_ENTRYID="entry_id";
    public final static String COLUMNNAME_FONT="font_size";
    public final static String COLUMNNAME_SHOW_IMAGE="show_image";
    public final static String COLUMNNAME_BACKGROUND="background";

    private final static String SQL_CREATE_QUERY="CREATE TABLE "+TABLE_NAME+" ("+COLUMNNAME_ENTRYID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMNNAME_FONT+" TEXT,"+COLUMNNAME_SHOW_IMAGE+" TEXT,"+COLUMNNAME_BACKGROUND+" TEXT);";
    private final static String SQL_DELETE_QUERY="DROP TABLE IF EXISTS "+TABLE_NAME;

    public SettingDatabaseOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("SQL_CREATE_QUERY \n"+SQL_CREATE_QUERY);
        db.execSQL(SQL_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_QUERY);
        onCreate(db);
    }
}
