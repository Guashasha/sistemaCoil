package InterfazGrafica.Controlador;

import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConsultaUniversidadesControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void start(Stage stage){
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../Plantilla/ConsultaUniversidades.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene escena = new Scene(root);
            stage.setScene(escena);
            stage.show();
        }
        else {
            BITACORA.error("Ocurrió un error al iniciar la ventana windowEditarUniversidad");
        }
    }
    
}
