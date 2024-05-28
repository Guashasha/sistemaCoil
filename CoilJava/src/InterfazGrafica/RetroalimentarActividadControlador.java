package InterfazGrafica;

import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Stack;

public class RetroalimentarActividadControlador {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);
    private Pane ventanaPrincipal;
    private Pane ventanaAnterior;
    private Stack<Pane> historialPaneles;

    @FXML
    private Pane pnPrincipal;
    @FXML
    private Slider slInteraccion = new Slider();
    @FXML
    private Slider slDificultad = new Slider();
    @FXML
    private Slider slInteres = new Slider();
    @FXML
    private TextField tfComentario = new TextField();

    private ActividadDTO actividad;

    public void initialize (Pane ventanaPrincipal, Pane ventanaAnterior, Stack<Pane> historialPaneles, ActividadDTO actividad) {
        if (!actividad.esCorrecta()) {
            return;
        }

        this.actividad = actividad;
        this.ventanaPrincipal = ventanaPrincipal;
        this.ventanaAnterior = ventanaAnterior;
        this.historialPaneles = historialPaneles;
    }

    public Pane getPane () {
        return pnPrincipal;
    }

    public void guardarRetroalimentacion () {
        RetroalimentacionActividadDTO retroalimentacion = leerDatosRetroalimentacion();

        RetroalimentacionActividadAuxiliar dao = new RetroalimentacionActividadAuxiliar();

        try {
            dao.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            Alert alertaError = crearAlerta(error);

            alertaError.showAndWait();
            return;
        }

        Alert mensajeConfirmacion = new Alert(Alert.AlertType.INFORMATION);
        mensajeConfirmacion.setHeaderText("Colaboración calificada correctamente");
        mensajeConfirmacion.setContentText("La información de la retroalimentación se guardó correctamente. Gracias por participar en la colaboración.");
        mensajeConfirmacion.showAndWait();
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

    private RetroalimentacionActividadDTO leerDatosRetroalimentacion () {
        int interaccionPar = (int) slInteraccion.getValue();
        int dificultad = (int) slDificultad.getValue();
        int interes = (int) slInteres.getValue();
        String mensaje = tfComentario.getText();

        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();
        retroalimentacion.setIdActividad(actividad.getIdActividad());
        retroalimentacion.setInteres(interes);
        retroalimentacion.setDificultad(dificultad);
        retroalimentacion.setInteraccionConPar(interaccionPar);
        retroalimentacion.setComentario(mensaje);

        return retroalimentacion;
    }
}
