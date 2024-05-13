package InterfazGrafica;

import Logica.DAO.DAOAcademico;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Logica.Dominio.Universidad;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SolicitudCuentaControlador implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(SolicitudCuentaControlador.class);
    private Map<String, Universidad> cacheUniversidades = new HashMap<>();
    private final DAOUniversidad DAO_UNIVERSIDAD = new DAOUniversidad();
    private final DAOAcademico DAO_ACADEMICO = new DAOAcademico();
    @FXML
    private ComboBox<String> cmbUniversidad;
    @FXML
    private TextField pfConfirmaContrasena;
    @FXML
    private TextField pfContrasena;
    @FXML
    private TextField tfApellidoM;
    @FXML
    private TextField tfApellidoP;
    @FXML
    private TextField tfCedula;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfUsuario;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) throws ErrorDAO {
        List<Universidad> listaUniversidad;
        listaUniversidad = DAO_UNIVERSIDAD.getTodasAlfabeticamente();
        if (!listaUniversidad.isEmpty()) {
            cargarCacheUniversidades(listaUniversidad);
            cargarListaUniversidad();
        }
        else {
            throw new ErrorDAO("No se encuentran universidades registradas en la base de datos\nInténtelo mas tarde", ErrorDAO.Tipo.CONSULTA);
        }
    }

    @FXML
    public void regresarAInicio () {
        boolean btmAceptadoSeleccionado = mostrarAlertaConfirmacion();
        if (btmAceptadoSeleccionado) {
            try {
                cargarVentanaInicioSesion();
            }
            catch (IOException ioException) {
                BITACORA.fatal(ioException);
                mostrarAlert("No se pudo regresar al inicio de sesión", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void realizarSolicitud() {
        if (sonContrasenasIguales()) {
            try {
                registarCuenta(getDatosAcademico(), getDatosCuenta());
            } catch (ErrorDAO errorDAO) {
                manejarErrorDAO(errorDAO);
            } catch (IOException ioException) {
                mostrarAlert(ioException.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlert("Las contraseñas no coinciden", Alert.AlertType.WARNING);
        }
    }

    private void manejarErrorDAO(ErrorDAO errorDAO) {
        mostrarAlert(errorDAO.getMessage(), Alert.AlertType.WARNING);
        if (errorDAO.getTipo() == ErrorDAO.Tipo.CONEXION) {
            try {
                cargarVentanaInicioSesion();
            } catch (IOException ioException) {
                mostrarAlert(ioException.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void cargarVentanaInicioSesion () throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) tfUsuario.getScene()
                                           .getWindow();
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new IOException("Error al abrir la ventana de inicio de sesión");
        }
    }

    public void registarCuenta (Academico academico, Cuenta cuenta) throws IOException, ErrorDAO {
        int registrarAcamicoConCuenta = DAO_ACADEMICO.agregarAcademicoConCuenta(academico, cuenta);
        if (registrarAcamicoConCuenta == 3) {
            mostrarAlert("Su solicitud ha sido registrada." +
                                 "\nrevise el correo proporcionado en los proximos días", Alert.AlertType.INFORMATION);
            cargarVentanaInicioSesion();
        }
        else {
            mostrarAlert("No se pudo realizar su solicitud, intentenlo mas tarde", Alert.AlertType.WARNING);
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
        alert.setContentText("La solicitud de cuenta no será guardada");

        ButtonType btmAceptar = new ButtonType("Aceptar");
        ButtonType btmRegresar = new ButtonType("Regresar");
        alert.getButtonTypes()
             .setAll(btmAceptar, btmRegresar);

        alert.showAndWait();

        return alert.getResult() == btmAceptar;
    }

    private int obtenerIdUniversidad () {
        String nombreUniversidad = cmbUniversidad.getValue();
        int idUniversidad = -1;
        if (cacheUniversidades.containsKey(nombreUniversidad)) {
            Universidad universidad = cacheUniversidades.get(nombreUniversidad);
            idUniversidad = universidad.getId();
        }
        if (idUniversidad <= 0) {
            throw new ErrorDAO("Error al obtener el identificador de la univervisidad", ErrorDAO.Tipo.CONEXION);
        }
        return idUniversidad;
    }

    private Cuenta getDatosCuenta () throws ErrorDAO {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario(tfUsuario.getText());
        cuenta.setContrasena(pfContrasena.getText());
        cuenta.setTipo(Cuenta.TipoUsuario.academico);
        cuenta.setEstado(Cuenta.EstadoCuenta.pendiente);
        return cuenta;
    }

    private Academico getDatosAcademico () {
        Academico academico = new Academico();
        academico.setNombre(tfNombre.getText());
        academico.setApellidoPaterno(tfApellidoP.getText());
        academico.setApellidoMaterno(tfApellidoM.getText());
        academico.setCorreoElectronico(tfCorreo.getText());
        academico.setCedulaProfesional(tfCedula.getText());
        academico.setIdUniversidad(obtenerIdUniversidad());
        return academico;
    }

    private boolean sonContrasenasIguales () {
        return pfConfirmaContrasena.getText().equals(pfContrasena.getText());
    }

    private void cargarListaUniversidad () {
        ObservableList<String> nombresUniversidades = FXCollections.observableArrayList(cacheUniversidades.keySet());
        cmbUniversidad.setItems(nombresUniversidades);
        eliminarUniversidadEspecifica();
    }

    private void cargarCacheUniversidades (List<Universidad> listaUniversidad) {
        for (Universidad universidad : listaUniversidad) {
            cacheUniversidades.put(universidad.getNombre(), universidad);
        }
    }

    private void eliminarUniversidadEspecifica () {
        cmbUniversidad.getItems()
                      .remove("Universidad Veracruzana");
    }
}
