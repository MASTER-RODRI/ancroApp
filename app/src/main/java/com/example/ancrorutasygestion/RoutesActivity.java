package com.example.ancrorutasygestion;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


/*import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;*/


import com.example.ancrorutasygestion.Apis.GeocodingTask;
import com.example.ancrorutasygestion.Helpers.TokenManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class RoutesActivity extends AppCompatActivity {
    private CardView backButtonMenu;
    private TextView menuTextHead,textoOrigen,textoDestino,londata,latdata,londata1,latdata1;
    //private MapView mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //private MyLocationNewOverlay mMyLocationOverlay;
    //private IMapController controller;
    private double lat,lon;
    private Button btnOpenWaze;
    private String Codigo,Direccion,CLiente;
    private Double latDestino,LonDestino;
    private GeocodingTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
        }
        if (!bundle.getString("idRuta").equals("")) {
            Codigo = bundle.getString("idRuta");
            Direccion = bundle.getString("Direccion");
            CLiente = bundle.getString("Cliente");
            latDestino = bundle.getDouble("lat",0);
            LonDestino = bundle.getDouble("lon",0);
        } else {
            finish();
        }
        // Mapbox.getInstance(this,"X8QXRLdaro4EE1eizRsf", WellKnownTileServer.MapTiler);
        backButtonMenu = (CardView) findViewById(R.id.back_button_menu);
        backButtonMenu.setVisibility(View.VISIBLE);
        backButtonMenu.setOnClickListener(v -> {
            finish();
        });
        menuTextHead = (TextView) findViewById(R.id.menu_header);
        menuTextHead.setText(CLiente.toString());
        menuTextHead.setTextSize(15);
        findViewById(R.id.dotsForMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(RoutesActivity.this, v);

                popupMenu.setOnMenuItemClickListener(item -> {
                    if(item.getItemId()==R.id.logout){
                        ClearToken();
                        return true;
                    }
                    return true;
                });
                popupMenu.inflate(R.menu.menu); // Asegúrate de que el nombre del archivo de menú sea correcto
                popupMenu.show();
            }
        });

        textoDestino = (TextView) findViewById(R.id.textoDestino);
        textoOrigen = (TextView) findViewById(R.id.textoorigen);
        londata = (TextView) findViewById(R.id.londata);
        latdata = (TextView) findViewById(R.id.latdata);
        londata1 = (TextView) findViewById(R.id.londata1);
        latdata1 = (TextView) findViewById(R.id.latdata1);

        textoDestino.setText(Direccion.toString());
        londata1.setText(LonDestino.toString());
        latdata1.setText(latDestino.toString());

        //maps
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation();
        }
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        //getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.
        //getLastLocation();
        //inflate and create the map
        //setContentView(R.layout.activity_routes);
        btnOpenWaze = findViewById(R.id.btnOpenWaze);
        btnOpenWaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openWazeWithRoute(-12.060370, -77.039961, latDestino, LonDestino); // Reemplaza con tus coordenadas
            }
        });
        //Configuration.getInstance().setCachePath
       /* mMap = findViewById(R.id.osmmap);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        mMap.setMaxZoomLevel(22.0);
        mMap.getLocalVisibleRect(new Rect());
//
        mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(RoutesActivity.this), mMap);
        controller = mMap.getController();
        controller.setZoom(1.0);
        GeoPoint gPt;
        if(lat != 0 || lon != 0){
            gPt=new GeoPoint(lat,lon);
        }else{        gPt = new GeoPoint(-12.0460407,-77.0332379);}
        controller.animateTo(gPt);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        //mMyLocationOverlay.enableMyLocation();
        //mMyLocationOverlay.enableFollowLocation();
        //mMyLocationOverlay.setDrawAccuracyEnabled(true);
        //rutas
       // RoadManager roadManager = new OSRMRoadManager(this, MY_USER_AGENT);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(gPt);
        GeoPoint endPoint = new GeoPoint(-12.043045, -77.036285);
        waypoints.add(endPoint);

       /* mMyLocationOverlay.runOnFirstFix(() ->
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    controller.setCenter(mMyLocationOverlay.getMyLocation());
                    controller.animateTo(mMyLocationOverlay.getMyLocation());
                }
            })
        );*/
        // val mapPoint = GeoPoint(latitude, longitude)



        //Log.e("TAG", "onCreate:in " + controller.zoomIn());
        //Log.e("TAG", "onCreate: out  " + controller.zoomOut());

        // controller.animateTo(mapPoint)
        //mMap.getOverlayManager().add(mMyLocationOverlay);

        //mMap.addMapListener();
    }
    private void ClearToken(){
        TokenManager.getInstance(getApplicationContext()).clearToken();
        startActivity(new Intent(RoutesActivity.this,LoginActivity.class));
        finish();
    }
    public void getCoordinatesAndExecuteTask() {
        // ... (código para obtener la latitud y longitud)

        task = new GeocodingTask(new GeocodingTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                // Actualizar la UI con el resultado
                textoOrigen.setText(result);
            }
        });

        task.execute(""+lat, ""+lon);
    }
    private void openWazeWithRoute(double startLat, double startLng, double endLat, double endLng) {
        try {
            String url = "waze://?ll=" + endLat + "," + endLng + "&navigate=yes&from=" + startLat + "," + startLng;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            // Si Waze no está instalado, abre la URL en un navegador
            String url = "https://waze.com/ul?ll=" + endLat + "," + endLng + "&navigate=yes&from=" + startLat + "," + startLng;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }
    /*public void addMarker (GeoPoint center,String title){
        Marker marker = new Marker(mMap);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.ping_ancro));
        mMap.getOverlays().clear();
        mMap.getOverlays().add(marker);
        mMap.invalidate();
        marker.setTitle(title);
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
       // mMap.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    protected void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        //mMap.onPause();  //needed for compass, my location overlays, v6.0.0 and up
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
                            latdata.setText(""+lat);
                            londata.setText(""+lon);
                            Log.e("TAG", "lat" + latitude);
                            Log.e("TAG", "long " + longitude);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getCoordinatesAndExecuteTask();
                                }
                            });
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                // Maneja el caso en que los permisos no fueron concedidos
            }
        }
    }
}
