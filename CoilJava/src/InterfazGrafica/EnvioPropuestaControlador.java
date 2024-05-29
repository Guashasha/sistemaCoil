package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.Stack;

public class EnvioPropuestaControlador {
    private final ColaboracionAuxiliar COLABORACION_AUXILIAR = new ColaboracionAuxiliar();
    private AcademicoDTO academicoAnfitrion;
    @FXML
    private TextArea taObjetivo;
    @FXML
    private TextField tfTemaInteres;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;

    private ColaboracionDTO getDatosGUI () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres(tfTemaInteres.getText());
        colaboracionDTO.setObjetivo(taObjetivo.getText());
        return colaboracionDTO;
    }

    @FXML
    public void enviarPropuestaColaboracion () {
        if (validarCampos()) {
            try {
                ColaboracionDTO colaboracionDTO = getDatosGUI();
                colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
                COLABORACION_AUXILIAR.registrarPropuestaColaboracion(colaboracionDTO, academicoAnfitrion);
                mostrarMensajeEmergente("Su propuesta ha sido registrada.\nPronto será evaluada,", Alert.AlertType.INFORMATION);
            }
            catch (ErrorDAO errorDAO) {
                mostrarMensajeEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validarCampos () {
        String objetivo = taObjetivo.getText()
                                    .trim();
        String temaInteres = tfTemaInteres.getText()
                                          .trim();

        if (objetivo.isEmpty()) {
            mostrarMensajeEmergente("El campo 'Objetivo' no puede estar vacío", Alert.AlertType.WARNING);
            return false;
        }

        if (temaInteres.isEmpty()) {
            mostrarMensajeEmergente("El campo 'Tema de Interés' no puede estar vacío", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    @FXML
    public void cancelarEnvioPropuesta () {
        boolean btnSiSeleccionado = mostrarAlertaConfirmacion();
        if (btnSiSeleccionado) {
            this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
        }
    }

    private boolean mostrarAlertaConfirmacion () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que desea cancelar el envío de su propuesta?");
        alert.setContentText("La propuesta no será guardada");

        ButtonType btnSi = new ButtonType("Si");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes()
             .setAll(btnSi, btnNo);

        alert.showAndWait();

        return alert.getResult() == btnSi;
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    public void setAcademicoAnfitrion (AcademicoDTO academicoAnfitrion) {
        this.academicoAnfitrion = academicoAnfitrion;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }
}
