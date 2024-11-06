package com.example.ancrorutasygestion.Apis;

import com.example.ancrorutasygestion.Pojos.PojoDetalleRuta;
import com.example.ancrorutasygestion.Pojos.PojoHojaRutaVista;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IDetalleRutas {
    @GET("api/DetalleDeRuta")
    Call<ArrayList<PojoDetalleRuta>> ObtenerDetalleDeRuta(@Query("FolioContrato") String  FolioContrato, @Query("CodDire") String  CodDire, @Query("CodClie") String  CodClie);
    @GET("api/DetalleDeRuta/Obtener/uno")
    Call<PojoDetalleRuta> ObtenerUnProducto(@Query("NumeroContrato") String  NumeroContrato, @Query("CODIGO_DIRECCION") String  CODIGO_DIRECCION, @Query("codigoSerie") String  codigoSerie);

}
