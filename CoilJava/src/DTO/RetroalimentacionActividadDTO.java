package DTO;

/**
 * La clase RetroalimentacionActividadDTO funciona como transfer object, para transferir la información desde la base de datos a capas superiores dentro de la aplicación.
 */
public class RetroalimentacionActividadDTO extends RetroalimentacionDTO {
    private int idActividad;
    private int dificultad;
    private int interes;

    public RetroalimentacionActividadDTO() {
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

    @Override
    public boolean equals (Object objeto) {
        if (objeto == null || !objeto.getClass().getName().equals(RetroalimentacionActividadDTO.class.getName())) {
            return false;
        }

        RetroalimentacionActividadDTO retroalimentacion = (RetroalimentacionActividadDTO) objeto;

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
