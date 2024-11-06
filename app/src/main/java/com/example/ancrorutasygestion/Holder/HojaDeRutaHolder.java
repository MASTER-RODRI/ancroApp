package com.example.ancrorutasygestion.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancrorutasygestion.R;

public class HojaDeRutaHolder extends RecyclerView.ViewHolder  {
    public HojaDeRutaHolder(@NonNull View itemView) {
        super(itemView);
        this.qrdatatext = itemView.findViewById(R.id.qrdatatext);
        this.EstadoMantenimiento = itemView.findViewById(R.id.EstadoMantenimiento);
        this.productoname = itemView.findViewById(R.id.productoname);
        this.tipoMantenimiento = itemView.findViewById(R.id.tipoMantenimiento);
        this.parairADetalle2  = itemView.findViewById(R.id.parairADetalle2);
    }
    private TextView qrdatatext,productoname,tipoMantenimiento;
    private Switch EstadoMantenimiento;
    private LinearLayout parairADetalle2;

    public LinearLayout getParairADetalle2() {
        return parairADetalle2;
    }

    public void setParairADetalle2(LinearLayout parairADetalle2) {
        this.parairADetalle2 = parairADetalle2;
    }

    public TextView getQrdatatext() {
        return qrdatatext;
    }

    public void setQrdatatext(TextView qrdatatext) {
        this.qrdatatext = qrdatatext;
    }

    public TextView getProductoname() {
        return productoname;
    }

    public void setProductoname(TextView productoname) {
        this.productoname = productoname;
    }

    public TextView getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(TextView tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public Switch getEstadoMantenimiento() {
        return EstadoMantenimiento;
    }

    public void setEstadoMantenimiento(Switch estadoMantenimiento) {
        EstadoMantenimiento = estadoMantenimiento;
    }

}
