package InterfazGrafica;

import Logica.DAO.DAOCuenta;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Utilidades.Correo;
import Utilidades.ErrorDAO;
import Utilidades.ManejadorCorreo;
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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
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
        List<Cuenta> cuentasPendientes = getCuentaEnEstadoPendiente();
        for (Cuenta cuenta : cuentasPendientes) {
            agregarCuentaItem(cuenta);
        }
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

    private void configurarBotonEvaluar (CuentaItemControlador cuentaItemController, VBox vBox) {
        cuentaItemController.getBtEvaluar()
                            .setOnAction(event -> {
                                Cuenta cuentaSeleccionada = cuentaItemController.getCuentaObtenida();
                                Academico academico = cuentaItemController.getAcademico(cuentaSeleccionada.getIdPersona());
                                int resultado = confirmarAccionCuenta();
                                if (resultado != -1) {
                                    cambiarEstadoCuenta(cuentaSeleccionada, resultado);
                                    lyInformacionCuenta.getChildren()
                                                       .remove(vBox);
                                    ManejadorCorreo manejadorCorreo = ManejadorCorreo.getInstancia();
                                    manejadorCorreo.enviarCorreoHilo(academico.getCorreoElectronico(), "Tema", "Meo");
                                }
                            });
    }

    private int confirmarAccionCuenta() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Qué desea hacer con la cuenta?");
        alert.setContentText("Por favor seleccione una opción:");

        ButtonType btnAceptar = new ButtonType("Aceptar");
        ButtonType btnRechazar = new ButtonType("Rechazar");
        alert.getButtonTypes().setAll(btnAceptar, btnRechazar);
        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> alert.hide());
        Optional<ButtonType> result = alert.showAndWait();

        int resultado = -1;
        if (result.isPresent()) {
            if (result.get() == btnAceptar) {
                resultado = 1;
            } else if (result.get() == btnRechazar) {
                resultado = 0;
            }
        }
        return resultado;
    }


    public List<Cuenta> getCuentaEnEstadoPendiente () {
        List<Cuenta> listaCuenta = null;
        DAOCuenta daoCuenta = new DAOCuenta();
        try {
            listaCuenta = daoCuenta.getCuentasPorEstado(Cuenta.EstadoCuenta.pendiente.toString());
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Aqui iria un alert");
        }
        return listaCuenta;
    }

    public int cambiarEstadoCuenta (Cuenta cuenta, int resultado) {
        DAOCuenta daoCuenta = new DAOCuenta();
        String estadoCuenta;
        if (resultado == 1) {
            estadoCuenta = Cuenta.EstadoCuenta.aceptada.toString();
        }
        else {
            estadoCuenta = Cuenta.EstadoCuenta.rechazada.toString();
        }
        try {
            resultado = daoCuenta.cambiarEstadoCuenta(cuenta, estadoCuenta);
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Mostra un alert");
        }
        return resultado;
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
