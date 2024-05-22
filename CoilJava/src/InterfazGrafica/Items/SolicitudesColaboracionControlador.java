package InterfazGrafica.Items;

import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;

public class SolicitudesColaboracionControlador {
    private ColaboracionDTO colaboracionDTO;
    private AcademicoDTO academicoDTO;

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }
}
