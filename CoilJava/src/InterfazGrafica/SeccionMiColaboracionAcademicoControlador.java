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

import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

public class SeccionMiColaboracionAcademicoControlador {
    private AcademicoDTO academicoDTO;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    @FXML
    private BorderPane bpMiColaboracion;


    @FXML
    public void abrirVentanaSolicitudColaboracion () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Items/SolicitudesColaboracion.fxml"));
        AnchorPane apSolicitud = null;

        try {
            apSolicitud = fxmlLoader.load();
        }
        catch (IOException error) {
            mostrarMensajeEmergente("Error al cargar la ventana e solicitudes", Alert.AlertType.ERROR);
        }
        if (apSolicitud != null) {
            this.historialPaneles.push(this.bpMiColaboracion);
            VentanaActividadesControlador ventanaActividadesControlador = fxmlLoader.getController();
        }
    }

    @FXML
    public void abrirActividades () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaActividades.fxml"));
        AnchorPane apActividades = null;
        try {
            apActividades = fxmlLoader.load();
        }
        catch (IOException error) {
            mostrarMensajeEmergente("Error al mostrar la sección de actividades", Alert.AlertType.ERROR);
        }
        if (apActividades != null) {
            this.historialPaneles.push(this.bpMiColaboracion);
            VentanaActividadesControlador ventanaActividadesControlador = fxmlLoader.getController();
            ventanaActividadesControlador.setPnVentanaPrincipal(this.pnVentanaPrincipal);
            ventanaActividadesControlador.setAcademicoDTO(this.academicoDTO);
            this.pnVentanaPrincipal.setCenter(apActividades);
        }
    }

    @FXML
    private void abrirSeccionEstudiantes () {
        if (obtenerColaboracionVinculadaOActiva().isPresent()) {
            ColaboracionDTO colaboracion = obtenerColaboracionVinculadaOActiva().get();

        }
        else {
            mostrarMensajeEmergente("No es parte de una colaboración actualmente\n", Alert.AlertType.WARNING);
        }
    }

    private Optional<ColaboracionDTO> obtenerColaboracionVinculadaOActiva () {
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

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }


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
}

