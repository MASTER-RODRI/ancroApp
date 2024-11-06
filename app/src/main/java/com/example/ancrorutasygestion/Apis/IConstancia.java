package com.example.ancrorutasygestion.Apis;

import com.example.ancrorutasygestion.Pojos.PojoConstancia;
import com.example.ancrorutasygestion.Pojos.PojoHojaRuta;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IConstancia {
    @POST("api/Constancia")
    Call<PojoConstancia> AgregarConstancia(@Body PojoConstancia constancia);
}
