package com.example.ancrorutasygestion.Pojos;

public class PojoDetalleDeContancia {

        private String Serie ;
        private Boolean Realizado ;
        private String FechaDeEscaneo ;
        private String Articulo ;
        private String Observacion ;
        private String RutaImagen ;
        private Integer IdConstancia ;
    private Integer ph;
    private Integer bolsa;
    private Integer quimico;
    private Integer jabon;

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String serie) {
        Serie = serie;
    }

    public Boolean getRealizado() {
        return Realizado;
    }

    public void setRealizado(Boolean realizado) {
        Realizado = realizado;
    }

    public String getFechaDeEscaneo() {
        return FechaDeEscaneo;
    }

    public void setFechaDeEscaneo(String fechaDeEscaneo) {
        FechaDeEscaneo = fechaDeEscaneo;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getRutaImagen() {
        return RutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        RutaImagen = rutaImagen;
    }

    public Integer getIdConstancia() {
        return IdConstancia;
    }

    public void setIdConstancia(Integer idConstancia) {
        IdConstancia = idConstancia;
    }

    public String getArticulo() {
        return Articulo;
    }

    public void setArticulo(String articulo) {
        Articulo = articulo;
    }

    public Integer getPh() {
        return ph;
    }

    public void setPh(Integer ph) {
        this.ph = ph;
    }

    public Integer getBolsa() {
        return bolsa;
    }

    public void setBolsa(Integer bolsa) {
        this.bolsa = bolsa;
    }

    public Integer getQuimico() {
        return quimico;
    }

    public void setQuimico(Integer quimico) {
        this.quimico = quimico;
    }

    public Integer getJabon() {
        return jabon;
    }

    public void setJabon(Integer jabon) {
        this.jabon = jabon;
    }
}
