package InterfazGrafica.Items;

import DAO.ColaboracionAuxiliar;
import DAO.ColaboracionDAO;
import DAO.UniversidadAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class SolicitudAcademicoItemControlador {

    @FXML
    private Button btnConfirmar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Label lbApellidoPaterno;

    @FXML
    private Label lbApellidoMaterno;

    @FXML
    private Label lbFacultad;

    @FXML
    private Label lbNombre;

    @FXML
    private Label lbUniversidad;

    @FXML
    private Label lbRegion;

    @FXML
    private Label lbAreaEstudios;

    private AcademicoDTO academicoDTO;
    private ColaboracionDTO colaboracionDTO;


    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    private String getUniversidad () {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
        Optional<UniversidadDTO> universidadDTOOptional = universidadAuxiliar.getUniversidadPorId(this.academicoDTO.getIdUniversidad());
        if (universidadDTOOptional.isPresent()) {
            return universidadDTOOptional.get().getNombre();
        }
        throw new IllegalArgumentException("Error al momento de obtener la universidad de LOS académicoS");
    }

    public void setLabel () {
        if (this.academicoDTO.getIdFacultad() == null) {
            lbFacultad.setVisible(false);
            lbRegion.setVisible(false);
        }
        lbNombre.setText(this.academicoDTO.getNombre());
        lbApellidoPaterno.setText(this.academicoDTO.getApellidoPaterno());
        lbApellidoMaterno.setText(this.academicoDTO.getApellidoMaterno());
        lbUniversidad.setText(getUniversidad());
        lbAreaEstudios.setText(this.academicoDTO.getAreaEstudios());
    }

    @FXML
    private void rechazarSolicitud () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        try {
            colaboracionDAO.actualizarEstadoSolicitudDeParticipacion(this.colaboracionDTO.getIdColaboracion(), this.academicoDTO.getCedulaProfesional(), "rechazado");
        }
        catch (ErrorDAO error) {
            mostrarAlert(error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void aceptarSolicitud () {
        if (this.colaboracionDTO.getEstado().toString() != ColaboracionDTO.EstadoColaboracion.vinculada.toString()) {

            ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();

            try {
                colaboracionAuxiliar.aceptarSolicitud(this.colaboracionDTO.getIdColaboracion(), this.academicoDTO.getCedulaProfesional(), "aceptado");
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.vinculada);
                mostrarAlert("Academico aceptado con exito", Alert.AlertType.INFORMATION);
            }
            catch (ErrorDAO error) {
                mostrarAlert(error.getMessage(), Alert.AlertType.ERROR);
            }
        }else {
            mostrarAlert("Ya cuenta con un par académico en su colaboración", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }



    private boolean mostrarConfirmacion(String mensaje) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setContentText(mensaje);
        confirmacion.setHeaderText(null);

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}
