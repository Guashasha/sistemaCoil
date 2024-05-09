package InterfazGrafica;

import Logica.DAO.DAOCuenta;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Cuenta;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class InicioSesionControlador extends Application implements Initializable {

    private static Logger bitacora = Logger.getLogger(InicioSesionControlador.class);
    private String nombreUsuario;
    private String contrasena;
    private Cuenta cuenta;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContrasena;
    private Stage stageInicio;



    @FXML
    public void solicitarCuenta (ActionEvent evento) {
        mostrarVentanaWindowSolicitarCuenta();

    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void ingresarCuenta (ActionEvent evento) {
        obtenerContenidoTextField();
        if (!estanVaciosTextField() && sonCredencialesValidas()) {
            getCuenta();
            if (esCuentaAceptada()) {
                abrirVentanaPorTipoCuenta(evento);
            }
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
            bitacora.fatal(error.getMessage());
        }
    }

    private void mostrarVentanaAlert (String mensaje, Alert.AlertType tipoAlert) {
        Alert alert = new Alert(tipoAlert);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    @FXML
    private void mostrarVentanaWindowSolicitarCuenta () {
        try {
            Stage stagePrincipal = (Stage) tfUsuario.getScene()
                                                    .getWindow();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SolicitarCuenta.fxml"));
                Parent root = fxmlLoader.load();
                Scene nuevaEscena = new Scene(root);
                stagePrincipal.setScene(nuevaEscena);
            }
            catch (ErrorDAO errorDAO) {
                mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
            }
        }
        catch (IOException error) {
            bitacora.fatal(error.getMessage());
        }
    }


    private boolean sonCredencialesValidas () {
        boolean coincidenEnBD = false;
        DAOCuenta daoCuenta = new DAOCuenta();
        try {
            coincidenEnBD = daoCuenta.verificarCredenciales(this.nombreUsuario, this.contrasena);
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
        if (!coincidenEnBD) {
            mostrarVentanaAlert("Contraseña o usuario incorrecto", Alert.AlertType.WARNING);
        }
        return coincidenEnBD;
    }

    private boolean estanVaciosTextField () {
        boolean estanVacios = nombreUsuario.isEmpty() && contrasena.isEmpty();
        if (estanVacios) {
            mostrarVentanaAlert("No se ingresó una contraseña o un usuario", Alert.AlertType.WARNING);
        }
        return estanVacios;
    }

    private void obtenerContenidoTextField () {
        this.nombreUsuario = tfUsuario.getText()
                                      .trim();
        this.contrasena = tfContrasena.getText()
                                      .trim();
    }

    private void getCuenta () {
        DAOCuenta daoCuenta = new DAOCuenta();
        try {
            Optional<Cuenta> cuentaOptional;
            cuentaOptional = daoCuenta.getCuentaPorUsuario(this.nombreUsuario);
            if (cuentaOptional.isPresent()) {
                this.cuenta = cuentaOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean esCuentaAceptada () {
        boolean esAceptada = true;
        if (cuenta.getEstado() != Cuenta.EstadoCuenta.aceptada) {
            mostrarVentanaAlert("La cuenta " + cuenta.getNombreUsuario() + " Se encuentra en estado " +
                                        cuenta.getEstado()
                                              .toString(), Alert.AlertType.WARNING);
            esAceptada = false;
        }
        return esAceptada;
    }

    private void abrirVentanaPorTipoCuenta (ActionEvent evento) {
        switch (this.cuenta.getTipo()) {
            case academico:
                mostrarVentanaWindowMenuPrincipalAcademico();
            case estudiante:
                System.out.println("Implementar ventana estudiante");
            case administrador:
                System.out.println("Implementar ventana admin");
        }
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
