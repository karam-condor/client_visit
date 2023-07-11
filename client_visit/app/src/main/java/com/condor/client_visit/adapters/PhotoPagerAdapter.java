package com.condor.client_visit.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.condor.client_visit.R;
import com.condor.client_visit.model.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoPagerAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<Photo> items;

    public PhotoPagerAdapter(Activity activity, ArrayList<Photo> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imgVw = new ImageView(activity);
        Picasso.get().load(items.get(position).getUrl())
                .resize(600,780)
                .onlyScaleDown()
                .rotate(90)
                .into(imgVw, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        imgVw.setImageResource(R.drawable.ic_error_photo);
                    }
                });
        container.addView(imgVw);
        return imgVw;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
