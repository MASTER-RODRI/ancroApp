package com.example.ancrorutasygestion.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Detalles_hoja_ruta extends ViewModel {
    private MutableLiveData<List<String>> miLista;

    public LiveData<List<String>> getMiLista() {
        if (miLista == null) {
            miLista = new MutableLiveData<>(new ArrayList<>());
        }
        return miLista;
    }

    public void agregarDato(String dato) {
        List<String> listaActual = miLista.getValue();
        if (listaActual != null) {
            listaActual.add(dato);
            miLista.setValue(listaActual);
        }
    }
}
