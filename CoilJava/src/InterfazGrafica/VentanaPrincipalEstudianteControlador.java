package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VentanaPrincipalEstudianteControlador implements Initializable {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalEstudianteControlador.class);
    @FXML
    private BorderPane pnVentanaPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        abrirSeccionNumeralia();
    }

    @FXML
    public void cerrarVentana () {
        Stage window = (Stage) pnVentanaPrincipal.getScene().getWindow();
        window.close();
    }

    @FXML
    private void abrirSeccionNumeralia () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Numeralia.fxml"));
        BorderPane pnNumeralia = null;

        try {
            pnNumeralia = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar la Numeralia", Alert.AlertType.ERROR);
        }

        if (pnNumeralia != null) {
            this.pnVentanaPrincipal.setCenter(pnNumeralia);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
