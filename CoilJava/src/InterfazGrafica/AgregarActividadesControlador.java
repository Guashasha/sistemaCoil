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

public class AgregarActividadesControlador {
    public static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);

    @FXML
    private SplitPane spVerActividades = new SplitPane();

    public AgregarActividadesControlador () {
        try {
            spVerActividades = FXMLLoader.load(getClass().getResource("agregarActividades.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }
    }

    private void leerActividades () {
        
    }
}
