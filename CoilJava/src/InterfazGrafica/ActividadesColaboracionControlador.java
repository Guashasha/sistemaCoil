package InterfazGrafica;

import DAO.ActividadDAO;
import DTO.ActividadDTO;
import DTO.ColaboracionDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class ActividadesColaboracionControlador {
    private CuentaDTO usuario;
    private ColaboracionDTO colaboracion;
    private BorderPane ventanaPrincipal;
    private Pane ventanaAnterior;

    @FXML
    private VBox vboxActividades;
    @FXML
    private Pane pnMain;
    @FXML
    private Button btnNuevaActividad;

    public void initialize (Pane ventanaAnterior, BorderPane ventanaPrincipal, ColaboracionDTO colaboracion) {
        this.ventanaAnterior = ventanaAnterior;
        this.ventanaPrincipal = ventanaPrincipal;
        this.colaboracion = colaboracion;

        actualizarLista();
    }

    public void regresar () {
        this.ventanaPrincipal.setCenter(this.ventanaAnterior);
    }

    private void actualizarLista () {
        ActividadDAO dao = new ActividadDAO();
        List<ActividadDTO> actividades;

        try {
             actividades = dao.getPorIdColaboracion(this.colaboracion.getIdColaboracion());
        } catch (ErrorDAO e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Ocurrió un error");
            alerta.setContentText("No se pudo conseguir las actividades");
            alerta.showAndWait();
            return;
        }

        for (ActividadDTO actividad : actividades) {
            Pane panel = crearPanelActividad(actividad);
            vboxActividades.getChildren().add(panel);
        }
    }

    private Pane crearPanelActividad (ActividadDTO actividad) {
        HBox panelActividad = new HBox();
        panelActividad.setPadding(new Insets(10, 20, 10,20));

        if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.finalizada) {
            return null;
        }
        else if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            btnNuevaActividad.setDisable(false);
            btnNuevaActividad.setVisible(true);
            panelActividad.setSpacing(50.0);

            HBox pnFechas = new HBox(10);

            panelActividad.getChildren().addAll(new Label(actividad.getTitulo()), pnFechas);
        }
        else {
            panelActividad.setSpacing(30.0);
            Button boton = crearBotonRetroalimentar(actividad);

            panelActividad.getChildren().addAll(new Label(actividad.getTitulo()), boton);
        }

        return panelActividad;
    }

    private Button crearBotonRetroalimentar (ActividadDTO actividad) {
        Button boton = new Button("Calificar");

        boton.setOnAction( e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RetroalimentarActividad.fxml"));
            Pane apActividades;

            try {
                apActividades = fxmlLoader.load();
            }
            catch (IOException error) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("No se pudo abrir la ventana de retroalimentacion de actividades");
                alerta.setHeaderText("Ocurrió un error");
                alerta.showAndWait();
                return;
            }

            if (apActividades != null) {
                RetroalimentarActividadControlador ventanaActividadesControlador = fxmlLoader.getController();
                ventanaActividadesControlador.initialize(this.ventanaPrincipal, this.pnMain, actividad);
                this.ventanaPrincipal.setCenter(apActividades);
            }
        } );

        return boton;
    }

    private void abrirNuevaActividad () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NuevaActividad.fxml"));
        Pane apActividades;

        try {
            apActividades = fxmlLoader.load();
        }
        catch (IOException error) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("No se pudo abrir la ventana de retroalimentacion de actividades");
            alerta.setHeaderText("Ocurrió un error");
            alerta.showAndWait();
            return;
        }

        if (apActividades != null) {
            NuevaActividadControlador ventanaActividadesControlador = fxmlLoader.getController();
            ventanaActividadesControlador.initialize(this.colaboracion, this.ventanaPrincipal, this.pnMain);
            this.ventanaPrincipal.setCenter(apActividades);
        }
    }
}
