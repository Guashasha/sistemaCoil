package InterfazGrafica;

import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import javafx.scene.image.ImageView;
import java.util.Stack;

public class ListaEstudiantesItemControlador {
    private static final Logger BITACORA = Logger.getLogger(ListaEstudiantesControlador.class);
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUniversidad;
    @FXML
    private ImageView imgRetirar;
    @FXML
    private Button btnEditar;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private EstudianteDTO estudiante;
    private UniversidadDTO universidad;

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setColaboracion(ColaboracionDTO colaboracion) {
        this.colaboracion = colaboracion;
    }

    public void setEstudiante(EstudianteDTO estudiante) {
        this.estudiante = estudiante;
        this.lbMatricula
                .setText(estudiante.getMatricula());
        String nombreConpleto = estudiante.getNombre() + " " + estudiante.getApellidoPaterno() + " " + estudiante.getApellidoMaterno();
        this.lbNombre
                .setText(nombreConpleto);
    }

    public void setUniversidad(UniversidadDTO universidad) {
        this.universidad = universidad;
        this.lbUniversidad
                .setText(universidad.getNombre());
    }

    @FXML
    private void retirarEstudiante () {

    }

    @FXML
    private void editarEstudiante () {

    }
}
