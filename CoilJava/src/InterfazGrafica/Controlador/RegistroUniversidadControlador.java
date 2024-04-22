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
    void registrarUniversidad (ActionEvent event) {
        if (!camposValidos()) {
            return;
        }

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

    @FXML
    void cancelarRegistro () {

    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        DAOPais daoPais = new DAOPais();
        List<String> listaPaises;
        try {
            listaPaises = daoPais.getNombresPaisesAlfabeticamente();
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        ObservableList<String> paisesObservable = FXCollections.observableArrayList(new ArrayList<>(listaPaises));
        cmbPaises.setItems(paisesObservable);
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
