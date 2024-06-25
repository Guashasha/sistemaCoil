package InterfazGrafica;

import DAO.*;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import InterfazGrafica.Items.VinculacionEstudianteItemControlador;
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

public class VinculacionEstudiantesControlador {
    private static final Logger BITACORA = Logger.getLogger(VinculacionEstudiantesControlador.class);
    @FXML
    private BorderPane pnAgregarEstudiante;
    @FXML
    private VBox vboxResultadosBusqueda;
    @FXML
    private TextField tfBarraBusqueda;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private AcademicoDTO academico;
    private final List<Integer> idsEstudiantesEnColaboracion = new ArrayList<>();
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

    public void setAcademico (AcademicoDTO academico) {
        this.academico = academico;
    }

    public void setListaEstudiantesControlador (ListaEstudiantesControlador listaEstudiantesControlador) {
        this.listaEstudiantesControlador = listaEstudiantesControlador;
    }

    @FXML
    private void registrarEstudiante () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroEstudiante.fxml"));
        BorderPane pnRegistroEstudiante = null;

        try {
            pnRegistroEstudiante = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al abrir el registro de estudiantes. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }

        if (pnRegistroEstudiante != null) {
            RegistroEstudianteControlador controlador = fxmlLoader.getController();
            this.historialPaneles.push(this.pnAgregarEstudiante);

            try {
                controlador.setRecursos(this.historialPaneles, this.pnVentanaPrincipal, this.academico.getIdUniversidad());
                controlador.setAgregarEstudianteControlador(this);
                this.pnVentanaPrincipal.setCenter(pnRegistroEstudiante);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
                historialPaneles.pop();
            }
        }
        else {
            mostrarMensajeEmergente("Error al cargar la ventana de registro de estudiante", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void regresar () {
        this.listaEstudiantesControlador.cargarListaEstudiantes();
        this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
    }

    @FXML
    private void buscarEstudiante () {
        String matricula = tfBarraBusqueda.getText();

        if (matricula != null && !matricula.isBlank()) {
            EstudianteAuxiliar estudianteAuxiliar = new EstudianteAuxiliar();
            Optional<EstudianteDTO> estudianteOptional = Optional.empty();

            try {
                estudianteOptional = estudianteAuxiliar.getEstudiantePorMatriculaYUniversidad(matricula, this.academico.getIdUniversidad());
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }

            if (estudianteOptional.isPresent()) {
                List<EstudianteDTO> estudianteEncontrado = new ArrayList<>();
                estudianteEncontrado.add(estudianteOptional.get());
                mostrarConsulta(estudianteEncontrado);
            }
        }
    }

    public void cargarConsultaGeneral () {
        EstudianteDAO estudianteAuxiliar = new EstudianteDAO();
        List<EstudianteDTO> listaEstudiantes = null;

        try {
            listaEstudiantes = estudianteAuxiliar.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(this.academico.getIdUniversidad());
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        if (listaEstudiantes != null) {
            mostrarConsulta(listaEstudiantes);
        }
    }

    private void mostrarConsulta (List<EstudianteDTO> listaEstudiantes) {
        llenarIdsEstudiantes();

        this.vboxResultadosBusqueda.getChildren()
                                   .clear();

        for (EstudianteDTO estudiante : listaEstudiantes) {
            if (!this.idsEstudiantesEnColaboracion.contains(estudiante.getIdEstudiante())) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Items/VinculacionEstudianteItem.fxml"));
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

                this.vboxResultadosBusqueda.getChildren()
                                           .add(hboxFila);
            }
        }
    }

    private void agregarDatosFilaEstudiante (VinculacionEstudianteItemControlador controlador, EstudianteDTO estudiante) throws ErrorDAO {
        UniversidadDAO universidadDAO = new UniversidadDAO();
        Optional<UniversidadDTO> universidadOptional = universidadDAO.getUniversidadPorId(estudiante.getIdUniversidad());

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
                if (estudiante.getIdUniversidad() == this.academico.getIdUniversidad()) {
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
