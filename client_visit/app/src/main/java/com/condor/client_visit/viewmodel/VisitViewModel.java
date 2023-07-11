package com.condor.client_visit.viewmodel;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

public class VisitViewModel extends AndroidViewModel {

    private MutableLiveData<Visita> visitMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> finalizeMutableLiveData = new MutableLiveData<>();
    private Visita visita;
    private Boolean isFinalized;

    public VisitViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Visita> loadVisit(String codusur, String id, String token){
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
                    visita = null;
                }
                visitMutableLiveData.setValue(visita);
            }

            @Override
            public void onFailure(Call<Visita> call, Throwable t) {
                visitMutableLiveData.setValue(null);
            }
        });
        Gson gson = new Gson();
        Visita vi = gson.fromJson("{\"ID\":\"22\",\"CODUSUR\":\"6842\",\"CNPJ\":\"45.316.728\\/0001-65\",\"CODCLI\":\"408251\",\"EMAIL\":null,\"TELEFONE\":null,\"REPRESENTANTE\":null,\"OBS\":null,\"LATITUDE\":\"37.422672\",\"LONGITUDE\":\"-122.084984\",\"fotos\":[]}",Visita.class);
        return visitMutableLiveData;
    }

    public LiveData<Boolean> finalizeVisit(String id, String codusur, String email, String telefone, String representante, String obs, String token){
        Visita vis = new Visita(id,codusur,email,telefone,representante,obs);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsManager.BASE_URL)
                .client(Lib.setTokenHeader(token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<String> call = jsonPlaceHolderApi.finalizeVisit(vis);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    isFinalized = Boolean.parseBoolean(response.body());
                }else{
                    isFinalized = false;
                }
                finalizeMutableLiveData.setValue(isFinalized);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                finalizeMutableLiveData.setValue(false);
            }
        });
        return finalizeMutableLiveData;
    }




    public boolean checkPhotoCondition(){
        return visitMutableLiveData.getValue().getFotos() != null && visitMutableLiveData.getValue().getFotos().size() > 0;
    }

    public String getClientCnpj(){
        return visitMutableLiveData.getValue().getCnpj();
    }

    public String getPhotosListJson(){
        Gson gson = new Gson();
        return gson.toJson(visitMutableLiveData.getValue().getFotos());
    }

    public ArrayList<Photo> getPhotos(){
        return visitMutableLiveData.getValue().getFotos();
    }

}
