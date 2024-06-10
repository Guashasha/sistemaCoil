package InterfazGrafica;

import DAO.ColaboracionDAO;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import InterfazGrafica.Items.SolicitudAcademicoItemControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SolicitudesAColaboracionControlador {
    private static final Logger BITACORA = Logger.getLogger(SolicitudAcademicoItemControlador.class);
    private Pane panelAnterior;
    private ColaboracionDTO colaboracionDTO;
    private AcademicoDTO academicoDTO;

    @FXML
    private TextField tfBusqueda;

    @FXML
    private VBox vbContenedor;

    private ColaboracionDAO colaboracionDAO;
    public BorderPane panelVentanaPrincial;
    private Stack<Pane> historialPaneles = new Stack<>();


    public SolicitudesAColaboracionControlador() {
        this.colaboracionDAO = new ColaboracionDAO();
    }

    private List<AcademicoDTO> getAcademicosSolicitantes() throws ErrorDAO {
        List<AcademicoDTO> academicos = colaboracionDAO.getSolicitudAcademicoColaboracion(this.colaboracionDTO.getIdColaboracion());

        String textoBusqueda = tfBusqueda.getText().trim().toLowerCase();

        if (!textoBusqueda.isEmpty()) {
            academicos = academicos.stream()
                                   .filter(academico -> academico.getNombre().toLowerCase().contains(textoBusqueda))
                                   .collect(Collectors.toList());
        }

        return academicos;
    }

    private void agregarAcademicosItem(AcademicoDTO academicoDTO) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../InterfazGrafica/Items/SolicitudAcademicoItem.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            SolicitudAcademicoItemControlador controlador = fxmlLoader.getController();
            controlador.setAcademicoDTO(academicoDTO);
            controlador.setColaboracionDTO(this.colaboracionDTO);
            controlador.setLabel();
            controlador.panelVentanaPrincipal = this.panelVentanaPrincial;
            controlador.setSolicitudesAColaboracionControlador(this);
            controlador.setVbContenedor(this.vbContenedor);
            vbContenedor.getChildren().add(anchorPane);

        } catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            mostrarAlert("Error al cargar las solicitudes de los academicos", Alert.AlertType.ERROR);
        }
    }

    public void cargarAcademicosItem() {
        vbContenedor.getChildren().clear();
        try {
            List<AcademicoDTO> academicos = getAcademicosSolicitantes();
            academicos.forEach(this::agregarAcademicosItem);
        } catch (ErrorDAO errorDAO) {
            mostrarAlert(errorDAO.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void cargarAcademicosItemPorBusqueda() {
        tfBusqueda.textProperty().addListener((observable, oldValue, newValue) -> cargarAcademicosItem());
    }

    private void mostrarAlert(String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    public void setColaboracionDTO(ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }
    public void setAcademicoDTO(AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }
    public void setPaneles (Pane panelAnterior, BorderPane panelVentanaPrincial) {
        this.panelAnterior = panelAnterior;
        this.panelVentanaPrincial = panelVentanaPrincial;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }

    public void regresar() {
        this.panelVentanaPrincial.setCenter(this.historialPaneles.pop());
    }
}
