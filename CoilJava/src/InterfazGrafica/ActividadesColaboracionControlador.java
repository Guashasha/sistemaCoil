package InterfazGrafica;

import DAO.ActividadDAO;
import DAO.CronogramaActividadAuxiliar;
import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import DTO.ColaboracionDTO;
import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public void initialize (Pane ventanaAnterior, BorderPane ventanaPrincipal, ColaboracionDTO colaboracion, CuentaDTO usuario) {
        this.ventanaAnterior = ventanaAnterior;
        this.ventanaPrincipal = ventanaPrincipal;
        this.colaboracion = colaboracion;
        this.usuario = usuario;

        actualizarLista();

        if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            btnNuevaActividad.setDisable(true);
        }
    }

    public void regresar () {
        this.ventanaPrincipal.setCenter(this.ventanaAnterior);
    }

    public void actualizarLista () {
        vboxActividades.getChildren().clear();
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

        if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            RetroalimentacionActividadAuxiliar retroalimentacionDao = new RetroalimentacionActividadAuxiliar();

            for (ActividadDTO actividad : actividades) {
                if (retroalimentacionDao.getPorPersonaYActividad(this.usuario.getIdPersona(), actividad.getIdActividad()).isEmpty()) {
                    Pane panel = crearPanelActividad(actividad);
                    vboxActividades.getChildren().add(panel);
                }
            }

            btnNuevaActividad.setDisable(true);
            btnNuevaActividad.setVisible(false);
        }
        else {
            for (ActividadDTO actividad : actividades) {
                Pane panel = crearPanelActividad(actividad);
                vboxActividades.getChildren().add(panel);
            }
        }
    }

    private Pane crearPanelActividad (ActividadDTO actividad) {
        HBox panelActividad = new HBox();
        panelActividad.setPadding(new Insets(10, 20, 10,20));
        panelActividad.setStyle("-fx-background-color: #B7DCF5; -fx-background-radius: 8;");

        if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.finalizada) {
            return null;
        }
        else if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            panelActividad.setSpacing(30.0);
            Button boton = crearBotonRetroalimentar(actividad);

            RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

            if (dao.getPorPersonaYActividad(usuario.getIdPersona(), actividad.getIdActividad()).isPresent()) {
                boton.setDisable(true);
            }

            panelActividad.getChildren().addAll(new Label(actividad.getTitulo()), boton);
        }
        else {
            panelActividad.setSpacing(50.0);

            Label lbDescripcion = new Label(actividad.getDescripcion());
            lbDescripcion.setWrapText(true);
            lbDescripcion.setMaxWidth(200);

            panelActividad.getChildren().addAll(new Label(actividad.getTitulo()), lbDescripcion);

            if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.activa) {
                panelActividad.getChildren().add(crearBotonMarcarConcluida(actividad));
            }

            panelActividad.getChildren().add(crearBotonBorrar(actividad));
        }

        return panelActividad;
    }

    private Button crearBotonBorrar (ActividadDTO actividad) {
        Button boton = new Button("Borrar");

        CronogramaActividadAuxiliar dao = new CronogramaActividadAuxiliar();
        Optional<ActividadVinculadaDTO> actividadVinculada = dao.getPorActividadYColaboracion(actividad.getIdActividad(), this.colaboracion.getIdColaboracion());

        if (actividadVinculada.isPresent() && actividadVinculada.get().getPeriodo() == null) {
            boton.setOnAction(e -> {
                try {
                    if (dao.desvincular(new ActividadVinculadaDTO(actividad, this.colaboracion)) < 1) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setHeaderText("Error");
                        alerta.setContentText("No se pudo eliminar la actividad, intente de nuevo");
                        alerta.showAndWait();
                    }
                } catch (ErrorDAO error) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setContentText(error.getMessage());
                    alerta.setHeaderText("Error");
                    alerta.showAndWait();
                }

                actualizarLista();
            });
        }
        else {
            boton.setDisable(true);
        }

        return boton;
    }

    private Button crearBotonMarcarConcluida (ActividadDTO actividad) {
        Button boton = new Button("Finalizar");

        CronogramaActividadAuxiliar dao = new CronogramaActividadAuxiliar();
        Optional<ActividadVinculadaDTO> actividadVinculada = dao.getPorActividadYColaboracion(actividad.getIdActividad(), this.colaboracion.getIdColaboracion());

        if (actividadVinculada.isPresent() && actividadVinculada.get().getPeriodo() == null) {
            boton.setOnAction(e -> {
                actividadVinculada.get().setPeriodo(LocalDate.now());

                try {
                    if (dao.modificar(actividadVinculada.orElse(null)) < 1) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setHeaderText("Error");
                        alerta.setContentText("No se pudo finalizar la actividad, intente de nuevo");
                        alerta.showAndWait();
                    }
                } catch (ErrorDAO error) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setContentText(error.getMessage());
                    alerta.setHeaderText("Error");
                    alerta.showAndWait();
                }

                actualizarLista();
            });
        }
        else {
            boton.setDisable(true);
        }

        return boton;
    }

    private Button crearBotonRetroalimentar (ActividadDTO actividad) {
        Button boton = new Button("Calificar");

        boton.setOnAction( e -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RetroalimentarActividad.fxml"));
            SplitPane apActividades;

            try {
                apActividades = fxmlLoader.load();
            }
            catch (IOException error) {
                System.out.println(error.getMessage());
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setContentText("No se pudo abrir la ventana de retroalimentacion de actividades");
                alerta.setHeaderText("Ocurrió un error");
                alerta.showAndWait();
                return;
            }

            if (apActividades != null) {
                RetroalimentarActividadControlador ventanaActividadesControlador = fxmlLoader.getController();
                ventanaActividadesControlador.initialize(this.ventanaPrincipal, this.pnMain, actividad, this.usuario, this);
                this.ventanaPrincipal.setCenter(apActividades);
            }
        } );

        return boton;
    }

    @FXML
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
            ventanaActividadesControlador.initialize(this.colaboracion, this.ventanaPrincipal, this.pnMain, this);
            ventanaActividadesControlador.colaboracionDTO = colaboracion;
            this.ventanaPrincipal.setCenter(apActividades);
        }
    }

    public void volver () {
        ventanaPrincipal.setCenter(ventanaAnterior);
    }
}
