package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import InterfazGrafica.Items.PropuestaItemControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class EvaluacionPropuestaControlador implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(EvaluacionPropuestaControlador.class);
    @FXML
    private Pane pnPropuestaPlantilla;
    @FXML
    private VBox vboxContenedor;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        cargarPropuestaItem();
    }

    private void cargarPropuestaItem () {
        ArrayList<ColaboracionDTO> arrayListPropuestas;
        try {
            arrayListPropuestas = (ArrayList<ColaboracionDTO>) getPropuestas();
            if (!arrayListPropuestas.isEmpty()) {
                for (ColaboracionDTO colaboracionDTO : arrayListPropuestas) {
                    agregarPuestaItem(colaboracionDTO);
                }
            }
            else {
                mostrarAlert("No hay propuestas de colaboracion registradas", Alert.AlertType.INFORMATION);
            }
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void regresar () {
        this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
    }

    private void agregarPuestaItem (ColaboracionDTO colaboracionDTO) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../InterfazGrafica/Items/PropuestaItem.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            PropuestaItemControlador propuestaItemControlador = fxmlLoader.getController();
            propuestaItemControlador.setColaboracionDTO(colaboracionDTO);
            propuestaItemControlador.inicializarLabels();

            vboxContenedor.getChildren()
                          .add(pane);
            configuarBotones(propuestaItemControlador, pane);
        }
        catch (IOException ioException) {
            BITACORA.fatal(ioException.getMessage());
        }
    }

    private void configuarBotones (PropuestaItemControlador propuestaItemControlador, Pane pane) {
        propuestaItemControlador.getBtnAceptar()
                                .setOnAction(event -> cambiarEstadoPropuestaAceptado(propuestaItemControlador, pane));
        propuestaItemControlador.getBtnRechazar()
                                .setOnAction(event -> cambiarEstadoPropuestaRechazado(propuestaItemControlador, pane));
    }

    private void cambiarEstadoPropuestaAceptado (PropuestaItemControlador propuestaItemControlador, Pane pane) {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        ColaboracionDTO colaboracionDTO = propuestaItemControlador.getColaboracionDTO();
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.aceptada);
        try {
            colaboracionAuxiliar.cambiarEstadoColaboracion(colaboracionDTO);
            vboxContenedor.getChildren()
                          .remove(pane);
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cambiarEstadoPropuestaRechazado (PropuestaItemControlador propuestaItemControlador, Pane pane) {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        ColaboracionDTO colaboracionDTO = propuestaItemControlador.getColaboracionDTO();
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.rechazada);
        try {
            colaboracionAuxiliar.cambiarEstadoColaboracion(colaboracionDTO);
            vboxContenedor.getChildren()
                          .remove(pane);
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private List<ColaboracionDTO> getPropuestas () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        return colaboracionAuxiliar.obtenerPropuestasColaboracion();
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }
}
