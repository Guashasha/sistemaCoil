package Logica.Dominio;

import java.sql.Date;

public class Colaboracion {
    private enum TipoColaboracion {
        claseEspejo,
        COIL
    }
    private enum EstadoColaboracion {
        propuesta,
        aceptada,
        rechazada,
        disponible,
        vinculada,
        activa,
        enRevision,
        finalizada,
    }

    private int idColaboracion;
    private TipoColaboracion tipo;
    private EstadoColaboracion estado;
    private String temaInteres;
    private String idioma;
    private String objetivo;
    private Periodo periodo;
    private String perfilEstudiante;

    public Colaboracion(int idColaboracion, TipoColaboracion tipo, String temaInteres, String idioma, String objetivo, String perfilEstudiante, Periodo periodo) {
        this.idColaboracion = idColaboracion;
        this.tipo = tipo;
        this.temaInteres = temaInteres;
        this.idioma = idioma;
        this.objetivo = objetivo;
        this.perfilEstudiante = perfilEstudiante;
        this.periodo = periodo;
    }

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public TipoColaboracion getTipo() {
        return tipo;
    }

    public void setTipo(TipoColaboracion tipo) {
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
    public EstadoColaboracion getEstado () {
        return estado;
    }

    public void setEstado (EstadoColaboracion estado) {
        this.estado = estado;
    }

}
