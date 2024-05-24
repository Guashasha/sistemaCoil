package InterfazGrafica.Items;

import DTO.ColaboracionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SolicitudHistorialItemControlador {
    @FXML
    private Label lbIdioma;

    @FXML
    private Label lbObjetivo;

    @FXML
    private Label lbPerfilEstudiante;

    @FXML
    private Label lbTemaInteres;
    @FXML
    private Button btnEliminarSolicitud;
    private ColaboracionDTO colaboracionDTO;

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    public ColaboracionDTO getColaboracionDTO () {
        return colaboracionDTO;
    }

    public Button getBtnEliminarSolicitud () {
        return btnEliminarSolicitud;
    }

    public void inicializarLabel () {
        lbIdioma.setText(this.colaboracionDTO.getIdioma());
        lbObjetivo.setText(this.colaboracionDTO.getObjetivo());
        lbPerfilEstudiante.setText(this.colaboracionDTO.getPerfilEstudiante());
        lbTemaInteres.setText(this.colaboracionDTO.getPerfilEstudiante() );
    }

}
