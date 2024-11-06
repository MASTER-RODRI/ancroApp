package com.example.ancrorutasygestion;

import static com.example.ancrorutasygestion.Apis.BuildRetrofit.buildRetrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.ByteArrayOutputStream;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ancrorutasygestion.Apis.IDetalleRutas;
import com.example.ancrorutasygestion.Apis.ISession;
import com.example.ancrorutasygestion.Helpers.TokenManager;
import com.example.ancrorutasygestion.Pojos.PojoDetalleRuta;
import com.example.ancrorutasygestion.Pojos.PojoLoginUser;
import com.example.ancrorutasygestion.Pojos.UsuariosReturnPortalDTO;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TecnicoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private boolean isQrCodeDetected = false;
    private ImageButton backButtonMenu;
    private TextView menuTextHead;
    private EditText serie;
    private AutoCompleteTextView realizado,obraoevento;
    private Button guardarDatos,btnTakePhoto,btnDeletePhoto;
    private LinearLayout RealizadoView,cameraspot;
    private String qrCode,fechaQrScan;
    private ImageView imgPreview;
    private String base64Image;
    private IDetalleRutas iDetalleRutas;
    private ProgressDialog progressDialog;
    private String numerocontrato,CodigoDireccion;
    private TextInputEditText observaciones;
    private boolean ProcesoRealizado=false;
    private  Boolean continar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tecnico);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //header
        backButtonMenu = (ImageButton) findViewById(R.id.back_button_menu);
        backButtonMenu.setVisibility(View.VISIBLE);
        backButtonMenu.setOnClickListener(v -> {
            finish();
        });
        menuTextHead = (TextView) findViewById(R.id.menu_header);
        menuTextHead.setText("Registrar Insumos y datos");
        findViewById(R.id.dotsForMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TecnicoActivity.this, v);

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
        //menuTextHead.setTextSize(15);
        //fin header

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); progressDialog.setCancelable(false);
        progressDialog.setMessage("Buscando producto...");

        Intent intent = getIntent();
        if (intent.hasExtra("numerocontrato")) {
            numerocontrato = intent.getStringExtra("numerocontrato");
            CodigoDireccion = intent.getStringExtra("CodigoDireccion");
        } else {
            finish();
        }


        guardarDatos=(Button) findViewById(R.id.guardarDatos);

        cameraView = findViewById(R.id.cameraView1);
        serie= (EditText) findViewById(R.id.Serie);
        realizado = (AutoCompleteTextView) findViewById(R.id.realizado);
        //obraoevento = (AutoCompleteTextView) findViewById(R.id.obraoevento);
        //RealizadoView = (LinearLayout) findViewById(R.id.Realizadoview);
        observaciones = (TextInputEditText) findViewById(R.id.observaciones);
        cameraspot  =(LinearLayout) findViewById(R.id.cameraspot);
        btnTakePhoto  =(Button) findViewById(R.id.btnTakePhoto);
        imgPreview = findViewById(R.id.imgPreview);
        btnDeletePhoto = findViewById(R.id.btnDeletePhoto);

        String[] products = getResources().getStringArray(R.array.realizado);
       // String[] obraOevento = getResources().getStringArray(R.array.obraoevento);
        ArrayAdapter<String> adapterrealizado = new ArrayAdapter<String>(TecnicoActivity.this,android.R.layout.simple_list_item_1, products);
        //ArrayAdapter<String> adapterObra = new ArrayAdapter<String>(TecnicoActivity.this,android.R.layout.simple_list_item_1, obraOevento);
        realizado.setAdapter(adapterrealizado);
        //obraoevento.setAdapter(adapterObra);

        realizado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if(selectedItem.equals("Si")){
                    ProcesoRealizado =true;
                }else{
                    ProcesoRealizado=false;
                }
                cameraspot.setVisibility(View.VISIBLE);
            }
        });

        //camera mode
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(TecnicoActivity.this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setRequestedPreviewSize(1920, 1080) // Ajusta el tamaño de la vista previa
                .build();


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ContextCompat.checkSelfPermission(TecnicoActivity.this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    try {
                        cameraSource.start(holder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(TecnicoActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, 123);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0 && isQrCodeDetected) {
                    continar=false;
                    isQrCodeDetected = false;
                    reproducirSonido();
                    qrCode = barcodes.valueAt(0).displayValue;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TecnicoActivity.this);
// Add the buttons.
                            builder.setMessage("La serie escaneada es: "+qrCode+" es correcta?");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    continar=true;
                                    Date currenttime = Calendar.getInstance().getTime();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    String currentTimeString = dateFormat.format(currenttime);
                                    fechaQrScan = currentTimeString;
                                    //                    qrResult.post(() -> qrResult.setText(qrCode));
                                    Log.i("response consulta ", "QrCode: " + qrCode);
                                    //String[] RutaBien = qrCode.split("/");

                                            ValidarProducto(numerocontrato, CodigoDireccion, qrCode);
                                            serie.setText(qrCode);


                                }
                            });
                            builder.setNegativeButton("No, volver a escanear", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isQrCodeDetected = true;
                                }
                            });


                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    });



                    //String IdBien = RutaBien[RutaBien.length-1];
                    /*if(continar) {

                    }*/

                }
            }
        });

        guardarDatos.setOnClickListener(v -> {
            if(qrCode!=null) {
                if (!qrCode.isEmpty()) {
                    ObtenerBienPorQr(qrCode,ProcesoRealizado,observaciones.getText().toString(),base64Image);
                }
            }
        });
        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            } else {
                takePhoto();
            }
        });
        btnDeletePhoto.setOnClickListener(v -> {
            imgPreview.setImageBitmap(null);
            imgPreview.setVisibility(View.GONE);
            btnDeletePhoto.setVisibility(View.GONE);
            base64Image = null;
        });
    }
    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void ClearToken(){
        TokenManager.getInstance(getApplicationContext()).clearToken();
        startActivity(new Intent(TecnicoActivity.this,LoginActivity.class));
        finish();
    }
    private void ObtenerBienPorQr(String qr,boolean procesoRealizado,String Observaciones,String Image){
        Intent i = new Intent(TecnicoActivity.this, HojaRutaActivity.class);
        i.putExtra("DatosQr",qr);
        i.putExtra("procesoRealizado",procesoRealizado);
        if (Observaciones != null && !Observaciones.isEmpty()) {
            i.putExtra("Observaciones", Observaciones);
        }
        if (Image != null && !Image.isEmpty()) {
            i.putExtra("Image", Image);
        }
        i.putExtra("fecha",fechaQrScan);
        //startActivity(i);
        setResult(RESULT_OK, i);
        finish();
    }
    private String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private void reproducirSonido() {
        MediaPlayer mediaPlayer = MediaPlayer.create(TecnicoActivity.this, R.raw.click);
        mediaPlayer.start();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                cameraSource.start(cameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume() {
        isQrCodeDetected=true;
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgPreview.setImageBitmap(imageBitmap);
            imgPreview.setVisibility(View.VISIBLE);
            btnDeletePhoto.setVisibility(View.VISIBLE);
            base64Image = encodeImageToBase64(imageBitmap);
        }
    }
    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    public void ValidarProducto(String numerocontrato,String CodigoDireccion, String Serie){
        Retrofit re = buildRetrofit();
        iDetalleRutas = re.create(IDetalleRutas.class);
        Call<PojoDetalleRuta> pj = iDetalleRutas.ObtenerUnProducto(numerocontrato,CodigoDireccion,Serie);
        pj.enqueue(new Callback<PojoDetalleRuta>() {
            @Override
            public void onResponse(Call<PojoDetalleRuta> call, Response<PojoDetalleRuta> response) {
                if(response.body()==null){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(TecnicoActivity.this);
// Add the buttons.
                    builder.setMessage("No se encontró el bien escaneado con serie: "+Serie+", dentro del contrato N° "+numerocontrato+", para la direccion con codigo: "+CodigoDireccion);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User taps OK button.
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                   // Toast.makeText(TecnicoActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                   // nextActivity("tecnico");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PojoDetalleRuta> call, Throwable t) {
                Toast.makeText(TecnicoActivity.this,"Error: "+t.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}