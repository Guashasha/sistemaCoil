package InterfazGrafica;

import Logica.DAO.DAOAcademico;
import Logica.DAO.DAOCuenta;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Logica.Dominio.Universidad;
import Utilidades.ErrorDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class SolicitarCuentaControlador implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(SolicitarCuentaControlador.class);
    private Map<String, Universidad> cacheUniversidades = new HashMap<>();
    private StringProperty nombreProperty = new SimpleStringProperty();
    private StringProperty apellidoPaternoProperty = new SimpleStringProperty();
    private StringProperty apellidoMaternoProperty = new SimpleStringProperty();
    private StringProperty cedulaProperty = new SimpleStringProperty();
    private StringProperty correoElectronicoProperty = new SimpleStringProperty();
    private StringProperty areaProperty = new SimpleStringProperty();
    private StringProperty numeroPersonalProperty = new SimpleStringProperty();
    private StringProperty numeroTelefonoProperty = new SimpleStringProperty();
    private StringProperty usuarioProperty = new SimpleStringProperty();
    private StringProperty contrasenaProperty = new SimpleStringProperty();
    private StringProperty univeridadProperty = new SimpleStringProperty();
    private final DAOUniversidad DAO_UNIVERSIDAD = new DAOUniversidad();
    private final DAOAcademico DAO_ACADEMICO = new DAOAcademico();
    private final DAOCuenta DAO_CUENTA = new DAOCuenta();
    @FXML
    private TextField tfNumeroTelefono;
    @FXML
    private TextField tfNumeroPersonal;
    @FXML
    private TextField tfArea;
    @FXML
    private PasswordField pwfContrasena;
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

    //fixme quitar metodos de validación y pasarlos al DAO. No implementar tanta logica.
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) throws ErrorDAO {
        List<Universidad> listaUniversidad;
        try {
            listaUniversidad = DAO_UNIVERSIDAD.getTodasAlfabeticamente();
        }
        catch (ErrorDAO errorDAO){
            throw errorDAO;
        }
        if (listaUniversidad != null) {
            cargarCacheUniversidades(listaUniversidad);
            cargarListaUniversidad();
        }
        vincularPropiedades();
    }

    @FXML
    public void regresarAInicio () {
        boolean btmAceptadoSeleccionado = mostrarAlertaConfirmacion();
        if (btmAceptadoSeleccionado) {
            cargarVentanaInicioSesion ();
        }
    }

    public void cargarVentanaInicioSesion  () {
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
        }
    }

    @FXML
    public void registrarSolicitud () {
        procesarRegistro();
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

    private boolean sonCamposValidos () {
        return DAOAcademico.esCadenaValidaProperty(nombreProperty) &&
                DAOAcademico.esCadenaValidaProperty(apellidoPaternoProperty) &&
                DAOAcademico.esCadenaValidaProperty(apellidoMaternoProperty) &&
                DAOAcademico.esCadenaValidaProperty(cedulaProperty) &&
                DAOAcademico.esCadenaValidaProperty(correoElectronicoProperty) &&
                DAOAcademico.esCorreoValido(correoElectronicoProperty) &&
                DAOAcademico.esCadenaValidaProperty(areaProperty) &&
                DAOAcademico.esCadenaValidaProperty(numeroPersonalProperty) &&
                DAOAcademico.esCadenaValidaProperty(numeroTelefonoProperty) &&
                DAOAcademico.esLongitudNumeroTelefonoValida(numeroTelefonoProperty) &&
                DAOAcademico.esCadenaValidaProperty(usuarioProperty) &&
                DAOAcademico.esCadenaValidaProperty(contrasenaProperty) &&
                DAOAcademico.esCadenaValidaProperty(univeridadProperty);
    }

    private void vincularPropiedadTextField (StringProperty property, TextField textField) {
        textField.textProperty()
                 .bindBidirectional(property);
    }

    private void vincularPropiedades () {
        vincularPropiedadTextField(nombreProperty, tfNombre);
        vincularPropiedadTextField(apellidoPaternoProperty, tfApellidoPaterno);
        vincularPropiedadTextField(apellidoMaternoProperty, tfApellidoMaterno);
        vincularPropiedadTextField(cedulaProperty, tfCedula);
        vincularPropiedadTextField(correoElectronicoProperty, tfCorreoElectronico);
        vincularPropiedadTextField(areaProperty, tfArea);
        vincularPropiedadTextField(numeroPersonalProperty, tfNumeroPersonal);
        vincularPropiedadTextField(numeroTelefonoProperty, tfNumeroTelefono);
        vincularPropiedadTextField(usuarioProperty, tfUsuario);
        vincularPropiedadTextField(contrasenaProperty, pwfContrasena);
        cmbUniversidad.valueProperty()
                      .bindBidirectional(univeridadProperty);
    }

    private Academico crearAcademico () throws ErrorDAO {
        Academico academico = new Academico();
        academico.setNombre(nombreProperty.get());
        academico.setApellidoPaterno(apellidoPaternoProperty.get());
        academico.setApellidoMaterno(apellidoMaternoProperty.get());
        academico.setCedulaProfesional(cedulaProperty.get());
        academico.setNumeroPersonal(numeroPersonalProperty.get());
        academico.setNumeroTelefonico(numeroTelefonoProperty.get());
        academico.setAreaEstudios(areaProperty.get());
        academico.setIdUniversidad(obtenerIdUniversidad());
        academico.setCorreoElectronico(correoElectronicoProperty.get());
        return academico;
    }

    private int obtenerIdUniversidad() {
        String nombreUniversidad = univeridadProperty.get();
        int idUniversidad = -1;
        if (cacheUniversidades.containsKey(nombreUniversidad)) {
            Universidad universidad = cacheUniversidades.get(nombreUniversidad);
            idUniversidad = universidad.getId();
        }
        return idUniversidad;
    }


    private Cuenta crearCuenta () throws ErrorDAO {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario(usuarioProperty.get());
        cuenta.setContrasena(contrasenaProperty.get());
        cuenta.setTipo(Cuenta.TipoUsuario.academico);
        cuenta.setEstado(Cuenta.EstadoCuenta.pendiente);
        try {
            cuenta.setIdPersona(obtenerIdPersonaAcademico());
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.WARNING);
        }
        return cuenta;
    }

    private int obtenerIdPersonaAcademico () throws ErrorDAO {
        int idPersona = 0;
        Optional<Academico> academicoOptional = DAO_ACADEMICO.getAcademicoPorCedula(cedulaProperty.get());
        if (academicoOptional.isPresent()) {
            idPersona = academicoOptional.get()
                                         .getIdPersona();
        }
        return idPersona;
    }

    private int agregarAcademico () {
        Academico academico = crearAcademico();
        int filasAfectadas = -1;
        try {
            filasAfectadas = DAO_ACADEMICO.agregarAcademicoExterno(academico);

        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
        return filasAfectadas;
    }

    private int agregarCuenta () {
        Cuenta cuenta = crearCuenta();
        int filasAfectadas = -1;
        try {
            filasAfectadas = DAO_CUENTA.agregar(cuenta);
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
        return filasAfectadas;
    }

    private boolean existeUsuario () {
        String nombreUsuario = usuarioProperty.get();
        try {
            boolean usuarioExiste = DAO_CUENTA.getCuentaPorUsuario(nombreUsuario)
                                              .isPresent();
            if (usuarioExiste) {
                mostrarAlert("El nombre de usuario " + nombreUsuario + " ya se encuentra registrado", Alert.AlertType.WARNING);
            }
            return usuarioExiste;
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public void procesarRegistro () {
        if (sonCamposValidos()) {
            if (!existeUsuario()) {
                int filasAfectadasAcademico = agregarAcademico();
                int filasAfectadasCuenta = agregarCuenta();

                if (filasAfectadasAcademico > 0 && filasAfectadasCuenta > 0) {
                    mostrarAlert("La solicitud se ha procesado correctamente", Alert.AlertType.INFORMATION);
                    limpiarCamposYComboBox();
                }
                else {
                    mostrarAlert("Hubo un problema al procesar la solicitud. Por favor, inténtelo de nuevo.", Alert.AlertType.ERROR);
                }
            }
        }
        else {
            mostrarAlert("Al menos un campo está vacío o no fue seleccionado", Alert.AlertType.WARNING);
        }

    }

    private void cargarListaUniversidad() {
        ObservableList<String> nombresUniversidades = FXCollections.observableArrayList(cacheUniversidades.keySet());
        cmbUniversidad.setItems(nombresUniversidades);
        eliminarUniversidadEspecifica();
    }

    private void cargarCacheUniversidades(List<Universidad> listaUniversidad) {
        for (Universidad universidad : listaUniversidad) {
            cacheUniversidades.put(universidad.getNombre(), universidad);
        }
    }


    private void eliminarUniversidadEspecifica () {
        cmbUniversidad.getItems()
                      .remove("Universidad Veracruzana");
    }
}
