package com.example.ancrorutasygestion;

import static com.example.ancrorutasygestion.Apis.BuildRetrofit.buildRetrofit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ancrorutasygestion.Adapters.AdaptadoresListaRutas;
import com.example.ancrorutasygestion.Apis.IRutas;
import com.example.ancrorutasygestion.Helpers.TokenManager;
import com.example.ancrorutasygestion.Pojos.PojoHojaRutaVista;
import com.example.ancrorutasygestion.Pojos.PojoRutaVista;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<PojoRutaVista> reporte = new ArrayList<PojoRutaVista>();
    private RecyclerView listaRutas;
    private AdaptadoresListaRutas adapterlista;
    private CardView backButtonMenu;
    private TextView menuTextHead,Rutasasignadas;
    private Button inciarViajeButton;
    private IRutas iRutas;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String Tipo;
    private Boolean conductor  =true;
    private ProgressDialog progressDialog;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                getLastLocation();
            }
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                finish();
            }
            if (!bundle.getString("tipo").equals("")) {
                Tipo = bundle.getString("tipo");
                if (Tipo.toLowerCase().equals("tecnico")) {
                    conductor = false;

                }
            } else {
                finish();
            }

            backButtonMenu = (CardView) findViewById(R.id.back_button_menu);
            backButtonMenu.setVisibility(View.GONE);
            menuTextHead = (TextView) findViewById(R.id.menu_header);
            menuTextHead.setText("Hoja de ruta");
            findViewById(R.id.dotsForMenu).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.logout) {
                            ClearToken();
                            return true;
                        }
                        return true;
                    });
                    popupMenu.inflate(R.menu.menu); // Asegúrate de que el nombre del archivo de menú sea correcto
                    popupMenu.show();
                }
            });

            inciarViajeButton = (Button) findViewById(R.id.iniciarViajebutton);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
            swipeRefreshLayout.setOnRefreshListener(this);

            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando datos...");

            Rutasasignadas=(TextView) findViewById(R.id.Rutasasignadas);
            if (!conductor) {
                inciarViajeButton.setVisibility(View.GONE);
            } else {
                inciarViajeButton.setVisibility(View.GONE);
                inciarViajeButton.setOnClickListener(v -> {
                    nextActivity();
                });
            }

            adapterlista = new AdaptadoresListaRutas(MainActivity.this);
            listaRutas = (RecyclerView) findViewById(R.id.reciclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            listaRutas.setLayoutManager(linearLayoutManager);
            listaRutas.setAdapter(adapterlista);


            String codOpe = TokenManager.getInstance(getApplicationContext()).GetDni();
            ObtenerRutas(null, null, codOpe, false);

            //PojoRutaVista pj = new PojoRutaVista();
            //pj.setCliente("JK Smart Data S.A.C");
            //pj.setDireecion("Domingo de la presa 103 - Santiago de Surco");
            //pj.setRucodatos("20605772073_123456");
            //pj.setEstado("En proceso");
            //pj.setTipo(conductor);
            //adapterlista.addInventaris(pj);
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
    private void nextActivity(){
        startActivity(new Intent(MainActivity.this,RoutesActivity.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    private void ClearToken(){
        TokenManager.getInstance(getApplicationContext()).clearToken();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }
    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                String codOpe = TokenManager.getInstance(getApplicationContext()).GetDni();
                adapterlista.RemoverTodo();
                ObtenerRutas(null,null,codOpe,true);

            }
        },10);
    }
    public void ObtenerRutas(String  operario1,  String  operario2, String  conductor,Boolean refresh){
        progressDialog.show();
        Retrofit re = buildRetrofit();
        iRutas = re.create(IRutas.class);
        Call<ArrayList<PojoHojaRutaVista>> pj = iRutas.ObtenerRutas(operario1,operario2,conductor,lat,lon);
        pj.enqueue(new Callback<ArrayList<PojoHojaRutaVista>>() {
            @Override
            public void onResponse(Call<ArrayList<PojoHojaRutaVista>> call, Response<ArrayList<PojoHojaRutaVista>> response) {
                if(refresh){swipeRefreshLayout.setRefreshing(false);}
                if(response.body()==null){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
// Add the buttons.
                    builder.setMessage("No se encontró hoja de ruta activa");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User taps OK button.
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                     progressDialog.dismiss();
                    //nextActivity("tecnico");
                   // progressDialog.dismiss();
                    ArrayList<PojoHojaRutaVista> dat = response.body();
                    if(dat.size()>0) {
                        menuTextHead.setText("Hoja de ruta: " + dat.get(0).getHojaRuta());
                        dat.forEach(rut -> {
                            PojoRutaVista pj = new PojoRutaVista(
                                    rut.getHojaRuta(),
                                    rut.getChofer(),
                                    rut.getTipoRuta(),
                                    rut.getDireccion(), rut.getCodigoDireccion(), rut.getCliente(),
                                    rut.getCodigoCliente().toString(), rut.getCodigoCliente(),
                                    rut.getFolioContrato(), true, rut.getLatitud(), rut.getLongitud(), rut.getDocumentoChofer(), rut.getEstadoRuta());
                            adapterlista.addInventaris(pj);
                        });
                        if(adapterlista.ObtenerListaRutas().size()==0){
                            Rutasasignadas.setText("Ya no tiene mas puntos de ruta pendientes por hoy");
                            Rutasasignadas.setVisibility(View.VISIBLE);
                        }
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Rutasasignadas.setVisibility(View.VISIBLE);
                               Toast.makeText(MainActivity.this,"No se encontraron rutas",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PojoHojaRutaVista>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Error: "+t.getMessage(),Toast.LENGTH_LONG).show();
                // progressDialog.dismiss();
            }
        });
    }
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            lat =latitude;
                            lon = longitude;
                            Log.e("TAG", "lat" + latitude);
                            Log.e("TAG", "long " + longitude);
                            //controller.animateTo(new GeoPoint(lat,lon));
                            //addMarker(new GeoPoint(lat,lon),"Chofer");
                            //controller.setZoom(1.0);
                            // Usa las coordenadas aquí
                        } else {
                            // Maneja el caso en que no se pudo obtener la ubicación
                        }
                    }
                });
    }
}