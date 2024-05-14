package DTO;

public class ActividadVinculadaDTO {
    private ActividadDTO actividadDTO;
    private ColaboracionDTO colaboracionDTO;
    private PeriodoDTO periodoDTO;

    public ActividadVinculadaDTO(ActividadDTO actividadDTO, ColaboracionDTO colaboracionDTO, PeriodoDTO periodoDTO) {
        this.actividadDTO = actividadDTO;
        this.colaboracionDTO = colaboracionDTO;
        this.periodoDTO = periodoDTO;
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

    public PeriodoDTO getPeriodo () {
        return periodoDTO;
    }

    public void setPeriodo (PeriodoDTO periodoDTO) {
        this.periodoDTO = periodoDTO;
    }

    public boolean esCorrecto () {
        return this.actividadDTO.esCorrecta() &&
                this.colaboracionDTO.esValido() &&
                this.periodoDTO.esCorrecto();
    }
}
