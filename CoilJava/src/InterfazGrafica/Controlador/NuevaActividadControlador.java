package InterfazGrafica.Controlador;

import Logica.DAO.DAOActividad;
import Logica.Dominio.Actividad;
import Logica.Dominio.Periodo;
import Logica.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class NuevaActividadControlador {
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

            errorAlert.showAndWait();

            return;
        }

        if (resultado > 0) {
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Actividad agregada correctamente");
            errorAlert.setContentText("La actividad se encuentra ahora en su cronograma de actividades");
            errorAlert.showAndWait();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar");
            errorAlert.setContentText("Ocurrió un error al agregar la actividad");
            errorAlert.showAndWait();
        }
    }

    public void cerrarVentana () {

    }
}
