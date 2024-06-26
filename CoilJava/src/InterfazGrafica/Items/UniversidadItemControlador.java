package InterfazGrafica.Items;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import InterfazGrafica.ConsultaUniversidadesControlador;
import InterfazGrafica.EdicionUniversidadControlador;
import Utilidades.ErrorDAO;
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
    private ConsultaUniversidadesControlador consultaUniversidadesControlador;

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

    public void setConsultaUniversidadesControlador (ConsultaUniversidadesControlador consultaUniversidadesControlador) {
        this.consultaUniversidadesControlador = consultaUniversidadesControlador;
    }

    @FXML
    private void editarUniversidad () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../EdicionUniversidad.fxml"));
        BorderPane pnEdicionUniversidad = null;

        try {
            pnEdicionUniversidad = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar las configuraciones de la Universidad", Alert.AlertType.ERROR);
        }

        if (pnEdicionUniversidad != null) {
            EdicionUniversidadControlador edicionUniversidadControlador = fxmlLoader.getController();

            try {
                edicionUniversidadControlador.setRecursos(this.pnVentanaPrincipal, this.historialPaneles, new UniversidadDTO(lbUniversidad.getText()), new PaisDTO(lbPais.getText()), this.consultaUniversidadesControlador);
                this.pnVentanaPrincipal.setCenter(pnEdicionUniversidad);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
