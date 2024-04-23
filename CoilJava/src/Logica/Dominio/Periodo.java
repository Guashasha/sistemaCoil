package Logica.Dominio;


import Logica.ErrorDAO;

import java.time.LocalDate;

public class Periodo {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Periodo (LocalDate fechaInicio, LocalDate fechaFinal) throws ErrorDAO {
        if (fechaInicio.isAfter(fechaFinal)) {
            throw new ErrorDAO("La fecha final no puede ser antes que la fecha de inicio", ErrorDAO.Tipo.VALIDACION);
        }
        
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFinal;
    }

    public Periodo () {}

    public LocalDate getFechaInicio () {
        return fechaInicio;
    }

    public void setFechaInicio (LocalDate fechaInicio) {
        if (this.fechaFin == null || fechaInicio.isBefore(this.fechaFin)) {
            this.fechaInicio = fechaInicio;
        }
        else {
            throw new ErrorDAO("Inserte una fecha anterior a la fecha de fin", ErrorDAO.Tipo.VALIDACION);
        }
    }

    public LocalDate getFechaFin () {
        return fechaFin;
    }

    public void setFechaFin (LocalDate fechaFin) {
        if (this.fechaInicio == null || fechaFin.isAfter(this.fechaInicio)) {
            this.fechaFin = fechaFin;
        }
        else {
            throw new ErrorDAO("Inserte una fecha posterior a la fecha de inicio", ErrorDAO.Tipo.VALIDACION);
        }
    }

    public boolean validarNulo () {
        return fechaInicio != null &&
                fechaFin != null;
    }

    public boolean esCorrecto () {
        return this.fechaInicio.isBefore(this.fechaFin);
    }
}
