package InterfazGrafica;

import DAO.ActividadDAO;
import DTO.ActividadDTO;
import DTO.ColaboracionDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Stack<Pane> historialPaneles;
    private Pane ventanaAnterior;

    @FXML
    private VBox vboxActividades;
    @FXML
    private Pane pnMain;

    public void initialize (Pane ventanaAnterior, BorderPane ventanaPrincipal, Stack<Pane> historialPaneles, ColaboracionDTO colaboracion) {
        this.ventanaAnterior = ventanaAnterior;
        this.ventanaPrincipal = ventanaPrincipal;
        this.historialPaneles = historialPaneles;
        this.colaboracion = colaboracion;

        actualizarLista();
    }

    public void regresar () {

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
        Button boton = new Button("Calificar");

        boton.setOnAction( e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RetroalimentarActividad.fxml"));
            AnchorPane apActividades = null;

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
                this.historialPaneles.push(this.pnMain);
                NuevaActividadControlador ventanaActividadesControlador = fxmlLoader.getController();
                ventanaActividadesControlador.initialize(this.colaboracion, this.pnMain, this.ventanaPrincipal, historialPaneles);
                this.ventanaPrincipal.setCenter(apActividades);
            }
        } );

        panelActividad.getChildren().addAll(new Label(actividad.getTitulo()), boton);

        return panelActividad;
    }
}
