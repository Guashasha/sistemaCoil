package InterfazGrafica;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.IOException;

public class VentanaPrincipalAcademicoControlador extends Application {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalAcademicoControlador.class);

    @FXML
    private Button btnColaboraciones = new Button();

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        BorderPane root = null;

        try {
            // crear panel de fxml
            root = FXMLLoader.load(getClass().getResource("../Plantilla/VentanaPrincipalAcademico.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Crear actividad nueva");

            Scene escena = new Scene(root, Color.TRANSPARENT);
            escena.getStylesheets().add("InterfazGrafica/Recursos/EstiloVentanas.css");

            stage.setScene(escena);
            stage.show();
        } else {
            BITACORA.error("Ocurrió un error al iniciar la ventana windowNuevaActividad");
        }
    }

    public void cerrarVentana () {
        Stage window = (Stage) btnColaboraciones.getScene().getWindow();
        window.close();
    }

    public void abrirConfiguracionCuenta () {
        // TODO
    }
}
