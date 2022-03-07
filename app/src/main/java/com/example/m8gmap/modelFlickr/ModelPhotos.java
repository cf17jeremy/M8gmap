package com.example.m8gmap.modelFlickr;

public class ModelPhotos {
    public int page,pages,perpage,total;
    public ModelPhoto photo;

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public int getTotal() {
        return total;
    }

    public ModelPhoto getPhoto() {
        return photo;
    }
}
