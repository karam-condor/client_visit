package com.condor.client_visit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.condor.client_visit.helpers.SharedPrefCaller;
import com.condor.client_visit.helpers.SimpleMessageDialog;
import com.condor.client_visit.model.Usuario;
import com.condor.client_visit.viewmodel.LoginViewModel;
import com.condor.client_visit.R;
import com.condor.client_visit.databinding.ActivityLoginBinding;
import com.condor.client_visit.helpers.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private LoginViewModel loginMV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginMV =new ViewModelProvider(this).get(LoginViewModel.class);

        getSupportActionBar().hide();

        binding.loginButton.setOnClickListener(this);
    }



    private void navigateNext(){
        Intent searchIntent = new Intent(LoginActivity.this,SearchActivity.class);
        startActivity(searchIntent);
        LoginActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.loginButton.getId()){
            String usuario = String.valueOf(binding.usernameEdittext.getText()).trim().toUpperCase();
            String senha = String.valueOf(binding.passwordEdittext.getText()).trim();
            if(usuario.equals("") | senha.equals("")){
                Toast.makeText(LoginActivity.this, getString(R.string.login_invalid_user_pass_msg), Toast.LENGTH_SHORT).show();
                return;
            }
            LoadingDialog.showLoadingDialog(this);
            try{
                loginMV.login(usuario,senha).observe(this, new Observer<Usuario>() {
                    @Override
                    public void onChanged(Usuario usuario) {
                        if(usuario.getToken() == null | String.valueOf(usuario.getToken()) == ""){
                            Toast.makeText(LoginActivity.this, usuario.getMessage(), Toast.LENGTH_LONG).show();
                        }else{
                            SharedPrefCaller.setSharedPref(LoginActivity.this,"string",getString(R.string.sh_token),usuario.getToken());
                            SharedPrefCaller.setSharedPref(LoginActivity.this,"string",getString(R.string.sh_codusur),usuario.getCodusur());
                            navigateNext();
                        }
                        LoadingDialog.closeLoadingDialog();
                    }
                });
            }catch (Exception ex){
                SimpleMessageDialog.showMessage(this,"Erro",ex.getMessage());
                LoadingDialog.closeLoadingDialog();
            }
        }
    }
}