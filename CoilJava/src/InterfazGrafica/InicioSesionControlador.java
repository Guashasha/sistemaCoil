package InterfazGrafica;

import DAO.CuentaAuxiliar;
import DTO.CuentaDTO;
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
    private static final CuentaAuxiliar DAO_CUENTA = new CuentaAuxiliar();

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
            hacerTransicion();
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
        catch (IOException e) {
            mostrarVentanaAlert("Error a mostrar la ventana solicitar cuenta", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void ingresarCuenta (ActionEvent evento) {
        try {
            CuentaDTO cuentaDTO = getCuentaRegistrada();
            sonCredencialesValidas(cuentaDTO);
            esCuentaAceptada(cuentaDTO);
            abrirVentanaPorTipoCuenta(cuentaDTO);
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuPrincipalAcademico.fxml"));
            Parent root = fxmlLoader.load();
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


    @FXML
    private void mostrarVentanaWindowSolicitarCuenta () {
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
        }
    }


    private void sonCredencialesValidas (CuentaDTO cuentaDTO) {
        if (!DAO_CUENTA.verificarCredenciales(cuentaDTO.getNombreUsuario(), cuentaDTO.getContrasena())) {
            throw new ErrorDAO("Cuentas no validas", ErrorDAO.Tipo.VALIDACION);
        }
    }
    private void esCuentaAceptada (CuentaDTO cuentaDTO) {
        if (cuentaDTO.getEstado() != CuentaDTO.EstadoCuenta.aceptada) {
            throw new ErrorDAO("El estado de la cuentaDTO esta en " + cuentaDTO.getEstado().toString() +"\n" +
                                       "En caso de dudas, mande mensaje un correo a vic@uv.mx", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void abrirVentanaPorTipoCuenta (CuentaDTO cuentaDTO) {
        switch (cuentaDTO.getTipo()) {
            case academico:
                mostrarVentanaWindowMenuPrincipalAcademico();
            case estudiante:
                System.out.println("Implementar ventana estudiante");
            case administrador:
                System.out.println("Implementar ventana admin");
        }
    }

    private CuentaDTO getCuentaPorTextField () {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNombreUsuario(tfUsuario.getText());
        cuentaDTO.setContrasena(tfUsuario.getText());
        return cuentaDTO;
    }

    private CuentaDTO getCuentaRegistrada () {
        Optional<CuentaDTO> cuentaOptional = DAO_CUENTA.getCuentaPorUsuario(getCuentaPorTextField().getNombreUsuario());
        if (cuentaOptional.isEmpty()) {
            throw new ErrorDAO("Error al obtener la cuenta del usuario", ErrorDAO.Tipo.INICIO_SESION);
        }
        return cuentaOptional.get();
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
