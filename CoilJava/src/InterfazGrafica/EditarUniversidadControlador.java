package InterfazGrafica;

import Logica.DAO.DAOPais;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditarUniversidadControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);
    private Universidad universidadActual = new Universidad("Universidad Veracruzana");;
    private Pais paisActual = new Pais("México");
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

    public void setUniversidadActual (Universidad universidadActual) {
        this.universidadActual = universidadActual;
    }

    public void setPaisActual (Pais paisActual) {
        this.paisActual = paisActual;
    }

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxPaises();
        autocompletarCampos();
    }

    @Override
    public void start(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../Plantilla/EditarUniversidad.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene escena = new Scene(root);
            stage.setScene(escena);
            stage.show();
        }
        else {
            BITACORA.error("Ocurrió un error al iniciar la ventana windowEditarUniversidad");
        }

        if (!objetosValidos()) {
            mostrarMensajeEmergente("Algo salió mal. Vuelva a intentarlo más tarde", Alert.AlertType.ERROR);
            stage.close();
        }
    }

    public boolean objetosValidos () {
        return this.universidadActual != null && this.paisActual != null;
    }

    @FXML
    void editarUniversidad (ActionEvent event) {
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
    void cancelarEdicion () {
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

    public void llenarComboBoxPaises () {
        DAOPais daoPais = new DAOPais();
        List<String> listaPaises;
        try {
            listaPaises = daoPais.getNombresPaisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        ObservableList<String> paisesObservable = FXCollections.observableArrayList(listaPaises);
        this.cmbPaises.setItems(paisesObservable);
    }

    public void autocompletarCampos () {
        this.tfNombre.setText(this.universidadActual.getNombre());
        this.cmbPaises.setValue(this.paisActual.getNombre());
    }

    public void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    public boolean camposValidos () {
        boolean nombreValido = DAOUniversidad.cadenaValida(tfNombre.getText());
        boolean paisValido = DAOUniversidad.cadenaValida(cmbPaises.getValue());
        txtObligatorioNombre.setVisible(!nombreValido);
        txtObligatorioPais.setVisible(!paisValido);
        return nombreValido && paisValido;
    }

    public boolean camposIguales () {
        String nuevoNombre = tfNombre.getText().trim();
        String nuevoPais = cmbPaises.getValue();
        return nuevoNombre.equals(this.universidadActual.getNombre()) && nuevoPais.equals(this.paisActual.getNombre());
    }
}
