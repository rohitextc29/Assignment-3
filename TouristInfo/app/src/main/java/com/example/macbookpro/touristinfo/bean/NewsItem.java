package com.example.macbookpro.touristinfo.bean;

/**
 * Created by macbookpro on 09/04/17.
 */

public class NewsItem {
    private int entryid;
    private String title;
    private int viewed;

    public int getEntryid() {
        return entryid;
    }

    public void setEntryid(int entry_id) {
        this.entryid = entry_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
