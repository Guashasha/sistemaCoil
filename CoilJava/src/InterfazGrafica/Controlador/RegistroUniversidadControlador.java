package InterfazGrafica.Controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegistroUniversidadControlador {

    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField txtNombre;

    @FXML
    void registrarUniversidad(ActionEvent event) {
        System.out.println("Hola");
    }

    

}
