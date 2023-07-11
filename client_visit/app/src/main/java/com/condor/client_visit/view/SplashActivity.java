package com.condor.client_visit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.condor.client_visit.R;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.helpers.LoadingDialog;
import com.condor.client_visit.helpers.SimpleMessageDialog;
import com.condor.client_visit.viewmodel.SplashViewModel;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    SplashViewModel splashVM;
    private String token;
    private String codusur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setCreds();
        getSupportActionBar().hide();
        splashVM = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    @Override
    protected void onResume() {
        validateUser();
        super.onResume();
    }

    private void setCreds(){
        HashMap<String,String> arr = Lib.getCreds(this);
        token = arr.get(getString(R.string.sh_token));
        codusur = arr.get(getString(R.string.sh_codusur));
    }
    private void navigateNext(boolean needLogin){
        Intent searchIntent = new Intent(SplashActivity.this,needLogin ? LoginActivity.class : SearchActivity.class);
        startActivity(searchIntent);
        this.finish();
    }


    private void validateUser(){
        if(token != ""){
            try{
                splashVM.validateToken(token,codusur).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean){
                            LoadingDialog.closeLoadingDialog();
                            navigateNext(false);
                            return;
                        }
                        navigateNext(true);
                    }
                });
            }catch (Exception ex){
                SimpleMessageDialog.showMessage(SplashActivity.this,"Erro",ex.getMessage());
                navigateNext(true);
            }
            return;
        }
        navigateNext(true);
    }


}