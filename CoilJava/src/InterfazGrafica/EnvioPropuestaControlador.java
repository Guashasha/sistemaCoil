package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EnvioPropuestaControlador {
    private final ColaboracionAuxiliar COLABORACION_AUXILIAR = new ColaboracionAuxiliar();
    private AcademicoDTO academicoAnfitrion;
    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnEnviar;

    @FXML
    private TextArea taObjetivo;

    @FXML
    private TextField tfTemaInteres;

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
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Error aqui");
        }
    }

    @FXML
    public void cancelarEnvioPropuesta () {
        boolean btnSiSeleccionado = mostrarAlertaConfirmacion();
        if (btnSiSeleccionado) {
            //todo
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

    public void setAcademicoAnfitrion (AcademicoDTO academicoAnfitrion) {
        this.academicoAnfitrion = academicoAnfitrion;
    }
}
