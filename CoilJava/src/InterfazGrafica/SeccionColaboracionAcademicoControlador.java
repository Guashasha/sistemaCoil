package InterfazGrafica;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

public class SeccionColaboracionAcademicoControlador {
    private static final Logger BITACORA = Logger.getLogger(SeccionColaboracionAcademicoControlador.class);
    private AcademicoDTO academicoDTO;
    private final Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    @FXML
    private AnchorPane apSeccionColaboracion;


    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }


    @FXML
    private void abrirColaboracionesDisponibbles () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConsultaColaboracion.fxml"));
        BorderPane bpConsultaColaboracion = null;
        try {
            bpConsultaColaboracion = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la ventana de colaboraciones disponibles", Alert.AlertType.ERROR);
        }
        if (bpConsultaColaboracion != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            ConsultaColaboracionControlador consultaColaboracionControlador = fxmlLoader.getController();
            consultaColaboracionControlador.setAcademicoDTO(this.academicoDTO);
            consultaColaboracionControlador.cargarColaboracionItem();
            consultaColaboracionControlador.cargarItemsColaboracionPorBusqueda();
            consultaColaboracionControlador.setHistorialPaneles(this.historialPaneles);
            consultaColaboracionControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            this.pnVentanaPrincipal.setCenter(bpConsultaColaboracion);
        }
    }

    @FXML
    private void abrirCompletarDatos () {
        Optional<ColaboracionDTO> optionalColaboracionDTO = obtenerColaboracionAceptada();
        if (optionalColaboracionDTO.isPresent()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CompletaDatosColaboracion.fxml"));
            BorderPane bpCompleDatos = null;
            try {
                bpCompleDatos = fxmlLoader.load();
            }
            catch (IOException error) {
                BITACORA.fatal(error.getMessage());
                mostrarMensajeEmergente("Error al cargar la ventana para completar los datos", Alert.AlertType.ERROR);
            }
            if (bpCompleDatos != null) {
                this.historialPaneles.push(this.apSeccionColaboracion);
                CompletaDatosColaboracionControlador completaDatosColaboracionControlador = fxmlLoader.getController();
                completaDatosColaboracionControlador.setColaboracionDTO(optionalColaboracionDTO.get());
                completaDatosColaboracionControlador.setHistorialPaneles(this.historialPaneles);
                completaDatosColaboracionControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
                this.pnVentanaPrincipal.setCenter(bpCompleDatos);
            }
        } else {
            mostrarMensajeEmergente("No existe una propuesta aceptada", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void abrirVerMisPostulaciones () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HistorialSolicitud.fxml"));
        BorderPane pnHistorialSolicitud;

        try {
            pnHistorialSolicitud = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la ventana de historial de solicitudes", Alert.AlertType.ERROR);
            return;
        }

        if (pnHistorialSolicitud != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            HistorialSolicitudControlador historialSolicitudControlador = fxmlLoader.getController();
            historialSolicitudControlador.setAcademicoDTO(this.academicoDTO);
            historialSolicitudControlador.cargarItemSolicitud();
            historialSolicitudControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            historialSolicitudControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(pnHistorialSolicitud);
        }
    }

    @FXML
    private void abrirCrearPropuestaColaboracion () {
        if (propuestaYaExiste()) {
            mostrarMensajeEmergente("Ya existe una propuesta realizada, espere la evaluación de la misma", Alert.AlertType.INFORMATION);
            return;
        }

        if (colaboracionAceptadaExiste()) {
            mostrarMensajeEmergente("Su propuesta fue aceptada, por favor vaya a la sección de completar datos", Alert.AlertType.INFORMATION);
            return;
        }

        cargarVentanaCrearPropuesta();
    }

    private boolean propuestaYaExiste () {
        Optional<ColaboracionDTO> propuesta = obtenerPropuesta();
        return propuesta.isPresent();
    }

    private boolean colaboracionAceptadaExiste () {
        Optional<ColaboracionDTO> colaboracion = obtenerColaboracionAceptada();
        return colaboracion.isPresent();
    }

    private void cargarVentanaCrearPropuesta () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EnvioPropuesta.fxml"));
        AnchorPane apEnvioPropuesta;

        try {
            apEnvioPropuesta = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la ventana de crear propuesta", Alert.AlertType.ERROR);
            return;
        }

        if (apEnvioPropuesta != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            EnvioPropuestaControlador envioPropuestaControlador = fxmlLoader.getController();
            envioPropuestaControlador.setAcademicoAnfitrion(this.academicoDTO);
            envioPropuestaControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            envioPropuestaControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(apEnvioPropuesta);
        }
    }

    @FXML
    public void abrirMiColaboracion () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SeccionMiColaboracionAcademico.fxml"));
        BorderPane bpSeccionColaboracion = null;

        try {
            bpSeccionColaboracion = fxmlLoader.load();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarMensajeEmergente("Error al cargar la ventana de crear propuesta", Alert.AlertType.ERROR);
        }
        if (bpSeccionColaboracion != null) {
            this.historialPaneles.push(this.apSeccionColaboracion);
            SeccionMiColaboracionAcademicoControlador seccionMiColaboracionAcademicoControlador = fxmlLoader.getController();
            seccionMiColaboracionAcademicoControlador.setAcademicoDTO(this.academicoDTO);
            seccionMiColaboracionAcademicoControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            seccionMiColaboracionAcademicoControlador.setHistorialPaneles(this.historialPaneles);
            this.pnVentanaPrincipal.setCenter(bpSeccionColaboracion);
        }
    }

    private Optional<ColaboracionDTO> obtenerPropuesta () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> optionalColaboracionDTO = Optional.empty();
        try {
            optionalColaboracionDTO = colaboracionDAO.getPropuestaPorAcademico(this.academicoDTO);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
        return optionalColaboracionDTO;
    }

    private Optional<ColaboracionDTO> obtenerColaboracionAceptada () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> optionalColaboracionDTO = Optional.empty();
        try {
            optionalColaboracionDTO = colaboracionDAO.getColaboracionAceptadaPorAcademico(this.academicoDTO);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
        return optionalColaboracionDTO;
    }


    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
