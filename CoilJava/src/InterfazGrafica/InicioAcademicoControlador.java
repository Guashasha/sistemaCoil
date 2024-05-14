package InterfazGrafica;

import DAO.ActividadAuxiliar;
import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ActividadDTO;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class InicioAcademicoControlador{
    private static final Logger BITACORA = Logger.getLogger(InicioAcademicoControlador.class);

    @FXML
    VBox pnInicio;

    private AcademicoDTO usuario;
    private Optional<ColaboracionDTO> colaboracion = Optional.empty();

    public InicioAcademicoControlador (AcademicoDTO usuario) {
        this.usuario = usuario;

        try {
            pnInicio = FXMLLoader.load(getClass().getResource("InicioAcademico.fxml"));
        }
        catch (IOException error) {
            BITACORA.fatal(error);
            return;
        }

        setOverviewColaboracion();
    }

    public Pane getPane () {
        return pnInicio;
    }

    private void setOverviewColaboracion () {
        getInformacionColaboracion();

        if (this.colaboracion.isEmpty()) {
            AnchorPane menuSinColaboracion;

            try {
                menuSinColaboracion = FXMLLoader.load(getClass().getResource("MenuSinColaboracionAcademico.fxml"));
            }
            catch (IOException error) {
                BITACORA.fatal(error);
                return;
            }

            pnInicio.getChildren().add(menuSinColaboracion);

            return;
        }

        HBox informacionColaboracion;
        TableView<ActividadDTO> informacionActividades;

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

    private void getInformacionColaboracion () throws ErrorDAO {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        //colaboracion = colaboracionAuxiliar.getActivaPorAcademico(this.usuario.getIdPersona());
    }

    private HBox getInfoHBox () {
        HBox informacionColaboracion = new HBox();

        Label temaInteres = new Label(this.colaboracion.get().getTemaInteres());
        Label colaborador = new Label("AcademicoDTO colaborador: " + this.colaboracion.get().getAcademicoPar().getNombre());
        Label fechaFin = new Label("Fecha de finalización: " + this.colaboracion.get().getPeriodo()
                .getFechaFin());

        informacionColaboracion.getChildren()
                .addAll(temaInteres, colaborador, fechaFin);

        return informacionColaboracion;
    }

    private TableView<ActividadDTO> getInformacionActividades () throws ErrorDAO {
        ActividadAuxiliar actividadAuxiliar = new ActividadAuxiliar();
        List<ActividadDTO> resultado;

        resultado = actividadAuxiliar.getPorIdColaboracion(this.colaboracion.get().getIdColaboracion());

        TableView<ActividadDTO> actividades = new TableView<>(FXCollections.observableList(resultado));
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
