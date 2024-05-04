package InterfazGrafica;

import Logica.DAO.DAOPais;
import Logica.DAO.DAOUniversidad;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConsultaUniversidadesControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(ConsultaUniversidadesControlador.class);
    @FXML
    private VBox vboxConsultaUniversidades;
    @FXML
    private TextField tfBarraBusqueda;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        consultaTodasAlfaticamente();
    }

    @Override
    public void start(Stage stage){
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("ConsultaUniversidades.fxml"));
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
            BITACORA.error("Ocurrió un error al iniciar la ventana windowConsultaUniversidades");
        }
    }

    private void consultaTodasAlfaticamente() {
        DAOUniversidad daoUniversidad = new DAOUniversidad();
        List<Universidad> listaUniversidades = new ArrayList<>();
        try {
            listaUniversidades = daoUniversidad.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
        mostrarConsulta(listaUniversidades);
    }

    @FXML
    private void consultar () {
        String nombre = tfBarraBusqueda.getText();
        if (!nombre.isBlank()) {
            DAOUniversidad daoUniversidad = new DAOUniversidad();
            Universidad universidad = new Universidad(nombre);
            List<Universidad> listaUniversidades = new ArrayList<>();
            try {
                listaUniversidades = daoUniversidad.getUniversidadesPorNombre(universidad);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
            mostrarConsulta(listaUniversidades);
        }
    }

    @FXML
    private void registrarUniversidad () {
        RegistroUniversidadControlador ventanaRegistro = new RegistroUniversidadControlador();
        ventanaRegistro.start(new Stage());
    }

    private void mostrarConsulta (List<Universidad> listaUniversidades) {
        vboxConsultaUniversidades.getChildren().clear();
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
            if (paisOptional.isPresent()) {
                controladorFilaUniversidad.setPais(paisOptional.get());
            }
            this.vboxConsultaUniversidades.getChildren().add(hboxFila);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
    
}
