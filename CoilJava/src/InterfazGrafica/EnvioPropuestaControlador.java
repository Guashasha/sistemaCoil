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

    private ColaboracionDTO obtenerDatosGUI () {
        ColaboracionDTO colaboracionDTO = new ColaboracionDTO();
        colaboracionDTO.setObjetivo(taObjetivo.getText());
        colaboracionDTO.setTemaInteres(tfTemaInteres.getText());
        return colaboracionDTO;
    }

    @FXML
    public void enviarPropuestaColaboracion () {
        ColaboracionDTO colaboracionDTO = obtenerDatosGUI();
        colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.propuesta);
        try {
            COLABORACION_AUXILIAR.registrarPropuestaColaboracion(colaboracionDTO, academicoAnfitrion);
            mostrarMensajeEmergente("Su propuesta ha sido registrada.\nPronto será evaluada,", Alert.AlertType.INFORMATION);
        }
        catch (ErrorDAO errorDAO) {
            mostrarMensajeEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
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
        alert.setHeaderText("¿Seguro que desea cancelar el envio de su propuesta?");
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
