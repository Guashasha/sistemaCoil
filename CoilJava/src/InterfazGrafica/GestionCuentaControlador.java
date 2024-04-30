package InterfazGrafica;

import Logica.DAO.DAOCuenta;
import Logica.Dominio.Cuenta;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GestionCuentaControlador implements Initializable {

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
        for (int i = 0; i < cuentasPendientes.size(); i++) {
            final Cuenta cuenta = cuentasPendientes.get(i);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../Plantilla/CuentaItem.fxml"));
            try {
                VBox vBox = fxmlLoader.load();
                CuentaItemControlador cuentaItemController = fxmlLoader.getController();
                cuentaItemController.setCuentaObtenida(cuenta);
                cuentaItemController.setLabel(cuenta);
                cuentaItemController.getBtEvaluar()
                                    .setOnAction(event -> {
                                        Cuenta cuentaSeleccionada = cuentaItemController.getCuentaObtenida();
                                        cambiarEstadoCuenta(cuentaSeleccionada);
                                        lyInformacionCuenta.getChildren().remove(vBox);
                                    });

                lyInformacionCuenta.getChildren()
                                   .add(vBox);
            }
            catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }
    }

    private boolean mostrarAlertaConfirmacion () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Que desea hacer con la cuenta");
        alert.setContentText("mensaje");

        ButtonType btmAceptar = new ButtonType("Aceptar");
        ButtonType btmRechazar = new ButtonType("Rechazar");
        alert.getButtonTypes()
             .setAll(btmAceptar, btmRechazar);

        alert.showAndWait();

        return alert.getResult() == btmAceptar;
    }
    private boolean seAceptaLaCuenta () {
        return mostrarAlertaConfirmacion();
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

    public int cambiarEstadoCuenta (Cuenta cuenta) {
        DAOCuenta daoCuenta = new DAOCuenta();
        int resultado = -1;
        String estadoCuenta;
        if (seAceptaLaCuenta()) {
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


}
