package InterfazGrafica.Controlador;

import Logica.DAO.DAOPais;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
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

public class RegistroUniversidadControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);
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

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxPaises();
    }

    @Override
    public void start(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../Plantilla/RegistroUniversidad.fxml"));
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
            BITACORA.error("Ocurrió un error al iniciar la ventana windowRegistroUniversidad");
        }
    }

    @FXML
    void registrarUniversidad (ActionEvent event) {
        if (camposValidos()) {
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
            }
            else {
                mostrarMensajeEmergente("Algo salió mal. Intentelo de nuevo más tarde", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void cancelarRegistro () {
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
}
