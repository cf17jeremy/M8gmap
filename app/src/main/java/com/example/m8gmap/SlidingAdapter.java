package com.example.m8gmap;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class SlidingAdapter extends PagerAdapter {
    private ArrayList<String> urls;
    private Context context;

    public SlidingAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
