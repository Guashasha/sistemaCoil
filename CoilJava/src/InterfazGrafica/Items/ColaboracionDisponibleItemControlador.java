package InterfazGrafica.Items;

import DAO.ColaboracionAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ColaboracionDisponibleItemControlador implements Initializable {
    private final ColaboracionAuxiliar COLABORACION_AUXILIAR = new ColaboracionAuxiliar();
    private ColaboracionDTO colaboracionDTO;
    private AcademicoDTO academicoDTO;
    @FXML
    private Label lbIdioma;

    @FXML
    private Label lbObjetivo;

    @FXML
    private Label lbtemaInteres;


    public void inicializarLabel () {
        lbtemaInteres.setText(colaboracionDTO.getTemaInteres());
        lbObjetivo.setText(colaboracionDTO.getObjetivo());
        lbIdioma.setText(colaboracionDTO.getIdioma());
    }


    public ColaboracionDTO getColaboracionDTO () {
        return colaboracionDTO;
    }

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    @FXML
    public void clicEnElPanel () {
        try {
            boolean esSolicitud = mostrarDetallesColaboracion();
            if (esSolicitud) {
                registrarSolicitudParticipacion();
            }
        }
        catch (IllegalArgumentException illegalArgumentException) {
            mostrarAlert(illegalArgumentException.getMessage(), Alert.AlertType.WARNING);
        }
        catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.WARNING);
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        if (colaboracionDTO != null) {
            inicializarLabel();
        }
    }

    private boolean mostrarDetallesColaboracion () {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tema de la colaboración: " + colaboracionDTO.getTemaInteres());
        alert.setHeaderText(null);
        alert.setContentText(
                "Tipo: " + colaboracionDTO.getTipo()
                                          .toString() + "\n\n" +
                        "Fecha de inicio: " + colaboracionDTO.getPeriodo()
                                                             .getFechaInicio()
                                                             .toString() + "\n\n" +
                        "Fecha de cierre: " + colaboracionDTO.getPeriodo()
                                                             .getFechaFin()
                                                             .toString() + "\n\n" +
                        "Perfil del estudiante: " + colaboracionDTO.getPerfilEstudiante() + "\n\n" +
                        "Academico: " + colaboracionDTO.getAnfitrion()
                                                       .getNombre() + " " + colaboracionDTO.getAnfitrion()
                                                                                           .getApellidoPaterno() + " " + colaboracionDTO.getAnfitrion()
                                                                                                                                        .getApellidoMaterno() + "\n\n" +
                        "Universidad: " + obtenerUniversidad(colaboracionDTO.getAnfitrion()
                                                                            .getIdUniversidad()).getNombre() + "\n"
        );

        ButtonType btnSolicitud = new ButtonType("Solicitar participación");
        ButtonType btnSalir = new ButtonType("Salir");
        alert.getButtonTypes()
             .setAll(btnSolicitud, btnSalir);

        alert.showAndWait();
        return alert.getResult() == btnSolicitud;
    }

    private UniversidadDTO obtenerUniversidad (int id) {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
        Optional<UniversidadDTO> universidadDTOOptional = universidadAuxiliar.getUniversidadPorId(id);
        if (universidadDTOOptional.isPresent()) {
            return universidadDTOOptional.get();
        }
        throw new IllegalArgumentException("No se encuentra la universidad perteciente al academico proponedor");
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }

    private void registrarSolicitudParticipacion () {
        COLABORACION_AUXILIAR.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }
}
