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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

        try {
            filasAfectadas = daoUniversidad.registrarUniversidad(universidad,pais);
        }
        catch (ErrorDAO error) {
            //Mostrar ventana de error
        }

        if (filasAfectadas == 1) {
            //mostrar ventana de registro exitoso
            System.out.println("Registro exitoso");
        }
        else {
            //mostrar ventana de error
            System.out.println("Error");
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
            //Mostrar ventana de error
        }

        ObservableList<String> paisesObservable = FXCollections.observableArrayList(new ArrayList<>(listaPaises));
        cmbPaises.setItems(paisesObservable);
    }
    

}
