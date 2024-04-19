package InterfazGrafica.Controlador;

import Logica.DAO.DAOAcademico;
import Logica.DAO.DAOCuenta;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SolicitarCuentaControlador implements Initializable {

    @FXML
    private TextField tfNumeroTelefono;
    @FXML
    private TextField tfNumeroPersonal;
    @FXML
    private TextField tfArea;
    @FXML
    private PasswordField pwfContrasena;
    @FXML
    private Hyperlink hpkCuenta;
    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfCorreoElectronico;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCedula;
    @FXML
    private ComboBox<String> cmbUniversidad;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        DAOUniversidad daoUniversidad = new DAOUniversidad();
        List<Universidad> listaUniversidad = null;

        try {
            listaUniversidad = daoUniversidad.getTodasAlfabeticamente();
        }
        catch (ErrorDAO error) {

            mostrarAlertError(error.getMessage());
        }

        if (listaUniversidad != null) {
            List<String> nombresUniversidades = new ArrayList<>();

            for (Universidad universidad : listaUniversidad) {
                nombresUniversidades.add(universidad.getNombre());
            }

            ObservableList<String> universidadObservable = FXCollections.observableList(nombresUniversidades);

            cmbUniversidad.setItems(universidadObservable);
        }

    }


    @FXML
    public void regresarAInicio (ActionEvent evento) {
        boolean btmAceptadoSeleccionado = mostrarAlertaConfirmacion("La solicitud de cuenta no será guardada");
        if (btmAceptadoSeleccionado) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Plantilla/WindowInicioSesion.fxml"));
                Parent root = fxmlLoader.load();
                Stage escenario = (Stage) ((Node) evento.getSource()).getScene()
                                                                     .getWindow();
                Scene escena = new Scene(root);
                escenario.setScene(escena);
                escenario.show();
            }
            catch (IOException error) {
                System.out.println("Mostrar");
            }
        }

    }

    @FXML
    public void registrarSolicitud (ActionEvent evento) {
        String cedula = tfCedula.getText();

        Academico academico = new Academico();
        academico.setNombre(tfNombre.getText());
        academico.setApellidoPaterno(tfApellidoPaterno.getText());
        academico.setApellidoMaterno(tfApellidoMaterno.getText());

        academico.setCedulaProfesional(cedula);
        academico.setNumeroPersonal(tfNumeroPersonal.getText());
        academico.setAreaEstudios(tfArea.getText());
        academico.setCorreoElectronico(tfCorreoElectronico.getText());
        academico.setNumeroTelefonico(tfNumeroTelefono.getText());

        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario(tfUsuario.getText());
        cuenta.setContrasena(pwfContrasena.getText());
        cuenta.setTipo(Cuenta.TipoUsuario.academico);
        cuenta.setEstado(Cuenta.EstadoCuenta.pendiente);

        DAOAcademico daoAcademico = new DAOAcademico();
        DAOCuenta daoCuenta = new DAOCuenta();
        DAOUniversidad daoUniversidad = new DAOUniversidad();
        String nombreUniversidad = cmbUniversidad.getValue();

        int filasAfectadasAcademico = 0;
        int filasAfectadasCuenta = 0;
        try {
            Optional<Universidad> optionalUniversidad = daoUniversidad.getUniversidadPorNombre(nombreUniversidad);
            int idUniversidad = optionalUniversidad.orElseThrow(() -> new ErrorDAO("La universidad con el nombre especificado no existe.", ErrorDAO.Tipo.VALIDACION)).getId();
            academico.setIdUniversidad(idUniversidad);
            filasAfectadasAcademico = daoAcademico.agregarAcademicoExterno(academico);

            Optional<Academico> academicoOptional = daoAcademico.getAcademicoPorCedula(cedula);
            Academico academicoAux = academicoOptional.orElseThrow(() -> new ErrorDAO("El académico con la cédula especificada no existe.", ErrorDAO.Tipo.VALIDACION));
            int idPersona = academicoAux.getIdPersona();
            cuenta.setIdPersona(idPersona);
            filasAfectadasCuenta = daoCuenta.agregar(cuenta);
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlertInfo(errorDAO.getMessage());
        }

        if (filasAfectadasCuenta == 1 && filasAfectadasAcademico == 2) {
            mostrarAlertInfo("Se ha enviado su solicitud, pronto será evaluada");
            limpiarCamposYComboBox();
        }
        else if (filasAfectadasCuenta != 1) {
            mostrarAlertError("Error al crear cuenta: Contacte con un administrador");
        }
        else {
            mostrarAlertError("Error el registro de la información: Contacte con un administrador");
        }
    }

    private void mostrarAlertError (String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    private void mostrarAlertInfo (String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Solicitud exitosa");
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }

    private boolean mostrarAlertaConfirmacion (String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Seguro que desea regresar?");
        alert.setContentText(mensaje);

        ButtonType btmAceptar = new ButtonType("Aceptar");
        ButtonType btmRegresar = new ButtonType("Regresar");
        alert.getButtonTypes()
             .setAll(btmAceptar, btmRegresar);

        alert.showAndWait();

        return alert.getResult() == btmAceptar;
    }

    private void limpiarCamposYComboBox () {
        tfNombre.clear();
        tfApellidoPaterno.clear();
        tfApellidoMaterno.clear();
        tfCedula.clear();
        cmbUniversidad.setValue(null);
        tfCorreoElectronico.clear();
        tfArea.clear();
        tfNumeroPersonal.clear();
        tfNumeroTelefono.clear();
        tfUsuario.clear();
        pwfContrasena.clear();
    }
}
