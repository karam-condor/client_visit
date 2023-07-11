package com.condor.client_visit.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.condor.client_visit.R;
import com.condor.client_visit.helpers.ConstantsManager;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.model.Photo;
import com.condor.client_visit.view.CameraActivity;
import com.condor.client_visit.view.PhotoGallaryActivity;
import com.condor.client_visit.view.PhotosActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Photo> photos;

    private String TAG = "PhotoAdapter";

    public PhotoAdapter(Activity activity, ArrayList<Photo> photos) {
        this.activity = activity;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return 15;
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView1, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.img_layout,parent,false);
        RelativeLayout itemLayout = convertView.findViewById(R.id.img_master_layout);
        int[] dims = Lib.getScreenSize(activity);
        itemLayout.setLayoutParams(new RelativeLayout.LayoutParams(dims[0]/3,dims[0]/3));
        ImageView imgVw = convertView.findViewById(R.id.photo_img);
        ImageView imgVwIcon = convertView.findViewById(R.id.photo_camera);
        CardView cardView = convertView.findViewById(R.id.photo_card);
        convertView.setOnClickListener(null);
        if(position >= photos.size()){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, CameraActivity.class);
                    activity.startActivityForResult(intent, ConstantsManager.REQUEST_IMAGE_CAPTURE);
                }
            });
        }else{
            Picasso.get().load(photos.get(position).getUrl())
                    .resize(200,200)
                    .onlyScaleDown()
                    .into(imgVw, new Callback() {
                        @Override
                        public void onSuccess() {
                            cardView.setVisibility(View.VISIBLE);
                        }
                         @Override
                        public void onError(Exception e) {
                            imgVwIcon.setImageResource(R.drawable.ic_error_photo);
                        }
                    });
           //open the photo when onClick
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galaryIntent = new Intent(activity, PhotoGallaryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    galaryIntent.putExtras(bundle);
                    activity.startActivity(galaryIntent);
                }
            });
        }
        return convertView;
    }
}
