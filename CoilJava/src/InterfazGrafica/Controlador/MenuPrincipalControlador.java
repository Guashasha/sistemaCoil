package InterfazGrafica.Controlador;

import Logica.DAO.DAOAcademico;
import Logica.DAO.DAOCuenta;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Logica.ErrorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuPrincipalControlador implements Initializable {
    @FXML
    private Label lbDatosUsuario;

    private String usuario;

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void regresarLogin (ActionEvent evento) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Plantilla/WindowInicioSesion.fxml"));
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

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        DAOCuenta daoCuenta = new DAOCuenta();
        DAOAcademico daoAcademico = new DAOAcademico();
        Cuenta cuenta = null;
        Academico academico = null;

        try {
            Optional<Cuenta> optionalCuenta = daoCuenta.getCuentaPorUsuario(usuario);
            cuenta = optionalCuenta.get();
            Optional<Academico> optionalAcademico = daoAcademico.getAcademicoPorIdPersona(cuenta.getIdPersona());
            academico = optionalAcademico.get();
            lbDatosUsuario.setText("Soy " + academico.getNombre() + "Mi cuenta es de tipo " + cuenta.getTipo().toString());
        }
        catch (ErrorDAO errorDAO) {
            //todo
        }

    }
}
