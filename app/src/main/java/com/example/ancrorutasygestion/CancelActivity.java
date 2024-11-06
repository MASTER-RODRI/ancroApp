package com.example.ancrorutasygestion;

import static com.example.ancrorutasygestion.Apis.BuildRetrofit.buildRetrofit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ancrorutasygestion.Apis.IRutas;
import com.example.ancrorutasygestion.Helpers.TokenManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CancelActivity extends AppCompatActivity {
    private TextView direccion, estado, cliente, rucycodigo;
    private String HoraTrabajo,valorSleect;
    private ImageButton backButtonMenu;
    private TextView menuTextHead;
    private Button ConfirmarCancelar;
    private ProgressDialog progressDialog;
    private IRutas iRutas;
    private AutoCompleteTextView motivocancel;
    private TextInputEditText observacionescancel; //motivocancel
    private String numeroCOntrato,codDireccion,codcliente,hojaRutaDat,docChofer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cancel);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        direccion = (TextView)findViewById(R.id.direccion);
        //estado = (TextView)findViewById(R.id.estado);
        cliente= (TextView)findViewById(R.id.cliente);
        rucycodigo= (TextView)findViewById(R.id.rucycodigo);
        Intent intent = getIntent();
        //String qrdato = intent.getStringExtra("DatosQr");
        if (intent.hasExtra("Cliente")) {

            direccion.setText("Dir. "+intent.getStringExtra("Direccion"));
            //estado.setText(intent.getStringExtra("Estado"));
            cliente.setText(intent.getStringExtra("Cliente"));
            rucycodigo.setText(intent.getStringExtra("Rucycodigo"));
            HoraTrabajo=intent.getStringExtra("HoraTrabajo");
            numeroCOntrato=intent.getStringExtra("numeroCOntrato");
            codDireccion=intent.getStringExtra("codDireccion");
            codcliente=intent.getStringExtra("codcliente");
            hojaRutaDat=intent.getStringExtra("idRuta");
            docChofer=intent.getStringExtra("docChofer");
        }else{
            finish();
        }
        //header
        backButtonMenu = (ImageButton) findViewById(R.id.back_button_menu);
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
                PopupMenu popupMenu = new PopupMenu(CancelActivity.this, v);

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
        progressDialog.setMessage("Cargando data...");

        motivocancel = (AutoCompleteTextView) findViewById(R.id.motivocancel);
        observacionescancel = (TextInputEditText) findViewById(R.id.observacionescancel);
        ConfirmarCancelar  = (Button) findViewById(R.id.ConfirmarCancelar);

        String[] products = getResources().getStringArray(R.array.motivos);
        ArrayAdapter<String> adapterrealizado = new ArrayAdapter<String>(CancelActivity.this,android.R.layout.simple_list_item_1, products);
        motivocancel.setAdapter(adapterrealizado);
        motivocancel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                valorSleect=selectedItem;
                /*if(selectedItem.equals("Sin respuesta")){
                    valorSleect =true;
                }else{
                    valorSleect=false;
                }*/
            }
        });
        ConfirmarCancelar.setOnClickListener(v -> {
            progressDialog.show();
            ActualizacionDeEstadoHojaRUta(hojaRutaDat,docChofer,codDireccion,numeroCOntrato,valorSleect,observacionescancel.getText().toString());
        });
    }
    public void  ActualizacionDeEstadoHojaRUta(String HojaRuta,String DocumentoChofer,String CodigoDireccion,String NumeroContrato,String motivo, String Observacion){
        try{
            Retrofit re = buildRetrofit();
            iRutas = re.create(IRutas.class);
            Call<Boolean> pj = iRutas.CancelarEstadoRuta(HojaRuta,DocumentoChofer,CodigoDireccion,NumeroContrato,"C",Observacion,motivo);
            pj.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    progressDialog.dismiss();
                    finish(); // Cierra la actividad actual
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(CancelActivity.this,"Error en cancelacion: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }catch (Exception ex){throw  ex;}
    }
    private void ClearToken(){
        TokenManager.getInstance(getApplicationContext()).clearToken();
        startActivity(new Intent(CancelActivity.this,LoginActivity.class));
        finish();
    }
}