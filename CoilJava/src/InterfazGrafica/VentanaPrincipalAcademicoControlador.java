package InterfazGrafica;

import Logica.Dominio.Academico;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Stack;

public class VentanaPrincipalAcademicoControlador extends Application {
    private final Logger BITACORA = Logger.getLogger(VentanaPrincipalAcademicoControlador.class);

    @FXML
    private BorderPane pnPrincipal;
    @FXML
    private VBox vBoxBotones;

    private Stack<Pane> historialPaneles = new Stack<>();
    private Academico usuario;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        try {
            pnPrincipal = FXMLLoader.load(getClass().getResource("VentanaPrincipal.fxml"));
        }
        catch (IOException error) {
            BITACORA.error(error);
        }

        if (pnPrincipal != null) {
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene escena = new Scene(pnPrincipal, Color.TRANSPARENT);
            escena.getStylesheets().add("InterfazGrafica/Recursos/EstiloVentanas.css");

            stage.setScene(escena);
            stage.show();
        } else {
            BITACORA.error("Ocurrió un error al iniciar la ventana principal");
            return;
        }

        vBoxBotones = (VBox) stage.getScene().lookup("#vBoxBotones");

        agregarBotones();
        abrirMenuPrincipal();
    }

    public void agregarBotones () {
        Button btnColaboraciones = new Button("Colaboraciones");
        btnColaboraciones.getStyleClass().add("button-menu-lateral");

        Button btnNumeralia = new Button("Numeralia");
        btnNumeralia.getStyleClass().add("button-menu-lateral");

        vBoxBotones.getChildren().addAll(btnColaboraciones, btnNumeralia);
    }

    public void cerrarVentana () {
        Stage window = (Stage) pnPrincipal.getScene().getWindow();
        window.close();
    }

    public void abrirMenuPrincipal () {
        InicioAcademicoControlador inicioAcademicoControlador = new InicioAcademicoControlador(this.usuario);
        Pane inicio =  inicioAcademicoControlador.getPane();

        pnPrincipal.setCenter(inicio);

        historialPaneles.add(inicio);
    }

    public void abrirConfiguracionCuenta () {
        // TODO
    }

    public void regresar () {
        if (historialPaneles.size() > 1) {
            Pane ventanaActual;

            historialPaneles.pop();
            ventanaActual = historialPaneles.peek();

            pnPrincipal.setCenter(ventanaActual);
        }
    }
}
