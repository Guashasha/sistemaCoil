package InterfazGrafica;

import DAO.ColaboracionDAO;
import DAO.RetroalimentacionColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class ProgresoColaboracionControlador {
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnFinalizar;
    @FXML
    private Button btnRetroalimentar;

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
    @FXML
    private Pane pnActual;

    private ColaboracionDTO colaboracionDTO;

    private AcademicoDTO academicoDTO;
    private Optional<RetroalimentacionColaboracionDTO> retroalimentacionColaboracionOpt = Optional.empty();
    private BorderPane pnVentanaPrincipal;

    public void inicializar () {
        dpFechaInicio.getEditor()
                     .setDisable(true);
        dpFechaInicio.getEditor()
                     .setOpacity(1);
        dpFechaFin.getEditor()
                  .setDisable(true);
        dpFechaFin.getEditor()
                  .setOpacity(1);
        lbPeriodo.setVisible(false);
        lbPeriodoTitulo.setVisible(false);
        getAcademicoParPorColaboracion();
        cargarLabels();
        actualizarVisibilidadBotones();
        if (colaboracionDTO.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision) {
            RetroalimentacionColaboracionAuxiliar retroalimentacionColaboracionAuxiliar = new RetroalimentacionColaboracionAuxiliar();
            retroalimentacionColaboracionOpt = retroalimentacionColaboracionAuxiliar.getPorPersonaYColaboracion(academicoDTO.getIdPersona(), colaboracionDTO.getIdColaboracion());
        }
    }

    private void cargarLabels () {
        this.lbAcademicoPar.setText(colaboracionDTO.getAcademicoPar()
                                                   .getNombre() + " " + colaboracionDTO.getAcademicoPar()
                                                                                       .getApellidoPaterno() + " " + colaboracionDTO.getAcademicoPar()
                                                                                                                                    .getApellidoMaterno());
        this.lbIdioma.setText(colaboracionDTO.getIdioma());
        this.lbObjetivo.setText(colaboracionDTO.getObjetivo());
        this.lbPerfil.setText(colaboracionDTO.getPerfilEstudiante());
        this.lbTemaInteres.setText(colaboracionDTO.getTemaInteres());
        this.lbTipo.setText(colaboracionDTO.getTipo()
                                           .toString());
        if (colaboracionDTO.getPeriodo() != null) {
            if (colaboracionDTO.getPeriodo()
                               .getFechaInicio() != null || colaboracionDTO.getPeriodo()
                                                                           .getFechaFin() != null) {
                lbPeriodoTitulo.setVisible(true);
                lbPeriodo.setVisible(true);
                lbPeriodo.setText(colaboracionDTO.getPeriodo()
                                                 .getFechaInicio()
                                                 .toString() + " - " + colaboracionDTO.getPeriodo()
                                                                                      .getFechaFin());
                actualizarEtiquetaPeriodo(colaboracionDTO.getPeriodo()
                                                         .getFechaInicio(), colaboracionDTO.getPeriodo()
                                                                                           .getFechaFin());
            }
        }
    }

    public void actualizarVisibilidadBotones () {
        ColaboracionDTO.EstadoColaboracion estado = this.colaboracionDTO.getEstado();

        switch (estado) {
            case finalizada:
                btnRetroalimentar.setVisible(false);
                btnIniciar.setVisible(false);
                btnFinalizar.setVisible(false);
                break;
            case vinculada:
                btnRetroalimentar.setVisible(false);
                btnFinalizar.setVisible(false);
                break;
            case activa:
                btnRetroalimentar.setVisible(false);
                btnFinalizar.setVisible(true);
                dpFechaFin.setVisible(false);
                dpFechaInicio.setVisible(false);
                btnIniciar.setVisible(false);
                break;
            case enRevision:
                if (retroalimentacionColaboracionOpt.isPresent()) {
                    btnRetroalimentar.setVisible(true);
                    btnFinalizar.setVisible(true);
                    btnIniciar.setVisible(false);
                }
                dpFechaInicio.setVisible(false);
                dpFechaFin.setVisible(false);
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
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        PeriodoDTO periodo = new PeriodoDTO(fechaInicio, fechaFin);
        this.colaboracionDTO.setPeriodo(periodo);

        actualizarEtiquetaPeriodo(fechaInicio, fechaFin);
    }

    private boolean sonFechasValidas () throws ErrorDAO {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();

        if (fechaInicio == null || fechaFin == null) {
            mostrarMensajeEmergente("Por favor seleccione ambas fechas", Alert.AlertType.WARNING);
            return false;
        }

        int anioInicio = fechaInicio.getYear();
        int anioFin = fechaFin.getYear();

        LocalDate fechaActual = LocalDate.now();

        if (!fechaInicio.isAfter(fechaActual) || !fechaFin.isAfter(fechaActual)) {
            mostrarMensajeEmergente("Las fechas deben ser posteriores a la fecha actual.", Alert.AlertType.WARNING);
            return false;
        }

        if (anioInicio != anioFin) {
            mostrarMensajeEmergente("Las fechas deben estar en el mismo año.", Alert.AlertType.WARNING);
            return false;
        }

        if (fechaInicio.equals(fechaFin)) {
            mostrarMensajeEmergente("La fecha de inicio no debe ser igual a la del fin.", Alert.AlertType.WARNING);
            return false;
        }

        int mesInicio = fechaInicio.getMonthValue();
        int mesFin = fechaFin.getMonthValue();

        boolean esPrimerSemestre = (mesInicio >= 1 && mesInicio <= 7) && (mesFin >= 1 && mesFin <= 7);
        boolean esSegundoSemestre = (mesInicio >= 8 && mesInicio <= 12) && (mesFin >= 8 && mesFin <= 12);

        if (!esPrimerSemestre && !esSegundoSemestre) {
            mostrarMensajeEmergente("""
                                            Las fechas deben estar dentro del mismo semestre:
                                            1. Enero a Julio.
                                            2. Agosto a Diciembre.
                                            """, Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void actualizarEtiquetaPeriodo (LocalDate fechaInicio, LocalDate fechaFin) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES"));
        String mesInicio = fechaInicio.format(monthFormatter);
        String mesFin = fechaFin.format(monthFormatter);
        int anio = fechaInicio.getYear();

        String periodo = mesInicio + " - " + mesFin + " " + anio;

        lbPeriodo.setText(periodo);

        String tooltipText = "Periodo: " + fechaInicio.toString() + " - " + fechaFin.toString();
        Tooltip tooltip = new Tooltip(tooltipText);
        Tooltip.install(lbPeriodo, tooltip);
    }

    @FXML
    private void iniciarColaboracion () {
        if (this.colaboracionDTO.getEstado() != ColaboracionDTO.EstadoColaboracion.activa) {
            ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
            if (sonFechasValidas()) {
                try {
                    getFechas();
                    colaboracionDAO.cambiarEstadoColaboracion("activa", this.colaboracionDTO.getIdColaboracion());
                    colaboracionDAO.agregarPeriodoAColaboracion(colaboracionDTO);
                    this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.activa);
                    cargarLabels();
                    btnFinalizar.setVisible(true);
                    btnIniciar.setVisible(false);
                    mostrarMensajeEmergente("Colaboración iniciada", Alert.AlertType.INFORMATION);
                    dpFechaFin.setVisible(false);
                    dpFechaInicio.setVisible(false);
                }
                catch (ErrorDAO error) {
                    mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
                }
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

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    @FXML
    private void finalizarColaboracion () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        RetroalimentacionColaboracionAuxiliar retroalimentacionDAO = new RetroalimentacionColaboracionAuxiliar();
        if (this.colaboracionDTO.getEstado() == ColaboracionDTO.EstadoColaboracion.activa) {
            try {
                colaboracionDAO.cambiarEstadoColaboracion("enRevision", this.colaboracionDTO.getIdColaboracion());
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.enRevision);
                mostrarMensajeEmergente("Retroalimente la colaboracion para poder finalizarla", Alert.AlertType.INFORMATION);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        }

        if (this.colaboracionDTO.getEstado() == ColaboracionDTO.EstadoColaboracion.enRevision && retroalimentacionDAO.getPorPersonaYColaboracion(this.academicoDTO.getIdPersona(), this.colaboracionDTO.getIdColaboracion())
                                                                                                                     .isPresent()) {
            try {
                colaboracionDAO.cambiarEstadoColaboracion("finalizada", this.colaboracionDTO.getIdColaboracion());
                this.colaboracionDTO.setEstado(ColaboracionDTO.EstadoColaboracion.enRevision);
                mostrarMensajeEmergente("Colaboracion finalizada, gracias por participar.", Alert.AlertType.INFORMATION);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            }
        }
        else {
            mostrarMensajeEmergente("Retroalimente la colaboración para poder continuar", Alert.AlertType.INFORMATION);
        }

        actualizarVisibilidadBotones();
    }

    @FXML
    private void abrirRetroalimentarColaboracion () {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RetroalimentarColaboracion.fxml"));
        Pane pnRetroalimentacion;

        try {
            pnRetroalimentacion = fxmlLoader.load();
        }
        catch (IOException error) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("No se pudo abrir la ventana de retroalimentacion de colaboración");
            alerta.setHeaderText("Ocurrió un error");
            alerta.showAndWait();
            return;
        }

        if (pnRetroalimentacion != null) {
            RetroalimentarColaboracionControlador controlador = fxmlLoader.getController();
            controlador.initialize(this.colaboracionDTO, this.pnVentanaPrincipal, this.pnActual, this);
            this.pnVentanaPrincipal.setCenter(pnRetroalimentacion);
        }
    }
}
