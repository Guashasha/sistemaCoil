package InterfazGrafica;

import DAO.PaisAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Stack;

public class EditarUniversidadControlador {
    private UniversidadDTO universidadActual;
    private PaisDTO paisActual;
    @FXML
    private Label txtObligatorioNombre;
    @FXML
    private Label txtObligatorioPais;
    @FXML
    private TextField tfNombre;
    @FXML
    private ComboBox<String> cmbPaises;
    private Stack<Pane> historialPaneles;
    private BorderPane pnVentanaPrincipal;
    private ConsultaUniversidadesControlador consultaUniversidadesControlador;

    public void setRecursos (BorderPane pnVentanaPrincipal, Stack<Pane> historialPaneles, UniversidadDTO universidadActual, PaisDTO paisActual, ConsultaUniversidadesControlador consultaUniversidadesControlador) throws ErrorDAO {
        if (pnVentanaPrincipal != null && historialPaneles != null && universidadActual != null && paisActual != null && consultaUniversidadesControlador != null) {
            this.pnVentanaPrincipal = pnVentanaPrincipal;
            this.historialPaneles = historialPaneles;
            this.universidadActual = universidadActual;
            this.paisActual = paisActual;
            this.consultaUniversidadesControlador = consultaUniversidadesControlador;
            llenarComboBoxPaises();
            autocompletarCampos();
        }
        else {
            throw new ErrorDAO("Algo salió mal, reinicie la aplicación y si el problema persiste, contacte con soporte técnico", ErrorDAO.Tipo.VALIDACION);
        }
    }

    @FXML
    private void editarUniversidad () {
        if (!objetosValidos()) {
            mostrarMensajeEmergente("Algo salió mal. Vuelva a intentarlo más tarde", Alert.AlertType.ERROR);
        }
        else if (!camposVacios() && !camposSinCambios()) {
            UniversidadDTO universidadDTO = new UniversidadDTO(tfNombre.getText());
            PaisDTO paisDTO = new PaisDTO(cmbPaises.getValue());
            int filasAfectadas;
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();

            try {
                filasAfectadas = universidadAuxiliar.editarUniversidad(this.universidadActual, universidadDTO, paisDTO);
            }
            catch (ErrorDAO error) {
                Alert.AlertType tipoAlerta = Alert.AlertType.WARNING;
                if (error.getTipo() == ErrorDAO.Tipo.CONEXION) {
                    tipoAlerta = Alert.AlertType.ERROR;
                }
                mostrarMensajeEmergente(error.getMessage(), tipoAlerta);
                filasAfectadas = -1;
            }

            if (filasAfectadas > 0) {
                this.universidadActual.setNombre(universidadDTO.getNombre());
                this.paisActual.setNombre(paisDTO.getNombre());
                mostrarMensajeEmergente("Se han guardado los cambios exitosamente", Alert.AlertType.INFORMATION);
            }
            else if (filasAfectadas == 0) {
                mostrarMensajeEmergente("Algo salió mal. Intentelo de nuevo más tarde", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void limitarCaracteres () {
        int longitud = tfNombre.getLength();
        if (longitud > UniversidadDTO.LONGITUD_NOMBRE) {
            tfNombre.setText(tfNombre.getText()
                                     .substring(0, UniversidadDTO.LONGITUD_NOMBRE));
            tfNombre.positionCaret(tfNombre.getLength());
        }
    }

    @FXML
    private void cancelarEdicion () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("No se guardarán los cambios");
        alerta.setHeaderText(null);
        alerta.showAndWait()
              .ifPresent(response -> {
                  if (response == ButtonType.OK) {
                      this.pnVentanaPrincipal.setCenter(this.historialPaneles.pop());
                      this.consultaUniversidadesControlador.cargarConsultaGeneral();
                  }
              });
    }

    private boolean objetosValidos () {
        return this.universidadActual != null && this.paisActual != null;
    }

    private void llenarComboBoxPaises () throws ErrorDAO {
        PaisAuxiliar paisAuxiliar = new PaisAuxiliar();
        List<String> listaPaises = paisAuxiliar.getNombresPaisesAlfabeticamente();

        if (!listaPaises.isEmpty()) {
            ObservableList<String> paisesObservable = FXCollections.observableArrayList(listaPaises);
            this.cmbPaises.setItems(paisesObservable);
        }
        else {
            throw new ErrorDAO("No existen los recursos suficientes en la base de datos. Contacte a soporte técnico", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    private boolean camposVacios () {
        String nombre = tfNombre.getText();
        boolean nombreVacio = nombre == null || nombre.isBlank();
        boolean paisVacio = cmbPaises.getValue() == null;
        etiquetarCamposVacios(nombreVacio, paisVacio);
        return nombreVacio || paisVacio;
    }

    private void etiquetarCamposVacios (boolean nombreVacio, boolean paisVacio) {
        txtObligatorioNombre.setVisible(nombreVacio);
        txtObligatorioPais.setVisible(paisVacio);
    }

    private boolean camposSinCambios () {
        String nuevoNombre = tfNombre.getText()
                                     .trim();
        String[] nuevoNombreUniversidadSeparado = nuevoNombre.split("\\s+");
        String[] nombreUniversidadSeparado = this.universidadActual.getNombre().split("\\s+");
        String nuevoNombreUniversidad = String.join(" ",nuevoNombreUniversidadSeparado);
        String nombreUniversidad = String.join(" ",nombreUniversidadSeparado);
        String nuevoPais = cmbPaises.getValue();
        return nombreUniversidad.equals(nuevoNombreUniversidad) && nuevoPais.equals(this.paisActual.getNombre());
    }

    private void autocompletarCampos () {
        this.tfNombre.setText(this.universidadActual.getNombre());
        this.cmbPaises.setValue(this.paisActual.getNombre());
    }
}
