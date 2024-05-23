package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
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
    private BorderPane pnAgregarEstudiante;
    @FXML
    private TextField tfBarraBusqueda;
    @FXML
    private VBox vboxResultadosBusqueda;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private UniversidadDTO universidad;
    private List<Integer> idsEstudiantesEnColaboracion = new ArrayList<>();
    private ListaEstudiantesControlador listaEstudiantesControlador;

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

    public void setListaEstudiantesControlador(ListaEstudiantesControlador listaEstudiantesControlador) {
        this.listaEstudiantesControlador = listaEstudiantesControlador;
    }

    @FXML
    private void consultar () {

    }

    @FXML
    private void registrarEstudiante () {
        boolean cargarVentanaExitoso = false;
        BorderPane pnRegistroEstudiante = null;

        if (objetosValidos()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroEstudiante.fxml"));

            try {
                pnRegistroEstudiante = fxmlLoader.load();
            }
            catch (IOException error) {
                System.out.println(error.getMessage());
                BITACORA.info(error.getMessage());
            }

            if (pnRegistroEstudiante != null) {
                cargarVentanaExitoso = agregarDatosVentanaRegistroEstudiante(fxmlLoader.getController());
            }
        }

        if (!cargarVentanaExitoso) {
            mostrarMensajeEmergente("Algo salió mal al abrir el registro de estudiantes. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
        else {
            this.pnVentanaPrincipal.setCenter(pnRegistroEstudiante);
        }
    }

    @FXML
    private void regresar () {
        this.listaEstudiantesControlador.cargarListaEstudiantes();
        this.pnVentanaPrincipal
                .setCenter(this.historialPaneles
                        .pop());
    }

    private boolean agregarDatosVentanaRegistroEstudiante (RegistroEstudianteControlador controlador) {
        boolean cargarDatosAVentanaExitoso = false;
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
        Optional<UniversidadDTO> universidadOptional = Optional.empty();

        try {
            universidadOptional = universidadAuxiliar.getUniversidadPorId(this.universidad.getId());
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        if (universidadOptional.isPresent()) {
            this.historialPaneles.push(this.pnAgregarEstudiante);
            controlador.setUniversidad(universidadOptional.get());
            controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            controlador.setHistorialPaneles(this.historialPaneles);
            cargarDatosAVentanaExitoso = true;
        }

        return cargarDatosAVentanaExitoso;
    }

    private boolean objetosValidos () {
        return this.colaboracion != null && this.universidad != null;
    }

    public void cargarConsultaGeneral () {
        if (objetosValidos()) {
            EstudianteAuxiliar estudianteAuxiliar = new EstudianteAuxiliar();
            List<EstudianteDTO> listaEstudiantes = null;

            try {
                listaEstudiantes = estudianteAuxiliar.getEstudiantePorUniversidad(this.universidad.getId());
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }

            if (listaEstudiantes != null) {
                mostrarConsulta(listaEstudiantes);
            }
        }
        else {
            mostrarMensajeEmergente("Algo salió mal. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }

    private void mostrarConsulta (List<EstudianteDTO> listaEstudiantes) {
        llenarIdsEstudiantes();

        this.vboxResultadosBusqueda.getChildren().clear();

        for (EstudianteDTO estudiante : listaEstudiantes) {
            if (!idsEstudiantesEnColaboracion.contains(estudiante.getIdEstudiante())) {
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
            controlador.setAgregarEstudianteControlador(this);
        }
        else {
            throw new ErrorDAO("Algo salió mal. Inténtelo de nuevo más tarde", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private void llenarIdsEstudiantes () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        List<EstudianteDTO> listaEstudiantes = null;

        try {
            listaEstudiantes = colaboracionAuxiliar.getListaDeEstudiantes(this.colaboracion);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        if (listaEstudiantes != null) {
            for (EstudianteDTO estudiante : listaEstudiantes) {
                if (estudiante.getIdUniversidad() == this.universidad.getId()) {
                    this.idsEstudiantesEnColaboracion.add(estudiante.getIdEstudiante());
                }
            }
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
