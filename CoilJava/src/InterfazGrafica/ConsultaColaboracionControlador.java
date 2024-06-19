package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import InterfazGrafica.Items.ColaboracionDisponibleItemControlador;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.stream.Collectors;

public class ConsultaColaboracionControlador implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(ConsultaColaboracionControlador.class);
    private AcademicoDTO academicoDTO;

    @FXML
    private GridPane pnContenedorColaboraciones;

    @FXML
    private TextField tfBusqueda;
    private BorderPane pnVentanaPrincipal;
    private Stack<Pane> historialPaneles = new Stack<>();

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        if (academicoDTO != null) {
            cargarColaboracionItem();

        }
    }

    private List<ColaboracionDTO> getColaboracionesDisponibles () {
        ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();
        List<ColaboracionDTO> colaboraciones = colaboracionAuxiliar.getColaboracionDisponible(academicoDTO.getCedulaProfesional(), academicoDTO.getIdUniversidad());
        String textoBusqueda = tfBusqueda.getText()
                                         .trim()
                                         .toLowerCase();
        if (!textoBusqueda.isEmpty()) {
            colaboraciones = colaboraciones.stream()
                                           .filter(colaboracion -> colaboracion.getTemaInteres()
                                                                               .toLowerCase()
                                                                               .contains(textoBusqueda))
                                           .collect(Collectors.toList());
        }
        return colaboraciones;
    }

    public void cargarColaboracionItem () {
        pnContenedorColaboraciones.getChildren()
                                  .clear();

        ArrayList<ColaboracionDTO> arrayListColaboracion;
        try {
            arrayListColaboracion = (ArrayList<ColaboracionDTO>) getColaboracionesDisponibles();
            if (!arrayListColaboracion.isEmpty()) {
                int filas = 1;
                int columnas = 0;
                for (ColaboracionDTO colaboracionDTO : arrayListColaboracion) {
                    agregarColaboracionItem(colaboracionDTO, filas, columnas);
                    columnas++;
                    if (columnas == 2) {
                        columnas = 0;
                        filas++;
                    }
                }
            }
        }
        catch (ErrorDAO error) {
            mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void agregarColaboracionItem (ColaboracionDTO colaboracionDTO, int filas, int columnas) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../InterfazGrafica/Items/ColaboracionDisponibleItem.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            ColaboracionDisponibleItemControlador controlador = fxmlLoader.getController();
            controlador.setColaboracionDTO(colaboracionDTO);
            controlador.setAcademicoDTO(academicoDTO);
            pnContenedorColaboraciones.add(pane, columnas++, filas);
            GridPane.setMargin(pane, new Insets(10));
            controlador.inicializarLabel();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al agregar las colaboraciones", ErrorDAO.Tipo.INSERCION);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setContentText(mensaje);
        alert.setHeaderText("Informacion");
        alert.showAndWait();
    }

    public AcademicoDTO getAcademicoDTO () {
        return academicoDTO;
    }

    public void setAcademicoDTO (AcademicoDTO academicoDTO) {
        this.academicoDTO = academicoDTO;
    }

    public void setPnVentanaPrincipal (BorderPane pnVentanaPrincipal) {
        this.pnVentanaPrincipal = pnVentanaPrincipal;
    }

    public void setHistorialPaneles (Stack<Pane> historialPaneles) {
        this.historialPaneles = historialPaneles;
    }


    public void cargarItemsColaboracionPorBusqueda () {
        tfBusqueda.textProperty()
                  .addListener((observable, oldValue, newValue) -> {
                      cargarColaboracionItem();
                  });
    }

}



