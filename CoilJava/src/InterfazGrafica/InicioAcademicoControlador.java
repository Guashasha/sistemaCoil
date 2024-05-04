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
    private Optional<Colaboracion> colaboracion;

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
            BITACORA.fatal(error);
            return;
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
            return;
        }

        setOverviewColaboracion();
    }

    private void setOverviewColaboracion () {
        getInformacionColaboracion(this.usuario);

        if (this.colaboracion.isEmpty()) {
            // TODO
            // Si no hay colaboración activa mostrar otra cosa
            return;
        }

        HBox informacionColaboracion;
        TableView<Actividad> informacionActividades;

        try {
             informacionColaboracion = getInfoHBox();
             informacionActividades = getInformacionActividades();
        }
        catch (ErrorDAO error) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("ocurrió un error inesperado al leer datos");
            alerta.setContentText(error.getMessage());
            alerta.showAndWait();
            return;
        }

        pnInicio.getChildren().addAll(informacionColaboracion, informacionActividades);
    }

    private void getInformacionColaboracion (Academico usuario) throws ErrorDAO {
        DAOColaboracion daoColaboracion = new DAOColaboracion();

        colaboracion = daoColaboracion.getActivaPorAcademico(this.usuario.getIdPersona());
    }

    private HBox getInfoHBox () {
        HBox informacionColaboracion = new HBox();

        Label temaInteres = new Label(this.colaboracion.get().getTemaInteres());
        Label colaborador = new Label("Academico colaborador: " + this.colaboracion.get().getAcademicoPar().getNombre());
        Label fechaFin = new Label("Fecha de finalización: " + this.colaboracion.get().getPeriodo()
                .getFechaFin());

        informacionColaboracion.getChildren()
                .addAll(temaInteres, colaborador, fechaFin);

        return informacionColaboracion;
    }

    private TableView<Actividad> getInformacionActividades () throws ErrorDAO {
        DAOActividad daoActividad = new DAOActividad();
        List<Actividad> resultado;

        resultado = daoActividad.getPorIdColaboracion(this.colaboracion.get().getIdColaboracion());

        TableView<Actividad> actividades = new TableView<>(FXCollections.observableList(resultado));
        actividades.setEditable(false);

        return actividades;
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
