package InterfazGrafica;

import DTO.PaisDTO;
import DTO.UniversidadDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import java.io.IOException;

public class UniversidadItemControlador {
    private static final Logger BITACORA = Logger.getLogger(EditarUniversidadControlador.class);
    @FXML
    private Label lbPais;
    @FXML
    private Label lbUniversidad;

    public void setUniversidad (UniversidadDTO universidadDTO) {
        this.lbUniversidad.setText(universidadDTO.getNombre());
    }

    public void setPais (PaisDTO paisDTO) {
        this.lbPais.setText(paisDTO.getNombre());
    }

    @FXML
    private void editarUniversidad () {
        try {
            Stage stagePrincipal = (Stage)  lbPais.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditarUniversidad.fxml"));
            Parent root = fxmlLoader.load();

            agregarDatosVentanEditar(fxmlLoader);

            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal, inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }

    private void agregarDatosVentanEditar (FXMLLoader fxmlLoader) {
        EditarUniversidadControlador ventanaEditar = fxmlLoader.getController();
        ventanaEditar.setUniversidadActual(new UniversidadDTO(lbUniversidad.getText()));
        ventanaEditar.setPaisActual(new PaisDTO(lbPais.getText()));
        ventanaEditar.getTfNombre().
                setText(lbUniversidad.getText());
        ventanaEditar.getCmbPaises().
                setValue(lbPais.getText());
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
