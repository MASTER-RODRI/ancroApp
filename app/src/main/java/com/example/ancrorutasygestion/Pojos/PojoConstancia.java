package com.example.ancrorutasygestion.Pojos;

public class PojoConstancia {
    private Integer id;
    private String fecIniTrans;
    private String fecFinTrans;
    private String fecInicTrab;
    private String fecFinTrab;
    private String observaciones;
    private Boolean obraOEvento;
    private Integer cantPapelHigienico;
    private Integer cantQuimico;
    private Integer cantBolsas;
    private Integer jabon;
    private Integer volumenResiduo;
    private Integer idOperario;

    private String HojaRuta;
    private String CodigoDireccion;
    private String NumeroContrato;
    public String DocumentoChofer;

    public PojoConstancia() {
    }

    public PojoConstancia(String fecIniTrans, String fecFinTrans, String fecInicTrab, String fecFinTrab, String observaciones, Boolean obraOEvento, Integer cantPapelHigienico, Integer cantQuimico, Integer cantBolsas, Integer jabon, Integer volumenResiduo, Integer idOperario,String HojaRuta, String CodigoDireccion,String NumeroContrato,String DocumentoChofer) {
        id = 0;
        this.fecIniTrans = fecIniTrans;
        this.fecFinTrans = fecFinTrans;
        this.fecInicTrab = fecInicTrab;
        this.fecFinTrab = fecFinTrab;
        this.observaciones = observaciones;
        this.obraOEvento = obraOEvento;
        this.cantPapelHigienico = cantPapelHigienico;
        this.cantQuimico = cantQuimico;
        this.cantBolsas = cantBolsas;
        this.jabon = jabon;
        this.volumenResiduo = volumenResiduo;
        this.idOperario = idOperario;
        this.HojaRuta =HojaRuta;
        this.CodigoDireccion =CodigoDireccion;
        this.NumeroContrato = NumeroContrato;
        this.DocumentoChofer =DocumentoChofer;
    }

    public String getHojaRuta() {
        return HojaRuta;
    }

    public void setHojaRuta(String hojaRuta) {
        HojaRuta = hojaRuta;
    }

    public String getCodigoDireccion() {
        return CodigoDireccion;
    }

    public void setCodigoDireccion(String codigoDireccion) {
        CodigoDireccion = codigoDireccion;
    }

    public String getNumeroContrato() {
        return NumeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        NumeroContrato = numeroContrato;
    }

    public String getDocumentoChofer() {
        return DocumentoChofer;
    }

    public void setDocumentoChofer(String documentoChofer) {
        DocumentoChofer = documentoChofer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecIniTrans() {
        return fecIniTrans;
    }

    public void setFecIniTrans(String fecIniTrans) {
        this.fecIniTrans = fecIniTrans;
    }

    public String getFecFinTrans() {
        return fecFinTrans;
    }

    public void setFecFinTrans(String fecFinTrans) {
        this.fecFinTrans = fecFinTrans;
    }

    public String getFecInicTrab() {
        return fecInicTrab;
    }

    public void setFecInicTrab(String fecInicTrab) {
        this.fecInicTrab = fecInicTrab;
    }

    public String getFecFinTrab() {
        return fecFinTrab;
    }

    public void setFecFinTrab(String fecFinTrab) {
        this.fecFinTrab = fecFinTrab;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getObraOEvento() {
        return obraOEvento;
    }

    public void setObraOEvento(Boolean obraOEvento) {
        this.obraOEvento = obraOEvento;
    }

    public Integer getCantPapelHigienico() {
        return cantPapelHigienico;
    }

    public void setCantPapelHigienico(Integer cantPapelHigienico) {
        this.cantPapelHigienico = cantPapelHigienico;
    }

    public Integer getCantQuimico() {
        return cantQuimico;
    }

    public void setCantQuimico(Integer cantQuimico) {
        this.cantQuimico = cantQuimico;
    }

    public Integer getCantBolsas() {
        return cantBolsas;
    }

    public void setCantBolsas(Integer cantBolsas) {
        this.cantBolsas = cantBolsas;
    }

    public Integer getJabon() {
        return jabon;
    }

    public void setJabon(Integer jabon) {
        this.jabon = jabon;
    }

    public Integer getVolumenResiduo() {
        return volumenResiduo;
    }

    public void setVolumenResiduo(Integer volumenResiduo) {
        this.volumenResiduo = volumenResiduo;
    }

    public Integer getIdOperario() {
        return idOperario;
    }

    public void setIdOperario(Integer idOperario) {
        this.idOperario = idOperario;
    }
}
