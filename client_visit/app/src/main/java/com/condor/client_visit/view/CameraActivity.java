package com.condor.client_visit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.condor.client_visit.R;

public class CameraActivity extends AppCompatActivity {

    private Fragment fragment;

    private String imgPath;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //Set the layout controls
        setLayout();
    }

    private void setLayout() {
        getSupportActionBar().hide();
        fragment = new CameraPreviewFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.camera_container_layout,fragment).addToBackStack(null).commit();
    }

    public void sendImageResult(String imgPath,String description){
        final Intent returnIntent = new Intent();
        returnIntent.putExtra("imgPath",imgPath);
        returnIntent.putExtra("description",description);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}