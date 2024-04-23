package InterfazGrafica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class windowNuevaActividad extends Application {
    public static final Logger BITACORA = Logger.getLogger(windowNuevaActividad.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("Plantilla/NuevaActividad.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        if (root != null) {
            stage.setTitle("Crear actividad nueva");
            stage.setScene(new Scene(root));
            stage.show();
        }
        else {
            BITACORA.error("Ocurrió un error al iniciar la ventana windowNuevaActividad");
        }
    }
}
