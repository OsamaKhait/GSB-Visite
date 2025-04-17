package com.example.gsb_visite;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface gsbServices {

    @POST("visiteur/login")
    Call<LoginResponse> login(@Body Visiteur visiteur);

    @POST("visite")
    Call<Void> creerRapport(@Header("Authorization") String token, @Body Visite visite);

    @GET("visiteur/{id}")
    Call<Visiteur> getVisiteur(@Header("Authorization") String token, @Path("id") String visiteurId);

    @GET("praticien")
    Call<List<Praticien>> getPraticiens(@Header("Authorization") String token);

    @GET("praticien/{id}")
    Call<Praticien> getPraticienDetails(@Header("Authorization") String token, @Path("id") String praticienId);

    @GET("visite")
    Call<List<Visite>> getVisites(@Header("Authorization") String token);

    @GET("motif")
    Call<List<Motif>> getMotifs(@Header("Authorization") String token);

    @GET("motif/{id}")
    Call<Motif> getMotifDetails(@Header("Authorization") String token, @Path("id") String motifId);


}