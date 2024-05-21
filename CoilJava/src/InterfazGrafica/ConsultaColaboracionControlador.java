package InterfazGrafica;

import DAO.ColaboracionAuxiliar;
import DTO.AcademicoDTO;
import DTO.ColaboracionDTO;
import InterfazGrafica.Items.ColaboracionDisponibleItemControlador;
import Utilidades.ErrorDAO;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.stream.Collectors;

public class ConsultaColaboracionControlador extends Application implements Initializable {
    private static final Logger BITACORA = Logger.getLogger(ConsultaColaboracionControlador.class);
    private AcademicoDTO academicoDTO;

    @FXML
    private GridPane gpContenedorColaboraciones;

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
        List<ColaboracionDTO> colaboraciones = colaboracionAuxiliar.obtenerColaboracionDisponible(academicoDTO.getCedulaProfesional());
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
        gpContenedorColaboraciones.getChildren()
                                  .clear();

        ArrayList<ColaboracionDTO> arrayListColaboracion;
        try {
            arrayListColaboracion = (ArrayList<ColaboracionDTO>) getColaboracionesDisponibles();
            if (!arrayListColaboracion.isEmpty()) {
                int filas = 0;
                int columnas = 0;
                for (ColaboracionDTO colaboracionDTO : arrayListColaboracion) {
                    agregarColaboracionItem(colaboracionDTO, filas, columnas);
                    columnas++;
                    if (columnas == 3) {
                        columnas = 0;
                        filas++;
                    }
                }
            }
        }
        catch (ErrorDAO error) {
            mostrarAlert(error.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void agregarColaboracionItem (ColaboracionDTO colaboracionDTO, int filas, int columnas) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../Items/ColaboracionDisponibleItem.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            ColaboracionDisponibleItemControlador controlador = fxmlLoader.getController();
            controlador.setColaboracionDTO(colaboracionDTO);
            controlador.setAcademicoDTO(academicoDTO);
            gpContenedorColaboraciones.add(pane, columnas, filas);
            GridPane.setMargin(pane, new Insets(10));
            controlador.inicializarLabel();
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al agregar las colaboraciones", ErrorDAO.Tipo.INSERCION);
        }
    }

    private void mostrarAlert (String mensaje, Alert.AlertType tipoAlerta) {
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

    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../InterfazGrafica/ConsultaColaboracion.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    public void cargarItemsColaboracionPorBusqueda () {
        tfBusqueda.textProperty()
                  .addListener((observable, oldValue, newValue) -> {
                      cargarColaboracionItem();
                  });
    }



    public static void main (String[] args) {
        launch(args);
    }
}
