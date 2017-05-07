package com.example.macbookpro.touristinfo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.macbookpro.touristinfo.bean.LocalNewsBean;
import com.example.macbookpro.touristinfo.bean.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macbookpro on 02/04/17.
 */

public class DatabaseHelper {
    private SPDatabeseOpenHelper mDBHelper;
    private SettingDatabaseOpenHelper mDBSettingHelper;
    private LocalStorageDatabaseOpenHelper mDBLocalHelper;
    private SQLiteDatabase mDB;
    private SQLiteDatabase mDBLocal;

    public DatabaseHelper(Context context,String DATABASE_NAME){
        mDBHelper = new SPDatabeseOpenHelper(context,DATABASE_NAME);
        mDB=mDBHelper.getWritableDatabase();
    }

    public DatabaseHelper(Context context){
        mDBLocalHelper = new LocalStorageDatabaseOpenHelper(context);
        mDBLocal=mDBLocalHelper.getWritableDatabase();
    }

    public void saveInDatabase(String title){
        System.out.println("COLUMNNAME_TITLE -- "+title);
        ContentValues contentValues = new ContentValues();
        contentValues.put(SPDatabeseOpenHelper.COLUMNNAME_TITLE,title);
        mDB.insert(SPDatabeseOpenHelper.TABLE_NAME,null,contentValues);
    }

    public void newsLocalDatabase(String title,String url,String description,String imgpath){
        System.out.println("COLUMNNAME_TITLE -- "+title);
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocalStorageDatabaseOpenHelper.COLUMNNAME_TITLE,title);
        contentValues.put(LocalStorageDatabaseOpenHelper.COLUMNNAME_URL,url);
        contentValues.put(LocalStorageDatabaseOpenHelper.COLUMNNAME_DESCRIPTION,description);
        contentValues.put(LocalStorageDatabaseOpenHelper.COLUMNNAME_IMAGE_PATH,imgpath);

        mDBLocal.insert(LocalStorageDatabaseOpenHelper.TABLE_NAME,null,contentValues);
    }

    public List<LocalNewsBean> getLocalNews(){
        List<LocalNewsBean> beanList=new ArrayList<>();

        String[] requiredColumns = {LocalStorageDatabaseOpenHelper.COLUMNNAME_ENTRYID,LocalStorageDatabaseOpenHelper.COLUMNNAME_TITLE,LocalStorageDatabaseOpenHelper.COLUMNNAME_URL,LocalStorageDatabaseOpenHelper.COLUMNNAME_IMAGE_PATH,LocalStorageDatabaseOpenHelper.COLUMNNAME_DESCRIPTION};
        Cursor cursor = mDBLocal.query(LocalStorageDatabaseOpenHelper.TABLE_NAME,requiredColumns,null,null,null,null,null);
        cursor.moveToFirst();

        while (cursor.moveToNext()){
            LocalNewsBean news =new LocalNewsBean();

            int entryid=cursor.getInt(cursor.getColumnIndex(LocalStorageDatabaseOpenHelper.COLUMNNAME_ENTRYID));
            String title = cursor.getString(cursor.getColumnIndex(LocalStorageDatabaseOpenHelper.COLUMNNAME_TITLE));
            String url = cursor.getString(cursor.getColumnIndex(LocalStorageDatabaseOpenHelper.COLUMNNAME_URL));
            String imagepath = cursor.getString(cursor.getColumnIndex(LocalStorageDatabaseOpenHelper.COLUMNNAME_IMAGE_PATH));
            String description = cursor.getString(cursor.getColumnIndex(LocalStorageDatabaseOpenHelper.COLUMNNAME_DESCRIPTION));
            news.setEntryid(entryid);
            news.setTitle(title);
            news.setUrl(url);
            news.setImagepath(imagepath);
            news.setDescription(description);

            beanList.add(news);
        }

        return beanList;
    }

    public List<NewsItem> getSaveData(){
        List<NewsItem> beanList=new ArrayList<>();

        String[] requiredColumns = {SPDatabeseOpenHelper.COLUMNNAME_ENTRYID,SPDatabeseOpenHelper.COLUMNNAME_TITLE};
        Cursor cursor = mDB.query(SPDatabeseOpenHelper.TABLE_NAME,requiredColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()){
            NewsItem news =new NewsItem();

            int entryid=cursor.getInt(cursor.getColumnIndex(SPDatabeseOpenHelper.COLUMNNAME_ENTRYID));
            String title = cursor.getString(cursor.getColumnIndex(SPDatabeseOpenHelper.COLUMNNAME_TITLE));
            news.setEntryid(entryid);
            news.setTitle(title);

            beanList.add(news);
        }

        return beanList;
    }
}
