package InterfazGrafica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primatyStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primatyStage.setTitle("Hello world");
        primatyStage.setScene(new Scene(root,300,275));
        primatyStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
