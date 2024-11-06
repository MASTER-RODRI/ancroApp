package com.example.ancrorutasygestion.Pojos;

public class PojoRutaVista {
    private String id;
    private String descripcion;
    private String estado;
    private String estadoRuta;
    private String direecion;
    private String CodDireccion;
    private String rucodatos;
    private String cliente;
    private String CodCliente;
    private String DocumentoChofer;
    private String NumContrato;
    private boolean tipo; //true conductor false tecnico
    private Double  latitud;
    private Double  longitud;

    public PojoRutaVista(String id, String descripcion, String estado, String direecion, String codDireccion, String rucodatos, String cliente, String codCliente, String numContrato, boolean tipo, Double latitud, Double longitud,String DocumentoChofer,String estadoRuta) {
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
        this.direecion = direecion;
        CodDireccion = codDireccion;
        this.rucodatos = rucodatos;
        this.cliente = cliente;
        CodCliente = codCliente;
        NumContrato = numContrato;
        this.tipo = tipo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.DocumentoChofer = DocumentoChofer;
        this.estadoRuta =estadoRuta;
    }

    public String getEstadoRuta() {
        return estadoRuta;
    }

    public void setEstadoRuta(String estadoRuta) {
        this.estadoRuta = estadoRuta;
    }

    public String getDocumentoChofer() {
        return DocumentoChofer;
    }

    public void setDocumentoChofer(String documentoChofer) {
        DocumentoChofer = documentoChofer;
    }

    public String getCodDireccion() {
        return CodDireccion;
    }

    public void setCodDireccion(String codDireccion) {
        CodDireccion = codDireccion;
    }

    public String getCodCliente() {
        return CodCliente;
    }

    public void setCodCliente(String codCliente) {
        CodCliente = codCliente;
    }

    public String  getNumContrato() {
        return NumContrato;
    }

    public void setNumContrato(String numContrato) {
        NumContrato = numContrato;
    }

    public boolean isTipo() {
        return tipo;
    }
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireecion() {
        return direecion;
    }

    public void setDireecion(String direecion) {
        this.direecion = direecion;
    }

    public String getRucodatos() {
        return rucodatos;
    }

    public void setRucodatos(String rucodatos) {
        this.rucodatos = rucodatos;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
