package Logica.Dominio;

import java.sql.Date;

public class Colaboracion {
    private enum tipoColaboracion {
        claseEspejo,
        COIL
    }

    private tipoColaboracion tipo;
    private String temaInteres;
    private String idioma;
    private String objetivo;
    private Date fechaInicio;
    private Date fechaFin;
    private String perfilEstudiante;

    public Colaboracion () {

    }
    public tipoColaboracion getTipo() {
        return tipo;
    }

    public void setTipo(tipoColaboracion tipo) {
        this.tipo = tipo;
    }

    public String getTemaInteres() {
        return temaInteres;
    }

    public void setTemaInteres(String temaInteres) {
        this.temaInteres = temaInteres;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getPerfilEstudiante() {
        return perfilEstudiante;
    }

    public void setPerfilEstudiante(String perfilEstudiante) {
        this.perfilEstudiante = perfilEstudiante;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
