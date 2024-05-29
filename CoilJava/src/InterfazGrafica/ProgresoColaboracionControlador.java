package InterfazGrafica;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Optional;

public class ProgresoColaboracionControlador {
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

    public void inicializar() {
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

    private void cargarLabels() {
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
            actualizarEtiquetaPeriodo(colaboracionDTO.getPeriodo().getFechaInicio(), colaboracionDTO.getPeriodo().getFechaFin());
        }
    }

    private void actualizarVisibilidadBotones() {
        ColaboracionDTO.EstadoColaboracion estado = this.colaboracionDTO.getEstado();

        switch (estado) {
            case finalizada:
                btnIniciar.setVisible(false);
                btnFinalizar.setVisible(false);
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

    private void getAcademicoParPorColaboracion() {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<AcademicoDTO> academicoDTOOptional = Optional.empty();
        try {
            academicoDTOOptional = colaboracionDAO.getAcademicoPar(this.colaboracionDTO);
        } catch (ErrorDAO errorDAO) {
            mostrarMensajeEmergente(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
        if (academicoDTOOptional.isPresent()) {
            this.colaboracionDTO.setAcademicoPar(academicoDTOOptional.get());
        } else {
            mostrarMensajeEmergente("Aún no cuenta con un académico par en su colaboración", Alert.AlertType.INFORMATION);
        }
    }

    private void getFechas() throws ErrorDAO {
        LocalDate hoy = LocalDate.now();

        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        if (fechaInicio == null || fechaFin == null) {
            mostrarMensajeEmergente("Por favor seleccione ambas fechas", Alert.AlertType.WARNING);
            return;
        }

        validarFechas(fechaInicio, fechaFin);

        PeriodoDTO periodo = new PeriodoDTO(fechaInicio, fechaFin);
        this.colaboracionDTO.setPeriodo(periodo);

        actualizarEtiquetaPeriodo(fechaInicio, fechaFin);
    }

    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) throws ErrorDAO {
        int anioInicio = fechaInicio.getYear();
        int anioFin = fechaFin.getYear();

        LocalDate hoy = LocalDate.now();

        if (!fechaInicio.isAfter(hoy) || !fechaFin.isAfter(hoy)) {
            throw new ErrorDAO("Las fechas deben ser posteriores a la fecha actual.", ErrorDAO.Tipo.VALIDACION);
        }

        if (anioInicio != anioFin) {
            throw new ErrorDAO("Las fechas deben estar en el mismo año.", ErrorDAO.Tipo.VALIDACION);
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            throw new ErrorDAO("La fecha de fin debe ser posterior a la fecha de inicio.", ErrorDAO.Tipo.VALIDACION);
        }

        int mesInicio = fechaInicio.getMonthValue();
        int mesFin = fechaFin.getMonthValue();

        boolean esPrimerSemestre = (mesInicio >= 1 && mesInicio <= 7) && (mesFin >= 1 && mesFin <= 7);
        boolean esSegundoSemestre = (mesInicio >= 8 && mesInicio <= 12) && (mesFin >= 8 && mesFin <= 12);

        if (!esPrimerSemestre && !esSegundoSemestre) {
            throw new ErrorDAO("""
            Las fechas deben estar dentro del mismo semestre:
            1. Enero a Julio.
            2. Agosto a Diciembre.
            """, ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void actualizarEtiquetaPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        int mesInicio = fechaInicio.getMonthValue();
        int anio = fechaInicio.getYear();

        String semestre;
        if (mesInicio >= 1 && mesInicio <= 7) {
            semestre = "Enero - Julio " + anio;
        } else {
            semestre = "Agosto - Diciembre " + anio;
        }

        lbPeriodo.setText(semestre);

        String tooltipText = "Periodo: " + fechaInicio.toString() + " - " + fechaFin.toString();
        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(lbPeriodo, tooltip);
    }

    @FXML
    private void iniciarColaboracion() {
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
            } catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarMensajeEmergente("La colaboracion ya se encuentra activa", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarMensajeEmergente(String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    public void setAcademicoDTO(AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }

    public void setColaboracionDTO(ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }

    @FXML
    private void finalizarColaboracion() {
        if (this.colaboracionDTO.getEstado() != ColaboracionDTO.EstadoColaboracion.finalizada) {
            ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
            try {
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.finalizada);
                colaboracionDAO.cambiarEstadoColaboracion(colaboracionDTO);
                mostrarMensajeEmergente("Colaboración finalizada", Alert.AlertType.INFORMATION);
            } catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarMensajeEmergente("La colaboracion ya se encuentra finalizada", Alert.AlertType.INFORMATION);
        }
    }
}
