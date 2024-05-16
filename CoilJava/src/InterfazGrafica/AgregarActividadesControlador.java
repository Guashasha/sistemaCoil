package InterfazGrafica;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class AgregarActividadesControlador extends Application {
    public static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);

    @FXML
    private SplitPane spVerActividades = new SplitPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SplitPane root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("agregarActividades.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        AnchorPane panelExtra = null;

        try {
            panelExtra = FXMLLoader.load(getClass().getResource("NuevaActividad.fxml"));
        }
        catch (IOException e) {
            System.err.println("error al crear el panel extra");
        }

        if (panelExtra != null) {
            root.getItems().add(panelExtra);
        }
        else {
            System.err.println("panel es nulo");
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
