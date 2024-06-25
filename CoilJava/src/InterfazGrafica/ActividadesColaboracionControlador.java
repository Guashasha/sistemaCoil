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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActividadesColaboracionControlador {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);
    private CuentaDTO usuario;
    private ColaboracionDTO colaboracion;
    private BorderPane pnVentanaPrincipal;
    private Pane pnVentanaAnterior;

    @FXML
    private VBox vboxActividades;
    @FXML
    private Pane pnMain;
    @FXML
    private Button btnNuevaActividad;

    public void initialize (Pane pnVentanaAnterior, BorderPane pnVentanaPrincipal, ColaboracionDTO colaboracion, CuentaDTO usuario) {
        this.pnVentanaAnterior = pnVentanaAnterior;
        this.pnVentanaPrincipal = pnVentanaPrincipal;
        this.colaboracion = colaboracion;
        this.usuario = usuario;

        actualizarLista();

        if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            btnNuevaActividad.setDisable(true);
        }
    }

    public void regresar () {
        this.pnVentanaPrincipal.setCenter(this.pnVentanaAnterior);
    }

    public void actualizarLista () {
        vboxActividades.getChildren().clear();
        ActividadDAO actividadDAO = new ActividadDAO();
        List<ActividadDTO> actividades;

        try {
             actividades = actividadDAO.getPorIdColaboracion(this.colaboracion.getIdColaboracion());
        } catch (ErrorDAO e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Ocurrió un error");
            alerta.setContentText("No se pudo conseguir las actividades");
            alerta.showAndWait();
            return;
        }

        if (this.colaboracion.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            btnNuevaActividad.setDisable(true);
            btnNuevaActividad.setVisible(false);
        }

        for (ActividadDTO actividad : actividades) {
            Pane panel = crearPanelActividad(actividad);
            vboxActividades.getChildren().add(panel);
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
            Button btnRetroalimentar = crearBotonRetroalimentar(actividad);

            RetroalimentacionActividadAuxiliar actividadAUX = new RetroalimentacionActividadAuxiliar();

            if (actividadAUX.getPorPersonaYActividad(usuario.getIdPersona(), actividad.getIdActividad()).isPresent()) {
                btnRetroalimentar.setDisable(true);
            }

            panelActividad.getChildren().addAll(new Label(actividad.getTitulo()), btnRetroalimentar);
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
        Button btnBorrar = new Button("Borrar");

        CronogramaActividadAuxiliar actividadAUX = new CronogramaActividadAuxiliar();
        Optional<ActividadVinculadaDTO> actividadVinculada = actividadAUX.getPorActividadYColaboracion(actividad.getIdActividad(), this.colaboracion.getIdColaboracion());

        if (actividadVinculada.isPresent() && actividadVinculada.get().getPeriodo() == null) {
            btnBorrar.setOnAction(e -> {
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setHeaderText("Borrar actividad");
                confirmacion.setContentText("¿Está seguro que deséa borrar la actividad?");
                ButtonType btnAceptar = new ButtonType("Aceptar");
                ButtonType btnCancelar = new ButtonType("Cancelar");
                confirmacion.getButtonTypes()
                        .setAll(btnAceptar, btnCancelar);
                confirmacion.showAndWait();

                if (confirmacion.getResult() == btnAceptar) {
                    try {
                        if (actividadAUX.desvincular(new ActividadVinculadaDTO(actividad, this.colaboracion)) < 1) {
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setHeaderText("Error");
                            alerta.setContentText("No se pudo eliminar la actividad, intente de nuevo");
                            alerta.showAndWait();
                        }
                    } catch (ErrorDAO error) {
                        BITACORA.error(error);
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setContentText(error.getMessage());
                        alerta.setHeaderText("Error");
                        alerta.showAndWait();
                    }

                    actualizarLista();
                }
            });
        }
        else {
            btnBorrar.setDisable(true);
        }

        return btnBorrar;
    }

    private Button crearBotonMarcarConcluida (ActividadDTO actividad) {
        Button btnBorrar = new Button("Finalizar");

        CronogramaActividadAuxiliar actividadAUX = new CronogramaActividadAuxiliar();
        Optional<ActividadVinculadaDTO> actividadVinculada = actividadAUX.getPorActividadYColaboracion(actividad.getIdActividad(), this.colaboracion.getIdColaboracion());

        if (actividadVinculada.isPresent() && actividadVinculada.get().getPeriodo() == null) {
            btnBorrar.setOnAction(e -> {
                actividadVinculada.get().setPeriodo(LocalDate.now());

                try {
                    if (actividadAUX.modificar(actividadVinculada.orElse(null)) < 1) {
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                        alerta.setHeaderText("Error");
                        alerta.setContentText("No se pudo finalizar la actividad, intente de nuevo");
                        alerta.showAndWait();
                    }
                } catch (ErrorDAO error) {
                    BITACORA.error(error);
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setContentText(error.getMessage());
                    alerta.setHeaderText("Error");
                    alerta.showAndWait();
                }

                actualizarLista();
            });
        }
        else {
            btnBorrar.setDisable(true);
        }

        return btnBorrar;
    }

    private Button crearBotonRetroalimentar (ActividadDTO actividad) {
        Button btnBorrar = new Button("Calificar");

        btnBorrar.setOnAction( e -> {
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
                ventanaActividadesControlador.initialize(this.pnVentanaPrincipal, this.pnMain, actividad, this.usuario, this);
                this.pnVentanaPrincipal.setCenter(apActividades);
            }
        } );

        return btnBorrar;
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
            ventanaActividadesControlador.initialize(this.colaboracion, this.pnVentanaPrincipal, this.pnMain, this);
            ventanaActividadesControlador.colaboracionDTO = colaboracion;
            this.pnVentanaPrincipal.setCenter(apActividades);
        }
    }

    public void volver () {
        pnVentanaPrincipal.setCenter(pnVentanaAnterior);
    }
}
