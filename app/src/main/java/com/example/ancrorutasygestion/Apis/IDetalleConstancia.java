package com.example.ancrorutasygestion.Apis;

import com.example.ancrorutasygestion.Pojos.PojoConstancia;
import com.example.ancrorutasygestion.Pojos.PojoDetalleDeContancia;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IDetalleConstancia {

    @POST("api/ConstanciaDetalle")
    Call<Boolean> AgregarDatosDeConstancia(@Body ArrayList<PojoDetalleDeContancia> constancia);
}
