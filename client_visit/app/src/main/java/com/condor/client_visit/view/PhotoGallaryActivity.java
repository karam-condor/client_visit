package com.condor.client_visit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.condor.client_visit.R;
import com.condor.client_visit.adapters.PhotoPagerAdapter;
import com.condor.client_visit.databinding.ActivityPhotoGallaryBinding;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.model.Photo;
import com.condor.client_visit.viewmodel.PhotoViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class PhotoGallaryActivity extends AppCompatActivity {

    ActivityPhotoGallaryBinding binding;
    PhotoViewModel photoVM;
    int position = 0;

    private String token;
    private String codusur;
    private String visit_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoGallaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        photoVM = new ViewModelProvider(this).get(PhotoViewModel.class);
        getSupportActionBar().hide();
        getCreds();
        getposition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            photoVM.loadPhotos(codusur,visit_id,token).observe(this, new Observer<ArrayList<Photo>>() {
                @Override
                public void onChanged(ArrayList<Photo> photos) {
                    if(photos != null && photos.size() > 0){
                        PhotoPagerAdapter photoPagerAdapter = new PhotoPagerAdapter(PhotoGallaryActivity.this,photos);
                        binding.photoGallaryVwPager.setAdapter(photoPagerAdapter);
                        binding.photoGallaryVwPager.setCurrentItem(position);
                        binding.photoGallaryVwPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                binding.photoGallaryDescriptionTxtVw.setText(photos.get(position).getDescricao());
                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                    }else{
                        onError(getString(R.string.create_visit_error_message));
                    }
                }
            });
        }catch (Exception ex){
            onError(ex.getMessage());
        }
    }

    private void onError(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        this.finish();
    }
    private void getCreds(){
        HashMap<String,String> arr = Lib.getCreds(this);
        token = arr.get(getString(R.string.sh_token));
        codusur = arr.get(getString(R.string.sh_codusur));
        visit_id = arr.get(getString(R.string.sh_idvisita));
    }

    private void getposition() {
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position",0);
    }

}