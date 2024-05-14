package InterfazGrafica;

import Logica.DAO.DAOAcademico;
import Logica.DAO.DAOCuenta;
import Logica.DAO.DAOEstudiante;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Logica.Dominio.Estudiante;
import Utilidades.ErrorDAO;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;



public class InicioSesionControlador extends Application implements Initializable {

    private static final Logger BITACORA = Logger.getLogger(InicioSesionControlador.class);
    private static final DAOCuenta DAO_CUENTA = new DAOCuenta();

    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContrasena;
    @FXML
    public BorderPane bdPane;
    @FXML
    private AnchorPane apPane;
    @FXML
    private Pane pPane;

    @FXML
    public void solicitarCuenta (ActionEvent evento) {
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
    public void ingresarCuenta (ActionEvent evento) {
        try {
            Cuenta cuenta = getCuentaRegistrada();
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
    private void mostrarVentanaFormularioCompletarDatos (Academico academico) {
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

    @FXML
    private void hacerTransicion () throws IOException, ErrorDAO {
        Parent root = FXMLLoader.load(getClass().getResource("SolicitudCuenta.fxml"));
        Scene scene = tfContrasena.getScene();
        root.translateXProperty()
            .set(scene.getWidth());
        bdPane.getChildren()
              .add(root);
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);
        timeline.getKeyFrames()
                .add(keyFrame);
        timeline.setOnFinished(event -> {
            bdPane.getChildren()
                  .remove(pPane);
            bdPane.getChildren()
                  .remove(apPane);
        });
        timeline.play();
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
        if (!DAO_CUENTA.verificarCredenciales(usuario, contrasena)) {
            throw new ErrorDAO("Cuentas no validas", ErrorDAO.Tipo.VALIDACION);
        }
    }
    private void esCuentaAceptada (Cuenta cuenta) {
        if (cuenta.getEstado() != Cuenta.EstadoCuenta.aceptada) {
            throw new ErrorDAO("El estado de la cuenta esta en " + cuenta.getEstado().toString() +"\n" +
                                       "En caso de dudas, mande mensaje un correo a vic@uv.mx", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void abrirVentanaPorTipoCuenta (Cuenta cuenta) {
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

    private void procesarInicioSesionAcademico (Cuenta cuenta) {
        Optional <Academico> optionalAcademico = recuperarAcademicoPorCuenta(cuenta);
        verificarOptional(optionalAcademico);
        if (existenDatosNulosAcademico(optionalAcademico.get())) {
            mostrarVentanaAlert("Para poder ingresar necesita completar sus datos.", Alert.AlertType.INFORMATION);
            mostrarVentanaFormularioCompletarDatos(optionalAcademico.get());
        }
        else {
            mostrarVentanaWindowMenuPrincipalAcademico();
        }
    }

    private Cuenta getCuentaPorTextField () {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario(tfUsuario.getText());
        cuenta.setContrasena(tfContrasena.getText());
        return cuenta;
    }

    private Cuenta getCuentaRegistrada () {
        Optional<Cuenta> cuentaOptional = DAO_CUENTA.getCuentaPorUsuario(getCuentaPorTextField().getNombreUsuario());
        if (cuentaOptional.isEmpty()) {
            throw new ErrorDAO("Error al obtener la cuenta del usuario", ErrorDAO.Tipo.INICIO_SESION);
        }
        return cuentaOptional.get();
    }
    private Optional<Academico> recuperarAcademicoPorCuenta (Cuenta cuenta) {
        DAOAcademico daoAcademico = new DAOAcademico();
        Optional<Academico> optionalAcademico = daoAcademico.getAcademicoPorIdPersona(cuenta.getIdPersona());
        return optionalAcademico;
    }
    private Optional<Estudiante> recupearEstudiantePorCuenta (Cuenta cuenta) {
        DAOEstudiante daoEstudiante = new DAOEstudiante();
        Optional<Estudiante> optionalEstudiante = daoEstudiante.getEstudiantePorIdPersona(cuenta.getIdPersona());
        return optionalEstudiante;
    }

    private void verificarOptional (Optional optional) {
        if (!optional.isPresent()) {
            throw new ErrorDAO("No se puede obtener la información del usuario", ErrorDAO.Tipo.CONSULTA);
        }

    }
    private boolean existenDatosNulosAcademico (Academico academico) {
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
