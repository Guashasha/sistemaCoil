package InterfazGrafica.Items;

import DAO.ColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import InterfazGrafica.AgregarEstudianteControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class AgregarEstudianteItemControlador {
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUniversidad;
    private ColaboracionDTO colaboracion;
    private EstudianteDTO estudiante;
    private AgregarEstudianteControlador agregarEstudianteControlador;

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
        this.lbUniversidad
                .setText(universidad.getNombre());
    }

    public void setAgregarEstudianteControlador(AgregarEstudianteControlador agregarEstudianteControlador) {
        this.agregarEstudianteControlador = agregarEstudianteControlador;
    }

    @FXML
    private void agregarEstudiante () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        int filasAfectadas = 0;

        try {
            filasAfectadas = colaboracionAuxiliar.agregarEstudianteAColaboracion(this.colaboracion,this.estudiante);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        if (filasAfectadas == 1) {
            mostrarMensajeEmergente("Se ha agregado el estudiante a la colaboración", Alert.AlertType.INFORMATION);
            this.agregarEstudianteControlador.cargarConsultaGeneral();
        }
        else {
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
