package InterfazGrafica.Items;

import DAO.ColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import InterfazGrafica.VinculacionEstudiantesControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class VinculacionEstudianteItemControlador {
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUniversidad;
    private ColaboracionDTO colaboracion;
    private EstudianteDTO estudiante;
    private VinculacionEstudiantesControlador vinculacionEstudiantesControlador;

    public void setColaboracion (ColaboracionDTO colaboracion) {
        this.colaboracion = colaboracion;
    }

    public void setEstudiante (EstudianteDTO estudiante) {
        this.estudiante = estudiante;
        this.lbMatricula.setText(estudiante.getMatricula());
        String nombreCompleto = estudiante.getNombre() + " " + estudiante.getApellidos();
        this.lbNombre.setText(nombreCompleto);
    }

    public void setUniversidad (UniversidadDTO universidad) {
        this.lbUniversidad
                .setText(universidad.getNombre());
    }

    public void setAgregarEstudianteControlador (VinculacionEstudiantesControlador vinculacionEstudiantesControlador) {
        this.vinculacionEstudiantesControlador = vinculacionEstudiantesControlador;
    }

    @FXML
    private void agregarEstudiante () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        int filasAfectadas;

        try {
            filasAfectadas = colaboracionAuxiliar.agregarEstudianteAColaboracion(this.colaboracion, this.estudiante);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            filasAfectadas = -1;
        }

        if (filasAfectadas > 0) {
            mostrarMensajeEmergente("Se ha agregado el estudiante a la colaboración", Alert.AlertType.INFORMATION);
            this.vinculacionEstudiantesControlador.cargarConsultaGeneral();
        }
        else if (filasAfectadas == 0) {
            mostrarMensajeEmergente("Algo salió mal. Inténtelo de nuevo más tarde", Alert.AlertType.WARNING);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

}
