package InterfazGrafica;

import DAO.EstudianteAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.util.Optional;
import java.util.Stack;

public class RegistroEstudianteControlador {
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfMatricula;
    @FXML
    private Text txtUniversidadAcademico;
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioApellidoPaterno;
    @FXML
    private Label txtObligatorioApellidoMaterno;
    @FXML
    private Label txtObligatorioMatricula;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private UniversidadDTO universidad;
    private AgregarEstudianteControlador agregarEstudianteControlador;

    public void setAgregarEstudianteControlador(AgregarEstudianteControlador agregarEstudianteControlador) {
        this.agregarEstudianteControlador = agregarEstudianteControlador;
    }

    public void setRecursos (Stack<Pane> historialPaneles, BorderPane pnVentanaPrincipal, int idUniversidad) throws ErrorDAO {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
        Optional<UniversidadDTO> universidadOptional = universidadAuxiliar.getUniversidadPorId(idUniversidad);

        if (universidadOptional.isPresent()) {
            this.historialPaneles = historialPaneles;
            this.pnVentanaPrincipal = pnVentanaPrincipal;
            this.universidad = universidadOptional.get();
            this.txtUniversidadAcademico.setText(this.universidad.getNombre());
        }
        else {
            throw new ErrorDAO("Algo salió mal al cargar la ventana de Registro de estudiante. Reinicie la aplicación", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @FXML
    private void registrarEstudiante () {
        if (!camposVacios()) {
            EstudianteAuxiliar estudianteAuxiliar = new EstudianteAuxiliar();
            EstudianteDTO estudiante = new EstudianteDTO();
            int filasAfectadas;

            try {
                estudiante.setNombre(this.tfNombre
                        .getText());
                estudiante.setApellidos(this.tfApellidoPaterno
                        .getText());
                estudiante.setIdUniversidad(this.universidad
                        .getId());
                estudiante.setMatricula(this.tfMatricula
                        .getText());
                filasAfectadas = estudianteAuxiliar.agregar(estudiante);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                filasAfectadas = -1;
            }

            if (filasAfectadas > 0) {
                mostrarMensajeEmergente("Se ha registrado el estudiante exitosamente", Alert.AlertType.INFORMATION);
                etiquetarCamposVacios();
                limpiarCampos();
            }
            else if (filasAfectadas == 0) {
                mostrarMensajeEmergente("Algo salió mal al intentar registrar el estudiante", Alert.AlertType.ERROR);
            }
        }
        else {
            etiquetarCamposVacios();
        }
    }

    @FXML
    private void cancelarRegistro () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("Se cancelará el registro del estudiante");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
                if (this.agregarEstudianteControlador != null) {
                    agregarEstudianteControlador.cargarConsultaGeneral();
                }
            }
        });
    }

    private void limpiarCampos () {
        tfNombre.setText(null);
        tfApellidoPaterno.setText(null);
        tfApellidoMaterno.setText(null);
        tfMatricula.setText(null);
    }

    private boolean camposVacios () {
        TextField[] camposTexto = new TextField[]{this.tfNombre,this.tfApellidoPaterno,this.tfApellidoMaterno,this.tfMatricula};
        boolean vacio = true;

        for (TextField textField : camposTexto) {
            String texto = textField.getText();
            vacio = texto == null || texto.isBlank();
            if (vacio) {
                break;
            }
        }

        return vacio;
    }

    private void etiquetarCamposVacios () {
        String nombre = tfNombre.getText();
        String apellidoPaterno = tfApellidoPaterno.getText();
        String apellidoMaterno = tfApellidoMaterno.getText();
        String matricula = tfMatricula.getText();
        txtObligatorioNombre.setVisible(nombre == null || nombre.isBlank());
        txtObligatorioApellidoPaterno.setVisible(apellidoPaterno == null || apellidoPaterno.isBlank());
        txtObligatorioApellidoMaterno.setVisible(apellidoMaterno == null || apellidoMaterno.isBlank());
        txtObligatorioMatricula.setVisible(matricula == null || matricula.isBlank());
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
