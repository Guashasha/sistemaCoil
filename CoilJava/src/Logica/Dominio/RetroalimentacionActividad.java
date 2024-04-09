package Logica.Dominio;

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

    public boolean esCorrecto () {
        boolean resultado = calificacionCorrecta(this.getInteres());

        if (!calificacionCorrecta(this.getDificultad())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getInteraccionConPar())) {
            resultado = false;
        }

        return resultado;
    }

    public boolean calificacionCorrecta (int calificacion) {
        return calificacion >= 1 && calificacion <= 5;
    }
}
