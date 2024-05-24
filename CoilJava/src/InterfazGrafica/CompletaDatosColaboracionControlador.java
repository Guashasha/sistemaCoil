package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private TextField tfPerfil;
    private ColaboracionDTO colaboracionDTO;
    private Stack<Pane> historialPaneles = new Stack<>();
    private BorderPane pnVentanaPrincipal;
    private final ColaboracionAuxiliar COLABORACION_AUXILIAR = new ColaboracionAuxiliar();
    private final ArrayList<String> ARRAY_LIST_IDIOMA = new ArrayList<>(Arrays.asList("Español", "Inglés", "Francés"));

    @FXML
    private void completarColaboracion () {
        boolean continuar = mostrarAlertaConfirmacion("¿Estás seguro de que deseas completar la información de la colaboración?");
        if (continuar) {
            try {
                obtenerDatosGUI();
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.disponible);
                COLABORACION_AUXILIAR.modificar(this.colaboracionDTO);
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
    private void obtenerDatosGUI () {
        this.colaboracionDTO.setPerfilEstudiante(tfPerfil.getText());
            this.colaboracionDTO.setIdioma(obtenerIdiomaCmb());
            this.colaboracionDTO.setTipo(obtenerDatosRadio());
    }

    private ColaboracionDTO.TipoColaboracion obtenerDatosRadio () {
        ColaboracionDTO.TipoColaboracion tipoColaboracion;
        if (rbClaseEspejo.isSelected()) {
            tipoColaboracion = ColaboracionDTO.TipoColaboracion.claseEspejo;
        }
        else if (rbCoil.isSelected()) {
            tipoColaboracion = ColaboracionDTO.TipoColaboracion.COIL;
        }
        else {
            throw new IllegalArgumentException("Selecciona un tipo de colaboracion");
        }
        return tipoColaboracion;
    }

    private String obtenerIdiomaCmb () {
        String idioma = cmbIdioma.getValue();
        if (idioma == null) {
            throw new IllegalArgumentException("Selecciona una opción un idioma");
        }
        return idioma;
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

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
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

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBoxIdioma();
    }
}
