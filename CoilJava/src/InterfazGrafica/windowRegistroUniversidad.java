package InterfazGrafica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class windowRegistroUniversidad extends Application {

    @Override
    public void start(Stage primatyStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Plantilla/RegistroUniversidad.fxml"));
        primatyStage.setTitle("Registro de institución universitaria");
        primatyStage.setScene(new Scene(root));
        primatyStage.show();
    }

    public static void main (String[] args) {
        launch();
    }
}
