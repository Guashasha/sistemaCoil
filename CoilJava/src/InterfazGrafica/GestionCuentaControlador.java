package InterfazGrafica;

import DAO.CuentaAuxiliar;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
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
        List<CuentaDTO> cuentasPendientes = getCuentaEnEstadoPendiente();
        for (CuentaDTO cuentaDTO : cuentasPendientes) {
            agregarCuentaItem(cuentaDTO);
        }
    }

    private void agregarCuentaItem (CuentaDTO cuentaDTO) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("CuentaItem.fxml"));
        try {
            VBox vBox = fxmlLoader.load();
            CuentaItemControlador cuentaItemController = fxmlLoader.getController();
            cuentaItemController.setCuentaObtenida(cuentaDTO);
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
                                CuentaDTO cuentaDTOSeleccionada = cuentaItemController.getCuentaObtenida();
                                AcademicoDTO academicoDTO = cuentaItemController.getAcademico(cuentaDTOSeleccionada.getIdPersona());
                                int resultado = confirmarAccionCuenta();
                                if (resultado != -1) {
                                    cambiarEstadoCuenta(cuentaDTOSeleccionada, resultado);
                                    lyInformacionCuenta.getChildren()
                                                       .remove(vBox);
                                    ManejadorCorreo manejadorCorreo = ManejadorCorreo.getInstancia();
                                    manejadorCorreo.enviarCorreoHilo(academicoDTO.getCorreoElectronico(), "Tema", "Meo");
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


    public List<CuentaDTO> getCuentaEnEstadoPendiente () {
        List<CuentaDTO> listaCuentaDTO = null;
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar();
        try {
            listaCuentaDTO = cuentaAuxiliar.getCuentasPorEstado(CuentaDTO.EstadoCuenta.pendiente.toString());
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Aqui iria un alert");
        }
        return listaCuentaDTO;
    }

    public int cambiarEstadoCuenta (CuentaDTO cuentaDTO, int resultado) {
        CuentaAuxiliar cuentaAuxiliar = new CuentaAuxiliar();
        String estadoCuenta;
        if (resultado == 1) {
            estadoCuenta = CuentaDTO.EstadoCuenta.aceptada.toString();
        }
        else {
            estadoCuenta = CuentaDTO.EstadoCuenta.rechazada.toString();
        }
        try {
            resultado = cuentaAuxiliar.cambiarEstadoCuenta(cuentaDTO, estadoCuenta);
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
