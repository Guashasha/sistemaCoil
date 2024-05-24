package InterfazGrafica;

import DAO.ActividadAuxiliar;
import DAO.CronogramaActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;

public class NuevaActividadControlador {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);

    @FXML
    private Pane pnPrincipal;
    private ColaboracionDTO colaboracionDTO;

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

    public NuevaActividadControlador (ColaboracionDTO colaboracionDTO) {
        if (!colaboracionDTO.esValido()) {
            return;
        }

        this.colaboracionDTO = colaboracionDTO;

        try {
             pnPrincipal = FXMLLoader.load(getClass().getResource("NuevaActividad.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }
    }

    public Pane getPane () {
        return pnPrincipal;
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
        ActividadDTO.TipoActividad tipo = ActividadDTO.TipoActividad.valueOf(rbTipoActividad.getText());
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        PeriodoDTO periodoDTO = new PeriodoDTO(fechaInicio, fechaFin);

        ActividadDTO actividadDTO = new ActividadDTO(titulo, descripcion, tipo);

        ActividadAuxiliar dao = new ActividadAuxiliar();
        int resultado = -1;

        try {
            resultado = dao.agregar(actividadDTO);
        }
        catch (ErrorDAO error) {
            Alert errorAlert = crearAlerta(error);
            errorAlert.showAndWait();

            return;
        }

        if (resultado < 1) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar actividadDTO");
            errorAlert.setContentText("Ocurrió un error al agregar la actividadDTO");
            errorAlert.showAndWait();

            return;
        }

        ActividadVinculadaDTO actividadVinculadaDTO = new ActividadVinculadaDTO(actividadDTO, colaboracionDTO, periodoDTO);
        CronogramaActividadAuxiliar cronograma = new CronogramaActividadAuxiliar();

        try {
            resultado = cronograma.agregar(actividadVinculadaDTO);
        }
        catch (ErrorDAO error) {
            Alert errorAlert = crearAlerta(error);
            errorAlert.showAndWait();

            return;
        }

        if (resultado < 1) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar");
            errorAlert.setContentText("Ocurrió un error al vincular la actividadDTO");
            errorAlert.showAndWait();
        }
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
