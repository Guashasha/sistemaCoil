package InterfazGrafica;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Optional;

public class InicioColaboracionControlador {

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private Label lbAcademicoPar;

    @FXML
    private Label lbIdioma;

    @FXML
    private Label lbObjetivo;

    @FXML
    private Label lbPerfil;

    @FXML
    private Label lbTemaInteres;

    @FXML
    private Label lbTipo;

    @FXML
    private Label lbPeriodo;
    @FXML
    private Label lbPeriodoTitulo;

    private ColaboracionDTO colaboracionDTO;

    private AcademicoDTO academicoDTO;

    public void inicializar () {
        dpFechaInicio.getEditor().setDisable(true);
        dpFechaInicio.getEditor().setOpacity(1);
        dpFechaFin.getEditor().setDisable(true);
        dpFechaFin.getEditor().setOpacity(1);
        lbPeriodo.setVisible(false);
        lbPeriodoTitulo.setVisible(false);
        getColaboracionActivaPorAcademico();
        getAcademicoParPorColaboracion();
        cargarLabels();
    }

    private void cargarLabels () {
        this.lbAcademicoPar.setText(colaboracionDTO.getAcademicoPar().getNombre() + " " + colaboracionDTO.getAcademicoPar().getApellidoPaterno() + " " + colaboracionDTO.getAcademicoPar().getApellidoMaterno());
        this.lbIdioma.setText(colaboracionDTO.getIdioma());
        this.lbObjetivo.setText(colaboracionDTO.getObjetivo());
        this.lbPerfil.setText(colaboracionDTO.getPerfilEstudiante());
        this.lbTemaInteres.setText(colaboracionDTO.getTemaInteres());
        this.lbTipo.setText(colaboracionDTO.getTipo().toString());
        if (colaboracionDTO.getPeriodo().getFechaInicio() != null || colaboracionDTO.getPeriodo().getFechaFin() != null) {
            lbPeriodoTitulo.setVisible(true);
            lbPeriodo.setVisible(true);
            lbPeriodo.setText(colaboracionDTO.getPeriodo().getFechaInicio().toString() + " - " + colaboracionDTO.getPeriodo().getFechaFin());
        }
    }

    private void getColaboracionActivaPorAcademico () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<ColaboracionDTO> colaboracionDTOOptional = Optional.empty();
        try {
            colaboracionDTOOptional = colaboracionDAO.getVinculadaPorAcademico(academicoDTO);
        }
        catch (ErrorDAO errorDAO) {
            mostrarMensajeEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }

        if (colaboracionDTOOptional.isPresent()) {
            this.colaboracionDTO = colaboracionDTOOptional.get();
        }
        else {
            mostrarMensajeEmergente("No existe una cuenta en estado vinculada", Alert.AlertType.ERROR);
        }
    }

    private void getAcademicoParPorColaboracion () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<AcademicoDTO> academicoDTOOptional = Optional.empty();
        try {
            academicoDTOOptional = colaboracionDAO.getAcademicoPar(this.colaboracionDTO);
        }
        catch (ErrorDAO errorDAO) {
            mostrarMensajeEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
        if (academicoDTOOptional.isPresent()) {
            this.colaboracionDTO.setAcademicoPar(academicoDTOOptional.get());
        }
        else {
            mostrarMensajeEmergente("Aún no cuenta con un académico par en su colaboración", Alert.AlertType.INFORMATION);
        }
    }

    private void obtenerFechas() {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        if (fechaInicio != null && fechaFin != null) {
            try {
                PeriodoDTO periodo = new PeriodoDTO(fechaInicio, fechaFin);
                this.colaboracionDTO.setPeriodo(periodo);
            } catch (ErrorDAO error) {
                throw error;
            }
        } else {
            mostrarMensajeEmergente("Por favor seleccione ambas fechas", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void iniciarColaboracion () {
        if (this.colaboracionDTO.getEstado() != ColaboracionDTO.EstadoColaboracion.activa) {
            ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
            try {
                obtenerFechas();
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.activa);
                colaboracionDAO.cambiarEstadoColaboracion(colaboracionDTO);
                cargarLabels();
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            mostrarMensajeEmergente("La colaboracion ya se encuentra activa", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }
}
