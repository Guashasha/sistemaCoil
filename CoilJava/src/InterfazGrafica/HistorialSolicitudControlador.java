package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import InterfazGrafica.Items.SolicitudHistorialItemControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HistorialSolicitudControlador {
    private static final Logger BITACORA = Logger.getLogger(HistorialSolicitudControlador.class);
    @FXML
    private VBox vbContenedorSolicitud;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private AcademicoDTO academicoDTO;

    private List<ColaboracionDTO> getSolicitudes () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        return colaboracionDAO.obtenerSolicitudesDeAcademico(this.academicoDTO);
    }

    private void agregarHistorialItem (ColaboracionDTO colaboracionDTO) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../InterfazGrafica/Items/SolicitudHistorialItem.fxml"));

        try {
            VBox vBox = fxmlLoader.load();
            SolicitudHistorialItemControlador solicitudHistorialItemControlador = fxmlLoader.getController();
            solicitudHistorialItemControlador.setColaboracionDTO(colaboracionDTO);
            solicitudHistorialItemControlador.inicializarLabel();

            vbContenedorSolicitud.getChildren()
                                 .add(vBox);
            configurarBtnEliminar(solicitudHistorialItemControlador, vBox);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarAlert("Error al agregar el historial de solicitudes", Alert.AlertType.ERROR);
        }
    }

    public void cargarItemSolicitud () {
        ArrayList<ColaboracionDTO> arrayListSolicitud;
        try {
            arrayListSolicitud = (ArrayList<ColaboracionDTO>) getSolicitudes();
            if (!arrayListSolicitud.isEmpty()) {
                for (ColaboracionDTO colaboracionDTO : arrayListSolicitud) {
                    agregarHistorialItem(colaboracionDTO);
                }
            }
            else {
                mostrarAlert("No hay solicitudes", Alert.AlertType.INFORMATION);
            }
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void configurarBtnEliminar (SolicitudHistorialItemControlador solicitudHistorialItemControlador, VBox vBox) {
        solicitudHistorialItemControlador.getBtnEliminarSolicitud()
                                         .setOnAction(event ->
                                                              eliminarSolicitd(solicitudHistorialItemControlador, vBox)
                                         );
    }

    private void eliminarSolicitd (SolicitudHistorialItemControlador solicitudHistorialItemControlador, VBox vBox) {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        ColaboracionDTO colaboracionDelItem = solicitudHistorialItemControlador.getColaboracionDTO();
        try {
            colaboracionAuxiliar.eliminarSolicitudDeParticipacion(colaboracionDelItem, this.academicoDTO);
            mostrarAlert("Solicitud eliminada", Alert.AlertType.ERROR);
            this.vbContenedorSolicitud.getChildren()
                                      .remove(vBox);

        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
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
}
