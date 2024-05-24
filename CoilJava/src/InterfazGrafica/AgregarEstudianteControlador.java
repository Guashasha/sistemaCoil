package InterfazGrafica;

import DAO.EstudianteAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class AgregarEstudianteControlador {
    private static final Logger BITACORA = Logger.getLogger(AgregarEstudianteControlador.class);
    @FXML
    private BorderPane pnConsultaEstudiantes;
    @FXML
    private TextField tfBarraBusqueda;
    @FXML
    private VBox vboxResultadosBusqueda;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private UniversidadDTO universidad;
    private List<Integer> idEstudiantesEnColaboracion;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setColaboracion (ColaboracionDTO colaboracion) {
        this.colaboracion = colaboracion;
    }

    public void setUniversidad (UniversidadDTO universidad) {
        this.universidad = universidad;
    }

    public void setIdEstudiantesEnColaboracion (List<Integer> idEstudiantesEnColaboracion) {
        this.idEstudiantesEnColaboracion = idEstudiantesEnColaboracion;
    }

    @FXML
    private void consultar () {

    }

    @FXML
    private void registrarEstudiante () {

    }

    private boolean objetosValidos () {
        return this.colaboracion != null && this.universidad != null;
    }

    public void cargarConsultaGeneral () {
        if (objetosValidos()) {
            EstudianteAuxiliar estudianteAuxiliar = new EstudianteAuxiliar();
            List<EstudianteDTO> listaEstudiantes = new ArrayList<>();
            try {
                listaEstudiantes = estudianteAuxiliar.getEstudiantePorUniversidad(this.universidad.getId());
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
            mostrarConsulta(listaEstudiantes);
        }
        else {
            mostrarMensajeEmergente("Algo salió mal. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }

    private void mostrarConsulta (List<EstudianteDTO> listaEstudiantes) {
        if (!listaEstudiantes.isEmpty()) {
            this.historialPaneles
                    .push(this.pnConsultaEstudiantes);
        }

        for (EstudianteDTO estudiante : listaEstudiantes) {
            if (!idEstudiantesEnColaboracion.contains(estudiante.getIdEstudiante())) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AgregarEstudianteItem.fxml"));
                HBox hboxFila;

                try {
                    hboxFila = fxmlLoader.load();
                    agregarDatosFilaEstudiante(fxmlLoader.getController(), estudiante);
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

                this.vboxResultadosBusqueda
                        .getChildren().add(hboxFila);
            }
        }
    }

    private void agregarDatosFilaEstudiante (AgregarEstudianteItemControlador controlador, EstudianteDTO estudiante) throws ErrorDAO {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

        Optional<UniversidadDTO> universidadOptional = universidadAuxiliar.getUniversidadPorId(estudiante.getIdUniversidad());

        if (universidadOptional.isPresent()) {
            controlador.setUniversidad(universidadOptional.get());
            controlador.setColaboracion(this.colaboracion);
            controlador.setEstudiante(estudiante);
            controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            controlador.setHistorialPaneles(this.historialPaneles);
        }
        else {
            throw new ErrorDAO("Algo salió mal. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
