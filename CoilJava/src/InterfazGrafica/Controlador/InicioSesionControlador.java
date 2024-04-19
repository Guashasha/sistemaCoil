package InterfazGrafica.Controlador;

import Logica.DAO.DAOCuenta;
import Logica.ErrorDAO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class InicioSesionControlador implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContrasena;


    @FXML
    public void solicitarCuenta (ActionEvent evento) {
        mostrarVentanaWindowSolicitarCuenta(evento);

    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void ingresarCuenta (ActionEvent evento) {
        String usuario = tfUsuario.getText();
        String contrasena = tfContrasena.getText();

        DAOCuenta daoCuenta = new DAOCuenta();
        boolean sonCredencialesValidas = false;

        try {
            sonCredencialesValidas = daoCuenta.verificarCredenciales(usuario, contrasena);
        }
        catch (ErrorDAO errorDAO) {
            mostrarVentanaErrorDAO(errorDAO.getMessage());
        }

        if (sonCredencialesValidas) {
            mostrarVentanaWindowMenuPrincipalAcademico(evento);

        }
        else {
            mostrarVentanaError();
        }
    }

    private void mostrarVentanaWindowMenuPrincipalAcademico (ActionEvent evento) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Plantilla/WindowMenuPrincipalAcademico.fxml"));
            Parent root = fxmlLoader.load();
            Stage escenario = (Stage) ((Node) evento.getSource()).getScene()
                                                                 .getWindow();
            Scene escena = new Scene(root);
            escenario.setScene(escena);
            escenario.show();
        }
        catch (IOException error) {
            System.out.println("Mostrar");
        }
    }

    private void mostrarVentanaError () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Las credenciales no son validas");
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    private void mostrarVentanaErrorDAO (String errorDAO) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(errorDAO);
        alert.setHeaderText("Advertencia");
        alert.showAndWait();
    }

    private void mostrarVentanaWindowSolicitarCuenta (ActionEvent evento) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Plantilla/WindowSolicitarCuenta.fxml"));
            Parent root = fxmlLoader.load();
            Stage escenario = (Stage) ((Node) evento.getSource()).getScene()
                                                                 .getWindow();
            Scene escena = new Scene(root);
            escenario.setScene(escena);
            escenario.show();
        }
        catch (IOException error) {
            System.out.println("Mostrar");
        }
    }

}
