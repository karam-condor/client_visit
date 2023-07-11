package com.condor.client_visit.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.condor.client_visit.helpers.GoogleLocation;
import com.condor.client_visit.helpers.JsonPlaceHolderApi;
import com.condor.client_visit.helpers.Lib;
import com.condor.client_visit.model.Cliente;
import com.condor.client_visit.model.Visita;
import com.condor.client_visit.helpers.ConstantsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchViewModel extends AndroidViewModel {

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<List<Cliente>> clienteMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> idMutableLiveData = new MutableLiveData<>();
    private List<Cliente> clientes;
    private String id;
    public LiveData<List<Cliente>> loadClientes(String codudur, String palavaChave,String token){
        Retrofit retrofit = new Retrofit.Builder()
                .client(Lib.setTokenHeader(token))
                .baseUrl(ConstantsManager.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Cliente>> call = jsonPlaceHolderApi.searchClients(codudur,palavaChave);

        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(response.isSuccessful()){
                    clientes = response.body();
                }else{
                    clientes = new ArrayList<>();
                }
                clienteMutableLiveData.setValue(clientes);
            }
            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                clientes = new ArrayList<>();
                clienteMutableLiveData.setValue(clientes);
            }
        });
        return clienteMutableLiveData;
    }


    public LiveData<String> createVisit(String codusur,String codcli,String cnpj,String token){
        Visita visita = new Visita(codusur,codcli,cnpj);
        //Get Location
        GoogleLocation.requestSingleUpdate(getApplication().getApplicationContext(), new GoogleLocation.LocationCallback() {
            @Override
            public void onNewLocationAvailable(GoogleLocation.GPSCoordinates location) {
                visita.setLatitude(location.latitude);
                if(location.latitude == 0.0f)
                    visita.setLatitude(-1000);
                visita.setLongitude(location.longitude);
                if(location.longitude == 0.0f)
                    visita.setLongitude(-1000);

                Retrofit retrofit = new Retrofit.Builder()
                        .client(Lib.setTokenHeader(token))
                        .baseUrl(ConstantsManager.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                Call<String> call = jsonPlaceHolderApi.createVisit(visita);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            id = response.body();
                        }else{
                            id = null;
                        }
                        idMutableLiveData.setValue(id);
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        id= null;
                        clienteMutableLiveData.setValue(clientes);
                    }
                });
            }
        });
        return idMutableLiveData;
    }
}