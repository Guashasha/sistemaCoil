package InterfazGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controler {

    @FXML
    private Button btnOK;

    @FXML
    private TextField tctNombre;

    @FXML
    void guardar(ActionEvent event) {
        System.out.println("Hola");
    }

}
