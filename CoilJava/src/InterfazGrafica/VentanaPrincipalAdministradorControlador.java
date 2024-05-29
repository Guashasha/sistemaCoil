package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VentanaPrincipalAdministradorControlador implements Initializable {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalAdministradorControlador.class);
    @FXML
    private BorderPane pnPrincipal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        abrirSeccionNumeralia();
    }

    @FXML
    private void abrirSeccionColaboraciones () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SeccionColaboracionAdministrador.fxml"));
        AnchorPane apColaboracion = null;
        try {
            apColaboracion = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al mostrar la sección de colaboración", Alert.AlertType.ERROR);
        }
        if (apColaboracion != null) {
            SeccionColaboracionAdministradorControlador seccionColaboracionAdministradorControlador = fxmlLoader.getController();
            seccionColaboracionAdministradorControlador.setPnVentanaPrincipal(this.pnPrincipal);
            this.pnPrincipal.setCenter(apColaboracion);
        }
    }

    @FXML
    private void abrirSeccionUniversidades () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConsultaUniversidades.fxml"));
        BorderPane pnConsultaUniversidades = null;

        try {
            pnConsultaUniversidades = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar la sección de Universidades", Alert.AlertType.ERROR);
        }

        if (pnConsultaUniversidades != null) {
            ConsultaUniversidadesControlador consultaUniversidadesControlador = fxmlLoader.getController();
            consultaUniversidadesControlador.setPnVentanaPrincipal(this.pnPrincipal);
            consultaUniversidadesControlador.cargarConsultaGeneral();
            this.pnPrincipal.setCenter(pnConsultaUniversidades);
        }
    }

    @FXML
    private void abrirSeccionNumeralia() {
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
            this.pnPrincipal.setCenter(pnNumeralia);
        }
    }

    @FXML
    private void abrirCrearCuenta () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CrearCuentaAcademico.fxml"));
        Pane pnCrearCuenta = null;

        try {
            pnCrearCuenta = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al entrar a la ventana crear cuenta: "+ error.getMessage(), Alert.AlertType.ERROR);
        }

        if (pnCrearCuenta != null) {
            CrearCuentaAcademicoControlador controlador = fxmlLoader.getController();
            controlador.initialize(this.pnPrincipal);
            this.pnPrincipal.setCenter(pnCrearCuenta);
        }
    }

    @FXML
    private void abrirSeccionCuentas () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GestionCuenta.fxml"));
        BorderPane bpGestionCuenta = null;

        try {
            bpGestionCuenta = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar las cuentas en estado pendiente", Alert.AlertType.ERROR);
        }

        if (bpGestionCuenta != null) {
            GestionCuentaControlador gestionCuentaControlador = fxmlLoader.getController();
            gestionCuentaControlador.setPnVentanaPrincipal(this.pnPrincipal);
            this.pnPrincipal.setCenter(bpGestionCuenta);
        }


    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    @FXML
    private void cerrarVentana () {
        Stage window = (Stage) pnPrincipal.getScene().getWindow();
        window.close();
    }
}
