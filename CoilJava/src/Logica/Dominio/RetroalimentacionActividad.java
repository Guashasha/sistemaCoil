package Logica.Dominio;

import AccesoADatos.RetroalimentacionActividadDB;

public class RetroalimentacionActividad extends Retroalimentacion {
    private int idActividad;
    private int dificultad;
    private int interes;

    public RetroalimentacionActividad () {
        super();
    }

    public int getIdActividad () {
        return idActividad;
    }

    public void setIdActividad (int id) {
        this.idActividad = id;
    }

    public int getDificultad () {
        return dificultad;
    }

    public void setDificultad (int dificultad) {
        this.dificultad = dificultad;
    }

    public int getInteres () {
        return interes;
    }

    public void setInteres (int interes) {
        this.interes = interes;
    }

    public boolean equals (RetroalimentacionActividad retroalimentacion) {
        return this.getIdUsuario() == retroalimentacion.getIdUsuario() &&
                this.getComentario() == retroalimentacion.getComentario() &&
                this.getDificultad() == retroalimentacion.getDificultad() &&
                this.getIdActividad() == retroalimentacion.getIdActividad() &&
                this.getInteraccionConPar() == retroalimentacion.getInteraccionConPar() &&
                this.getInteres() == retroalimentacion.getInteres();
    }
}
