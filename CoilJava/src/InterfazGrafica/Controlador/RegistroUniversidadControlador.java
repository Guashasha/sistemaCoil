package InterfazGrafica.Controlador;

import Logica.DAO.DAOPais;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegistroUniversidadControlador implements Initializable {

    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<String> cmbPaises;

    @FXML
    void registrarUniversidad (ActionEvent event) {
        Universidad universidad = new Universidad(tfNombre.getText());
        Pais pais = new Pais(cmbPaises.getValue());
        int filasAfectadas = 0;
        DAOUniversidad daoUniversidad = new DAOUniversidad();
        Alert mensaje;

        try {
            filasAfectadas = daoUniversidad.registrarUniversidad(universidad,pais);
        }
        catch (ErrorDAO error) {
            mensaje = new Alert(Alert.AlertType.WARNING);
            mensaje.setContentText(error.getMessage());
            mensaje.setHeaderText(null);
            mensaje.show();
            return;
        }

        if (filasAfectadas == 1) {
            mensaje = new Alert(Alert.AlertType.INFORMATION);
            mensaje.setHeaderText(null);
            mensaje.setContentText("Se ha registrado la universidad exitosamente");
            mensaje.show();
        }
        else {
            mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setContentText("Ocurrió un error, intentelo de nuevo más tarde");
            mensaje.setHeaderText(null);
            mensaje.show();
        }
    }

    @FXML
    void cancelarRegistro () {

    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        DAOPais daoPais = new DAOPais();
        List<String> listaPaises = null;

        try {
            listaPaises = daoPais.getNombresPaisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            Alert mensaje = new Alert(Alert.AlertType.ERROR);
            mensaje.setContentText(error.getMessage());
            mensaje.setHeaderText(null);
            mensaje.show();
        }

        ObservableList<String> paisesObservable = FXCollections.observableArrayList(new ArrayList<>(listaPaises));
        cmbPaises.setItems(paisesObservable);
    }

    public void mostrarMensajeEmergente (Error error, Alert.AlertType tipoAlerta) {
        
    }

}
