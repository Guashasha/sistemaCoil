package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DAO.PaisDAO;
import DAO.UniversidadAuxiliar;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
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
import java.sql.SQLException;
import java.util.*;

public class SolicitudCuentaControlador implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(SolicitudCuentaControlador.class);
    private Map<String, UniversidadDTO> cacheUniversidades = new HashMap<>();
    private Map<String, PaisDTO> cachePaises = new TreeMap<>();
    private final UniversidadAuxiliar DAO_UNIVERSIDAD = new UniversidadAuxiliar();
    private final AcademicoAuxiliar DAO_ACADEMICO = new AcademicoAuxiliar();
    private final PaisDAO DAO_PAIS = new PaisDAO();
    @FXML
    private ComboBox<String> cmbUniversidad;
    @FXML
    private ComboBox<String> cmbPais;
    @FXML
    private TextField pfConfirmaContrasena;
    @FXML
    private TextField pfContrasena;
    @FXML
    private TextField tfApellidos;
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
        registrarEventFilters();
        try {
            cargarCachePaises();
            cargarCmbPais();
        }
        catch (SQLException e) {
            throw new ErrorDAO("No se pueden cargar los países", ErrorDAO.Tipo.CONSULTA);
        }

        cmbPais.setOnAction(event -> {
            String paisSeleccionado = cmbPais.getValue();
            if (paisSeleccionado != null) {
                cargarCmbUniversidad(paisSeleccionado);
            }
        });
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
                mostrarMensajeEmergente("No se pudo regresar al inicio de sesión", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void realizarSolicitud () {
        if (sonCamposValidos()) {
            try {
                registarCuenta(getDatosAcademico(), getDatosCuenta());
            }
            catch (IllegalArgumentException | ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
            }
            catch (IOException ioException) {
                mostrarMensajeEmergente(ioException.getMessage(), Alert.AlertType.ERROR);
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

    public void registarCuenta (AcademicoDTO academicoDTO, CuentaDTO cuentaDTO) throws IOException, ErrorDAO {
        int registrarAcamicoConCuenta = DAO_ACADEMICO.agregarAcademicoConCuenta(academicoDTO, cuentaDTO);
        if (registrarAcamicoConCuenta == 3) {
            mostrarMensajeEmergente("Su solicitud ha sido registrada." +
                                            "\nrevise el correo proporcionado en los próximos días", Alert.AlertType.INFORMATION);
            cargarVentanaInicioSesion();
        }
        else {
            mostrarMensajeEmergente("No se pudo realizar su solicitud, intentenlo más tarde", Alert.AlertType.WARNING);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText(null);
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

    private int getIdUniversidad () {
        String nombreUniversidad = cmbUniversidad.getValue();
        int idUniversidad = -1;
        if (cacheUniversidades.containsKey(nombreUniversidad)) {
            UniversidadDTO universidadDTO = cacheUniversidades.get(nombreUniversidad);
            idUniversidad = universidadDTO.getId();
        }
        if (idUniversidad <= 0) {
            throw new ErrorDAO("Error al obtener el identificador de la universidad", ErrorDAO.Tipo.CONSULTA);
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
        academicoDTO.setApellidos(tfApellidos.getText());
        academicoDTO.setCorreoElectronico(tfCorreo.getText());
        academicoDTO.setCedulaProfesional(tfCedula.getText());
        if (cmbPais.getValue() == null) {
            throw new IllegalArgumentException("Seleccione su país de origen");
        }
        if (cmbUniversidad.getValue() == null || cmbUniversidad.getValue()
                                                               .isEmpty()) {
            throw new IllegalArgumentException("Selecciona una universidad");
        }
        academicoDTO.setIdUniversidad(getIdUniversidad());
        return academicoDTO;
    }

    private void cargarCachePaises () throws SQLException {
        List<PaisDTO> listaPais = getListaPais();
        for (PaisDTO paisDTO : listaPais) {
            cachePaises.put(paisDTO.getNombre(), paisDTO);
        }
    }

    private void cargarCmbPais () {
        ObservableList<String> nombrePais = FXCollections.observableArrayList(cachePaises.keySet());
        cmbPais.setItems(nombrePais);
    }

    private void cargarCmbUniversidad (String pais) {
        cacheUniversidades.clear();
        cargarCacheUniversidades(pais);
        ObservableList<String> nombreUniversidad = FXCollections.observableArrayList(cacheUniversidades.keySet());
        cmbUniversidad.setItems(nombreUniversidad);
        eliminarUniversidadEspecifica();
    }

    private void cargarCacheUniversidades (String pais) {
        List<UniversidadDTO> listaUniversidad = getUniversidadesPorPais(pais);
        for (UniversidadDTO universidadDTO : listaUniversidad) {
            cacheUniversidades.put(universidadDTO.getNombre(), universidadDTO);
        }
    }

    private List<PaisDTO> getListaPais () throws SQLException {
        return DAO_PAIS.getPaisesAlfabeticamente();
    }

    private List<UniversidadDTO> getUniversidadesPorPais (String pais) {
        return DAO_UNIVERSIDAD.getUniversidadesPorPaisOrigen(pais);
    }

    @FXML
    private void restriccionTfNombre(KeyEvent evento) {
        String input = evento.getCharacter();
        if (tfNombre.getText().length() >= 100 || input.matches("\\d")) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionTfApellidos (KeyEvent evento) {
        String input = evento.getCharacter();
        if (tfApellidos.getText()
                       .length() >= 100 || input.matches("\\d")) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionTfCorreo (KeyEvent evento) {
        if (tfCorreo.getText()
                    .length() >= 320) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionTfCedula (KeyEvent evento) {
        String caracter = evento.getCharacter();
        if (!caracter.matches("\\d")) {
            evento.consume();
        }

        if (tfCedula.getText()
                    .length() >= 30) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionTfUsuario (KeyEvent evento) {
        if (tfUsuario.getText()
                     .length() >= 50) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionPfContrasena (KeyEvent evento) {
        if (pfContrasena.getText()
                        .length() >= 300) {
            evento.consume();
        }
    }

    @FXML
    private void restriccionPfConfirmaContrasena (KeyEvent evento) {
        if (pfConfirmaContrasena.getText()
                                .length() >= 300) {
            evento.consume();
        }
    }

    private void eliminarUniversidadEspecifica () {
        cmbUniversidad.getItems()
                      .remove("Universidad Veracruzana");
    }

    @FXML
    private void desplegarMensajeUniversidadNoEncontrada () {
        mostrarMensajeEmergente("""
                                        Si la universidad que busca no se encuentra registrada, mande un correo electrónico con el siguiente formato al correo vic@uv.mx:
                                            
                                        Asunto: Universidad faltante.
                                        Nombre de la universidad.
                                        País de origen
                                            
                                        Espere una respuesta del mismo correo.""", Alert.AlertType.INFORMATION);
    }

    private void registrarEventFilters () {
        tfNombre.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTfNombre);
        tfApellidos.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTfApellidos);
        tfCorreo.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTfCorreo);
        tfCedula.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTfCedula);
        tfUsuario.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionTfUsuario);
        pfContrasena.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionPfContrasena);
        pfConfirmaContrasena.addEventFilter(KeyEvent.KEY_TYPED, this::restriccionPfConfirmaContrasena);
    }

    private boolean sonCamposValidos () {
        if (tfNombre.getText() == null || tfNombre.getText()
                                                  .trim()
                                                  .isEmpty()) {
            mostrarMensajeEmergente("Ingrese su nombre", Alert.AlertType.WARNING);
            return false;
        }
        if (tfApellidos.getText() == null || tfApellidos.getText()
                                                        .trim()
                                                        .isEmpty()) {
            mostrarMensajeEmergente("Ingrese sus apellidos", Alert.AlertType.WARNING);
            return false;
        }
        if (tfCorreo.getText() == null || tfCorreo.getText()
                                                  .trim()
                                                  .isEmpty()) {
            mostrarMensajeEmergente("Ingrese su correo electrónico", Alert.AlertType.WARNING);
            return false;
        }
        if (tfUsuario.getText() == null || tfUsuario.getText()
                                                    .trim()
                                                    .isEmpty()) {
            mostrarMensajeEmergente("Ingrese un nombre de usuario", Alert.AlertType.WARNING);
            return false;
        }
        if (pfContrasena.getText() == null || pfContrasena.getText()
                                                          .trim()
                                                          .isEmpty()) {
            mostrarMensajeEmergente("Ingrese una contraseña", Alert.AlertType.WARNING);
            return false;
        }
        if (pfConfirmaContrasena.getText() == null || pfConfirmaContrasena.getText()
                                                                          .trim()
                                                                          .isEmpty()) {
            mostrarMensajeEmergente("Confirme su contraseña", Alert.AlertType.WARNING);
            return false;
        }
        if (!pfContrasena.getText()
                         .equals(pfConfirmaContrasena.getText())) {
            mostrarMensajeEmergente("Las contraseñas no coinciden", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}
