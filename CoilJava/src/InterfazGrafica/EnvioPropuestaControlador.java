package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class EnvioPropuestaControlador implements Initializable {
    private final ColaboracionAuxiliar COLABORACION_AUXILIAR = new ColaboracionAuxiliar();
    private AcademicoDTO academicoAnfitrion;
    @FXML
    private TextArea taObjetivo;
    @FXML
    private Label lbContadorObjetivo;
    @FXML
    private Label lbContadorTemaInteres;
    @FXML
    private TextArea taTemaInteres;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        actualizarContadorObjetivo();
        actualizarContadorTemaInteres();
        registrarEventFilters();
    }

    private ColaboracionDTO getDatosGUI () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setTemaInteres(taTemaInteres.getText());
        colaboracionDTO.setObjetivo(taObjetivo.getText());
        return colaboracionDTO;
    }

    @FXML
    public void enviarPropuestaColaboracion () {
        if (sonCamposValidos()) {
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

        this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
    }

    private boolean sonCamposValidos () {
        String objetivo = taObjetivo.getText()
                                    .trim();
        String temaInteres = taTemaInteres.getText()
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


    @FXML
    private void restriccionTaTemaInteres (KeyEvent evento) {
        actualizarContadorTemaInteres();
        if (taTemaInteres.getText()
                         .length() >= 100) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionTaObjetivo (KeyEvent evento) {
        actualizarContadorObjetivo();
        if (taObjetivo.getText()
                      .length() >= 300) {
            evento.consume();
        }
    }

    private void registrarEventFilters () {
        taTemaInteres.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTaTemaInteres);
        taObjetivo.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTaObjetivo);
    }

    private void actualizarContadorTemaInteres () {
        lbContadorTemaInteres.setText("Numero de caracteres: " + taTemaInteres.getText()
                                                                              .length() + "/100");
    }

    private void actualizarContadorObjetivo () {
        lbContadorObjetivo.setText("Numero de caracteres: " + taObjetivo.getText()
                                                                        .length() + "/300");
    }

}
