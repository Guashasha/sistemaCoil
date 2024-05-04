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

public class EditarUniversidadControlador implements Initializable {
    private Universidad universidadActual;
    private Pais paisActual;
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

    public void setUniversidadActual(Universidad universidadActual) {
        this.universidadActual = universidadActual;
    }

    public void setPaisActual(Pais paisActual) {
        this.paisActual = paisActual;
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

    private boolean objetosValidos () {
        return this.universidadActual != null && this.paisActual != null;
    }

    @FXML
    private void editarUniversidad () {
        if (!objetosValidos()) {
            mostrarMensajeEmergente("Algo salió mal. Vuelva a intentarlo más tarde", Alert.AlertType.ERROR);
            Stage window = (Stage) btnGuardarCambios.getScene().getWindow();
            window.close();
        }

        if (camposValidos() && !camposIguales()) {
            Universidad universidad = new Universidad(tfNombre.getText());
            Pais pais = new Pais(cmbPaises.getValue());
            int filasAfectadas;
            DAOUniversidad daoUniversidad = new DAOUniversidad();

            try {
                filasAfectadas = daoUniversidad.editarUniversidad(this.universidadActual,universidad,pais);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                return;
            }

            if (filasAfectadas == 1) {
                this.universidadActual.setNombre(universidad.getNombre());
                this.paisActual.setNombre(pais.getNombre());
                mostrarMensajeEmergente("Se han guardado los cambios exitosamente", Alert.AlertType.INFORMATION);
            }
            else {
                mostrarMensajeEmergente("Algo salió mal. Intentelo de nuevo más tarde", Alert.AlertType.ERROR);
            }
        }
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

    private boolean camposValidos () {
        boolean nombreValido = DAOUniversidad.cadenaValida(tfNombre.getText());
        boolean paisValido = DAOUniversidad.cadenaValida(cmbPaises.getValue());
        txtObligatorioNombre.setVisible(!nombreValido);
        txtObligatorioPais.setVisible(!paisValido);
        return nombreValido && paisValido;
    }

    private boolean camposIguales () {
        String nuevoNombre = tfNombre.getText().trim();
        String nuevoPais = cmbPaises.getValue();
        return nuevoNombre.equals(this.universidadActual.getNombre()) && nuevoPais.equals(this.paisActual.getNombre());
    }
}
