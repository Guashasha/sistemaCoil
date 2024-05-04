package InterfazGrafica;

import Logica.Dominio.Estudiante;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VentanaPrincipalEstudianteControlador {


    @FXML
    private Button btColaboracion;
    private Estudiante estudiante;

    public void setEstudiante (Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
