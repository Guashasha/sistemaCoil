package Logica.Dominio;


import java.time.LocalDate;

public class Periodo {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public LocalDate getFechaInicio () {
        return fechaInicio;
    }

    public void setFechaInicio (LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin () {
        return fechaFin;
    }

    public void setFechaFin (LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean validarNulo () {
        return fechaInicio != null &&
                fechaFin != null;
    }
}
