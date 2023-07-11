package com.condor.client_visit.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.condor.client_visit.R;
import com.condor.client_visit.adapters.PhotoAdapter;
import com.condor.client_visit.helpers.ConstantsManager;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.helpers.LoadingDialog;
import com.condor.client_visit.model.Photo;
import com.condor.client_visit.databinding.ActivityPhotosBinding;
import com.condor.client_visit.model.Visita;
import com.condor.client_visit.viewmodel.PhotoViewModel;
import com.condor.client_visit.viewmodel.VisitViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotosActivity extends AppCompatActivity {
    ActivityPhotosBinding binding;
    ArrayList<Photo> photos;
    PhotoViewModel photoVM;

    VisitViewModel visitVM;

    private String token;
    private String codusur;
    private String visit_id;

    private void getCreds(){
        HashMap<String,String> arr = Lib.getCreds(this);
        token = arr.get(getString(R.string.sh_token));
        codusur = arr.get(getString(R.string.sh_codusur));
        visit_id = arr.get(getString(R.string.sh_idvisita));
    }


    private void setLayout() {

        getCreds();
        photoVM = new ViewModelProvider(this).get(PhotoViewModel.class);
        visitVM = new ViewModelProvider(this).get(VisitViewModel.class);

    }


    @Override
    protected void onResume() {
        super.onResume();
        try{
            photoVM.loadPhotos(codusur,visit_id,token).observe(this, new Observer<ArrayList<Photo>>() {
                @Override
                public void onChanged(ArrayList<Photo> photos) {
                    if(photos != null){
                        PhotoAdapter photoAdapter = new PhotoAdapter(PhotosActivity.this,photos);
                        binding.photoGridVw.setAdapter(photoAdapter);
                    }
                }
            });
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(String imgPath, String description){
        try{
            LoadingDialog.showLoadingDialog(this);
            Bitmap img = BitmapFactory.decodeFile(imgPath);
            photoVM.savePhoto(visit_id,ConstantsManager.BASE_URL + "visit/fotos/",description,codusur,Lib.getBase64FromImage(img),token).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean) {
                        photoVM.loadPhotos(codusur,visit_id,token).observe(PhotosActivity.this, new Observer<ArrayList<Photo>>() {
                            @Override
                            public void onChanged(ArrayList<Photo> photos) {
                                if(photos != null){
                                    PhotoAdapter photoAdapter = new PhotoAdapter(PhotosActivity.this,photos);
                                    binding.photoGridVw.setAdapter(photoAdapter);
                                    LoadingDialog.closeLoadingDialog();
                                }
                            }
                        });
                    }else{
                        LoadingDialog.closeLoadingDialog();
                    }
                }
            });
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            LoadingDialog.closeLoadingDialog();
        }
    }

    //----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        setLayout();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ConstantsManager.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final String imgPath = data.getStringExtra("imgPath");
            final String description = data.getStringExtra("description");
            saveImage(imgPath,description);
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}