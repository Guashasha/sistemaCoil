package InterfazGrafica;

import DTO.EstudianteDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VentanaPrincipalEstudianteControlador {


    @FXML
    private Button btColaboracion;
    private EstudianteDTO estudianteDTO;

    public void setEstudiante (EstudianteDTO estudianteDTO) {
        this.estudianteDTO = estudianteDTO;
    }
}
