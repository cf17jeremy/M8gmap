package com.example.m8gmap.modelFlickr;

import java.util.ArrayList;
import java.util.List;

public class ModelPhotos {
    private int page;
    private int pages;
    private int perpage;
    private String total;
    private List<ModelPhoto> photo = new ArrayList<ModelPhoto>();

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public String getTotal() {
        return total;
    }

    public List<ModelPhoto> getPhoto() {
        return photo;
    }
}
