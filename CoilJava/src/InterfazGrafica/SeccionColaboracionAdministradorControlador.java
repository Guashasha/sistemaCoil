package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Stack;

public class SeccionColaboracionAdministradorControlador {
    private static final Logger BITACORA = Logger.getLogger(SeccionColaboracionAdministradorControlador.class);
    private final Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    @FXML
    private AnchorPane apSeccionColaboracion;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    @FXML
    public void abrirEvaluacionPropuesta () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EvaluacionPropuesta.fxml"));
        BorderPane bpEvaluacionColaboracion = null;

        try {
            bpEvaluacionColaboracion = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al mostrar las propuestas de colaboración", Alert.AlertType.ERROR);
        }
        if (bpEvaluacionColaboracion != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            EvaluacionPropuestaControlador evaluacionPropuestaControlador = fxmlLoader.getController();
            evaluacionPropuestaControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            evaluacionPropuestaControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(bpEvaluacionColaboracion);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

}
