package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import InterfazGrafica.Items.ListaEstudiantesItemControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.*;

public class ListaEstudiantesControlador {
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Items/ListaEstudiantesItem.fxml"));
                HBox hboxFila;

                try {
                    hboxFila = fxmlLoader.load();
                    ListaEstudiantesItemControlador controlador = fxmlLoader.getController();
                    controlador.setRecursos(this.pnVentanaPrincipal,this.historialPaneles,this.colaboracion,this,estudiante);
                    this.vboxListaEstudiantes
                            .getChildren().add(hboxFila);
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
            }
        }
    }

    private void agregarDatosVentanaAgregarEstudiante (AgregarEstudianteControlador controlador) {
        this.historialPaneles.push(this.pnListaEstudiantes);

        controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
        controlador.setHistorialPaneles(this.historialPaneles);
        controlador.setColaboracion(this.colaboracion);
        controlador.setAcademico(this.academico);
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
