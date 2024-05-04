package InterfazGrafica;

import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
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

    public void setUniversidad (Universidad universidad) {
        this.lbUniversidad.setText(universidad.getNombre());
    }

    public void setPais (Pais pais) {
        this.lbPais.setText(pais.getNombre());
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
        ventanaEditar.setUniversidadActual(new Universidad(lbUniversidad.getText()));
        ventanaEditar.setPaisActual(new Pais(lbPais.getText()));
        ventanaEditar.getTfNombre().setText(lbUniversidad.getText());
        ventanaEditar.getCmbPaises().setValue(lbPais.getText());
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
