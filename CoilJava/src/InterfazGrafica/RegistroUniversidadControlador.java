package InterfazGrafica;

import Logica.DAO.DAOPais;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
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

public class RegistroUniversidadControlador implements Initializable {
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

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxPaises();
    }

    @FXML
    protected void registrarUniversidad () {
        if (!camposVacios()) {
            Universidad universidad = new Universidad(tfNombre.getText());
            Pais pais = new Pais(cmbPaises.getValue());
            int filasAfectadas;
            DAOUniversidad daoUniversidad = new DAOUniversidad();

            try {
                filasAfectadas = daoUniversidad.registrarUniversidad(universidad,pais);
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
    }

    @FXML
    protected void cancelarRegistro () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se registrará la universidad");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage window = (Stage) btnCancelar.getScene().getWindow();
                window.close();
            }
        });
    }

    @FXML
    private void limitarCaracteres () {
        int longitud = tfNombre.getLength();
        if (longitud > Universidad.LONGITUD_NOMBRE) {
            tfNombre.setText(tfNombre.getText()
                    .substring(0,Universidad.LONGITUD_NOMBRE));
            tfNombre.positionCaret(tfNombre.getLength());
        }
    }

    private void llenarComboBoxPaises () {
        DAOPais daoPais = new DAOPais();
        List<String> listaPaises = new ArrayList<>();
        try {
            listaPaises = daoPais.getNombresPaisesAlfabeticamente();
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
}
