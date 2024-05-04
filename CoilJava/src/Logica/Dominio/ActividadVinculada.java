package Logica.Dominio;

public class ActividadVinculada {
    private Actividad actividad;
    private Colaboracion colaboracion;
    private Periodo periodo;

    public ActividadVinculada (Actividad actividad, Colaboracion colaboracion, Periodo periodo) {
        this.actividad = actividad;
        this.colaboracion = colaboracion;
        this.periodo = periodo;
    }

    public Actividad getActividad () {
        return actividad;
    }

    public void setActividad (Actividad actividad) {
        this.actividad = actividad;
    }

    public Colaboracion getColaboracion () {
        return colaboracion;
    }

    public void setColaboracion (Colaboracion colaboracion) {
        this.colaboracion = colaboracion;
    }

    public Periodo getPeriodo () {
        return periodo;
    }

    public void setPeriodo (Periodo periodo) {
        this.periodo = periodo;
    }

    public boolean esCorrecto () {
        return this.actividad.esCorrecta() &&
                this.colaboracion.esValido() &&
                this.periodo.esCorrecto();
    }
}
