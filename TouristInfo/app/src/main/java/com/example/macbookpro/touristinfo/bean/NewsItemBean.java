package com.example.macbookpro.touristinfo.bean;

import java.util.HashMap;

/**
 * Created by macbookpro on 09/04/17.
 */

public class NewsItemBean {

    private String id;
    private String name;
    private String description;
    private String url;
    private HashMap<String,String> iconMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getIconMap() {
        return iconMap;
    }

    public void setIconMap(HashMap<String, String> iconMap) {
        this.iconMap = iconMap;
    }
}
