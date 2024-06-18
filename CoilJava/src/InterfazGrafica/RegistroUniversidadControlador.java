package InterfazGrafica;

import DAO.PaisAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.util.List;
import java.util.Stack;

public class RegistroUniversidadControlador {
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioPais;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<String> cmbPaises;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private ConsultaUniversidadesControlador consultaUniversidadesControlador;

    public void setRecursos (BorderPane pnVentanaPrincipal, Stack<Pane> historialPaneles, ConsultaUniversidadesControlador consultaUniversidadesControlador) throws ErrorDAO {
        if (pnVentanaPrincipal != null && historialPaneles != null && consultaUniversidadesControlador != null) {
            this.pnVentanaPrincipal = pnVentanaPrincipal;
            this.historialPaneles = historialPaneles;
            this.consultaUniversidadesControlador = consultaUniversidadesControlador;
            llenarComboBoxPaises();
        }
        else {
            throw new ErrorDAO("Algo salió mal, reinicie la aplicación y si el problema persiste, contacte con soporte técnico", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @FXML
    protected void registrarUniversidad () {
        if (!camposVacios()) {
            UniversidadDTO universidadDTO = new UniversidadDTO(tfNombre.getText());
            PaisDTO paisDTO = new PaisDTO(cmbPaises.getValue());
            int filasAfectadas;
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

            try {
                filasAfectadas = universidadAuxiliar.registrarUniversidad(universidadDTO, paisDTO);
            }
            catch (ErrorDAO error) {
                Alert.AlertType tipoAlerta = Alert.AlertType.WARNING;
                if (error.getTipo() == ErrorDAO.Tipo.CONEXION) {
                    tipoAlerta = Alert.AlertType.ERROR;
                }
                etiquetarCamposVacios();
                mostrarMensajeEmergente(error.getMessage(), tipoAlerta);
                filasAfectadas = -1;
            }

            if (filasAfectadas > 0) {
                mostrarMensajeEmergente("Se ha registrado la universidad exitosamente", Alert.AlertType.INFORMATION);
                etiquetarCamposVacios();
                limpiarCampos();
            }
            else if (filasAfectadas == 0) {
                mostrarMensajeEmergente("Algo salió mal. Inténtelo de nuevo más tarde", Alert.AlertType.ERROR);
                etiquetarCamposVacios();
            }
        }
        else {
            etiquetarCamposVacios();
        }
    }

    @FXML
    protected void cancelarRegistro () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se registrará la universidad");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
                this.consultaUniversidadesControlador.cargarConsultaGeneral();
            }
        });
    }

    @FXML
    private void limitarCaracteresCampoNombre () {
        int longitud = tfNombre.getLength();
        if (longitud > UniversidadDTO.LONGITUD_NOMBRE) {
            tfNombre.setText(tfNombre.getText()
                    .substring(0, UniversidadDTO.LONGITUD_NOMBRE));
            tfNombre.positionCaret(tfNombre.getLength());
        }
    }

    private void llenarComboBoxPaises () throws ErrorDAO {
        PaisAuxiliar paisAuxiliar = new PaisAuxiliar();
        List<String> listaPaises = paisAuxiliar.getNombresPaisesAlfabeticamente();

        if (!listaPaises.isEmpty()) {
            ObservableList<String> paisesObservable = FXCollections.observableArrayList(listaPaises);
            this.cmbPaises.setItems(paisesObservable);
        }
        else {
            throw new ErrorDAO("No existen los recursos suficientes en la base de datos. Contacte a soporte técnico", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    private void limpiarCampos () {
        tfNombre.setText(null);
        cmbPaises.setValue(null);
    }

    private boolean camposVacios() {
        String nombre = tfNombre.getText();
        boolean nombreVacio = nombre == null || nombre.isBlank();
        boolean paisVacio = cmbPaises.getValue() == null;
        return nombreVacio || paisVacio;
    }

    private void etiquetarCamposVacios () {
        String nombre = tfNombre.getText();
        txtObligatorioNombre.setVisible(nombre == null || nombre.isBlank());
        txtObligatorioPais.setVisible(this.cmbPaises.getValue() == null);
    }
}
