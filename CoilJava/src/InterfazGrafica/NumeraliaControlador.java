package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class NumeraliaControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(NumeraliaControlador.class);
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
    private Button btnPeriodoFebreroJulio;
    @FXML
    private Button btnPeriodoAgostoEnero;
    private final Map<String,Label[]> mapaEtiquetas= new HashMap<>();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
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
    }

    private void crearMapaEtiquetas () {
        this.mapaEtiquetas.put("Xalapa",new Label[]{this.lbAlumnosXalapa,this.lbProfesoresXalapa});
        this.mapaEtiquetas.put("Veracruz",new Label[]{lbAlumnosVeracruz,lbProfesoresVeracruz});
        this.mapaEtiquetas.put("Poza Rica - Tuxpan",new Label[]{lbAlumnosPozaRica,lbProfesoresPozaRica});
        this.mapaEtiquetas.put("Orizaba - Córdoba",new Label[]{lbAlumnosOrizaba,lbProfesoresOrizaba});
        this.mapaEtiquetas.put("Coatzacoalcos - Minatitlán",new Label[]{lbAlumnosCoatzacoalcos,lbProfesoresCoatzacoalcos});
        this.mapaEtiquetas.put("economico-administrativo",new Label[]{lbAlumnosEconomico,lbProfesoresEconomico});
        this.mapaEtiquetas.put("humanidades",new Label[]{lbAlumnosHumanidades,lbProfesoresHumanidades});
        this.mapaEtiquetas.put("tecnica",new Label[]{lbAlumnosTecnica,lbProfesoresTecnica});
        this.mapaEtiquetas.put("ciencias de la salud",new Label[]{lbAlumnosSalud,lbProfesoresSalud});
        this.mapaEtiquetas.put("biologia-agropecuarias",new Label[]{lbAlumnosBiologia,lbProfesoresBiologia});
        this.mapaEtiquetas.put("DGRI",new Label[]{lbAlumnosDGRI,lbProfesoresDGRI});
    }

    private void cargarNumeraliaPrincipal() {
        LocalDateTime fechaActual = LocalDateTime.now();
        PeriodoDTO periodo = new PeriodoDTO();

        switch (fechaActual.getMonth()) {
            case FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY -> {
                periodo.setFechaInicio(LocalDate.of(fechaActual.getYear() - 1, 8, 1));
                periodo.setFechaFin(LocalDate.of(fechaActual.getYear(), 1, 31));
            }
            default -> {
                periodo.setFechaInicio(LocalDate.of(fechaActual.getYear(), 2, 1));
                periodo.setFechaFin(LocalDate.of(fechaActual.getYear(), 7, 31));
            }
        }

        cargarNumeralia(periodo);
    }

    private void cargarNumeralia(PeriodoDTO periodo) {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        Map<String,int[]> numeraliaRegion = null;
        Map<String,int[]> numeraliaAreas = null;

        try {
            numeraliaRegion = colaboracionAuxiliar.getNumeraliaRegion(periodo);
            numeraliaAreas = colaboracionAuxiliar.getNumeraliaAreaAcademica(periodo);
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }

        if (numeraliaRegion != null && numeraliaAreas != null) {
            mostrarNumeraliaRegion(numeraliaRegion,numeraliaAreas);
        }
    }

    private void mostrarNumeraliaRegion (Map<String,int[]> numeraliaRegion, Map<String,int[]> numeraliaAreas) {
        String[] llavesRegion = new String[]{"Xalapa","Veracruz","Poza Rica - Tuxpan","Orizaba - Córdoba","Coatzacoalcos - Minatitlán"};
        String[] llavesAreas = new String[]{"economico-administrativo","humanidades","tecnica","ciencias de la salud","biologia-agropecuarias","DGRI"};

        asignarEtiquetas(llavesRegion,numeraliaRegion);
        asignarEtiquetas(llavesAreas,numeraliaAreas);
    }

    private void asignarEtiquetas (String[] llaves, Map<String,int[]> numeralia) {
        for (String llave : llaves){
            int[] cantidades = numeralia.get(llave);
            if (cantidades != null) {
                Label[] etiquetas = this.mapaEtiquetas.get(llave);
                etiquetas[0].setText(String.valueOf(cantidades[0]));
                etiquetas[1].setText(String.valueOf(cantidades[1]));
            }
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }
}
