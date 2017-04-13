package com.example.macbookpro.touristinfo;

/**
 * Created by macbookpro on 09/04/17.
 */

public class NewsItem {
    private int entry_id;
    private String title;
    private int viewed;

    public int getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(int entry_id) {
        this.entry_id = entry_id;
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
