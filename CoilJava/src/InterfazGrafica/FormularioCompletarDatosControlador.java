package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DTO.AcademicoDTO;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class FormularioCompletarDatosControlador implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(FormularioCompletarDatosControlador.class);

    private AcademicoDTO academico;
    private final AcademicoAuxiliar ACADEMICO_AUXILIAR = new AcademicoAuxiliar();
    @FXML
    private ComboBox<String> cmbAreaEstudios;

    @FXML
    private TextField tfNumeroPersonal;

    @FXML
    private TextField tfNumeroTelefono;
    private final ArrayList<String> ARRAY_LIST_AREA_ESTUDIOS = new ArrayList<>(Arrays.asList("Económico-Administrativo", "Humanidades", "Técnica", "Ciencias de la Salud", "Biología-Agropecuarias", "DGRI"));

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        llenarComboBox();
    }

    private void llenarComboBox () {
        ObservableList<String> areaEstudioObservable = FXCollections.observableArrayList(ARRAY_LIST_AREA_ESTUDIOS);
        this.cmbAreaEstudios.setItems(areaEstudioObservable);
    }

    private void getDatosAcademico () {
        if (!seSeleccionoUnOpcionComboBox() || !seLlenaronLosCampos()) {
            throw new ErrorDAO("Al menos no se lleno o selecciono un campo", ErrorDAO.Tipo.VALIDACION);
        }
        academico.setNumeroTelefonico(tfNumeroTelefono.getText());
        academico.setNumeroPersonal(tfNumeroPersonal.getText());
        academico.setAreaEstudios(sinAcentosYEnMinusculas(cmbAreaEstudios.getValue()));
    }

    private boolean seSeleccionoUnOpcionComboBox () {
        return this.cmbAreaEstudios.getValue() != null;
    }

    private boolean seLlenaronLosCampos () {
        return this.tfNumeroPersonal.getText() != null && tfNumeroTelefono.getText() != null;
    }

    private String sinAcentosYEnMinusculas (String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                         .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                         .toLowerCase();
    }

    @FXML
    private void verificarEntradaNumeros (KeyEvent evento) {
        if (evento.getCharacter()
                  .matches("\\d")) {
            evento.consume();
        }
    }

    private void agregarDatosFaltantes () {
        if (ACADEMICO_AUXILIAR.modificar(this.academico) < 0) {
            throw new ErrorDAO("Eror al registrar los datos faltantes", ErrorDAO.Tipo.INSERCION);
        }
    }

    @FXML
    private void continuarAccion () {
        try {
            getDatosAcademico();
            agregarDatosFaltantes();
            mostrarAlert("""
                                 ¡Excelente!, Bienvenido a MiCoil.
                                 El ultimo paso es volver a iniciar sesión.
                                 Será redireccionado al menu de inicio de sesión""", Alert.AlertType.INFORMATION);
            abrirVentanaInicioSesion();
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void regresarAccion () {
        if (mostrarAlertaConfirmacion()) {
            abrirVentanaInicioSesion();
        }
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }

    private boolean mostrarAlertaConfirmacion () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que desea regresar?");
        alert.setContentText("La información faltante no será registrada");
        ButtonType btmSi = new ButtonType("Si");
        ButtonType btmNo = new ButtonType("No");
        alert.getButtonTypes()
             .setAll(btmSi, btmNo);

        alert.showAndWait();

        return alert.getResult() == btmSi;
    }

    private void abrirVentanaInicioSesion () {
        try {
            Stage stagePrincipal = (Stage) tfNumeroTelefono.getScene()
                                                    .getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
                Parent root = fxmlLoader.load();
                Scene nuevaEscena = new Scene(root);
                stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al mostrar la ventana de inicio de sesión", ErrorDAO.Tipo.VALIDACION);
        }
    }

    public void setAcademico (AcademicoDTO academico) {
        this.academico = academico;
    }



}
