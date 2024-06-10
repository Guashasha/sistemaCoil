package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Stack;

public class CompletaDatosColaboracionControlador implements Initializable {
    @FXML
    private ComboBox<String> cmbIdioma;
    @FXML
    private RadioButton rbClaseEspejo;
    @FXML
    private RadioButton rbCoil;
    @FXML
    private TextArea taPerfilEstudiante;
    @FXML
    private Label lbContadorPerfilEstudiante;
    private ColaboracionDTO colaboracionDTO;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private final ColaboracionAuxiliar COLABORACION_AUXILIAR = new ColaboracionAuxiliar();
    private final ArrayList<String> ARRAY_LIST_IDIOMA = new ArrayList<>(Arrays.asList("Español", "Inglés", "Francés"));

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        registrarEventFilters();
        llenarComboBoxIdioma();
        actualizarContadorPerfilEstudiante();
    }

    @FXML
    private void completarColaboracion () {
        if (sonCamposValidos()) {
            boolean continuar = mostrarAlertaConfirmacion("¿Estás seguro de que deseas completar la información de la colaboración?");
            if (continuar) {
                try {
                    getDatosGUI();
                    this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.disponible);
                    COLABORACION_AUXILIAR.modificar(this.colaboracionDTO);
                    mostrarMensajeEmergente("Datos completados correctamente\nAhora puede ingresar a la sección de MiColaboración", Alert.AlertType.INFORMATION);
                    this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
                }
                catch (IllegalArgumentException error) {
                    mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                }
                catch (ErrorDAO error) {
                    mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }

    @FXML
    private void regresar () {
        boolean btnAceptarSeleccionado = mostrarAlertaConfirmacion("Los datos de la colaboración no se registrarán");
        if (btnAceptarSeleccionado) {
            this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
        }
    }

    private void llenarComboBoxIdioma () {
        ObservableList<String> idiomaObservable = FXCollections.observableArrayList(ARRAY_LIST_IDIOMA);
        this.cmbIdioma.setItems(idiomaObservable);
    }

    private void getDatosGUI () {
        this.colaboracionDTO.setPerfilEstudiante(taPerfilEstudiante.getText());
        this.colaboracionDTO.setIdioma(getIdiomaCmb());
        this.colaboracionDTO.setTipo(getDatosRadio());
    }

    private ColaboracionDTO.TipoColaboracion getDatosRadio () {
        if (rbClaseEspejo.isSelected()) {
            return ColaboracionDTO.TipoColaboracion.claseEspejo;
        }
        else if (rbCoil.isSelected()) {
            return ColaboracionDTO.TipoColaboracion.COIL;
        }
        else {
            throw new IllegalArgumentException("Selecciona un tipo de colaboración");
        }
    }

    private String getIdiomaCmb () {
        String idioma = cmbIdioma.getValue();
        if (idioma == null) {
            throw new IllegalArgumentException("Selecciona una opción de idioma");
        }
        return idioma;
    }

    private boolean sonCamposValidos () {
        if (taPerfilEstudiante.getText() == null || taPerfilEstudiante.getText()
                                                                      .trim()
                                                                      .isEmpty()) {
            mostrarMensajeEmergente("Ingresa un perfil de estudiante", Alert.AlertType.WARNING);
            return false;
        }
        if (cmbIdioma.getValue() == null || cmbIdioma.getValue()
                                                     .trim()
                                                     .isEmpty()) {
            mostrarMensajeEmergente("Selecciona un idioma", Alert.AlertType.WARNING);
            return false;
        }
        if (!rbClaseEspejo.isSelected() && !rbCoil.isSelected()) {
            mostrarMensajeEmergente("Selecciona un tipo de colaboración", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    private boolean mostrarAlertaConfirmacion (String contenido) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        ButtonType btnAceptar = new ButtonType("Aceptar");
        ButtonType btnCancelar = new ButtonType("Cancelar");
        alert.getButtonTypes()
             .setAll(btnAceptar, btnCancelar);

        alert.showAndWait();

        return alert.getResult() == btnAceptar;
    }

    @FXML
    private void restriccionTaPerfilEstudiante (KeyEvent evento) {
        actualizarContadorPerfilEstudiante();
        if (taPerfilEstudiante.getText()
                              .length() >= 200) {
            evento.consume();
        }
    }

    private void actualizarContadorPerfilEstudiante () {
        lbContadorPerfilEstudiante.setText("Número de caracteres: " + taPerfilEstudiante.getText()
                                                                                        .length() + "/200");
    }

    private void registrarEventFilters () {
        taPerfilEstudiante.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTaPerfilEstudiante);
    }

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }
}
