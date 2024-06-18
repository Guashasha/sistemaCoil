package InterfazGrafica.Items;

import DAO.ColaboracionAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.ColaboracionDTO;
import DTO.EstudianteDTO;
import DTO.UniversidadDTO;
import InterfazGrafica.EditarEstudianteControlador;
import InterfazGrafica.ListaEstudiantesControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;

public class ListaEstudiantesItemControlador {
    private static final Logger BITACORA = Logger.getLogger(ListaEstudiantesControlador.class);
    @FXML
    private Label lbMatricula;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUniversidad;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private ColaboracionDTO colaboracion;
    private EstudianteDTO estudiante;
    private ListaEstudiantesControlador listaEstudiantesControlador;

    public void setRecursos (BorderPane pnVentanaPrincipal, Stack<Pane> historialPaneles, ColaboracionDTO colaboracion, ListaEstudiantesControlador listaEstudiantesControlador, EstudianteDTO estudiante) throws ErrorDAO {
        if (pnVentanaPrincipal != null && historialPaneles != null && colaboracion != null && listaEstudiantesControlador != null && estudiante != null) {
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
            Optional<UniversidadDTO> universidadOptional = universidadAuxiliar.getUniversidadPorId(estudiante.getIdUniversidad());

            if (universidadOptional.isPresent()) {
                this.pnVentanaPrincipal = pnVentanaPrincipal;
                this.historialPaneles = historialPaneles;
                this.colaboracion = colaboracion;
                this.listaEstudiantesControlador = listaEstudiantesControlador;
                this.estudiante = estudiante;

                this.lbMatricula.setText(estudiante.getMatricula());
                String nombreCompleto = estudiante.getNombre() + " " + estudiante.getApellidos();
                this.lbNombre.setText(nombreCompleto);
                this.lbUniversidad.setText(universidadOptional.get()
                                                              .getNombre());
            }
            else {
                throw new ErrorDAO("Error al cargar recursos. Reinicie la aplicación", ErrorDAO.Tipo.VALIDACION);
            }
        }
        else {
            throw new ErrorDAO("Error al cargar recursos. Reinicie la aplicación", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @FXML
    private void retirarEstudiante () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("El alumno se retirará de la colaboración");
        alerta.setHeaderText(null);
        alerta.showAndWait()
              .ifPresent(response -> {
                  if (response == ButtonType.OK) {
                      retirarEstudianteDeColaboracion();
                  }
              });
    }

    @FXML
    private void editarEstudiante () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../EditarEstudiante.fxml"));
        BorderPane pnAgregarEstudiante;

        try {
            pnAgregarEstudiante = fxmlLoader.load();
            EditarEstudianteControlador controlador = fxmlLoader.getController();
            controlador.setRecursos(this.historialPaneles, this.pnVentanaPrincipal, this.estudiante, this.listaEstudiantesControlador);
            this.pnVentanaPrincipal.setCenter(pnAgregarEstudiante);
        }
        catch (IOException error) {
            BITACORA.info(error.getMessage());
            mostrarMensajeEmergente("Algo salió mal al cargar la sección: Editar estudiante. Inténtelo más tarde", Alert.AlertType.ERROR);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void retirarEstudianteDeColaboracion () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        int filasAfectadas;

        try {
            filasAfectadas = colaboracionAuxiliar.retirarEstudianteDeColaboracion(this.colaboracion, this.estudiante);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            filasAfectadas = -1;
        }

        if (filasAfectadas > 0) {
            mostrarMensajeEmergente("Se ha retirado el estudiante de la colaboración", Alert.AlertType.INFORMATION);
            this.listaEstudiantesControlador.cargarListaEstudiantes();
        }
        else if (filasAfectadas == 0) {
            mostrarMensajeEmergente("Algo salió mal al retirar el estudiante de la colaboración. Inténtelo de nuevo más tarde", Alert.AlertType.WARNING);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
