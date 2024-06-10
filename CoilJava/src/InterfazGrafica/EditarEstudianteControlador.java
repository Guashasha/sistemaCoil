package InterfazGrafica;

import DAO.EstudianteAuxiliar;
import DAO.EstudianteDAO;
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

public class EditarEstudianteControlador {
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private Text txtMatriculaActual;
    @FXML
    private Text txtUniversidadActual;
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioApellidoPaterno;
    @FXML
    private Label txtObligatorioApellidoMaterno;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private EstudianteDTO estudiante;
    private ListaEstudiantesControlador listaEstudiantesControlador;

    public void setRecursos (Stack<Pane> historialPaneles, BorderPane pnVentanaPrincipal, EstudianteDTO estudiante, ListaEstudiantesControlador listaEstudiantesControlador) throws ErrorDAO {
        Optional<EstudianteDTO> estudianteOptional = Optional.empty();
        Optional<UniversidadDTO> universidadOptional = Optional.empty();

        if (historialPaneles != null && pnVentanaPrincipal != null && estudiante != null) {
            EstudianteAuxiliar estudianteAuxiliar = new EstudianteAuxiliar();
            estudianteOptional = estudianteAuxiliar.getPorId(estudiante.getIdEstudiante());

            if (estudianteOptional.isPresent()) {
                UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
                universidadOptional = universidadAuxiliar.getUniversidadPorId(estudianteOptional.get()
                        .getIdUniversidad());

                if (universidadOptional.isPresent()) {
                    this.historialPaneles = historialPaneles;
                    this.pnVentanaPrincipal = pnVentanaPrincipal;
                    this.estudiante = estudianteOptional.get();
                    this.listaEstudiantesControlador = listaEstudiantesControlador;

                    this.tfNombre.setText(this.estudiante.getNombre());
                    this.tfApellidoPaterno.setText(this.estudiante.getApellidoPaterno());
                    this.tfApellidoMaterno.setText(this.estudiante.getApellidoMaterno());
                    this.txtMatriculaActual.setText(this.estudiante.getMatricula());
                    this.txtUniversidadActual.setText(universidadOptional.get()
                            .getNombre());
                }
            }
        }

        if (estudianteOptional.isEmpty() || universidadOptional.isEmpty()) {
            throw new ErrorDAO("Error al cargar recursos de la ventana: Editar estudiante", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @FXML
    private void editarEstudiante () {
        if (!camposVacios() && !camposIguales()) {
            EstudianteDAO estudianteDAO = new EstudianteDAO();
            EstudianteDTO estudianteEditado = new EstudianteDTO();
            int filasAfectadas;

            try {
                estudianteEditado.setNombre(tfNombre.getText());
                estudianteEditado.setApellidoPaterno(tfApellidoPaterno.getText());
                estudianteEditado.setApellidoMaterno(tfApellidoMaterno.getText());
                estudianteEditado.setMatricula(txtMatriculaActual.getText());
                estudianteEditado.setIdUniversidad(this.estudiante
                        .getIdUniversidad());
                filasAfectadas = estudianteDAO.modificar(estudianteEditado);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                filasAfectadas = -1;
            }

            if (filasAfectadas > 0) {
                this.estudiante = estudianteEditado;
                mostrarMensajeEmergente("Se han guardado los cambios exitosamente", Alert.AlertType.INFORMATION);
            }
            else if (filasAfectadas == 0) {
                mostrarMensajeEmergente("Algo salió mal al intentar editar los datos del estudiante", Alert.AlertType.ERROR);
            }
        }
        etiquetarCamposVacios();
    }

    @FXML
    private void cancelarEdicion () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se guardarán los cambios");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
                listaEstudiantesControlador.cargarListaEstudiantes();
            }
        });
    }

    private boolean camposVacios () {
        String nombre = this.tfNombre
                .getText();
        String apellidoPaterno = this.tfApellidoPaterno
                .getText();
        String apellidoMaterno = this.tfApellidoMaterno
                .getText();
        return nombre == null || nombre.isBlank() || apellidoPaterno == null || apellidoPaterno.isBlank() || apellidoMaterno == null || apellidoMaterno.isBlank();
    }

    private boolean camposIguales () {
        String nuevoNombre = tfNombre.getText().
                trim();
        String nuevoApeliidoPaterno = tfApellidoPaterno.getText()
                .trim();
        String nuevoApellidoMaterno = tfApellidoMaterno.getText()
                .trim();
        return nuevoNombre.equals(this.estudiante
                .getNombre()) && nuevoApeliidoPaterno.equals(this.estudiante
                .getApellidoPaterno()) && nuevoApellidoMaterno.equals(this.estudiante
                .getApellidoMaterno());
    }

    private void etiquetarCamposVacios () {
        String nombre = tfNombre.getText();
        String apellidoPaterno = tfApellidoPaterno.getText();
        String apellidoMaterno = tfApellidoMaterno.getText();
        txtObligatorioNombre.setVisible(nombre == null || nombre.isBlank());
        txtObligatorioApellidoPaterno.setVisible(apellidoPaterno == null || apellidoPaterno.isBlank());
        txtObligatorioApellidoMaterno.setVisible(apellidoMaterno == null || apellidoMaterno.isBlank());
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }


}
