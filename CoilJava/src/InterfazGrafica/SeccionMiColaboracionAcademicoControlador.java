package InterfazGrafica;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

public class SeccionMiColaboracionAcademicoControlador {
    private static final Logger BITACORA = Logger.getLogger(SeccionMiColaboracionAcademicoControlador.class);
    private AcademicoDTO academicoDTO;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private CuentaDTO usuario;
    @FXML
    private BorderPane pnActual;


    @FXML
    public void abrirVentanaSolicitudColaboracion () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> optionalColaboracion;

        try {
            optionalColaboracion = colaboracionDAO.getColaboracionDisponiblePorAcademico(academicoDTO.getCedulaProfesional());
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        if (getColaboracionVinculadaOActiva().isEmpty()) {
            if (optionalColaboracion.isPresent()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SolicitudesAColaboracion.fxml"));
                BorderPane pnSolicitudColaboracion = null;

                try {
                    pnSolicitudColaboracion = fxmlLoader.load();
                }
                catch (IOException error) {
                    mostrarMensajeEmergente("Error al cargar la ventana de solicitudes", Alert.AlertType.ERROR);
                    return;
                }

                if (pnSolicitudColaboracion != null) {
                    this.historialPaneles.push(this.pnActual);
                    SolicitudesAColaboracionControlador solicitudesAColaboracionControlador = fxmlLoader.getController();
                    solicitudesAColaboracionControlador.setColaboracionDTO(optionalColaboracion.get());
                    solicitudesAColaboracionControlador.setAcademicoDTO(this.academicoDTO);
                    solicitudesAColaboracionControlador.cargarAcademicosItem();
                    solicitudesAColaboracionControlador.cargarAcademicosItemPorBusqueda();
                    solicitudesAColaboracionControlador.setPaneles(this.pnActual, this.pnVentanaPrincipal);
                    solicitudesAColaboracionControlador.panelVentanaPrincial = this.pnVentanaPrincipal;
                    solicitudesAColaboracionControlador.setHistorialPaneles(this.historialPaneles);
                    this.pnVentanaPrincipal.setCenter(pnSolicitudColaboracion);
                }
            }
            else {
                mostrarMensajeEmergente("Actualmente no eres anfitrión de la colaboración en la que participas", Alert.AlertType.INFORMATION);
            }
        }
        else {
            mostrarMensajeEmergente("Actualmente te encuentras en una colaboración vinculada o ya iniciada. No puedes ver esta opción", Alert.AlertType.INFORMATION);
        }
    }


    @FXML
    public void abrirActividades () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ActividadesColaboracion.fxml"));
        Pane pnActividades = null;

        try {
            pnActividades = fxmlLoader.load();
        }
        catch (IOException error) {
            mostrarMensajeEmergente("Error al mostrar la sección de actividades: ", Alert.AlertType.ERROR);
        }
        if (pnActividades != null) {
            this.historialPaneles.push(this.pnActual);
            ActividadesColaboracionControlador ventanaActividadesControlador = fxmlLoader.getController();
            ventanaActividadesControlador.initialize(pnActual, pnVentanaPrincipal, this.colaboracion, this.usuario);
            this.pnVentanaPrincipal.setCenter(pnActividades);
        }
    }

    @FXML
    private void abrirSeccionEstudiantes () {
        if (getColaboracionVinculadaOActiva().isPresent()) {
            ColaboracionDTO colaboracion = getColaboracionVinculadaOActiva().get();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ListaEstudiantes.fxml"));
            BorderPane pnListaEstudiantes = null;

            try {
                pnListaEstudiantes = fxmlLoader.load();
            }
            catch (IOException error) {
                BITACORA.info(error.getMessage());
                mostrarMensajeEmergente("Algo salió mal al cargar la sección de estudiantes", Alert.AlertType.ERROR);
            }

            if (pnListaEstudiantes != null) {
                ListaEstudiantesControlador controlador = fxmlLoader.getController();
                this.historialPaneles.push(pnActual);

                controlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
                controlador.setHistorialPaneles(this.historialPaneles);
                controlador.setAcademico(this.academicoDTO);
                controlador.setColaboracion(colaboracion);
                controlador.cargarListaEstudiantes();
                this.pnVentanaPrincipal.setCenter(pnListaEstudiantes);
            }
        }
        else {
            mostrarMensajeEmergente("Esta sección será accesible una vex\nque su solaboración sea vinculada a un par académico.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void abrirProgresoColaboracion () {
        Optional<ColaboracionDTO> colaboracionOptional = getColaboracionVinculadaOActiva();
        if (colaboracionOptional.isPresent() || existeColaboracionEnRevision()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProgresoColaboracion.fxml"));
                BorderPane pnProgresoColaboracion = null;

                try {
                    pnProgresoColaboracion = fxmlLoader.load();
                }
                catch (IOException error) {
                    BITACORA.fatal(error.getMessage());
                    mostrarMensajeEmergente("Error al cargar la seccion de inicio de colaboracion", Alert.AlertType.ERROR);
                }
                if (pnProgresoColaboracion != null) {
                    this.historialPaneles.push(pnActual);
                    ProgresoColaboracionControlador progresoColaboracionControlador = fxmlLoader.getController();
                    progresoColaboracionControlador.setColaboracionDTO(colaboracionOptional.get());
                    progresoColaboracionControlador.setAcademicoDTO(this.academicoDTO);
                    progresoColaboracionControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
                    progresoColaboracionControlador.inicializar();
                    this.pnVentanaPrincipal.setCenter(pnProgresoColaboracion);
                }
        }
        else {
            mostrarMensajeEmergente("No existe una colaboracion activa o vinculada con un par", Alert.AlertType.WARNING);
        }
    }

    private Optional<ColaboracionDTO> getColaboracionVinculadaOActiva () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> colaboracionActivaOptional = Optional.empty();
        Optional<ColaboracionDTO> colaboracionVinculadaOptional = Optional.empty();
        try {
            colaboracionActivaOptional = colaboracionDAO.getActivaPorAcademico(this.academicoDTO);
            colaboracionVinculadaOptional = colaboracionDAO.getVinculadaPorAcademico(this.academicoDTO);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        return colaboracionActivaOptional.isPresent() ? colaboracionActivaOptional : colaboracionVinculadaOptional;
    }

    private boolean existeColaboracionEnRevision () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> colaboracionActualOptional = Optional.empty();
        try {
            colaboracionActualOptional = colaboracionDAO.getColaboracionActualPorAcademico(this.academicoDTO.getCedulaProfesional());
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
        if (colaboracionActualOptional.isPresent()) {
            return colaboracionActualOptional.get()
                                             .getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision;
        }
        else {
            return false;
        }
    }


    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    @FXML
    public void regresar () {
        this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }

    public void setColaboracion (ColaboracionDTO colaboracion) {
        this.colaboracion = colaboracion;
    }

    public void setUsuario (CuentaDTO usuario) {
        this.usuario = usuario;
    }
}

