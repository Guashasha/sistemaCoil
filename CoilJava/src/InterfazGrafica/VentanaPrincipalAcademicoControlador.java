package InterfazGrafica;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Stack;

public class VentanaPrincipalAcademicoControlador extends Application {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalAcademicoControlador.class);

    @FXML
    private BorderPane pnPrincipal = new BorderPane();

    private Stack<Pane> historialPaneles = new Stack<>();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        BorderPane root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("VentanaPrincipal.fxml"));
        }
        catch (IOException error) {
            BITACORA.error(error);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene escena = new Scene(root, Color.TRANSPARENT);
            escena.getStylesheets().add("InterfazGrafica/Recursos/EstiloVentanas.css");

            stage.setScene(escena);
            stage.show();
        } else {
            BITACORA.error("Ocurrió un error al iniciar la ventana principal");
        }

        abrirMenuPrincipal();
    }

    public void cerrarVentana () {
        Stage window = (Stage) pnPrincipal.getScene().getWindow();
        window.close();
    }

    public void abrirMenuPrincipal () {
        Pane inicio = null;

        try {
            inicio = FXMLLoader.load(getClass().getResource("InicioAcademico.fxml"));
        }
        catch (IOException error) {
            BITACORA.error(error);
            return;
        }

        pnPrincipal.setCenter(inicio);

        historialPaneles.add(inicio);
    }

    public void abrirConfiguracionCuenta () {
        // TODO
    }

    public void regresar () {
        // TODO
    }
}
