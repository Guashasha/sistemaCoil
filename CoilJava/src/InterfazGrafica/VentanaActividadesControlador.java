package InterfazGrafica;

import DTO.AcademicoDTO;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class VentanaActividadesControlador {
    @FXML
    private AnchorPane apActividades;
    private AcademicoDTO academicoDTO;
    private BorderPane pnVentanaPrincipal;

    public void setApActividades (AnchorPane apActividades) {
        this.apActividades = apActividades;
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }
}
