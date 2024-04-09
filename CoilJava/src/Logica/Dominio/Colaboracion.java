package Logica.Dominio;

import java.sql.Date;

public class Colaboracion {
    public enum TipoColaboracion {
        claseEspejo,
        COIL
    }
    public enum EstadoColaboracion {
        propuesta,
        aceptada,
        rechazada,
        disponible,
        vinculada,
        activa,
        en_revision,
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

    public boolean validarNulos() {
        return tipo != null &&
                estado != null &&
                cadenaValida(temaInteres) &&
                cadenaValida(idioma) &&
                cadenaValida(objetivo) &&
                cadenaValida(perfilEstudiante);
    }
    private boolean cadenaValida(String cadena) {
        return cadena != null && !cadena.isBlank();
    }

}
