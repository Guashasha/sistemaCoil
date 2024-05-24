package InterfazGrafica;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Stack;

public class UniversidadItemControlador {
    private static final Logger BITACORA = Logger.getLogger(UniversidadItemControlador.class);
    @FXML
    private Label lbPais;
    @FXML
    private Label lbUniversidad;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setUniversidad (UniversidadDTO universidadDTO) {
        this.lbUniversidad.setText(universidadDTO.getNombre());
    }

    public void setPais (PaisDTO paisDTO) {
        this.lbPais.setText(paisDTO.getNombre());
    }

    @FXML
    private void editarUniversidad () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditarUniversidad.fxml"));
        BorderPane pnEditarUniversidad = null;

        try {
            pnEditarUniversidad = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar las configuraciones de la Universidad", Alert.AlertType.ERROR);
        }

        if (pnEditarUniversidad != null) {
            agregarDatosVentanaEditar(fxmlLoader.getController());
            this.pnVentanaPrincipal.setCenter(pnEditarUniversidad);
        }
    }

    private void agregarDatosVentanaEditar (EditarUniversidadControlador controlador) {
        controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
        controlador.setHistorialPaneles(this.historialPaneles);
        controlador.setUniversidadActual(new UniversidadDTO(lbUniversidad.getText()));
        controlador.setPaisActual(new PaisDTO(lbPais.getText()));
        controlador.autocompletarCampos();
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
