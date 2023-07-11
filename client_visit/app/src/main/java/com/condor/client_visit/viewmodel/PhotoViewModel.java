package com.condor.client_visit.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.condor.client_visit.helpers.ConstantsManager;
import com.condor.client_visit.helpers.JsonPlaceHolderApi;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.model.Photo;
import com.condor.client_visit.model.Visita;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoViewModel extends ViewModel {
    MutableLiveData<Boolean> photoMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<Photo>>  photosMutableLiveData = new MutableLiveData<>();
    Boolean photoResponse;
    Visita visita;



    public LiveData<Boolean> savePhoto(String id_visita,String url,String descricao,String codusur,String base64,String token) {
        Photo photo = new Photo(id_visita, url, descricao, base64, codusur);
        Retrofit retrofit = new Retrofit.Builder()
                .client(Lib.setTokenHeader(token))
                .baseUrl(ConstantsManager.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<String> call = jsonPlaceHolderApi.createPhoto(photo);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    photoResponse = Boolean.parseBoolean(response.body());
                } else {
                    photoResponse = false;
                }
                photoMutableLiveData.setValue(photoResponse);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                photoMutableLiveData.setValue(false);
            }
        });
        return photoMutableLiveData;
    }



    public  LiveData<ArrayList<Photo>> loadPhotos(String codusur, String id, String token){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantsManager.BASE_URL)
                    .client(Lib.setTokenHeader(token))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Visita> call = jsonPlaceHolderApi.loadVisit(codusur,id);

            call.enqueue(new Callback<Visita>() {
                @Override
                public void onResponse(Call<Visita> call, Response<Visita> response) {
                    if(response.isSuccessful()) {
                        visita = response.body();
                    }else{
                        visita = new Visita();
                        visita.setFotos(visita.getFotos());
                    }
                    photosMutableLiveData.setValue(visita.getFotos());
                }

                @Override
                public void onFailure(Call<Visita> call, Throwable t) {
                    photosMutableLiveData.setValue(new ArrayList<Photo>());
                }
            });
            return photosMutableLiveData;
    }
}
