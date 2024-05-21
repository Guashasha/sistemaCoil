package InterfazGrafica;

import DTO.AcademicoDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Stack;

public class SeccionColaboracionAcademicoControlador {
    private static final Logger BITACORA = Logger.getLogger(SeccionColaboracionAcademicoControlador.class);
    private AcademicoDTO academicoDTO;
    private final Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    @FXML
    private AnchorPane apSeccionColaboracion;


    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }
    

    @FXML
    public void abrirColaboracionesDisponibbles () {
        //todo
    }

    @FXML
    public void abrirCrearPropuestaColaboracion () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EnvioPropuesta.fxml"));
        AnchorPane apEnvioPropuesta = null;

        try {
            apEnvioPropuesta = fxmlLoader.load();

        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la ventana de crear propuesta", Alert.AlertType.ERROR);
        }
        if (apEnvioPropuesta != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            EnvioPropuestaControlador envioPropuestaControlador = fxmlLoader.getController();
            envioPropuestaControlador.setAcademicoAnfitrion(this.academicoDTO);
            envioPropuestaControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            envioPropuestaControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(apEnvioPropuesta);
        }
    }

    @FXML
    public void abrirMiColaboracion () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SeccionMiColaboracion.fxml"));
        BorderPane bpSeccionColaboracion = null;

        try {
            bpSeccionColaboracion = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la ventana de crear propuesta", Alert.AlertType.ERROR);
        }
        if (bpSeccionColaboracion != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            SeccionMiColaboracionControlador seccionMiColaboracionControlador = fxmlLoader.getController();
            seccionMiColaboracionControlador.setAcademicoDTO(this.academicoDTO);
            seccionMiColaboracionControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            seccionMiColaboracionControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(bpSeccionColaboracion);
        }
    }


    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
