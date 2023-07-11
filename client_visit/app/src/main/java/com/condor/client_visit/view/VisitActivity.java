package com.condor.client_visit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.condor.client_visit.helpers.ConstantsManager;
import com.condor.client_visit.helpers.SharedPrefCaller;
import com.condor.client_visit.helpers.SimpleMessageDialog;
import com.condor.client_visit.model.Photo;
import com.condor.client_visit.model.Visita;
import com.condor.client_visit.R;
import com.condor.client_visit.databinding.ActivityVisitBinding;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.helpers.LoadingDialog;
import com.condor.client_visit.viewmodel.VisitViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import pub.devrel.easypermissions.EasyPermissions;

public class VisitActivity extends AppCompatActivity {
    ActivityVisitBinding binding;
    VisitViewModel visitMV;

    private String token;
    private String codusur;
    private String visit_id;

    private void getCreds(){
        HashMap<String,String> arr = Lib.getCreds(this);
        token = arr.get(getString(R.string.sh_token));
        codusur = arr.get(getString(R.string.sh_codusur));
        visit_id = arr.get(getString(R.string.sh_idvisita));
    }

    private void onErrorLoadVisit(){
        Toast.makeText(this, getString(R.string.load_visit_error_message), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validteFeilds(){
        return !String.valueOf(binding.emailEdittext.getText()).equals("") &&
                !String.valueOf(binding.telephoneEdittext.getText()).equals("") &&
                !String.valueOf(binding.sellerEdittext.getText()).equals("");
    }

    private boolean hasPermissions(){
        boolean cameraPerm = EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
        return (cameraPerm == true);
    }

    private void showDeinedPermissionMsg(){
        SimpleMessageDialog.showMessage(this,"",getString(R.string.no_cam_stor_per_message));
    }

    //----------------------------------------------------------------------------------------------
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        visitMV = new ViewModelProvider(this).get(VisitViewModel.class);
        //------------------------------------
        
        getCreds();
    }


    @Override
    protected void onResume() {
        super.onResume();
        try{
            LoadingDialog.showLoadingDialog(this);
            visitMV.loadVisit(codusur,visit_id,token).observe(this, new Observer<Visita>() {
                @Override
                public void onChanged(Visita visita) {
                    if(visita == null){
                        onErrorLoadVisit();
                        return;
                    }
                    getSupportActionBar().setTitle(visita.getCnpj());
                    LoadingDialog.closeLoadingDialog();
                }
            });
        }catch (Exception ex){
            onErrorLoadVisit();
        }finally {
            LoadingDialog.closeLoadingDialog();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.vis_fin:
                if(validteFeilds() && visitMV.checkPhotoCondition()){
                       String email = String.valueOf(binding.emailEdittext.getText());
                       String tele = String.valueOf(binding.telephoneEdittext.getText());
                       String repre = String.valueOf(binding.sellerEdittext.getText());
                       String obs = String.valueOf(binding.obsEdittext.getText());
                       try{
                           visitMV.finalizeVisit(visit_id,codusur,email,tele,repre,obs,token).observe(this, new Observer<Boolean>() {
                               @Override
                               public void onChanged(Boolean aBoolean) {
                                    if(aBoolean){
                                        SharedPrefCaller.setSharedPref(VisitActivity.this,"string",getString(R.string.sh_idvisita),null);
                                        Intent intent = new Intent(VisitActivity.this,SplashActivity.class);
                                        startActivity(intent);
                                        VisitActivity.this.finish();
                                    }else{
                                        Toast.makeText(VisitActivity.this, getString(R.string.create_visit_error_message), Toast.LENGTH_SHORT).show();
                                    }
                               }
                           });
                       }catch (Exception ex){
                           Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                }else{
                    SimpleMessageDialog.showMessage(this,"",getString(R.string.finalize_vist_conditions_msg));
                }
                break;
            case R.id.vis_foto:
                String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.no_cam_stor_per_message),
                        ConstantsManager.CAM_WRITE_READ_PERMISSION_REQUEST_CODE,
                        perms);
                if(hasPermissions()){
                    Intent intent = new Intent(VisitActivity.this,PhotosActivity.class);
                    startActivity(intent);
                }else{
                    EasyPermissions.requestPermissions(
                            this,
                            getString(R.string.no_cam_stor_per_message),
                            ConstantsManager.CAM_WRITE_READ_PERMISSION_REQUEST_CODE,
                            perms);
                }
                break;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(ConstantsManager.CAM_WRITE_READ_PERMISSION_REQUEST_CODE,permissions,grantResults,this);
    }
}