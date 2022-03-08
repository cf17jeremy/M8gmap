package com.example.m8gmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

public class PgAdater extends PagerAdapter {

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.item_image, view, false);

        final ImageView imageView = imageLayout.findViewById(R.id.imageView);

        /*GlideApp.with(context)
                .load(urls.get(position))
                .into(imageView);
*/
        Objects.requireNonNull(view).addView(imageLayout);

        return imageLayout;
    }
}
