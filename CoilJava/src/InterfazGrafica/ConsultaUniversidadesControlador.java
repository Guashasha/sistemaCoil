package InterfazGrafica;

import DAO.PaisAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
    @FXML
    private Button btnRegistrarUniversidad;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        consultaTodasAlfabeticamente();
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

    private void consultaTodasAlfabeticamente() {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
        List<UniversidadDTO> listaUniversidades = new ArrayList<>();
        try {
            listaUniversidades = universidadAuxiliar.getTodasAlfabeticamente();
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
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
            UniversidadDTO universidadDTO = new UniversidadDTO(nombre);
            List<UniversidadDTO> listaUniversidades = new ArrayList<>();
            try {
                listaUniversidades = universidadAuxiliar.getUniversidadesPorNombre(universidadDTO);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
            mostrarConsulta(listaUniversidades);
        }
    }

    @FXML
    private void registrarUniversidad () {
        try {
            Stage stagePrincipal = (Stage)  btnRegistrarUniversidad.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroUniversidad.fxml"));
            Parent root = fxmlLoader.load();
            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal, inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }

    private void mostrarConsulta (List<UniversidadDTO> listaUniversidades) {
        vboxConsultaUniversidades.getChildren().clear();
        for (UniversidadDTO universidadDTO : listaUniversidades) {
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

            PaisAuxiliar paisAuxiliar = new PaisAuxiliar();
            UniversidadItemControlador controladorFilaUniversidad = fxmlLoader.getController();
            Optional<PaisDTO> paisOptional;

            try {
                paisOptional = paisAuxiliar.getPaisPorId(universidadDTO.getIdPais());
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
                break;
            }

            controladorFilaUniversidad.setUniversidad(universidadDTO);
            paisOptional.ifPresent(controladorFilaUniversidad::setPais);
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
