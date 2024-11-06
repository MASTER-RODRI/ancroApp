package com.example.ancrorutasygestion.Apis;

import com.example.ancrorutasygestion.Pojos.PojoLoginUser;
import com.example.ancrorutasygestion.Pojos.UsuariosReturnPortalDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ISession {
    @POST("api/Session")
    Call<UsuariosReturnPortalDTO> LogearseEnApp(@Body PojoLoginUser po);
}
