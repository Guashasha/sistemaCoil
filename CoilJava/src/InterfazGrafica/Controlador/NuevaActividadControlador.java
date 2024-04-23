package InterfazGrafica.Controlador;

import Logica.DAO.DAOActividad;
import Logica.DAO.DAOCronogramaActividades;
import Logica.Dominio.Actividad;
import Logica.Dominio.ActividadVinculada;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;
import Logica.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;

public class NuevaActividadControlador extends Application {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);

    private Colaboracion colaboracion;

    @FXML
    private Button btnAceptar = new Button();
    @FXML
    private Button btnCancelar = new Button();
    @FXML
    private ToggleGroup tgTipoActividad = new ToggleGroup();
    @FXML
    private DatePicker dpFechaInicio = new DatePicker();
    @FXML
    private DatePicker dpFechaFin = new DatePicker();
    @FXML
    private TextField tfDescripcion = new TextField();
    @FXML
    private TextField tfTitulo = new TextField();

    public void setColaboracion (Colaboracion colaboracion) {
        if (colaboracion.validarNulos()) {
            this.colaboracion = colaboracion;
        }
        else {
            cerrarVentana();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../Plantilla/NuevaActividad.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Crear actividad nueva");

            Scene escena = new Scene(root, Color.TRANSPARENT);
            escena.getStylesheets().add("InterfazGrafica/Estilos/ventana.css");

            stage.setScene(escena);
            stage.show();
        } else {
            BITACORA.error("Ocurrió un error al iniciar la ventana windowNuevaActividad");
        }
    }

    private boolean camposInvalidos () {
        return tfTitulo.getText()
                .isBlank() ||
                tfDescripcion.getText()
                        .isBlank() ||
                tgTipoActividad.getSelectedToggle() == null ||
                dpFechaFin.getValue() == null ||
                dpFechaInicio.getValue() == null ||
                dpFechaInicio.getValue()
                        .isAfter(dpFechaFin.getValue());
    }

    public void agregarActividad () {
        if (camposInvalidos()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar");
            errorAlert.setContentText("Algunos datos están vacios, compruebe los datos e intente de nuevo");
            errorAlert.showAndWait();

            return;
        }

        RadioButton rbTipoActividad = (RadioButton) tgTipoActividad.getSelectedToggle();

        String titulo = tfTitulo.getText();
        String descripcion = tfDescripcion.getText();
        Actividad.TipoActividad tipo = Actividad.TipoActividad.valueOf(rbTipoActividad.getText());
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        Periodo periodo = new Periodo(fechaInicio, fechaFin);

        Actividad actividad = new Actividad(titulo, descripcion, tipo);

        DAOActividad dao = new DAOActividad();
        int resultado = -1;

        try {
            resultado = dao.agregar(actividad);
        }
        catch (ErrorDAO error) {
            Alert errorAlert = crearAlerta(error);
            errorAlert.showAndWait();

            return;
        }

        if (resultado < 1) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar");
            errorAlert.setContentText("Ocurrió un error al agregar la actividad");
            errorAlert.showAndWait();
        }

        ActividadVinculada actividadVinculada = new ActividadVinculada(actividad, colaboracion, periodo);
        DAOCronogramaActividades cronograma = new DAOCronogramaActividades();

        try {
            resultado = cronograma.agregar(actividadVinculada);
        }
        catch (ErrorDAO error) {
            Alert errorAlert = crearAlerta(error);
            errorAlert.showAndWait();

            return;
        }

        if (resultado < 1) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar");
            errorAlert.setContentText("Ocurrió un error al vincular la actividad");
            errorAlert.showAndWait();
        }
    }

    public void cerrarVentana () {
        Stage window = (Stage) btnCancelar.getScene().getWindow();
        window.close();
    }

    private static Alert crearAlerta (ErrorDAO error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        switch (error.getTipo()) {
            case VALIDACION:
                errorAlert.setHeaderText("Error de datos");
                errorAlert.setContentText("Los datos de la actividad son incorrectos, verifiquelos e intente de nuevo");
                break;

            case CONSULTA:
                errorAlert.setHeaderText("Error al agregar");
                errorAlert.setContentText("Ocurrió un error al agregar la actividad");
                break;

            case DUPLICIDAD:
                errorAlert.setHeaderText("La actividad ya existe");
                errorAlert.setContentText("Ya existe una actividad con estos datos, intente de nuevo");
                break;

            case CONEXION:
                errorAlert.setHeaderText("Error de base de datos");
                errorAlert.setContentText("Ocurrió un error al conectarse a la base de datos, intente nuevamente más tarde");
                break;
        }

        return errorAlert;
    }
}
