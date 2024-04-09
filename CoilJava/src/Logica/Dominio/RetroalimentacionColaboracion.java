package Logica.Dominio;

public class RetroalimentacionColaboracion extends Retroalimentacion {
    private int habilidadesObtenidas;
    private int calificacion;
    private int intercambioCultural;
    private int mejoraDelLenguaje;
    private int trabajoColaborativo;
    private int mejoraFormacionProfesional;
    private int colaboracion;

    public RetroalimentacionColaboracion () {
        super();
    }

    public int getHabilidadesObtenidas () {
        return habilidadesObtenidas;
    }

    public void setHabilidadesObtenidas (int habilidadesObtenidas) {
        this.habilidadesObtenidas = habilidadesObtenidas;
    }

    public int getCalificacion () {
        return calificacion;
    }

    public void setCalificacion (int calificacion) {
        this.calificacion = calificacion;
    }

    public int getIntercambioCultural () {
        return intercambioCultural;
    }

    public void setIntercambioCultural (int intercambioCultural) {
        this.intercambioCultural = intercambioCultural;
    }

    public int getMejoraDelLenguaje () {
        return mejoraDelLenguaje;
    }

    public void setMejoraDelLenguaje (int mejoraDelLenguaje) {
        this.mejoraDelLenguaje = mejoraDelLenguaje;
    }

    public int getTrabajoColaborativo () {
        return trabajoColaborativo;
    }

    public void setTrabajoColaborativo (int trabajoColaborativo) {
        this.trabajoColaborativo = trabajoColaborativo;
    }

    public int getMejoraFormacionProfesional () {
        return mejoraFormacionProfesional;
    }

    public void setMejoraFormacionProfesional (int mejoraFormacionProfesional) {
        this.mejoraFormacionProfesional = mejoraFormacionProfesional;
    }

    public int getColaboracion () {
        return colaboracion;
    }

    public void setColaboracion (int colaboracion) {
        this.colaboracion = colaboracion;
    }

    public boolean esCorrecta () {
        boolean resultado = true;

        if (!calificacionCorrecta(this.getCalificacion())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getHabilidadesObtenidas())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getIntercambioCultural())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getMejoraDelLenguaje())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getTrabajoColaborativo())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getMejoraFormacionProfesional())) {
            resultado = false;
        }

        if (!calificacionCorrecta(this.getIntercambioCultural())) {
            resultado = false;
        }

        return resultado;
    }

    private boolean calificacionCorrecta (int calificacion) {
        return (calificacion >= 1 && calificacion <= 5);
    }
}
