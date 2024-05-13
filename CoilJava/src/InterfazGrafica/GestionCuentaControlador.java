package InterfazGrafica;

import Logica.DAO.DAOCuenta;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Utilidades.ComprobadorInternet;
import Utilidades.ErrorDAO;
import Utilidades.ManejadorCorreo;
import Utilidades.PlantillasCorreo;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestionCuentaControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(GestionCuentaControlador.class);
    @FXML
    private VBox lyInformacionCuenta;
    @FXML
    private BorderPane root;

    public void setRoot (BorderPane root) {
        this.root = root;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        cargarItemCuentas();
    }

    private void agregarCuentaItem (Cuenta cuenta) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CuentaItem.fxml"));
        try {
            VBox vBox = fxmlLoader.load();
            CuentaItemControlador cuentaItemController = fxmlLoader.getController();
            cuentaItemController.setCuentaObtenida(cuenta);
            cuentaItemController.setLabel();

            lyInformacionCuenta.getChildren()
                               .add(vBox);
            configurarBotonEvaluar(cuentaItemController, vBox);
        }
        catch (IOException ioException) {
            BITACORA.fatal(ioException.getMessage());
        }
    }

    private void cargarItemCuentas () {
        ArrayList<Cuenta> arrayListCuentasPendientes;
        try {
            arrayListCuentasPendientes = (ArrayList<Cuenta>) getCuentaEnEstadoPendiente();
            if (!arrayListCuentasPendientes.isEmpty()) {
                for (Cuenta cuenta : arrayListCuentasPendientes) {
                    agregarCuentaItem(cuenta);
                }
            }
            else {
                mostrarAlert("No hay cuentas por revisar", Alert.AlertType.INFORMATION);
            }
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void configurarBotonEvaluar(CuentaItemControlador cuentaItemController, VBox vBox) {
        cuentaItemController.getBtEvaluar().setOnAction(event -> {
            try {
                evaluarCuenta(cuentaItemController, vBox);
            }
            catch (ErrorDAO errorDAO) {
                mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }
    private void evaluarCuenta(CuentaItemControlador cuentaItemController, VBox vBox) throws ErrorDAO {
        Cuenta cuentaSeleccionada = cuentaItemController.getCuentaObtenida();
        Academico academico = cuentaItemController.getAcademico(cuentaSeleccionada.getIdPersona());
        int resultado = confirmarAccionCuenta();
        if (resultado != -1) {
            try {
                cambiarEstadoCuenta(cuentaSeleccionada, resultado, academico);
            }
            catch (ErrorDAO errorDAO) {
                if (errorDAO.getTipo() == ErrorDAO.Tipo.ERROR_CONEXION_INTERNET) {
                    lyInformacionCuenta.getChildren().remove(vBox);
                }
                throw errorDAO;
            }
        }
    }

    private int confirmarAccionCuenta () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Qué desea hacer con la cuenta?");
        alert.setContentText("Por favor seleccione una opción:");

        ButtonType btnAceptar = new ButtonType("Aceptar");
        ButtonType btnRechazar = new ButtonType("Rechazar");
        alert.getButtonTypes()
             .setAll(btnAceptar, btnRechazar);
        Window window = alert.getDialogPane()
                             .getScene()
                             .getWindow();
        window.setOnCloseRequest(e -> alert.hide());
        Optional<ButtonType> result = alert.showAndWait();

        int resultado = -1;
        if (result.isPresent()) {
            if (result.get() == btnAceptar) {
                resultado = 1;
            }
            else if (result.get() == btnRechazar) {
                resultado = 0;
            }
        }
        return resultado;
    }

    public List<Cuenta> getCuentaEnEstadoPendiente () {
        List<Cuenta> listaCuenta = null;
        DAOCuenta daoCuenta = new DAOCuenta();
            listaCuenta = daoCuenta.getCuentasPorEstado(Cuenta.EstadoCuenta.pendiente.toString());
        return listaCuenta;
    }

    private int cambiarEstadoCuenta (Cuenta cuenta, int resultado, Academico academico) throws ErrorDAO{
        DAOCuenta daoCuenta = new DAOCuenta();
        String estadoCuenta;
        int filasAfectadas = -1;
        if (resultado == 1) {
            estadoCuenta = Cuenta.EstadoCuenta.aceptada.toString();
        }
        else {
            estadoCuenta = Cuenta.EstadoCuenta.rechazada.toString();
        }
            filasAfectadas = daoCuenta.cambiarEstadoCuenta(cuenta, estadoCuenta);
            procesarEnvioCorreo(academico, estadoCuenta);
        return filasAfectadas;
    }

    private void procesarEnvioCorreo (Academico academico, String estadoCuenta) throws ErrorDAO {
        String destinatario = academico.getCorreoElectronico();
        String tema = "Respuesta a solicitud de cuenta";
        String contenido;
        if (estadoCuenta.equals(Cuenta.EstadoCuenta.aceptada.toString())) {
            contenido = getMensajeCuentaAceptada(academico);
        }
        else {
            contenido = getMensajeCuentaRechazada(academico);
        }
        try {
            ComprobadorInternet.comprobarConexion();
            ManejadorCorreo.getInstancia()
                           .enviarCorreoHilo(destinatario, tema, contenido);
        }
        catch (IOException ioException) {
            BITACORA.error(ioException.getMessage());
            throw new ErrorDAO("No fue posible conectarse a internet.", ErrorDAO.Tipo.ERROR_CONEXION_INTERNET);
        }
    }

    private String getMensajeCuentaAceptada (Academico academico) {
        String nombreCompleto = getNombreAcademicoCompleto(academico);
        return PlantillasCorreo.cuentaAceptada(nombreCompleto);
    }

    private String getMensajeCuentaRechazada (Academico academico) {
        String nombreCompleto = getNombreAcademicoCompleto(academico);
        return PlantillasCorreo.cuentaRechazada(nombreCompleto);
    }

    private String getNombreAcademicoCompleto (Academico academico) {
        return academico.getNombre() + " " + academico.getApellidoPaterno() + " " + academico.getApellidoMaterno();
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../InterfazGrafica/GestionCuenta.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Gestión de Cuentas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }

}
