package InterfazGrafica;

import DAO.PaisAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditarUniversidadControlador implements Initializable {
    private UniversidadDTO universidadDTOActual;
    private PaisDTO paisDTOActual;
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioPais;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<String> cmbPaises;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardarCambios;

    public void setUniversidadActual(UniversidadDTO universidadDTOActual) {
        this.universidadDTOActual = universidadDTOActual;
    }

    public void setPaisActual(PaisDTO paisDTOActual) {
        this.paisDTOActual = paisDTOActual;
    }

    public TextField getTfNombre() {
        return tfNombre;
    }

    public ComboBox<String> getCmbPaises() {
        return cmbPaises;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxPaises();
    }

    @FXML
    private void editarUniversidad () {
        if (!objetosValidos()) {
            mostrarMensajeEmergente("Algo salió mal. Vuelva a intentarlo más tarde", Alert.AlertType.ERROR);
            Stage window = (Stage) btnGuardarCambios.getScene().getWindow();
            window.close();
        }

        if (!camposVacios() && !camposIguales()) {
            UniversidadDTO universidadDTO = new UniversidadDTO(tfNombre.getText());
            PaisDTO paisDTO = new PaisDTO(cmbPaises.getValue());
            int filasAfectadas;
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

            try {
                filasAfectadas = universidadAuxiliar.editarUniversidad(this.universidadDTOActual, universidadDTO, paisDTO);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                return;
            }

            if (filasAfectadas == 1) {
                this.universidadDTOActual.setNombre(universidadDTO.getNombre());
                this.paisDTOActual.setNombre(paisDTO.getNombre());
                mostrarMensajeEmergente("Se han guardado los cambios exitosamente", Alert.AlertType.INFORMATION);
            }
            else {
                mostrarMensajeEmergente("Algo salió mal. Intentelo de nuevo más tarde", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void limitarCaracteres () {
        int longitud = tfNombre.getLength();
        if (longitud > UniversidadDTO.LONGITUD_NOMBRE) {
            tfNombre.setText(tfNombre.getText()
                    .substring(0, UniversidadDTO.LONGITUD_NOMBRE));
            tfNombre.positionCaret(tfNombre.getLength());
        }
    }

    private boolean objetosValidos () {
        return this.universidadDTOActual != null && this.paisDTOActual != null;
    }

    @FXML
    private void cancelarEdicion () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se guardarán los cambios");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage window = (Stage) btnCancelar.getScene().getWindow();
                window.close();
            }
        });
    }

    private void llenarComboBoxPaises () {
        PaisAuxiliar paisAuxiliar = new PaisAuxiliar();
        List<String> listaPaises = new ArrayList<>();
        try {
            listaPaises = paisAuxiliar.getNombresPaisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
        ObservableList<String> paisesObservable = FXCollections.observableArrayList(listaPaises);
        this.cmbPaises.setItems(paisesObservable);
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    private boolean camposVacios() {
        boolean nombreVacio = tfNombre.getText().
                isBlank();
        boolean paisVacio = cmbPaises.getValue() == null;
        etiquetarCamposVacios(nombreVacio,paisVacio);
        return nombreVacio || paisVacio;
    }

    private void etiquetarCamposVacios (boolean nombreVacio, boolean paisVacio) {
        txtObligatorioNombre.setVisible(nombreVacio);
        txtObligatorioPais.setVisible(paisVacio);
    }

    private boolean camposIguales () {
        String nuevoNombre = tfNombre.getText().
                trim();
        String nuevoPais = cmbPaises.getValue();
        return nuevoNombre.equals(this.universidadDTOActual.
                getNombre()) && nuevoPais.equals(this.paisDTOActual.
                getNombre());
    }
}
