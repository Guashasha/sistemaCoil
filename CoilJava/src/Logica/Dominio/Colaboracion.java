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
    private Periodo periodo;
    private String perfilEstudiante;

    public Colaboracion(tipoColaboracion tipo, String temaInteres, String idioma, String objetivo, String perfilEstudiante) {
        this.tipo = tipo;
        this.temaInteres = temaInteres;
        this.idioma = idioma;
        this.objetivo = objetivo;
        this.perfilEstudiante = perfilEstudiante;
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
