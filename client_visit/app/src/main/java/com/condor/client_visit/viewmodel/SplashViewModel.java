package com.condor.client_visit.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.condor.client_visit.helpers.ConstantsManager;
import com.condor.client_visit.helpers.JsonPlaceHolderApi;
import com.condor.client_visit.helpers.Lib;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashViewModel extends ViewModel {
    private MutableLiveData<Boolean> isValidMutLiveData = new MutableLiveData<Boolean>();
    private Boolean isValid;
    public LiveData<Boolean> validateToken(String token, String codusur){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsManager.BASE_URL)
                .client(Lib.setTokenHeader(token))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<String> call = jsonPlaceHolderApi.validate(codusur);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    isValid = Boolean.parseBoolean(response.body());
                }else{
                    isValid = false;
                }
                isValidMutLiveData.setValue(isValid);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isValidMutLiveData.setValue(false);
            }
        });
        return isValidMutLiveData;
    }
}
