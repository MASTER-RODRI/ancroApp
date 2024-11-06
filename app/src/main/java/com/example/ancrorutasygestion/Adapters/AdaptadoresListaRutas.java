package com.example.ancrorutasygestion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancrorutasygestion.CancelActivity;
import com.example.ancrorutasygestion.HojaRutaActivity;
import com.example.ancrorutasygestion.Holder.ListaRutasHolder;
import com.example.ancrorutasygestion.Pojos.PojoRutaVista;
import com.example.ancrorutasygestion.R;
import com.example.ancrorutasygestion.RoutesActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadoresListaRutas extends RecyclerView.Adapter<ListaRutasHolder> {
    private List<PojoRutaVista> listDocs = new ArrayList<>();
    private BottomSheetDialog dialog;
    private Context c;

    public void addInventaris(PojoRutaVista lr){
        if(!lr.getEstadoRuta().equals("N") && !lr.getEstadoRuta().equals("C")  ) {
            listDocs.add(lr);
            notifyItemInserted(listDocs.size());
        }
    }
    public void RemoverInventario(PojoRutaVista pv){
        listDocs.remove(pv);
        notifyItemRemoved(listDocs.size());
    }
    public void RemoverTodo(){
        listDocs.clear();
        notifyDataSetChanged();
    }
    public List<PojoRutaVista> ObtenerListaRutas(){
        return listDocs;
    }
    public AdaptadoresListaRutas(Context c) {this.c = c;}
    @NonNull
    @Override
    public ListaRutasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_rutas,parent,false);
        return new ListaRutasHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaRutasHolder holder, int position) {
        PojoRutaVista model = listDocs.get(position);
        holder.getCliente().setText(model.getCliente());
        holder.getDireccion().setText("Dir.: " + model.getDireecion());
        holder.getRucycodigo().setText(model.getRucodatos());
        holder.getEstado().setText(model.getEstadoRuta());//model.getEstado()
        dialog =  new BottomSheetDialog(c,R.style.AppBottomSheetDialogTheme);
        CreateDialog(model);
        Drawable background = holder.getParairADetalle().getBackground();
        Drawable wrappedDrawable = DrawableCompat.wrap(background);
        DrawableCompat.setTintList(wrappedDrawable, null);
       /* if(position!= 0){
            holder.getParairADetalle().setOnClickListener(v -> {
                Toast.makeText(c, "Primero termina el punto de ruta pendiente", Toast.LENGTH_SHORT).show();
            });
        }else { */
            if (model.getEstadoRuta().equals("R")) {
                holder.getParairADetalle().setOnClickListener(v -> {
                    nextActivity(model);
                });
                // holder.getParairADetalle().setVisibility(View.VISIBLE);
            }
            if (model.getEstadoRuta().equals("N") || model.getEstadoRuta().equals("C")) {

                DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(c, R.color.gray));
                wrappedDrawable.setTintMode(PorterDuff.Mode.SRC_OVER);
                holder.getParairADetalle().setOnClickListener(v -> {
                    Toast.makeText(c, "Punto de ruta completado", Toast.LENGTH_SHORT).show();
                });
                // holder.getParairADetalle().setVisibility(View.GONE);
            }
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
       // }

    }
    private void nextActivity(PojoRutaVista model){
        if(model.isTipo()) {
            dialog.show();
            //Intent intent = new Intent(c, RoutesActivity.class);
            //intent.putExtra("idRuta", model.getId()); // Si necesitas pasar datos a la nueva actividad
            //c.startActivity(intent);
        }else{
            dialog.show();
        }
    }
    public void CreateDialog(PojoRutaVista model){
        View v = LayoutInflater.from(c).inflate(R.layout.bottom_dialog_route,null);
        Button submit  = v.findViewById(R.id.inicarTrabajo);
        Button inicarRuta  = v.findViewById(R.id.inicarRuta);
        Button CancelarRuta  = v.findViewById(R.id.CancelarRuta);
        TextView clientename  =v.findViewById(R.id.clientename);
        TextView contratopop  =v.findViewById(R.id.contratopop);
        TextView direpop  =v.findViewById(R.id.direpop);
        submit.setText("Iniciar trabajo");
        //if(model.isTipo()) {submit.setText("Iniciar viaje");}else{submit.setText("Iniciar trabajo");}
        clientename.setText(model.getCliente());
        contratopop.setText(model.getRucodatos());
        direpop.setText(model.getDireecion());
        inicarRuta.setOnClickListener(v2->{
            dialog.dismiss();
            Intent intent;
            intent = new Intent(c, RoutesActivity.class);
            intent.putExtra("idRuta", model.getId());
            intent.putExtra("Direccion", model.getDireecion());
            intent.putExtra("Direccion", model.getDireecion());
            intent.putExtra("Cliente", model.getCliente());
            intent.putExtra("CodCliente", model.getCliente());
            intent.putExtra("lat", model.getLatitud());
            intent.putExtra("lon", model.getLongitud());
            c.startActivity(intent);
        });
        submit.setOnClickListener(v1 -> {
            dialog.dismiss();
            Intent intent;
            /*if(model.isTipo()) {
                intent = new Intent(c, RoutesActivity.class);
                intent.putExtra("idRuta", model.getId());
            }else {*/
                intent = new Intent(c, HojaRutaActivity.class);
                intent.putExtra("idRuta", model.getId()); // Si necesitas pasar datos a la nueva actividad
                intent.putExtra("Cliente", model.getCliente());
                intent.putExtra("Direccion", model.getDireecion());
                intent.putExtra("Rucycodigo", model.getRucodatos());
                intent.putExtra("Estado", model.getEstado());
                Date currenttime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTimeString = dateFormat.format(currenttime);
                intent.putExtra("HoraTrabajo", currentTimeString);
                intent.putExtra("numeroCOntrato", model.getNumContrato());
                intent.putExtra("codDireccion", model.getCodDireccion());
                intent.putExtra("codcliente", model.getCodCliente());
                intent.putExtra("docChofer", model.getDocumentoChofer());
           // }
            c.startActivity(intent);
        });
        CancelarRuta.setOnClickListener(v3->{
            dialog.dismiss();
            Intent intent;
            intent = new Intent(c, CancelActivity.class);
            intent.putExtra("idRuta", model.getId()); // Si necesitas pasar datos a la nueva actividad
            intent.putExtra("Cliente", model.getCliente());
            intent.putExtra("Direccion", model.getDireecion());
            intent.putExtra("Rucycodigo", model.getRucodatos());
            intent.putExtra("Estado", model.getEstado());
            Date currenttime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTimeString = dateFormat.format(currenttime);
            intent.putExtra("HoraTrabajo", currentTimeString);
            intent.putExtra("numeroCOntrato", model.getNumContrato());
            intent.putExtra("codDireccion", model.getCodDireccion());
            intent.putExtra("codcliente", model.getCodCliente());
            intent.putExtra("docChofer", model.getDocumentoChofer());
            c.startActivity(intent);
        });
        dialog.setContentView(v);
    }
    @Override
    public int getItemCount() {
        return listDocs.size();
    }
}
