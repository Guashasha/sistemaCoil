package InterfazGrafica;

import DTO.AcademicoDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Stack;

public class VentanaPrincipalAcademicoControlador {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalAcademicoControlador.class);

    @FXML
    private BorderPane pnPrincipal;
    @FXML
    private VBox vBoxBotones;
    private AcademicoDTO academico;
    private Stack<Pane> historialPaneles = new Stack<>();

    public void cerrarVentana () {
        Stage window = (Stage) pnPrincipal.getScene().getWindow();
        window.close();
    }

    @FXML
    private void configuracionCuenta () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfiguracionCuenta.fxml"));

        try {
            BorderPane pnConfiguraciónCuenta = fxmlLoader.load();
            ConfiguracionCuentaControlador controlador = fxmlLoader.getController();
            controlador.setRecursos(this.pnPrincipal,this.academico);
            this.pnPrincipal.setCenter(pnConfiguraciónCuenta);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la selección", Alert.AlertType.ERROR);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void abrirSeccionColaboracion () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SeccionColaboracionAcademico.fxml"));
        AnchorPane apSeccionColaboracion = null;

        try {
            apSeccionColaboracion = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la selección", Alert.AlertType.ERROR);
        }

        if (apSeccionColaboracion != null) {
            SeccionColaboracionAcademicoControlador seccionColaboracionAcademicoControlador = fxmlLoader.getController();
            seccionColaboracionAcademicoControlador.setAcademicoDTO(this.academico);
            seccionColaboracionAcademicoControlador.setPnVentanaPrincipal(this.pnPrincipal);
            this.pnPrincipal.setCenter(apSeccionColaboracion);
        }
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
            this.pnPrincipal.setCenter(pnNumeralia);
        }
    }

    public void setAcademico(AcademicoDTO academico) {
        this.academico = academico;
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    public void regresar () {
        if (historialPaneles.size() > 1) {
            Pane ventanaActual;

            historialPaneles.pop();
            ventanaActual = historialPaneles.peek();

            pnPrincipal.setCenter(ventanaActual);
        }
    }
}
