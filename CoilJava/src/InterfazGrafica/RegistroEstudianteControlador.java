package InterfazGrafica;

import DTO.UniversidadDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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
    private TextField tfUniversidad;
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioApellidoPaterno;
    @FXML
    private Label txtObligatorioApellidoMaterno;
    @FXML
    private Label txtObligatorioMatricula;
    @FXML
    private BorderPane pnRegistroEstudiante;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private UniversidadDTO universidad;

    public void setHistorialPaneles(Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void setPnVentanaPrincipal(BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setUniversidad(UniversidadDTO universidad) {
        this.universidad = universidad;
        this.tfUniversidad
                .setText(this.universidad
                        .getNombre());
    }

    @FXML
    private void registrarEstudiante () {

    }

    @FXML
    private void cancelarRegistro () {

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

        for (int i = 0; i < camposTexto.length; i++) {
            String texto = camposTexto[i].getText();
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
