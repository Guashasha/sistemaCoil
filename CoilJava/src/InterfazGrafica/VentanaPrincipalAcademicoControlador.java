package InterfazGrafica;

import DTO.AcademicoDTO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Stack;

public class VentanaPrincipalAcademicoControlador {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalAcademicoControlador.class);

    @FXML
    private BorderPane pnPrincipal;
    @FXML
    private VBox vBoxBotones;
    private AcademicoDTO academicoDTO;
    private Stack<Pane> historialPaneles = new Stack<>();

    public void cerrarVentana () {
        Stage window = (Stage) pnPrincipal.getScene().getWindow();
        window.close();
    }


    public void abrirConfiguracionCuenta () {
        // TODO
    }

    @FXML
    public void abrirSeccionColaboracion () {
        
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
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
