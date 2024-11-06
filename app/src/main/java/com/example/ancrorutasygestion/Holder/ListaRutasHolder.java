package com.example.ancrorutasygestion.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancrorutasygestion.R;

public class ListaRutasHolder extends RecyclerView.ViewHolder {
    public ListaRutasHolder(@NonNull View itemView) {
        super(itemView);
        this.direccion = itemView.findViewById(R.id.direccion);
        this.estado = itemView.findViewById(R.id.estado);
        this.cliente= itemView.findViewById(R.id.cliente);
        this.rucycodigo= itemView.findViewById(R.id.rucycodigo);
        this.parairADetalle = itemView.findViewById(R.id.parairADetalle2);
    }

    private TextView rucycodigo;
    private TextView cliente;
    private TextView direccion;
    private TextView estado;
    private LinearLayout parairADetalle;

    public LinearLayout getParairADetalle() {
        return parairADetalle;
    }

    public void setParairADetalle(LinearLayout parairADetalle) {
        this.parairADetalle = parairADetalle;
    }

    public TextView getRucycodigo() {
        return rucycodigo;
    }

    public void setRucycodigo(TextView rucycodigo) {
        this.rucycodigo = rucycodigo;
    }

    public TextView getCliente() {
        return cliente;
    }

    public void setCliente(TextView cliente) {
        this.cliente = cliente;
    }

    public TextView getDireccion() {
        return direccion;
    }

    public void setDireccion(TextView direccion) {
        this.direccion = direccion;
    }

    public TextView getEstado() {
        return estado;
    }

    public void setEstado(TextView estado) {
        this.estado = estado;
    }
}
