package InterfazGrafica;

import Logica.DAO.DAOPais;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class NumeraliaControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(NumeraliaControlador.class);
    @FXML
    private VBox vboxEstadisticas;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("Numeralia.fxml"));
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
            BITACORA.error("Ocurrió un error al iniciar la ventana windowNumeralia");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void mostrarEstadisticasRegion (List<Map> EstadísticasRegion) {
        //vboxConsultaUniversidades.getChildren().clear();
        for (Universidad universidad : listaUniversidades) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UniversidadItem.fxml"));
            HBox hboxFila;

            try {
                hboxFila = fxmlLoader.load();
            }
            catch (IOException error) {
                BITACORA.info(error.getMessage());
                mostrarMensajeEmergente("Algo salió mal, inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
                break;
            }

            DAOPais daoPais = new DAOPais();
            UniversidadItemControlador controladorFilaUniversidad = fxmlLoader.getController();
            Optional<Pais> paisOptional;

            try {
                paisOptional = daoPais.getPaisPorId(universidad.getIdPais());
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
                break;
            }

            controladorFilaUniversidad.setUniversidad(universidad);
            paisOptional.ifPresent(controladorFilaUniversidad::setPais);
            this.vboxConsultaUniversidades.getChildren().add(hboxFila);
        }
    }
}
