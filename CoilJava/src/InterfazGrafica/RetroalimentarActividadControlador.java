package InterfazGrafica;

import DAO.RetroalimentacionActividadAuxiliar;
import DTO.ActividadDTO;
import DTO.CuentaDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;

public class RetroalimentarActividadControlador {
    private static final Logger BITACORA = Logger.getLogger(NuevaActividadControlador.class);
    private BorderPane ventanaPrincipal;
    private Pane ventanaAnterior;

    @FXML
    private Pane pnPrincipal;
    @FXML
    private Slider slInteraccion;
    @FXML
    private Slider slDificultad;
    @FXML
    private Slider slInteres;
    @FXML
    private TextField tfComentario;
    @FXML
    private Text txtInteraccionPar;
    @FXML
    private Text txtDificultad;
    @FXML
    private Text txtInteres;

    private ActividadDTO actividad;
    private CuentaDTO usuario;
    private ActividadesColaboracionControlador controladorAnterior;

    public void initialize (BorderPane ventanaPrincipal, Pane ventanaAnterior, ActividadDTO actividad, CuentaDTO usuario, ActividadesColaboracionControlador controlador) {
        if (!actividad.esCorrecta()) {
            return;
        }

        this.actividad = actividad;
        this.usuario = usuario;
        this.ventanaPrincipal = ventanaPrincipal;
        this.ventanaAnterior = ventanaAnterior;
        this.controladorAnterior = controlador;
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
        regresar();
    }

    public void regresar () {
        this.ventanaPrincipal.setCenter(this.ventanaAnterior);
        this.controladorAnterior.actualizarLista();
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
        retroalimentacion.setIdActividad(this.actividad.getIdActividad());
        retroalimentacion.setInteres(interes);
        retroalimentacion.setDificultad(dificultad);
        retroalimentacion.setInteraccionConPar(interaccionPar);
        retroalimentacion.setComentario(mensaje);
        retroalimentacion.setIdUsuario(this.usuario.getIdPersona());

        return retroalimentacion;
    }

    public void actualizarEtiquetas () {
        txtInteres.setText(String.valueOf(slInteres.getValue()));
        txtDificultad.setText(String.valueOf(slDificultad.getValue()));
        txtInteraccionPar.setText(String.valueOf(slInteraccion.getValue()));
    }
}
