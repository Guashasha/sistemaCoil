package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import java.util.Stack;

public class ListaEstudiantesItemControlador {
    private static final Logger BITACORA = Logger.getLogger(ListaEstudiantesControlador.class);
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUniversidad;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private EstudianteDTO estudiante;
    private UniversidadDTO universidad;
    private ListaEstudiantesControlador listaEstudiantesControlador;

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

    public void setListaEstudiantesControlador(ListaEstudiantesControlador listaEstudiantesControlador) {
        this.listaEstudiantesControlador = listaEstudiantesControlador;
    }

    @FXML
    private void retirarEstudiante () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("El alumno se retirará de la colaboración");
        alerta.setHeaderText(null);
        alerta.showAndWait()
                .ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        retirarEstudianteDeColaboracion();
                    }
                });
    }

    @FXML
    private void editarEstudiante () {

    }

    private void retirarEstudianteDeColaboracion () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        int filasAfectadas = 0;

        try {
            filasAfectadas = colaboracionAuxiliar.retirarEstudianteDeColaboracion(this.colaboracion,this.estudiante);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        if (filasAfectadas == 1) {
            mostrarMensajeEmergente("Se ha retirado el estudiante de la colaboración", Alert.AlertType.INFORMATION);
            this.listaEstudiantesControlador
                    .cargarListaEstudiantes();
        }
        else {
            mostrarMensajeEmergente("Algo salió mal al retirar el estudiante de la colaboración. Inténtelo de nuevo más tarde", Alert.AlertType.WARNING);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
