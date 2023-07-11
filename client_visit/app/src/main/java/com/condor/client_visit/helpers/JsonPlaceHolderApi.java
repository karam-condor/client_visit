package com.condor.client_visit.helpers;

import com.condor.client_visit.model.Cliente;
import com.condor.client_visit.model.Photo;
import com.condor.client_visit.model.Usuario;
import com.condor.client_visit.model.Visita;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @POST("auth/login.php")
    Call<Usuario> login(@Body Usuario usuario);

    @GET("auth/validateuser.php")
    Call<String> validate(@Query("CODUSUR") String codusur);


    @GET("client/search.php")
    Call<List<Cliente>> searchClients(@Query("CODUSUR") String codusur, @Query("PALAVRA_CHAVE") String palavraChave);

    @POST("visit/create.php")
    Call<String> createVisit(@Body Visita visita);


    @GET("visit/load.php")
    Call<Visita> loadVisit(@Query("CODUSUR") String codusur, @Query("ID") String id);

    @PUT("visit/finalize.php")
    Call<String> finalizeVisit(@Body Visita visita);


    @POST("visit/createphoto.php")
    Call<String> createPhoto(@Body Photo photo);

}
