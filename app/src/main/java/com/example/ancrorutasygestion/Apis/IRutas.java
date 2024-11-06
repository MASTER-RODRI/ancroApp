package com.example.ancrorutasygestion.Apis;

import com.example.ancrorutasygestion.Pojos.PojoHojaRuta;
import com.example.ancrorutasygestion.Pojos.PojoHojaRutaVista;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IRutas {
    @GET("api/Rutas")
    Call<ArrayList<PojoHojaRutaVista>> ObtenerRutas(@Query("operario1") String  operario1,
                                                    @Query("operario2") String  operario2,
                                                    @Query("conductor") String  conductor,
                                                    @Query("miLatitud") double  miLatitud,
                                                    @Query("miLongitud") double  miLongitud);
    @GET("api/Rutas/Obtener/uno")
    Call<PojoHojaRuta> ObtenerDatosDeUnaRuta(@Query("HojaRuta") String  HojaRuta,
                                             @Query("DocumentoChofer") String  DocumentoChofer,
                                             @Query("CodigoDireccion") String  CodigoDireccion,
                                             @Query("NumeroContrato") String  NumeroContrato);
    @PUT("api/Rutas/Estado")
    Call<Boolean> ActualizarEstadoRuta(@Query("HojaRuta") String  HojaRuta,
                                       @Query("DocumentoChofer") String  DocumentoChofer,
                                       @Query("CodigoDireccion") String CodigoDireccion,
                                       @Query("NumeroContrato") String NumeroContrato,
                                       @Query("estado") String estado);
    @PUT("api/Rutas/Cancelar")
    Call<Boolean> CancelarEstadoRuta(@Query("HojaRuta") String  HojaRuta,
                                       @Query("DocumentoChofer") String  DocumentoChofer,
                                       @Query("CodigoDireccion") String CodigoDireccion,
                                       @Query("NumeroContrato") String NumeroContrato,
                                       @Query("estado") String estado,
                                       @Query("observacion") String observacion,
                                       @Query("motivo") String motivo);
}
