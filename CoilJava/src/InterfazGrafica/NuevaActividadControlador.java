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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.Optional;

public class NuevaActividadControlador {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);
    private BorderPane panelPrincipal;
    private Pane panelAnterior;
    private ActividadesColaboracionControlador controlador;

    @FXML
    private Pane pnPrincipal;
    public ColaboracionDTO colaboracionDTO;

    @FXML
    private ToggleGroup tgTipoActividad = new ToggleGroup();
    @FXML
    private TextField tfDescripcion = new TextField();
    @FXML
    private TextField tfTitulo = new TextField();

    public void initialize (ColaboracionDTO colaboracionDTO, BorderPane panelPrincipal, Pane panelAnterior, ActividadesColaboracionControlador item) {
        if (!colaboracionDTO.esValido()) {
            return;
        }

        this.colaboracionDTO = colaboracionDTO;
        this.panelAnterior = panelAnterior;
        this.panelPrincipal = panelPrincipal;
        this.controlador = item;
    }

    public Pane getPane () {
        return pnPrincipal;
    }

    public void volver () {
        this.controlador.actualizarLista();
        panelPrincipal.setCenter(panelAnterior);
    }

    private boolean camposInvalidos () {
        return tfTitulo.getText()
                .isBlank() ||
                tfDescripcion.getText()
                        .isBlank() ||
                tgTipoActividad.getSelectedToggle() == null;
    }

    private boolean camposSobrepasanLimite () {
        if (tfTitulo.getText().length() > 100) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("El titulo puede tener un maximo de 50 caracteres");
            alerta.setHeaderText("Titulo demasiado largo");
            alerta.showAndWait();
            return true;
        }
        else if (tfDescripcion.getText().length() > 300) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("La descripción puede tener un maximo de 200 caracteres");
            alerta.setHeaderText("Descripción demasiado larga");
            alerta.showAndWait();
            return true;
        }

        return false;
    }

    public void agregarActividad () {
        if (camposInvalidos()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar");
            errorAlert.setContentText("Algunos datos están vacios, compruebe los datos e intente de nuevo");
            errorAlert.showAndWait();

            return;
        }
        else if (camposSobrepasanLimite()) {
            return;
        }

        RadioButton rbTipoActividad = (RadioButton) tgTipoActividad.getSelectedToggle();

        String titulo = tfTitulo.getText();
        String descripcion = tfDescripcion.getText();
        ActividadDTO.TipoActividad tipo = ActividadDTO.TipoActividad.valueOf(rbTipoActividad.getText());

        ActividadDTO actividadDTO = new ActividadDTO(titulo, descripcion, tipo);
        ActividadVinculadaDTO actividadVinculadaDTO = new ActividadVinculadaDTO(actividadDTO, this.colaboracionDTO);

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

        Optional<ActividadDTO> act;

        try {
             act = dao.getPorTitulo(titulo);

             if (act.isEmpty()) {
                 Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                 errorAlert.setHeaderText("Error al agregar actividadDTO");
                 errorAlert.setContentText("Ocurrió un error al agregar la actividadDTO");
                 errorAlert.showAndWait();

                 return;
             }
        }
        catch (ErrorDAO error) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error al agregar actividad");
            errorAlert.setContentText("Ocurrió un error al agregar la actividad: " + error.getMessage());
            errorAlert.showAndWait();

            return;
        }

        actividadVinculadaDTO.getActividad().setIdActividad(act.get().getIdActividad());
        CronogramaActividadAuxiliar cronograma = new CronogramaActividadAuxiliar();

        try {
            resultado = cronograma.agregar(actividadVinculadaDTO);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
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
        else {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setContentText("Se agregó la actividad correctamente");
            alerta.setHeaderText("Actividad agregada");
            alerta.showAndWait();
        }

        volver();
    }

    private static Alert crearAlerta (ErrorDAO error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);

        switch (error.getTipo()) {
            case VALIDACION:
                errorAlert.setHeaderText("Error de datos");
                errorAlert.setContentText("Los datos de la actividad son incorrectos, verifiquelos e intente de nuevo: "+ error.getMessage());
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
                errorAlert.setContentText("Ocurrió un error al conectarse a la base de datos, intente nuevamente más tarde: ");
                break;
        }

        return errorAlert;
    }
}
