package InterfazGrafica;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Optional;

public class InicioColaboracionControlador {
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnFinalizar;

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
        getAcademicoParPorColaboracion();
        cargarLabels();
        actualizarVisibilidadBotones();
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

    private void actualizarVisibilidadBotones() {
        ColaboracionDTO.EstadoColaboracion estado = this.colaboracionDTO.getEstado();

        switch (estado) {
            case finalizada:
                btnIniciar.setVisible(false);
                break;
            case vinculada:
                btnFinalizar.setVisible(false);
                break;
            case activa:
                btnFinalizar.setVisible(true);
                btnIniciar.setVisible(false);
                break;
            default:
                break;
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

    private void getFechas () throws ErrorDAO {
        LocalDate hoy = LocalDate.now();

        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        if (fechaInicio == null || fechaFin == null) {
            mostrarMensajeEmergente("Por favor seleccione ambas fechas", Alert.AlertType.WARNING);
            return;
        }

        validarFechas(hoy, fechaInicio, fechaFin);

        PeriodoDTO periodo = new PeriodoDTO(fechaInicio, fechaFin);
        this.colaboracionDTO.setPeriodo(periodo);
    }

    private void validarFechas(LocalDate hoy, LocalDate fechaInicio, LocalDate fechaFin) throws ErrorDAO {
        if (fechaInicio.isBefore(hoy)) {
            throw new ErrorDAO("La fecha de inicio no puede ser anterior a la fecha de hoy", ErrorDAO.Tipo.VALIDACION);
        }

        if (fechaFin.isBefore(hoy)) {
            throw new ErrorDAO("La fecha final no puede ser anterior a la fecha de hoy", ErrorDAO.Tipo.VALIDACION);
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new ErrorDAO("La fecha final no puede ser anterior a la fecha de inicio", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @FXML
    private void iniciarColaboracion () {
        if (this.colaboracionDTO.getEstado() != ColaboracionDTO.EstadoColaboracion.activa) {
            ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
            try {
                getFechas();
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.activa);
                colaboracionDAO.cambiarEstadoColaboracion(colaboracionDTO);
                colaboracionDAO.agregarPeriodoAColaboracion(colaboracionDTO);
                cargarLabels();
                btnFinalizar.setVisible(true);
                btnIniciar.setVisible(false);
                mostrarMensajeEmergente("Colaboración iniciada", Alert.AlertType.INFORMATION);
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

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    @FXML
    private void finalizarColaboracion () {
        if (this.colaboracionDTO.getEstado() != ColaboracionDTO.EstadoColaboracion.finalizada) {
            ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
            try {
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.finalizada);
                colaboracionDAO.cambiarEstadoColaboracion(colaboracionDTO);
                mostrarMensajeEmergente("Colaboración finalizada", Alert.AlertType.INFORMATION);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            mostrarMensajeEmergente("La colaboracion ya se encuentra finalizada", Alert.AlertType.INFORMATION);
        }
    }
}
