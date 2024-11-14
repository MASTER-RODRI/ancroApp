package com.example.ancrorutasygestion;

import static com.example.ancrorutasygestion.Apis.BuildRetrofit.buildRetrofit;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancrorutasygestion.Adapters.AdaptadorHojaDeRuta;
import com.example.ancrorutasygestion.Apis.IConstancia;
import com.example.ancrorutasygestion.Apis.IDetalleConstancia;
import com.example.ancrorutasygestion.Apis.IDetalleRutas;
import com.example.ancrorutasygestion.Apis.IRutas;
import com.example.ancrorutasygestion.Helpers.TokenManager;
import com.example.ancrorutasygestion.Pojos.PojoConstancia;
import com.example.ancrorutasygestion.Pojos.PojoDetalleDeContancia;
import com.example.ancrorutasygestion.Pojos.PojoDetalleRuta;
import com.example.ancrorutasygestion.ViewModel.Detalles_hoja_ruta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HojaRutaActivity extends AppCompatActivity {

    //viewmodel
        private Detalles_hoja_ruta viewModel;
    //viewmodel
    private Button guardarInsumos,salirbutton;
    private CardView backButtonMenu;
    private TextView menuTextHead;
    private ArrayList<String> miArray = new ArrayList<String>();
    private RecyclerView reciclerView;
    private AdaptadorHojaDeRuta adapterlista;
    private FloatingActionButton registrarInsumos;
    private TextView direccion, estado, cliente, rucycodigo;
    private String HoraTrabajo;
    private IDetalleRutas iDetalleRutas;
    private ProgressDialog progressDialog;
    private String numeroCOntrato,codDireccion,codcliente,hojaRutaDat,docChofer;
    private IConstancia iConstancia;
    private IDetalleConstancia iDetalleConstancia;
    private IRutas iRutas;
    private Boolean  ObraoEventoRes=false;
    private Integer cantPapHigi=0,cantbolsas=0,cantQuimico=0,jabon=0,volumenresiduo=0,volumr=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hoja_ruta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        direccion = (TextView)findViewById(R.id.direccion);
        estado = (TextView)findViewById(R.id.estado);
        cliente= (TextView)findViewById(R.id.cliente);
        rucycodigo= (TextView)findViewById(R.id.rucycodigo);

        //header
        backButtonMenu = (CardView) findViewById(R.id.back_button_menu);
        backButtonMenu.setVisibility(View.VISIBLE);
        backButtonMenu.setOnClickListener(v -> {
            finish();
        });
        menuTextHead = (TextView) findViewById(R.id.menu_header);
        menuTextHead.setText("Hoja de Ruta");
        //menuTextHead.setTextSize(15);
        findViewById(R.id.dotsForMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(HojaRutaActivity.this, v);

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
        //fin header


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); progressDialog.setCancelable(false);
        progressDialog.setMessage("Buscando producto...");

        viewModel = new ViewModelProvider(this).get(Detalles_hoja_ruta.class);
        guardarInsumos = (Button) findViewById(R.id.GuardarInsumos);
        registrarInsumos = (FloatingActionButton) findViewById(R.id.registrarInsumos);
        salirbutton = (Button) findViewById(R.id.salirbutton);


        /*viewModel.getMiLista().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> miLista) {
                adapterlista.RemoveAll();
              miLista.forEach(x->{
                      adapterlista.addInventaris(x);
              });// Actualiza el RecyclerView con los datos
            }
        }); */

        Intent intent = getIntent();
        //String qrdato = intent.getStringExtra("DatosQr");
        if (intent.hasExtra("Cliente")) {

            direccion.setText("Dir. "+intent.getStringExtra("Direccion"));
            estado.setText(intent.getStringExtra("Estado"));
            cliente.setText(intent.getStringExtra("Cliente"));
            rucycodigo.setText(intent.getStringExtra("Rucycodigo"));
            HoraTrabajo=intent.getStringExtra("HoraTrabajo");
            numeroCOntrato=intent.getStringExtra("numeroCOntrato");
            codDireccion=intent.getStringExtra("codDireccion");
            codcliente=intent.getStringExtra("codcliente");
            hojaRutaDat=intent.getStringExtra("idRuta");
            docChofer=intent.getStringExtra("docChofer");
        }
       // miArray.add(qrdato);
        ObtenerDetalleDePunto(numeroCOntrato,codDireccion,codcliente);
        adapterlista = new AdaptadorHojaDeRuta(HojaRutaActivity.this,estado.getText().toString());
        reciclerView = (RecyclerView) findViewById(R.id.reciclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HojaRutaActivity.this);
        reciclerView.setLayoutManager(linearLayoutManager);
        reciclerView.setAdapter(adapterlista);


        registrarInsumos.setOnClickListener(v -> {
            nextActivity();
        });
        guardarInsumos.setOnClickListener(v -> {
            showCustomDialog();
        });
        salirbutton.setOnClickListener(v -> {
            finish();
        });
    }
    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guarda el estado del RecyclerView y otros datos necesarios
        outState.putString("direccion", direccion.getText().toString());
        outState.putString("estado", estado.getText().toString());
        outState.putString("cliente", cliente.getText().toString());
        outState.putString("rucycodigo", rucycodigo.getText().toString());
        outState.putStringArrayList("miLista", miArray);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaura el estado del RecyclerView y otros datos necesarios
        if (savedInstanceState != null) {
            direccion.setText(savedInstanceState.getString("direccion"));
            estado.setText(savedInstanceState.getString("estado"));
            cliente.setText(savedInstanceState.getString("cliente"));
            rucycodigo.setText(savedInstanceState.getString("rucycodigo"));
            miArray = savedInstanceState.getStringArrayList("miLista");
            // Actualiza el RecyclerView con los datos restaurados
        }
    }*/
    private void ClearToken(){
        TokenManager.getInstance(getApplicationContext()).clearToken();
        startActivity(new Intent(HojaRutaActivity.this,LoginActivity.class));
        finish();
    }
    private void nextActivity(){
        Intent i = new Intent(HojaRutaActivity.this,TecnicoActivity.class);
        i.putExtra("numerocontrato",numeroCOntrato);
        i.putExtra("CodigoDireccion",codDireccion);
        startActivityForResult(i, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String resultadoQR = data.getStringExtra("DatosQr");
            Boolean procesoRealizado = data.getBooleanExtra("procesoRealizado",false);
            String Observaciones = data.getStringExtra("Observaciones");
            String Image = data.getStringExtra("Image");
            String fecha = data.getStringExtra("fecha");
            /*if (resultadoQR != null) {
                viewModel.agregarDato(resultadoQR);
            }*/
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  adapterlista.UpdateModel(resultadoQR, Observaciones, procesoRealizado, Image, fecha);
                              }
                          });

        }
    }
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_tecnico_layout, null);
        builder.setView(dialogView);
        List<PojoDetalleDeContancia> todat= adapterlista.ObtenerListaRutas();
        cantPapHigi=0;
        cantbolsas=0;cantQuimico=0;
        jabon=0;
        todat.forEach(x->{
            if(x.getRealizado()) {
                if (x.getPh() != null) {
                    cantPapHigi = cantPapHigi + x.getPh();
                }
                if (x.getBolsa() != null) {
                    cantbolsas = cantbolsas + x.getBolsa();
                }
                if (x.getQuimico() != null) {
                    cantQuimico = cantQuimico + x.getQuimico();
                }
                if (x.getJabon() != null) {
                    jabon = jabon + x.getJabon();
                }
            }
        });


        EditText editText1 = dialogView.findViewById(R.id.HoraInicio);
        AutoCompleteTextView editText2 = dialogView.findViewById(R.id.obraoevento);
        EditText editText3 = dialogView.findViewById(R.id.cantPapHigi);
        EditText editText4 = dialogView.findViewById(R.id.cantbolsas);
        EditText editText5 = dialogView.findViewById(R.id.cantQuimico);
        EditText editText6 = dialogView.findViewById(R.id.jabon);
        AutoCompleteTextView editText7 = dialogView.findViewById(R.id.volumenresiduo);
        TextInputEditText editText8 = dialogView.findViewById(R.id.observaciones);

            editText1.setText(HoraTrabajo.toString());
            editText1.setEnabled(false);
        String[] obraOevento = getResources().getStringArray(R.array.obraoevento);
        String[] volumnResiduo = getResources().getStringArray(R.array.volumen);
        ArrayAdapter<String> adapterObra = new ArrayAdapter<String>(HojaRutaActivity.this,android.R.layout.simple_list_item_1, obraOevento);
        ArrayAdapter<String> adaptervolumnResiduo = new ArrayAdapter<String>(HojaRutaActivity.this,android.R.layout.simple_list_item_1, volumnResiduo);
        editText2.setAdapter(adapterObra);
        editText3.setText(cantPapHigi.toString());
        editText4.setText(cantbolsas.toString());
        editText5.setText(cantQuimico.toString());
        editText6.setText(jabon.toString());
        editText7.setAdapter(adaptervolumnResiduo);
        if (obraOevento.length > 0) {
            editText2.setText(obraOevento[0], false); // false para evitar que se abra el dropdown
        }
        if(volumnResiduo.length>0){
            editText7.setText(volumnResiduo[0],false);
        }
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();

        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if(selectedItem.equals("Obra")){
                    ObraoEventoRes =true;
                }else{
                    ObraoEventoRes=false;
                }
            }
        });
        editText7.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if(selectedItem.equals("Bajo")){
                    volumr =0;
                }
                if(selectedItem.equals("Medio")){
                    volumr= 1;
                }
                else{
                    volumr=2;
                }
            }
        });

        Button btnSave = dialogView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes guardar los datos ingresados
                Toast.makeText(HojaRutaActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Guardando datos...");
                progressDialog.show();
                ArrayList<PojoDetalleDeContancia> pjd = (ArrayList<PojoDetalleDeContancia>) adapterlista.ObtenerListaRutas();
                Date currenttime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTimeString = dateFormat.format(currenttime);
                Integer idOpeario = TokenManager.getInstance(getApplicationContext()).getId();

                PojoConstancia pc = new PojoConstancia(
                        pjd.get(0).getFechaDeEscaneo().split(" ")[0]+"T"+pjd.get(0).getFechaDeEscaneo().split(" ")[1],
                        pjd.get(0).getFechaDeEscaneo().split(" ")[0]+"T"+pjd.get(0).getFechaDeEscaneo().split(" ")[1],
                        pjd.get(0).getFechaDeEscaneo().split(" ")[0]+"T"+pjd.get(0).getFechaDeEscaneo().split(" ")[1],
                        currentTimeString.split(" ")[0]+"T"+currentTimeString.split(" ")[1],
                        editText8.getText().toString(),
                        ObraoEventoRes,
                        Integer.parseInt(editText3.getText().toString()),
                        Integer.parseInt(editText5.getText().toString()),
                        Integer.parseInt(editText4.getText().toString()),
                        Integer.parseInt(editText6.getText().toString()),
                        volumr,
                        idOpeario,hojaRutaDat,codDireccion,numeroCOntrato,docChofer);
                String[] contr = numeroCOntrato.split("-");
                insertarConstancia(pc,pjd,hojaRutaDat,docChofer,codDireccion,numeroCOntrato);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    public void insertarConstancia(PojoConstancia constancia, ArrayList<PojoDetalleDeContancia> det,String HojaRuta,String DocumentoChofer,String CodigoDireccion,String NumeroContrato){
        try{
            Retrofit re = buildRetrofit();
            iConstancia = re.create(IConstancia.class);
            Call<PojoConstancia> pj = iConstancia.AgregarConstancia(constancia);
            pj.enqueue(new Callback<PojoConstancia>() {
                @Override
                public void onResponse(Call<PojoConstancia> call, Response<PojoConstancia> response) {
                    if(response.body()==null) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(HojaRutaActivity.this);// Add the buttons.
                        builder.setMessage("No se pudo guardar la cosntancia");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User taps OK button.
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    PojoConstancia c =  response.body();
                    Date currenttime = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentTimeString = dateFormat.format(currenttime);
                    //ordenarPorFecha(det);
                    det.forEach(x->{
                        if (x.getFechaDeEscaneo() != null) {
                            x.setFechaDeEscaneo(x.getFechaDeEscaneo().split(" ")[0] + "T" + x.getFechaDeEscaneo().split(" ")[1]);
                        }else{
                            x.setFechaDeEscaneo(currentTimeString.split(" ")[0] + "T" + currentTimeString.split(" ")[1]);
                        }
                        //x.setFechaDeEscaneo(x.getFechaDeEscaneo().split(" ")[0]+"T"+x.getFechaDeEscaneo().split(" ")[1]);
                        x.setIdConstancia(c.getId());
                        if(x.getRutaImagen()==null){x.setRutaImagen("");}
                        if(x.getObservacion()==null){x.setObservacion("");}
                    });
                    CreacionDeContenidoDEConstancia(det,HojaRuta,DocumentoChofer,CodigoDireccion,NumeroContrato);
                }

                @Override
                public void onFailure(Call<PojoConstancia> call, Throwable t) {
                    Toast.makeText(HojaRutaActivity.this,"Error: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch (Exception ex){throw  ex;}
    }
    public void ordenarPorFecha(ArrayList<PojoDetalleDeContancia> det) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date currenttime = Calendar.getInstance().getTime();
        //final String currentTimeString = dateFormat.format(currenttime);
        // Ordenar la lista por fecha de escaneo
        Collections.sort(det, new Comparator<PojoDetalleDeContancia>() {
            @Override
            public int compare(PojoDetalleDeContancia o1, PojoDetalleDeContancia o2) {
                try {
                    Date date1 = (o1.getFechaDeEscaneo() != null) ? dateFormat.parse(o1.getFechaDeEscaneo()) : currenttime;
                    Date date2 = (o2.getFechaDeEscaneo() != null) ? dateFormat.parse(o2.getFechaDeEscaneo()) : currenttime;

                    if (date1 == null && date2 == null) {
                        return 0;
                    } else if (date1 == null) {
                        return 1;  // Coloca nulos al final
                    } else if (date2 == null) {
                        return -1; // Coloca nulos al final
                    } else {
                        return date1.compareTo(date2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
    public void CreacionDeContenidoDEConstancia(ArrayList<PojoDetalleDeContancia> det,String HojaRuta,String DocumentoChofer,String CodigoDireccion,String NumeroContrato){
        try{
            Retrofit re = buildRetrofit();
            iDetalleConstancia = re.create(IDetalleConstancia.class);
            Call<Boolean> pj = iDetalleConstancia.AgregarDatosDeConstancia(det);
            pj.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                    ActualizacionDeEstadoHojaRUta(HojaRuta,DocumentoChofer,CodigoDireccion,NumeroContrato);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(HojaRutaActivity.this,"Error en detalle de cosntancia: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch (Exception ex){throw ex;}
    }
    public void  ActualizacionDeEstadoHojaRUta(String HojaRuta,String DocumentoChofer,String CodigoDireccion,String NumeroContrato){
        try{
            Retrofit re = buildRetrofit();
            iRutas = re.create(IRutas.class);
            Call<Boolean> pj = iRutas.ActualizarEstadoRuta(HojaRuta,DocumentoChofer,CodigoDireccion,NumeroContrato,"N");
            pj.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    finish(); // Cierra la actividad actual
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(HojaRutaActivity.this,"Error en detalle de cosntancia: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch (Exception ex){throw  ex;}
    }
    public void ObtenerDetalleDePunto(String numerocontrato,String CodigoDireccion, String CodCLiente){
        progressDialog.show();
        Retrofit re = buildRetrofit();
        iDetalleRutas = re.create(IDetalleRutas.class);
        Call<ArrayList<PojoDetalleRuta>> pj = iDetalleRutas.ObtenerDetalleDeRuta(numerocontrato,CodigoDireccion,CodCLiente);
        pj.enqueue(new Callback<ArrayList<PojoDetalleRuta>>() {
            @Override
            public void onResponse(Call<ArrayList<PojoDetalleRuta>> call, Response<ArrayList<PojoDetalleRuta>> response) {
                if(response.body()==null){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(HojaRutaActivity.this);
// Add the buttons.
                    builder.setMessage("No se encontró el detalle");
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
                    response.body().forEach(x-> {
                        PojoDetalleDeContancia poj = new PojoDetalleDeContancia();
                        poj.setArticulo(x.getArticulo());
                        poj.setRealizado(false);
                        poj.setSerie(x.getCodigO_SERIE());
                        if (x.getPh() != null) {
                            poj.setPh(x.getPh());
                        }
                        if (x.getBolsa() != null) {
                            poj.setBolsa(x.getBolsa());
                        }
                        if (x.getQuimico() != null) {
                            poj.setQuimico(x.getQuimico());
                        }
                        if (x.getJabon() != null) {
                            poj.setJabon(x.getJabon());
                        }
                        adapterlista.addInventaris(poj);

                        volumenresiduo = 0;
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PojoDetalleRuta>> call, Throwable t) {
                Toast.makeText(HojaRutaActivity.this,"Error: "+t.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}