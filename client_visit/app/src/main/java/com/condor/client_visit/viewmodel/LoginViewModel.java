package com.condor.client_visit.viewmodel;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.condor.client_visit.helpers.JsonPlaceHolderApi;
import com.condor.client_visit.model.Usuario;
import com.condor.client_visit.helpers.ConstantsManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Usuario> usuarioMutLiveData = new MutableLiveData<>();

    private Usuario usuario;


    public LiveData<Usuario> login(String usuarioStr, String senhaStr) {
        usuario = new Usuario(usuarioStr, senhaStr);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ConstantsManager.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<Usuario> call = jsonPlaceHolderApi.login(usuario);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                usuario.setMessage(null);
                if(response.isSuccessful()){
                    Usuario respostaUsuario = response.body();
                    usuario.setToken(respostaUsuario.getToken());
                    usuario.setCodusur(respostaUsuario.getCodusur());
                }else{
                    usuario.setMessage("Erro ao acesso " + response.code());
                }
                usuarioMutLiveData.setValue(usuario);
                Log.i("Karam1313", "onResponse: " + response.body());
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                usuario.setMessage(t.getMessage());
                usuarioMutLiveData.setValue(usuario);
                Log.i("Karam1313", "onResponse: " + t.getMessage());
            }
        });
        return usuarioMutLiveData;
    }



}