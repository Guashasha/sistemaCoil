package InterfazGrafica.Items;

import DAO.ColaboracionAuxiliar;
import DAO.ColaboracionDAO;
import DAO.UniversidadAuxiliar;
import DAO.UniversidadDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

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
    private TextArea taObjetivo;

    @FXML
    private TextArea taTemaInteres;


    public void inicializarLabel () {
        taTemaInteres.setText(colaboracionDTO.getTemaInteres());
        taObjetivo.setText(colaboracionDTO.getObjetivo());
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
        Optional<ColaboracionDTO> colaboracionDTOOptional = getColaboracion();
            try {
                boolean esSolicitud = mostrarDetallesColaboracion();
                if (colaboracionDTOOptional.isEmpty()) {
                    if (esSolicitud) {
                        registrarSolicitudParticipacion();
                        mostrarMensajeEmergente("Solicitud realizada con éxito", Alert.AlertType.INFORMATION);
                    }
                }
                else {
                    mostrarMensajeEmergente("Actualmente estas asociado a una colaboración " + colaboracionDTOOptional.get().getEstado().toString() +  ".\nNo puedes realizar una solicitud para participar", Alert.AlertType.INFORMATION);
                }
            }
            catch (IllegalArgumentException illegalArgumentException) {
                mostrarMensajeEmergente(illegalArgumentException.getMessage(), Alert.AlertType.WARNING);
            }
            catch (ErrorDAO errorDAO) {
                mostrarMensajeEmergente(errorDAO.getMessage(), Alert.AlertType.WARNING);
            }
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        if (colaboracionDTO != null) {
            inicializarLabel();
        }
    }

    private boolean mostrarDetallesColaboracion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tema de la colaboración: " + colaboracionDTO.getTemaInteres());
        alert.setHeaderText(null);

        StringBuilder contenido = new StringBuilder();
        contenido.append("Tipo: ").append(colaboracionDTO.getTipo().toString()).append("\n\n")
                 .append("Perfil del estudiante: ").append(colaboracionDTO.getPerfilEstudiante()).append("\n\n")
                 .append("Academico: ").append(colaboracionDTO.getAnfitrion().getNombre()).append(" ")
                 .append(colaboracionDTO.getAnfitrion().getApellidos()).append("\n\n")
                 .append("Universidad: ").append(getUniversidad(colaboracionDTO.getAnfitrion().getIdUniversidad()).getNombre()).append("\n");

        TextArea taAreaDatos = new TextArea(contenido.toString());
        taAreaDatos.setWrapText(true);
        taAreaDatos.setEditable(false);
        taAreaDatos.setMaxHeight(200);

        alert.getDialogPane().setContent(taAreaDatos);

        ButtonType btnSolicitud = new ButtonType("Solicitar participación");
        ButtonType btnSalir = new ButtonType("Salir");
        alert.getButtonTypes().setAll(btnSolicitud, btnSalir);

        alert.showAndWait();
        return alert.getResult() == btnSolicitud;
    }

    private UniversidadDTO getUniversidad (int id) {
        UniversidadDAO universidadDAO = new UniversidadDAO();
        Optional<UniversidadDTO> universidadDTOOptional = universidadDAO.getUniversidadPorId(id);
        if (universidadDTOOptional.isPresent()) {
            return universidadDTOOptional.get();
        }
        throw new IllegalArgumentException("No se encuentra la universidad perteciente al academico proponedor");
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }

    private Optional<ColaboracionDTO> getColaboracion() {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> colaboracionOptional = Optional.empty();

        try {
            colaboracionOptional = colaboracionDAO.getActivaPorAcademico(this.academicoDTO);
            colaboracionOptional = colaboracionOptional.isPresent() ? colaboracionOptional : colaboracionDAO.getVinculadaPorAcademico(this.academicoDTO);
            colaboracionOptional = colaboracionOptional.isPresent() ? colaboracionOptional : colaboracionDAO.getPropuestaPorAcademico(this.academicoDTO);
            colaboracionOptional = colaboracionOptional.isPresent() ? colaboracionOptional : colaboracionDAO.getColaboracionAceptadaPorAcademico(this.academicoDTO);
            colaboracionOptional = colaboracionOptional.isPresent() ? colaboracionOptional : colaboracionDAO.getColaboracionDisponiblePorAcademico(this.academicoDTO.getCedulaProfesional());
        } catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        return colaboracionOptional;
    }

    private void registrarSolicitudParticipacion () {
        COLABORACION_AUXILIAR.registrarSolicitudParticipacion(colaboracionDTO, academicoDTO);
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }
}
