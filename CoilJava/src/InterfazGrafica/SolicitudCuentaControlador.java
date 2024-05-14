package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import com.sun.mail.imap.ACL;
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
    private Map<String, UniversidadDTO> cacheUniversidades = new HashMap<>();
    private final UniversidadAuxiliar DAO_UNIVERSIDAD = new UniversidadAuxiliar();
    private final AcademicoAuxiliar DAO_ACADEMICO = new AcademicoAuxiliar();
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
        List<UniversidadDTO> listaUniversidadDTO;
        listaUniversidadDTO = DAO_UNIVERSIDAD.getTodasAlfabeticamente();
        if (!listaUniversidadDTO.isEmpty()) {
            cargarCacheUniversidades(listaUniversidadDTO);
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
    public void realizarSolicitud () {
        try {
            registarCuenta(getDatosAcademico(), getDatosCuenta());
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.WARNING);
            if (errorDAO.getTipo() == ErrorDAO.Tipo.CONEXION) {
                try {
                    cargarVentanaInicioSesion();
                }
                catch (IOException ioException) {
                    mostrarAlert(ioException.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
        catch (IOException ioException) {
            mostrarAlert(ioException.getMessage(), Alert.AlertType.ERROR);
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

    public void registarCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws IOException, ErrorDAO {
        int registrarAcamicoConCuenta = DAO_ACADEMICO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
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

    private void limpiarCamposYComboBox () {
        tfNombre.clear();
        tfApellidoP.clear();
        tfApellidoM.clear();
        tfCedula.clear();
        cmbUniversidad.setValue(null);
        tfCorreo.clear();
        tfUsuario.clear();
        pfContrasena.clear();
        pfConfirmaContrasena.clear();
    }

    private int obtenerIdUniversidad () {
        String nombreUniversidad = cmbUniversidad.getValue();
        int idUniversidad = -1;
        if (cacheUniversidades.containsKey(nombreUniversidad)) {
            UniversidadDTO universidadDTO = cacheUniversidades.get(nombreUniversidad);
            idUniversidad = universidadDTO.getId();
        }
        if (idUniversidad <= 0) {
            throw new ErrorDAO("Error al obtener el identificador de la univervisidad", ErrorDAO.Tipo.CONEXION);
        }
        return idUniversidad;
    }

    private CuentaDTO getDatosCuenta () throws ErrorDAO {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNombreUsuario(tfUsuario.getText());
        cuentaDTO.setContrasena(pfContrasena.getText());
        cuentaDTO.setTipo(CuentaDTO.TipoUsuario.academico);
        cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.pendiente);
        return cuentaDTO;
    }

    private AcademicoDTO getDatosAcademico () {
        AcademicoDTO academicoDTO = new AcademicoDTO();
        academicoDTO.setNombre(tfNombre.getText());
        academicoDTO.setApellidoPaterno(tfApellidoP.getText());
        academicoDTO.setApellidoMaterno(tfApellidoM.getText());
        academicoDTO.setCorreoElectronico(tfCorreo.getText());
        academicoDTO.setCedulaProfesional(tfCedula.getText());
        academicoDTO.setIdUniversidad(obtenerIdUniversidad());
        return academicoDTO;
    }

    private void cargarListaUniversidad () {
        ObservableList<String> nombresUniversidades = FXCollections.observableArrayList(cacheUniversidades.keySet());
        cmbUniversidad.setItems(nombresUniversidades);
        eliminarUniversidadEspecifica();
    }

    private void cargarCacheUniversidades (List<UniversidadDTO> listaUniversidadDTO) {
        for (UniversidadDTO universidadDTO : listaUniversidadDTO) {
            cacheUniversidades.put(universidadDTO.getNombre(), universidadDTO);
        }
    }

    private void eliminarUniversidadEspecifica () {
        cmbUniversidad.getItems()
                      .remove("UniversidadDTO Veracruzana");
    }
}
