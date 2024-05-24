package InterfazGrafica;

import DAO.PaisAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ConsultaUniversidadesControlador {
    private static final Logger BITACORA = Logger.getLogger(ConsultaUniversidadesControlador.class);
    @FXML
    private VBox vboxConsultaUniversidades;
    @FXML
    private TextField tfBarraBusqueda;
    @FXML
    private BorderPane pnConsultaUniversidades;
    private final Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroUniversidad.fxml"));
        BorderPane pnRegistroUniversidad = null;

        try {
            pnRegistroUniversidad = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar el registro de Universidades", Alert.AlertType.ERROR);
        }

        if (pnRegistroUniversidad != null) {
            this.historialPaneles.push(this.pnConsultaUniversidades);
            RegistroUniversidadControlador registroUniversidadControlador = fxmlLoader.getController();
            registroUniversidadControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            registroUniversidadControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(pnRegistroUniversidad);
        }
    }

    public void cargarConsultaTodos () {
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

    private void mostrarConsulta (List<UniversidadDTO> listaUniversidades) {
        vboxConsultaUniversidades.getChildren().clear();

        if (!listaUniversidades.isEmpty()) {
            this.historialPaneles
                    .push(this.pnConsultaUniversidades);
        }

        for (UniversidadDTO universidad : listaUniversidades) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UniversidadItem.fxml"));
            HBox hboxFila;

            try {
                hboxFila = fxmlLoader.load();
                agregarDatosFilaUniversidad(fxmlLoader.getController(), universidad);
            }
            catch (IOException error) {
                BITACORA.info(error.getMessage());
                mostrarMensajeEmergente("Algo salió mal, inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
                break;
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
                break;
            }

            this.vboxConsultaUniversidades.getChildren().add(hboxFila);
        }
    }

    private void agregarDatosFilaUniversidad (UniversidadItemControlador controlador, UniversidadDTO universidad) throws ErrorDAO {
        PaisAuxiliar paisAuxiliar = new PaisAuxiliar();

        Optional<PaisDTO> paisOptional = paisAuxiliar.getPaisPorId(universidad.getIdPais());

        controlador.setUniversidad(universidad);
        paisOptional.ifPresent(controlador::setPais);
        controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
        controlador.setHistorialPaneles(this.historialPaneles);
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
    
}
