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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

public class RegistroUniversidadControlador implements Initializable {
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
    private ConsultaUniversidadesControlador consultaUniversidadesControlador;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setConsultaUniversidadesControlador(ConsultaUniversidadesControlador consultaUniversidadesControlador) {
        this.consultaUniversidadesControlador = consultaUniversidadesControlador;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxPaises();
    }

    @FXML
    protected void registrarUniversidad () {
        if (!camposVacios()) {
            UniversidadDTO universidadDTO = new UniversidadDTO(tfNombre.getText());
            PaisDTO paisDTO = new PaisDTO(cmbPaises.getValue());
            int filasAfectadas;
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

            try {
                filasAfectadas = universidadAuxiliar.registrarUniversidad(universidadDTO, paisDTO);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                return;
            }

            if (filasAfectadas == 1) {
                mostrarMensajeEmergente("Se ha registrado la universidad exitosamente", Alert.AlertType.INFORMATION);
                limpiarCampos();
            }
            else {
                mostrarMensajeEmergente("Algo salió mal. Intentelo de nuevo más tarde", Alert.AlertType.ERROR);
            }
        }
        else {
            etiquetarCamposVacios();
        }
    }

    @FXML
    protected void cancelarRegistro () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se registrará la universidad");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
                this.consultaUniversidadesControlador.cargarConsultaGeneral();
            }
        });
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

    private void limpiarCampos () {
        tfNombre.setText(null);
        cmbPaises.setValue(null);
    }

    private boolean camposVacios() {
        String nombre = tfNombre.getText();
        boolean nombreVacio = nombre == null || nombre.isBlank();
        boolean paisVacio = cmbPaises.getValue() == null;
        return nombreVacio || paisVacio;
    }

    private void etiquetarCamposVacios () {
        String nombre = tfNombre.getText();
        txtObligatorioNombre.setVisible(nombre == null || nombre.isBlank());
        txtObligatorioPais.setVisible(this.cmbPaises.getValue() == null);
    }
}
