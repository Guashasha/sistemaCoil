package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.*;

public class ListaEstudiantesControlador extends Application {
    private static final Logger BITACORA = Logger.getLogger(ListaEstudiantesControlador.class);
    @FXML
    private BorderPane pnListaEstudiantes;
    @FXML
    private VBox vboxListaEstudiantes;
    private BorderPane pnVentanaPrincipal;
    private Stack<Pane> historialPaneles = new Stack<>();
    private AcademicoDTO academico;
    private ColaboracionDTO colaboracion;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setColaboracion(ColaboracionDTO colaboracion) {
        this.colaboracion = colaboracion;
    }

    public void setAcademico(AcademicoDTO academico) {
        this.academico = academico;
    }

    @Override
    public void start (Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("ListaEstudiantes.fxml"));
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
            BITACORA.error("Ocurrió un error al iniciar la ventana windowListaEstudiantes");
        }
    }

    @FXML
    private void agregarEstudiante () {
        if (objetosValidos()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AgregarEstudiante.fxml"));
            BorderPane pnAgregarEstudiante = null;

            try {
                pnAgregarEstudiante = fxmlLoader.load();
            }
            catch (IOException error) {
                BITACORA.info(error.getMessage());
                mostrarMensajeEmergente("Algo salió mal al cargar la sección: Agregar estudiante.  Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
            }

            if (pnAgregarEstudiante != null) {
                agregarDatosVentanaAgregarEstudiante(fxmlLoader.getController());
                this.pnVentanaPrincipal.setCenter(pnAgregarEstudiante);
            }
        }
        else {
            mostrarMensajeEmergente("Algo salió mal al cargar la sección: Agregar estudiante. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void regresar () {
        if (this.historialPaneles
                .peek() == this.pnListaEstudiantes) {
            this.historialPaneles
                    .pop();
        }
        this.pnVentanaPrincipal
                .setCenter(this.historialPaneles
                        .pop());
    }

    private boolean objetosValidos () {
        return academico != null && colaboracion != null;
    }

    public void cargarListaEstudiantes () {
        if (objetosValidos()) {
            ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
            List<EstudianteDTO> listaEstudiantes = new ArrayList<>();
            try {
                listaEstudiantes = colaboracionAuxiliar.getListaDeEstudiantes(this.colaboracion);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
            mostrarListaEstudiantes(listaEstudiantes);
        }
        else {
            mostrarMensajeEmergente("Algo salió mal. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }

    private void mostrarListaEstudiantes (List<EstudianteDTO> listaEstudiantes) {
        if (!listaEstudiantes.isEmpty() && historialPaneles.peek() != this.pnListaEstudiantes) {
            this.historialPaneles
                    .push(this.pnListaEstudiantes);
        }

        this.vboxListaEstudiantes.getChildren().clear();

        for (EstudianteDTO estudiante : listaEstudiantes) {
            if (estudiante.getIdUniversidad() == this.academico.getIdUniversidad()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ListaEstudiantesItem.fxml"));
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

                this.vboxListaEstudiantes
                        .getChildren().add(hboxFila);
            }
        }
    }

    private void agregarDatosFilaEstudiante (ListaEstudiantesItemControlador controlador, EstudianteDTO estudiante) throws ErrorDAO {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

        Optional<UniversidadDTO> universidadOptional = universidadAuxiliar.getUniversidadPorId(estudiante.getIdUniversidad());

        if (universidadOptional.isPresent()) {
            controlador.setUniversidad(universidadOptional.get());
            controlador.setColaboracion(this.colaboracion);
            controlador.setEstudiante(estudiante);
            controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            controlador.setHistorialPaneles(this.historialPaneles);
            controlador.setListaEstudiantesControlador(this);
        }
        else {
            throw new ErrorDAO("Algo salió mal. Inténtelo más tarde", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private void agregarDatosVentanaAgregarEstudiante (AgregarEstudianteControlador controlador) {
        this.historialPaneles.push(this.pnListaEstudiantes);

        controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
        controlador.setHistorialPaneles(this.historialPaneles);
        controlador.setColaboracion(this.colaboracion);
        controlador.setUniversidad(new UniversidadDTO(this.academico
                .getIdUniversidad()));
        controlador.setListaEstudiantesControlador(this);
        controlador.cargarConsultaGeneral();
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
