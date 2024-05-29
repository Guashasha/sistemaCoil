//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import InterfazGrafica.VentanaPrincipalAdministradorControlador;
import Utilidades.Correo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Main extends Application {
    private final Logger BITACORA = Logger.getLogger(Main.class);

    @Override
    public void start (Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfazGrafica/InicioSesion.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Inicio sesión");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException error) {
            error.printStackTrace();
            BITACORA.fatal(error.getMessage());
        }
    }

    public static void main (String[] args) {
        launch(args);
    }



}