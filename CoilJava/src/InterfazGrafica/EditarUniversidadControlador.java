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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class EditarUniversidadControlador implements Initializable {
    private UniversidadDTO universidadActual;
    private PaisDTO paisActual;
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioPais;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<String> cmbPaises;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setUniversidadActual (UniversidadDTO universidadDTOActual) {
        this.universidadActual = universidadDTOActual;
    }

    public void setPaisActual(PaisDTO paisDTOActual) {
        this.paisActual = paisDTOActual;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxPaises();
    }

    @FXML
    private void editarUniversidad () {
        if (!objetosValidos()) {
            mostrarMensajeEmergente("Algo salió mal. Vuelva a intentarlo más tarde", Alert.AlertType.ERROR);
        }
        else if (!camposVacios() && !camposIguales()) {
            UniversidadDTO universidadDTO = new UniversidadDTO(tfNombre.getText());
            PaisDTO paisDTO = new PaisDTO(cmbPaises.getValue());
            int filasAfectadas;
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

            try {
                filasAfectadas = universidadAuxiliar.editarUniversidad(this.universidadActual, universidadDTO, paisDTO);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                return;
            }

            if (filasAfectadas == 1) {
                this.universidadActual.setNombre(universidadDTO.getNombre());
                this.paisActual.setNombre(paisDTO.getNombre());
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

    @FXML
    private void cancelarEdicion () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se guardarán los cambios");
        alerta.setHeaderText(null);
        alerta.showAndWait()
                .ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.pnVentanaPrincipal
                        .setCenter(this.historialPaneles
                        .get(0));
            }
        });
    }

    private boolean objetosValidos () {
        return this.universidadActual != null && this.paisActual != null;
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
        String nombre = tfNombre.getText();
        boolean nombreVacio = nombre == null || nombre.isBlank();
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
        return nuevoNombre.equals(this.universidadActual.
                getNombre()) && nuevoPais.equals(this.paisActual.
                getNombre());
    }

    public void autocompletarCampos () {
        if (objetosValidos()) {
            this.tfNombre
                    .setText(this.universidadActual
                            .getNombre());
            this.cmbPaises
                    .setValue(this.paisActual
                            .getNombre());
        }
    }
}
