package com.example.ancrorutasygestion.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class TokenManager {
    private static final String SHARED_PREF_NAME = "MySharedPref";
    private static final String KEY_TOKEN = "token";
    private static final String User_name = "username";
    private static final String Dni = "dni";
    private static final String Id = "id";
    private static TokenManager mInstance;
    private static Context mCtx;

    private TokenManager(Context context) {
        mCtx = context;
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TokenManager(context);
        }
        return mInstance;
    }

    // Método para guardar el token
    public void saveToken(String token,String username, String dni,int id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(User_name, username);
        editor.putString(Dni, dni);
        editor.putInt(Id, id);
        editor.apply();
    }

    // Método para obtener el token guardado
    public String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Si no hay fecha de vencimiento, borrar el token y retornar null
        Date now = new Date();
        // Si la fecha de vencimiento es anterior a la fecha actual, borrar el token y retornar null

        // Si el token sigue siendo válido, retornarlo
        return sharedPreferences.getString(KEY_TOKEN, null);
    }
    public String GetName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Si el token sigue siendo válido, retornarlo
        return sharedPreferences.getString(User_name, null);
    }
    public String GetDni(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Si el token sigue siendo válido, retornarlo
        return sharedPreferences.getString(Dni, null);
    }
    public Integer getId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        // Si el token sigue siendo válido, retornarlo
        return sharedPreferences.getInt(Id, 0);
    }
    // Método para borrar el token
    public void clearToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.remove(User_name);
        editor.remove(Dni);
        editor.remove(Id);
        editor.apply();
    }
}
