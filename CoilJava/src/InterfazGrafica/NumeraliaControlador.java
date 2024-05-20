package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DAO.ColaboracionDAO;
import DTO.PeriodoDTO;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class NumeraliaControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(NumeraliaControlador.class);
    @FXML
    private BorderPane pnPrincipal;
    @FXML
    private Label lbAlumnosXalapa;
    @FXML
    private Label lbAlumnosVeracruz;
    @FXML
    private Label lbAlumnosPozaRica;
    @FXML
    private Label lbAlumnosOrizaba;
    @FXML
    private Label lbAlumnosCoatzacoalcos;
    @FXML
    private Label lbProfesoresXalapa;
    @FXML
    private Label lbProfesoresVeracruz;
    @FXML
    private Label lbProfesoresPozaRica;
    @FXML
    private Label lbProfesoresOrizaba;
    @FXML
    private Label lbProfesoresCoatzacoalcos;
    @FXML
    private Label lbAlumnosEconomico;
    @FXML
    private Label lbAlumnosHumanidades;
    @FXML
    private Label lbAlumnosTecnica;
    @FXML
    private Label lbAlumnosSalud;
    @FXML
    private Label lbAlumnosBiologia;
    @FXML
    private Label lbAlumnosDGRI;
    @FXML
    private Label lbProfesoresEconomico;
    @FXML
    private Label lbProfesoresHumanidades;
    @FXML
    private Label lbProfesoresTecnica;
    @FXML
    private Label lbProfesoresSalud;
    @FXML
    private Label lbProfesoresBiologia;
    @FXML
    private Label lbProfesoresDGRI;
    @FXML
    private Label lbAnio;
    @FXML
    private Label lbPeriodo;
    @FXML
    private Button btnAnioAtras;
    @FXML
    private Button btnAnioAdelante;
    @FXML
    private HBox hboxTablas;
    private final Map<String,Label[]> MAPA_ETIQUETAS = new HashMap<>();
    private int anioMaximo;
    private int anioMinimo;

    public static void main(String[] args) {
        launch(args);
    }

    public Pane getPane () {
        return pnPrincipal;
    }

    @Override
    public void start (Stage stage) throws Exception {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("Numeralia.fxml"));
        }
        catch (IOException e) {
            BITACORA.error(e);
        }

        if (root != null) {
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene escena = new Scene(root);
            stage.setScene(escena);
            stage.show();
        }
        else {
            BITACORA.error("Ocurrió un error al iniciar la ventana windowNumeralia");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        crearMapaEtiquetas();
        cargarNumeraliaPrincipal();
        asignarAnioMinimo();
    }

    @FXML
    private void cambiarPeriodoFebreroJulio () {
        int anio = Integer.parseInt(lbAnio.getText());
        PeriodoDTO periodoFebreroJulio = new PeriodoDTO(LocalDate.of(anio,2,1),LocalDate.of(anio,7,31));
        cargarNumeralia(periodoFebreroJulio);
    }

    @FXML
    private void cambiarPeriodoAgostoEnero () {
        int anio = Integer.parseInt(lbAnio.getText());
        PeriodoDTO periodoAgostoEnero = new PeriodoDTO(LocalDate.of(anio,8,1),LocalDate.of(anio+1,1,31));
        cargarNumeralia(periodoAgostoEnero);
    }

    @FXML
    private void anioAtras () {
        int anio = Integer.parseInt(this.lbAnio.getText()) - 1;
        this.lbAnio.setText(String.valueOf(anio));
        if (!this.btnAnioAdelante.isVisible()) {
            this.btnAnioAdelante.setVisible(true);
        }
        if (anio == this.anioMinimo) {
            this.btnAnioAtras.setVisible(false);
        }
        this.hboxTablas.setVisible(false);
        this.lbPeriodo.setText("Elige un periodo");
    }

    @FXML
    private void anioAdelante () {
        int anio = Integer.parseInt(this.lbAnio.getText()) + 1;
        this.lbAnio.setText(String.valueOf(anio));

        if (!this.btnAnioAtras.isVisible()) {
            this.btnAnioAtras.setVisible(true);
        }
        if (anio == this.anioMaximo) {
            this.btnAnioAdelante.setVisible(false);
        }

        this.hboxTablas.setVisible(false);
        this.lbPeriodo.setText("Elige un periodo");
    }

    private void crearMapaEtiquetas () {
        MAPA_ETIQUETAS.put("Xalapa",new Label[]{this.lbAlumnosXalapa,this.lbProfesoresXalapa});
        MAPA_ETIQUETAS.put("Veracruz",new Label[]{lbAlumnosVeracruz,lbProfesoresVeracruz});
        MAPA_ETIQUETAS.put("Poza Rica - Tuxpan",new Label[]{lbAlumnosPozaRica,lbProfesoresPozaRica});
        MAPA_ETIQUETAS.put("Orizaba - Córdoba",new Label[]{lbAlumnosOrizaba,lbProfesoresOrizaba});
        MAPA_ETIQUETAS.put("Coatzacoalcos - Minatitlán",new Label[]{lbAlumnosCoatzacoalcos,lbProfesoresCoatzacoalcos});
        MAPA_ETIQUETAS.put("economico-administrativo",new Label[]{lbAlumnosEconomico,lbProfesoresEconomico});
        MAPA_ETIQUETAS.put("humanidades",new Label[]{lbAlumnosHumanidades,lbProfesoresHumanidades});
        MAPA_ETIQUETAS.put("tecnica",new Label[]{lbAlumnosTecnica,lbProfesoresTecnica});
        MAPA_ETIQUETAS.put("ciencias de la salud",new Label[]{lbAlumnosSalud,lbProfesoresSalud});
        MAPA_ETIQUETAS.put("biologia-agropecuarias",new Label[]{lbAlumnosBiologia,lbProfesoresBiologia});
        MAPA_ETIQUETAS.put("DGRI",new Label[]{lbAlumnosDGRI,lbProfesoresDGRI});
    }

    private void cargarNumeraliaPrincipal() {
        LocalDateTime fechaActual = LocalDateTime.now();
        PeriodoDTO periodoActual = new PeriodoDTO();

        switch (fechaActual.getMonth()) {
            case FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY -> {
                periodoActual.setFechaInicio(LocalDate.of(fechaActual.getYear() - 1, 8, 1));
                periodoActual.setFechaFin(LocalDate.of(fechaActual.getYear(), 1, 31));
                this.lbAnio.setText(String.valueOf(fechaActual.getYear()-1));
            }
            default -> {
                periodoActual.setFechaInicio(LocalDate.of(fechaActual.getYear(), 2, 1));
                periodoActual.setFechaFin(LocalDate.of(fechaActual.getYear(), 7, 31));
                this.lbAnio.setText(String.valueOf(fechaActual.getYear()));
            }
        }

        asignarAnioMaximo(periodoActual);
        cargarNumeralia(periodoActual);
    }

    private void cargarNumeralia (PeriodoDTO periodo) {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        Map<String,int[]> numeraliaRegion = null;
        Map<String,int[]> numeraliaAreas = null;

        try {
            numeraliaRegion = colaboracionAuxiliar.getNumeraliaRegion(periodo);
            numeraliaAreas = colaboracionAuxiliar.getNumeraliaAreaAcademica(periodo);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            hboxTablas.setVisible(false);
        }

        if (numeraliaRegion != null && numeraliaAreas != null) {
            mostrarNumeralia(numeraliaRegion,numeraliaAreas);
            mostrarEtiquetaPeriodo(periodo);
        }
    }

    private void mostrarNumeralia(Map<String,int[]> numeraliaRegion, Map<String,int[]> numeraliaAreas) {
        String[] llavesRegion = new String[]{"Xalapa","Veracruz","Poza Rica - Tuxpan","Orizaba - Córdoba","Coatzacoalcos - Minatitlán"};
        String[] llavesAreas = new String[]{"economico-administrativo","humanidades","tecnica","ciencias de la salud","biologia-agropecuarias","DGRI"};

        mostrarCantidades(llavesRegion,numeraliaRegion);
        mostrarCantidades(llavesAreas,numeraliaAreas);

        if (!hboxTablas.isVisible()) {
            hboxTablas.setVisible(true);
        }
    }

    private void mostrarCantidades (String[] llaves, Map<String,int[]> numeralia) {
        for (String llave : llaves){
            int[] cantidades = numeralia.get(llave);
            Label[] etiquetas = this.MAPA_ETIQUETAS.get(llave);
            if (cantidades != null) {
                etiquetas[0].setText(String.valueOf(cantidades[0]));
                etiquetas[1].setText(String.valueOf(cantidades[1]));
            }
            else {
                etiquetas[0].setText("0");
                etiquetas[1].setText("0");
            }
        }
    }

    private void mostrarEtiquetaPeriodo (PeriodoDTO periodo) {
        String etiquetaPeriodo;
        Month mesInicio = periodo.getFechaInicio()
                                 .getMonth();
        etiquetaPeriodo = mesInicio == Month.FEBRUARY ? "Periodo Febrero - Julio" : "Periodo Agosto - Enero";
        lbPeriodo.setText(etiquetaPeriodo);
    }

    private void asignarAnioMaximo (PeriodoDTO periodoActual) {
        LocalDate fechaInicio = periodoActual.getFechaInicio();
        if (fechaInicio.getMonth() == Month.FEBRUARY) {
            this.anioMaximo = fechaInicio.getYear() - 1;
        }
        else {
            this.anioMaximo = fechaInicio.getYear();
        }
        this.btnAnioAdelante.setVisible(false);
    }

    private void asignarAnioMinimo () {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        Optional<LocalDate> fechaMasAntiguaOptional = Optional.empty();

        try {
            fechaMasAntiguaOptional = colaboracionDAO.getFechaColaboracionMasAntigua();
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
            this.anioMinimo = this.anioMaximo;
        }

        if (fechaMasAntiguaOptional.isPresent()) {
            LocalDate fecha = fechaMasAntiguaOptional.get();
            if (fecha.getMonth() == Month.JANUARY) {
                this.anioMinimo = fecha.getYear() - 1;
            }
            else {
                this.anioMinimo = fecha.getYear();
            }
        }
        if (this.anioMinimo == this.anioMaximo) {
            this.btnAnioAtras.setVisible(false);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}