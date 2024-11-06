package com.example.ancrorutasygestion.Apis;

import com.example.ancrorutasygestion.Pojos.PojoLoginUser;
import com.example.ancrorutasygestion.Pojos.PojoUsuario;
import com.example.ancrorutasygestion.Pojos.UsuariosReturnPortalDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IUsuario {
    @GET
    Call<List<PojoUsuario>> Uusuarios(@Url String uri);

}
