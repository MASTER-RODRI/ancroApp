package com.example.ancrorutasygestion;

import static com.example.ancrorutasygestion.Apis.BuildRetrofit.buildRetrofit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ancrorutasygestion.Apis.ISession;
import com.example.ancrorutasygestion.Helpers.TokenManager;
import com.example.ancrorutasygestion.Pojos.PojoLoginUser;
import com.example.ancrorutasygestion.Pojos.UsuariosReturnPortalDTO;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Query;

public class LoginActivity extends AppCompatActivity {

    private Button login_button;
    private ISession iSession;
    private EditText correolog, passlog;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando usuario...");
            correolog = (EditText) findViewById(R.id.Usuario);
            passlog = (EditText) findViewById(R.id.passlog2);

            login_button = (Button) findViewById(R.id.login_button);

            login_button.setOnClickListener(v -> {
                progressDialog.show();
                String email = correolog.getText().toString();
                String password = passlog.getText().toString();
                if (validarPass(email) && validarPass(password)) {
                }

                PojoLoginUser pj = new PojoLoginUser();
                pj.setLogin(email);
                pj.setPassword(password);
                logeo(pj);
            });
        }catch (Exception ex){
            Gson gson = new Gson();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:enrod_mont@outlook.es"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Informe de error");
            intent.putExtra(Intent.EXTRA_TEXT, "Descripción del error resumido: " + ex.getMessage() +
                    "\n" +
                    "Error completo: " +gson.toJson(ex)+
                    "\n" +
                    "Dispositivo: " + Build.MODEL + "\n" +
                    "Android: " + Build.VERSION.RELEASE);
            startActivity(intent);
        }
    }
    public void logeo(PojoLoginUser usuario){
        try {
            Retrofit re = buildRetrofit();
            iSession = re.create(ISession.class);
            Call<UsuariosReturnPortalDTO> pj = iSession.LogearseEnApp(usuario);
            pj.enqueue(new Callback<UsuariosReturnPortalDTO>() {
                @Override
                public void onResponse(Call<UsuariosReturnPortalDTO> call, Response<UsuariosReturnPortalDTO> response) {
                    if (response.body() == null) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
// Add the buttons.
                        builder.setMessage("No se encontró el usuario, revise el usuario o la contraseña e Inténtelo nuevamente");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User taps OK button.
                            }
                        });


                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        TokenManager.getInstance(getApplicationContext()).saveToken(
                                response.body().getToken(),
                                response.body().getNombre() + " " + response.body().getApellido(),
                                response.body().getCodigo(),
                                response.body().getId());
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        nextActivity("tecnico");
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<UsuariosReturnPortalDTO> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch (Exception ex){
            Gson gson = new Gson();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:enrod_mont@outlook.es"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Informe de error");
            intent.putExtra(Intent.EXTRA_TEXT, "Descripción del error resumido: " + ex.getMessage() +
                    "\n" +
                    "Error completo: " +gson.toJson(ex)+
                    "\n" +
                    "Dispositivo: " + Build.MODEL + "\n" +
                    "Android: " + Build.VERSION.RELEASE);
            startActivity(intent);
        }
    }

    public boolean validarPass(String pass){
        return !pass.isEmpty();
    }
    private void nextActivity(String tipo){
        Intent i =new Intent(LoginActivity.this,MainActivity.class);
        i.putExtra("tipo",tipo);
        startActivity(i);
        finish();
    }
    @Override
    protected void onResume() {
        try {
            super.onResume();
            //FirebaseUser currentUser = mAuth.getCurrentUser();
            String token = TokenManager.getInstance(getApplicationContext()).getToken();
            if (token != null) {
                nextActivity("tecnico");
            }
        }catch (Exception ex){
            Gson gson = new Gson();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:enrod_mont@outlook.es"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Informe de error");
            intent.putExtra(Intent.EXTRA_TEXT, "Descripción del error resumido: " + ex.getMessage() +
                    "\n" +
                    "Error completo: " +gson.toJson(ex)+
                    "\n" +
                    "Dispositivo: " + Build.MODEL + "\n" +
                    "Android: " + Build.VERSION.RELEASE);
            startActivity(intent);
        }
    }
}