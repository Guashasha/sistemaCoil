package DTO;

import java.time.LocalDate;

/**
 * La clase ActividadVinculadaDTO funciona como transfer object, para transferir la información desde la base de datos a capas superiores dentro de la aplicación.
 */
public class ActividadVinculadaDTO {
    private ActividadDTO actividadDTO;
    private ColaboracionDTO colaboracionDTO;
    private LocalDate fechaRealizada;

    public ActividadVinculadaDTO (ActividadDTO actividadDTO, ColaboracionDTO colaboracionDTO, LocalDate periodoDTO) {
        this.actividadDTO = actividadDTO;
        this.colaboracionDTO = colaboracionDTO;
        this.fechaRealizada = periodoDTO;
    }

    public ActividadVinculadaDTO (ActividadDTO actividad, ColaboracionDTO colaboracion) {
        this.actividadDTO = actividad;
        this.colaboracionDTO = colaboracion;
    }

    public ActividadDTO getActividad () {
        return actividadDTO;
    }

    public void setActividad (ActividadDTO actividadDTO) {
        this.actividadDTO = actividadDTO;
    }

    public ColaboracionDTO getColaboracion () {
        return colaboracionDTO;
    }

    public void setColaboracion (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    public LocalDate getPeriodo () {
        return fechaRealizada;
    }

    public void setPeriodo (LocalDate periodoDTO) {
        this.fechaRealizada = periodoDTO;
    }

    public boolean esCorrecto () {
        return this.actividadDTO.esCorrecta() &&
                this.colaboracionDTO.esValido();
    }
}
