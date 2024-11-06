package com.example.ancrorutasygestion.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancrorutasygestion.Holder.HojaDeRutaHolder;
import com.example.ancrorutasygestion.Holder.ListaRutasHolder;
import com.example.ancrorutasygestion.Pojos.PojoDetalleDeContancia;
import com.example.ancrorutasygestion.Pojos.PojoRutaVista;
import com.example.ancrorutasygestion.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorHojaDeRuta  extends RecyclerView.Adapter<HojaDeRutaHolder>{

    private List<PojoDetalleDeContancia> lista = new ArrayList<>();
    private Context c;
    private String EstadoGeneral;

    public void addInventaris(PojoDetalleDeContancia lr){
        lista.add(lr);
        notifyItemInserted(lista.size());
    }
    public void RemoverInventario(PojoDetalleDeContancia pv){
        lista.remove(pv);
        notifyItemRemoved(lista.size());
    }
    public void RemoveAll(){
        lista.clear();
        notifyItemRemoved(lista.size());
    }
    public List<PojoDetalleDeContancia> ObtenerListaRutas(){
        return lista;
    }
    public AdaptadorHojaDeRuta(Context c,String EstadoGeneral) {this.c = c;this.EstadoGeneral=EstadoGeneral;}

    @NonNull
    @Override
    public HojaDeRutaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.hoja_ruta_detalle,parent,false);
        return new HojaDeRutaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HojaDeRutaHolder holder, int position) {
        PojoDetalleDeContancia model = lista.get(position);
        holder.getQrdatatext().setText("Serie: "+model.getSerie());
        holder.getProductoname().setText(model.getArticulo());
        holder.getEstadoMantenimiento().setChecked(model.getRealizado());
        holder.getTipoMantenimiento().setText(EstadoGeneral);
        Drawable background = holder.getParairADetalle2().getBackground();
        Drawable wrappedDrawable = DrawableCompat.wrap(background);
        DrawableCompat.setTintList(wrappedDrawable, null);
        holder.getEstadoMantenimiento().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Date currenttime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentTimeString = dateFormat.format(currenttime);
                model.setFechaDeEscaneo(currentTimeString);
                if (isChecked) {
                    model.setRealizado(true);
                    // El usuario ha encendido el Switch manualmente
                } else {
                    model.setRealizado(false);
                    // El usuario ha apagado el Switch manualmente
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void UpdateModel(String serie,String obs,boolean realizado,String imagenbase64,String fecha){
        int posicion;
        for (PojoDetalleDeContancia item : lista) {
            if (item.getSerie().trim().equals(serie)) {
                posicion=(lista.indexOf(item));
                item.setObservacion(obs);
                item.setRealizado(realizado);
                item.setRutaImagen(imagenbase64);
                item.setFechaDeEscaneo(fecha);
                notifyItemChanged(posicion);
                break; // Salir del bucle una vez encontrado y actualizado
            }
        }

    }
}
