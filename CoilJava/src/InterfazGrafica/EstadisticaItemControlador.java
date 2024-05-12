package InterfazGrafica;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EstadisticaItemControlador  {
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbAlumnos;
    @FXML
    private Label lbProfesores;

    public void setNombre (String nombre) {
        this.lbNombre
                .setText(nombre);
    }

    public void setAlumnos (String alumnos) {
        this.lbAlumnos
                .setText(alumnos);
    }

    public void setProfesores (String profesores) {
        this.lbProfesores
                .setText(profesores);
    }
}
