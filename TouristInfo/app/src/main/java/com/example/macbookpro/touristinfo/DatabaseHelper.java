package com.example.macbookpro.touristinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 02/04/17.
 */

public class DatabaseHelper {
    private SPDatabeseOpenHelper mDBHelper;
    private SettingDatabaseOpenHelper mDBSettingHelper;
    private SQLiteDatabase mDB;
    private SQLiteDatabase mDBSetting;

    public DatabaseHelper(Context context,String DATABASE_NAME){
        mDBHelper = new SPDatabeseOpenHelper(context,DATABASE_NAME);
        mDBSettingHelper = new SettingDatabaseOpenHelper(context);
        mDB=mDBHelper.getWritableDatabase();
        mDBSetting=mDBSettingHelper.getWritableDatabase();
    }

    public DatabaseHelper(Context context){
        mDBSettingHelper = new SettingDatabaseOpenHelper(context);
        mDBSetting=mDBSettingHelper.getWritableDatabase();
    }

    public void saveInDatabase(String title){
        System.out.println("COLUMNNAME_TITLE -- "+title);
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPDatabeseOpenHelper.COLUMNNAME_TITLE,title);
        mDB.insert(SPDatabeseOpenHelper.TABLE_NAME,null,contentValues);
    }

    public void saveSettingInDatabase(SettingItem item){
        System.out.println("COLUMNNAME_TITLE -- "+item.getFont_size());
        ContentValues contentValues = new ContentValues();
        contentValues.put(SettingDatabaseOpenHelper.COLUMNNAME_FONT,item.getFont_size());
        contentValues.put(SettingDatabaseOpenHelper.COLUMNNAME_SHOW_IMAGE,item.getShow_image());
        contentValues.put(SettingDatabaseOpenHelper.COLUMNNAME_BACKGROUND,item.getBackground());
        mDBSetting.insert(SettingDatabaseOpenHelper.TABLE_NAME,null,contentValues);
    }

    public List<NewsItem> getSaveData(){
        List<NewsItem> beanList=new ArrayList<>();

        String[] requiredColumns = {SPDatabeseOpenHelper.COLUMNNAME_ENTRYID,SPDatabeseOpenHelper.COLUMNNAME_TITLE};
        Cursor cursor = mDB.query(SPDatabeseOpenHelper.TABLE_NAME,requiredColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            NewsItem news =new NewsItem();

            int entry_id=cursor.getInt(cursor.getColumnIndex(SPDatabeseOpenHelper.COLUMNNAME_ENTRYID));
            String title = cursor.getString(cursor.getColumnIndex(SPDatabeseOpenHelper.COLUMNNAME_TITLE));
            news.setEntry_id(entry_id);
            news.setTitle(title);

            beanList.add(news);
        }

        return beanList;
    }

    public SettingItem getSaveSettingData(){

        SettingItem setting =null;

        String[] requiredColumns = {SettingDatabaseOpenHelper.COLUMNNAME_FONT,SettingDatabaseOpenHelper.COLUMNNAME_SHOW_IMAGE,SettingDatabaseOpenHelper.COLUMNNAME_BACKGROUND};
        Cursor cursor = mDBSetting.query(SettingDatabaseOpenHelper.TABLE_NAME,requiredColumns,null,null,null,null,SettingDatabaseOpenHelper.COLUMNNAME_ENTRYID+" desc");
        cursor.moveToFirst();
        if (cursor.moveToNext()){
            setting =new SettingItem();
            String font_size = cursor.getString(cursor.getColumnIndex(SettingDatabaseOpenHelper.COLUMNNAME_FONT));
            String show_image = cursor.getString(cursor.getColumnIndex(SettingDatabaseOpenHelper.COLUMNNAME_SHOW_IMAGE));
            String background = cursor.getString(cursor.getColumnIndex(SettingDatabaseOpenHelper.COLUMNNAME_BACKGROUND));
            setting.setFont_size(font_size);
            setting.setShow_image(show_image);
            setting.setBackground(background);
        }

        return setting;
    }

}
