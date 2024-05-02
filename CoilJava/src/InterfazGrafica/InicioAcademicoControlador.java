package InterfazGrafica;

import Logica.DAO.DAOActividad;
import Logica.DAO.DAOColaboracion;
import Logica.Dominio.Academico;
import Logica.Dominio.Actividad;
import Logica.Dominio.Colaboracion;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class InicioAcademicoControlador extends Application {
    private static final Logger BITACORA = Logger.getLogger(InicioAcademicoControlador.class);

    @FXML
    VBox pnInicio = new VBox();

    private Academico usuario;

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage stage) {
        VBox root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("InicioAcademico.fxml"));
        }
        catch (IOException error) {
            BITACORA.error(error);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);

            Scene escena = new Scene(root, Color.TRANSPARENT);
            escena.getStylesheets()
                    .add("InterfazGrafica/Recursos/EstiloVentanas.css");

            stage.setScene(escena);
            stage.show();
        } else {
            BITACORA.error("Ocurrió un error al iniciar el panel inicio academico");
        }
    }

    private void setOverviewColaboracion () {
        Optional<Colaboracion> colaboracionActual = getInformacionColaboracion(usuario);

        if (colaboracionActual.isEmpty()) {
            // TODO
            // Si no hay colaboración activa mostrar otra cosa
            return;
        }

        List<Academico> participantes;

        try {
            participantes = daoColaboracion.getAcademicosParticipantes(colaboracionActual.get());
        }
        catch (ErrorDAO error) {
            Alert alerta = crearAlerta(error, "academicos");
            alerta.showAndWait();
            return;
        }

        // Si hay una colaboración activa debe forzosamente haber un par academico
        if (participantes.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText("Ocurrió un error inesperado");
            alerta.setContentText("No se pudo encontrar a su par academico, por favor comuniquese con soporte tecnico");
            alerta.showAndWait();
            return;
        }

        pnInicio.getChildren().add(getInformacionColaboracion(colaboracionActual.get(), participantes));
        TableView informacionActividades;

        try {
            informacionActividades = getInformacionActividades(colaboracionActual.get());
        }
        catch (ErrorDAO error) {
            Alert alerta = crearAlerta(error, "actividad");
            alerta.showAndWait();
            return;
        }

        pnInicio.getChildren().add(informacionActividades);
    }

    private Optional<HBox> getInformacionColaboracion (Academico usuario) {
        DAOColaboracion daoColaboracion = new DAOColaboracion();
        Optional<Colaboracion> colaboracion;

        try {
            colaboracion = daoColaboracion.getActivaPorAcademico(usuario.getIdPersona());
        }
        catch (ErrorDAO error) {
            Alert alerta = crearAlerta(error, "colaboración");
            alerta.showAndWait();
            return Optional.empty();
        }

        if (colaboracion == null) {
            return Optional.empty();
        }

        HBox informacionColaboracion = new HBox();

        Label temaInteres = new Label(colaboracion.getTemaInteres());
        Label colaborador = new Label("Academico colaborador: " + participantes.getFirst());
        Label fechaFin = new Label("Fecha de finalización: " + colaboracion.getPeriodo()
                                                                            .getFechaFin());

        informacionColaboracion.getChildren()
                                .addAll(temaInteres, colaborador, fechaFin);

        return Optional.of(informacionColaboracion);
    }

    private TableView getInformacionActividades (Colaboracion colaboracion) throws ErrorDAO {
        DAOActividad daoActividad = new DAOActividad();
        List<Actividad> resultado;

        resultado = daoActividad.getPorIdColaboracion(colaboracion.getIdColaboracion());

        return new TableView(FXCollections.observableList(resultado));
    }

    private Alert crearAlerta (ErrorDAO error, String objeto) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        switch (error.getTipo()) {
            case VALIDACION:
                errorAlert.setHeaderText("Error de datos");
                errorAlert.setContentText("Los datos de " + objeto + " son incorrectos, verifiquelos e intente de nuevo");
                break;

            case INSERCION:
                errorAlert.setHeaderText("Error al agregar");
                errorAlert.setContentText("Ocurrió un error al agregar " + objeto);
                break;

            case DUPLICIDAD:
                errorAlert.setHeaderText("La actividad ya existe");
                errorAlert.setContentText("Ya existe " + objeto + " con estos datos, intente de nuevo");
                break;

            case CONEXION:
                errorAlert.setHeaderText("Error de base de datos");
                errorAlert.setContentText("Ocurrió un error al conectarse a la base de datos, intente nuevamente más tarde");
                break;

            case CONSULTA:
                errorAlert.setHeaderText("Error al recuperar los datos");

                if (objeto.equals("colaboración")) {
                    errorAlert.setContentText("La colaboración buscada no existe en la base de datos");
                } else if (objeto.equals("actividad")) {
                    errorAlert.setContentText("Las actividades buscadas no existen en la base de datos");
                } else {
                    errorAlert.setContentText("Los academicos buscados no existen en la base de datos");
                }
        }

        return errorAlert;
    }
}
