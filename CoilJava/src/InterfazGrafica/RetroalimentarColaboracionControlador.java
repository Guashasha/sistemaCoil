package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DAO.RetroalimentacionColaboracionAuxiliar;
import DTO.ColaboracionDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

public class RetroalimentarColaboracionControlador {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);

    @FXML
    private Pane pnPrincipal;
    @FXML
    private Slider slCalificacion = new Slider();
    @FXML
    private Slider slHabilidades = new Slider();
    @FXML
    private Slider slIntercambio = new Slider();
    @FXML
    private Slider slMejoraLenguaje = new Slider();
    @FXML
    private Slider slTrabajo = new Slider();
    @FXML
    private Slider slMejoraFormacion = new Slider();
    @FXML
    private Slider slInteraccion = new Slider();
    @FXML
    private TextField tfComentario = new TextField();

    private ColaboracionDTO colaboracion;
    private BorderPane ventanaPrincipal;
    private Pane panelAnterior;
    private ProgresoColaboracionControlador controladorAnterior;

    public void initialize (ColaboracionDTO colaboracion, BorderPane ventanaPrincipal, Pane panelAnterior, ProgresoColaboracionControlador controladorAnterior) {
        this.panelAnterior = panelAnterior;
        this.ventanaPrincipal = ventanaPrincipal;
        this.colaboracion = colaboracion;
        this.controladorAnterior = controladorAnterior;
    }

    public Pane getPane () {
        return pnPrincipal;
    }

    public void guardarRetroalimentacion () {
        RetroalimentacionColaboracionDTO retroalimentacion = leerDatosRetroalimentacion();

        RetroalimentacionColaboracionAuxiliar dao = new RetroalimentacionColaboracionAuxiliar();

        try {
            dao.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            Alert alertaError = crearAlerta(error);

            alertaError.showAndWait();           
            return;
        }

        finalizarColaboracion();
        
        Alert mensajeConfirmacion = new Alert(Alert.AlertType.INFORMATION);
        mensajeConfirmacion.setHeaderText("Colaboración calificada correctamente");
        mensajeConfirmacion.setContentText("La información de la retroalimentación se guardó correctamente. Gracias por participar en la colaboración.");
        mensajeConfirmacion.showAndWait();

        regresar();
    }

    private void finalizarColaboracion () {
        ColaboracionAuxiliar dao = new ColaboracionAuxiliar();

        try {
            dao.cambiarEstadoColaboracion("finalizada", this.colaboracion.getIdColaboracion());
        } catch (ErrorDAO e) {
            BITACORA.error(e.getStackTrace());
            crearAlerta(e).showAndWait();
        }
    }

    private static Alert crearAlerta(ErrorDAO error) {
        Alert alertaError = new Alert(Alert.AlertType.ERROR);

        switch (error.getTipo()) {
            case VALIDACION -> {
                alertaError.setHeaderText("Retroalimentacion Incorrecta");
                alertaError.setContentText("Los datos de la retroalimentacion son incorrectos");
            }
            case DUPLICIDAD -> {
                alertaError.setHeaderText("Colaboración ya retroalimentada");
                alertaError.setContentText("Ya retroalimentó la colaboración");
            }
            case CONEXION -> {
                alertaError.setHeaderText("Error de conexión");
                alertaError.setContentText("No se pudo conectar a la base de datos, intente de nuevo más tarde");
            }
            default -> {
                alertaError.setHeaderText("Ocurrió un error");
                alertaError.setContentText("Ocurrió un error al retroalimentar la colaboración, intente de nuevo más tarde");
            }
        }
        return alertaError;
    }

    private RetroalimentacionColaboracionDTO leerDatosRetroalimentacion () {
        int calificacion = (int) slCalificacion.getValue();
        int habilidades = (int) slHabilidades.getValue();
        int intercambio = (int) slIntercambio.getValue();
        int mejoraLenguaje = (int) slMejoraLenguaje.getValue();
        int trabajo = (int) slTrabajo.getValue();
        int mejoraFormacion = (int) slMejoraFormacion.getValue();
        int interaccion = (int) slInteraccion.getValue();
        String mensaje = tfComentario.getText();

        RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();
        retroalimentacion.setColaboracion(colaboracion.getIdColaboracion());
        retroalimentacion.setCalificacion(calificacion);
        retroalimentacion.setHabilidadesObtenidas(habilidades);
        retroalimentacion.setIntercambioCultural(intercambio);
        retroalimentacion.setMejoraDelLenguaje(mejoraLenguaje);
        retroalimentacion.setTrabajoColaborativo(trabajo);
        retroalimentacion.setMejoraFormacionProfesional(mejoraFormacion);
        retroalimentacion.setInteraccionConPar(interaccion);
        retroalimentacion.setComentario(mensaje);

        return retroalimentacion;
    }

    public void regresar () {
        controladorAnterior.actualizarVisibilidadBotones();
        this.ventanaPrincipal.setCenter(this.panelAnterior);
    }
}
