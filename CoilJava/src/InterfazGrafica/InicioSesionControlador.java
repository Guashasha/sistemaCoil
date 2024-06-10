package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DAO.EstudianteAuxiliar;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import DAO.CuentaAuxiliar;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.stage.Screen;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Optional;

public class InicioSesionControlador {
    private static final Logger BITACORA = Logger.getLogger(InicioSesionControlador.class);
    private final CuentaAuxiliar CUENTA_AUXILIAR = new CuentaAuxiliar();
    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfContrasena;
    @FXML
    public BorderPane pnVentanaActual;

    @FXML
    public void solicitarCuenta () {
        try {
            mostrarVentanaSolicitudCuenta();
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void ingresarCuenta () {
        try {
            sonCredencialesValidas(tfUsuario.getText(), pfContrasena.getText());
            CuentaDTO cuenta = getCuentaRegistrada();
            esCuentaAceptada(cuenta);
            abrirVentanaPorTipoCuenta(cuenta);
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaEmergente(errorDAO.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void mostrarVentanaWindowMenuPrincipalAcademico (AcademicoDTO academicoDTO) {
        try {
            Stage stagePrincipal = (Stage) tfUsuario.getScene()
                                                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaPrincipalAcademico.fxml"));
            Parent root = fxmlLoader.load();
            VentanaPrincipalAcademicoControlador ventanaPrincipalAcademicoControlador = fxmlLoader.getController();
            ventanaPrincipalAcademicoControlador.setAcademico(academicoDTO);
            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarVentanaEmergente("Error al cargar la ventana principal", Alert.AlertType.ERROR);
        }
    }

    private void mostrarVentanaPrincipalEstudiante () {
        try {
            Stage stagePrincipal = (Stage) tfUsuario.getScene()
                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaPrincipalEstudiante.fxml"));
            Parent root = fxmlLoader.load();
            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarVentanaEmergente("Error al cargar la ventana principal", Alert.AlertType.ERROR);
        }
    }

    private void mostrarVentanaFormularioCompletarDatos (AcademicoDTO academico) {
        try {
            Stage stagePrincipal = (Stage) tfUsuario.getScene()
                                                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FormularioCompletarDatos.fxml"));
            Parent root = fxmlLoader.load();
            FormularioCompletarDatosControlador formularioCompletarDatosControlador = fxmlLoader.getController();
            formularioCompletarDatosControlador.setAcademico(academico);
            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());

        }
    }

    private void mostrarVentanaEmergente (String mensaje, Alert.AlertType tipoAlert) {
        Alert alert = new Alert(tipoAlert);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarVentanaSolicitudCuenta () {
        try {
            Stage stagePrincipal = (Stage) tfUsuario.getScene()
                                                    .getWindow();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SolicitudCuenta.fxml"));
                Parent root = fxmlLoader.load();
                Scene nuevaEscena = new Scene(root);
                stagePrincipal.setScene(nuevaEscena);
            }
            catch (ErrorDAO errorDAO) {
                mostrarVentanaEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
            }
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al abrir la ventana de solicitud de cuenta", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void mostrarVentanaPrincipalAdministrador() {
        try {
            Stage ventanaActual = (Stage) tfUsuario.getScene().getWindow();
            Stage stagePrincipal = new Stage(StageStyle.TRANSPARENT);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaPrincipalAdministrador.fxml"));
            Parent root = fxmlLoader.load();
            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
            stagePrincipal.setResizable(false);
            Screen screen = Screen.getPrimary();
            double screenWidth = screen.getBounds().getWidth();
            double screenHeight = screen.getBounds().getHeight();
            double ventanaWidth = Math.min(root.prefWidth(-1), screenWidth);
            double ventanaHeight = Math.min(root.prefHeight(ventanaWidth), screenHeight);
            stagePrincipal.setWidth(ventanaWidth);
            stagePrincipal.setHeight(ventanaHeight);
            stagePrincipal.show();
            ventanaActual.close();
        } catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al abrir la ventana de solicitud de cuenta", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void sonCredencialesValidas (String usuario, String contrasena) {
        if (!CUENTA_AUXILIAR.verificarCredenciales(usuario, contrasena)) {
            throw new ErrorDAO("El nombre de usuario o contraseña es incorrecto", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void esCuentaAceptada (CuentaDTO cuenta) {
        if (cuenta.getEstado() != CuentaDTO.EstadoCuenta.aceptada) {
            throw new ErrorDAO("El estado de la cuenta esta en " + cuenta.getEstado()
                                                                         .toString() + "\n" +
                                       "En caso de dudas, mande mensaje un correo a vic@uv.mx", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void abrirVentanaPorTipoCuenta (CuentaDTO cuenta) {
        switch (cuenta.getTipo()) {
            case academico:
                procesarInicioSesionAcademico(cuenta);
                break;
            case estudiante:
                mostrarVentanaPrincipalEstudiante();
                break;
            case administrador:
                mostrarVentanaPrincipalAdministrador();
                break;
        }
    }

    private void procesarInicioSesionAcademico (CuentaDTO cuenta) {
        Optional<AcademicoDTO> optionalAcademico = recuperarAcademicoPorCuenta(cuenta);
        verificarOptional(optionalAcademico);
        if (existenDatosNulosAcademico(optionalAcademico.get())) {
            mostrarVentanaEmergente("Para poder ingresar necesita completar sus datos.", Alert.AlertType.INFORMATION);
            mostrarVentanaFormularioCompletarDatos(optionalAcademico.get());
        }
        else {
            mostrarVentanaWindowMenuPrincipalAcademico(optionalAcademico.get());
        }
    }

    private CuentaDTO getCuentaPorTextField () {
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setNombreUsuario(tfUsuario.getText());
        cuenta.setContrasena(pfContrasena.getText());
        return cuenta;
    }

    private CuentaDTO getCuentaRegistrada () {
        Optional<CuentaDTO> cuentaOptional = CUENTA_AUXILIAR.getCuentaPorUsuario(getCuentaPorTextField().getNombreUsuario());
        if (cuentaOptional.isEmpty()) {
            throw new ErrorDAO("Error al obtener la cuenta del usuario", ErrorDAO.Tipo.INICIO_SESION);
        }
        return cuentaOptional.get();
    }

    private Optional<AcademicoDTO> recuperarAcademicoPorCuenta (CuentaDTO cuenta) {
        AcademicoAuxiliar daoAcademico = new AcademicoAuxiliar();
        return daoAcademico.getPorId(cuenta.getIdPersona());
    }

    private Optional<EstudianteDTO> recupearEstudiantePorCuenta (CuentaDTO cuenta) {
        EstudianteAuxiliar daoEstudiante = new EstudianteAuxiliar();
        Optional<EstudianteDTO> optionalEstudiante = daoEstudiante.getEstudiantePorIdPersona(cuenta.getIdPersona());
        return optionalEstudiante;
    }

    private void verificarOptional (Optional optional) {
        if (optional.isEmpty()) {
            throw new ErrorDAO("No se puede obtener la información del usuario", ErrorDAO.Tipo.CONSULTA);
        }

    }

    private boolean existenDatosNulosAcademico (AcademicoDTO academico) {
        return academico.getNumeroTelefonico() == null || academico.getAreaEstudios() == null
                || academico.getNumeroPersonal() == null;
    }

}