package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DAO.EstudianteAuxiliar;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import DAO.CuentaAuxiliar;
import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;



public class InicioSesionControlador extends Application implements Initializable {

    private static final Logger BITACORA = Logger.getLogger(InicioSesionControlador.class);
    private static final CuentaAuxiliar CUENTA_AUXILIAR = new CuentaAuxiliar();

    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContrasena;
    @FXML
    public BorderPane bdPane;

    @FXML
    public void solicitarCuenta () {
        try {
            mostrarVentanaSolicitudCuenta();
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void ingresarCuenta () {
        try {
            CuentaDTO cuenta = getCuentaRegistrada();
            sonCredencialesValidas(tfUsuario.getText(), tfContrasena.getText());
            esCuentaAceptada(cuenta);
            abrirVentanaPorTipoCuenta(cuenta);
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.WARNING);
        }
    }


    @FXML
    private void mostrarVentanaWindowMenuPrincipalAcademico () {
        try {
            Stage stagePrincipal = (Stage) tfUsuario.getScene()
                                                    .getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VentanaPrincipal.fxml"));
            Parent root = fxmlLoader.load();
            Scene nuevaEscena = new Scene(root);
            stagePrincipal.setScene(nuevaEscena);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());

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

    private void mostrarVentanaAlert (String mensaje, Alert.AlertType tipoAlert) {
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
                mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
            }
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al abrir la ventana de solicitud de cuenta", ErrorDAO.Tipo.VALIDACION);
        }
    }


    private void sonCredencialesValidas (String usuario, String contrasena) {
        if (!CUENTA_AUXILIAR.verificarCredenciales(usuario, contrasena)) {
            throw new ErrorDAO("Cuentas no validas", ErrorDAO.Tipo.VALIDACION);
        }
    }
    private void esCuentaAceptada (CuentaDTO cuenta) {
        if (cuenta.getEstado() != CuentaDTO.EstadoCuenta.aceptada) {
            throw new ErrorDAO("El estado de la cuenta esta en " + cuenta.getEstado().toString() +"\n" +
                                       "En caso de dudas, mande mensaje un correo a vic@uv.mx", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void abrirVentanaPorTipoCuenta (CuentaDTO cuenta) {
        switch (cuenta.getTipo()) {
            case academico:
                procesarInicioSesionAcademico(cuenta);
                break;
            case estudiante:
                System.out.println("Implementar ventana estudiante");
                break;
            case administrador:
                System.out.println("Implementar ventana admin");
                break;
        }
    }

    private void procesarInicioSesionAcademico (CuentaDTO cuenta) {
        Optional <AcademicoDTO> optionalAcademico = recuperarAcademicoPorCuenta(cuenta);
        verificarOptional(optionalAcademico);
        if (existenDatosNulosAcademico(optionalAcademico.get())) {
            mostrarVentanaAlert("Para poder ingresar necesita completar sus datos.", Alert.AlertType.INFORMATION);
            mostrarVentanaFormularioCompletarDatos(optionalAcademico.get());
        }
        else {
            mostrarVentanaWindowMenuPrincipalAcademico();
        }
    }

    private CuentaDTO getCuentaPorTextField () {
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setNombreUsuario(tfUsuario.getText());
        cuenta.setContrasena(tfContrasena.getText());
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
        Optional<AcademicoDTO> optionalAcademico = daoAcademico.getAcademicoPorIdPersona(cuenta.getIdPersona());
        return optionalAcademico;
    }
    private Optional<EstudianteDTO> recupearEstudiantePorCuenta (CuentaDTO cuenta) {
        EstudianteAuxiliar daoEstudiante = new EstudianteAuxiliar();
        Optional<EstudianteDTO> optionalEstudiante = daoEstudiante.getEstudiantePorIdPersona(cuenta.getIdPersona());
        return optionalEstudiante;
    }

    private void verificarOptional (Optional optional) {
        if (!optional.isPresent()) {
            throw new ErrorDAO("No se puede obtener la información del usuario", ErrorDAO.Tipo.CONSULTA);
        }

    }
    private boolean existenDatosNulosAcademico (AcademicoDTO academico) {
        return academico.getNumeroTelefonico() == null || academico.getAreaEstudios() == null
                || academico.getNumeroPersonal() == null;
    }
    @Override
    public void start (Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../InterfazGrafica/inicioSesion.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inicio sesión");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main (String[] args) {
        launch(args);
    }

}