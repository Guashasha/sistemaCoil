package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DAO.AcademicoDAO;
import DAO.UniversidadAuxiliar;
import DTO.AcademicoDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfiguracionCuentaControlador {
    @FXML
    private TextField tfCategoriaContratacion;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfTelefono;
    @FXML
    private Label lbCategoriaContratacion;
    @FXML
    private Label lbObligatorioAreaEstudios;
    @FXML
    private Label lbObligatorioCategoria;
    @FXML
    private Label lbObligatorioCorreo;
    @FXML
    private Label lbObligatorioTelefono;
    @FXML
    private ComboBox<String> cmbAreaEstudios;
    private BorderPane pnVentanaPrincipal;
    private AcademicoDTO academico;
    private UniversidadDTO universidadAcademico;

    public void setRecursos (BorderPane pnVentanaPrincipal, AcademicoDTO academico) throws ErrorDAO {
        if (pnVentanaPrincipal != null && academico != null) {
            this.pnVentanaPrincipal = pnVentanaPrincipal;
            this.academico = academico;
            llenarComboBoxAreasEstudio();
            autocompletarCampos();
        }
        else {
            throw new ErrorDAO("Error al cargar recursos de la ventana: Configuración de cuenta", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void setUniversidadAcademico (UniversidadDTO universidadAcademico) throws ErrorDAO {
        this.universidadAcademico = universidadAcademico;
        if (!universidadAcademico.getNombre()
                .equals("Universidad Veracruzana")) {
            this.lbCategoriaContratacion.setVisible(false);
            this.tfCategoriaContratacion.setVisible(false);
        }
    }

    @FXML
    private void regresar () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("Cualquier cambio no guardado se perderá");
        alerta.setHeaderText(null);
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                this.pnVentanaPrincipal.setCenter(null);
            }
        });
    }

    @FXML
    private void guardaCambios () {
        if (!camposVacios() && !camposSinCambios()) {
            int filasAfectadas;
            AcademicoDAO academicoDAO = new AcademicoDAO();
            AcademicoDTO academicoEditado = this.academico;

            try {
                academicoEditado.setAreaEstudios(cmbAreaEstudios.getValue());
                academicoEditado.setCategoriaContratacion(tfCategoriaContratacion.getText());
                academicoEditado.setCorreoElectronico(tfCorreo.getText());
                academicoEditado.setNumeroTelefonico(tfTelefono.getText());
                filasAfectadas = academicoDAO.modificar(academicoEditado);
            }
            catch (ErrorDAO error) {
                mostrarMensajeEmergente(error.getMessage(), Alert.AlertType.WARNING);
                filasAfectadas = -1;
            }

            if (filasAfectadas > 0) {
                mostrarMensajeEmergente("Se han guardado los cambios correctamente", Alert.AlertType.INFORMATION);
                this.academico = academicoEditado;
            }
            else if (filasAfectadas == 0) {
                mostrarMensajeEmergente("Ocurrió un error al actualizar la información", Alert.AlertType.ERROR);
            }
        }
        etiquetarCamposVacios();
    }

    private void llenarComboBoxAreasEstudio () {
        List<String> listaAreasEstudio = new ArrayList<>();
        listaAreasEstudio.add("Económico-Administrativo");
        listaAreasEstudio.add("Humanidades");
        listaAreasEstudio.add("Técnica");
        listaAreasEstudio.add("Ciencias de la Salud");
        listaAreasEstudio.add("Biología-Agropecuarias");
        listaAreasEstudio.add("DGRI");
        ObservableList<String> areaEstudioObservable = FXCollections.observableArrayList(listaAreasEstudio);
        this.cmbAreaEstudios.setItems(areaEstudioObservable);
    }

    private void autocompletarCampos () throws ErrorDAO {
        AcademicoAuxiliar academicoAuxiliar = new AcademicoAuxiliar();
        Optional<AcademicoDTO> academicoOptional = academicoAuxiliar.getPorId(this.academico.getIdPersona());
        Optional<UniversidadDTO> universidadOptional = Optional.empty();

        if (academicoOptional.isPresent()) {
            this.academico = academicoOptional.get();
            UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
            universidadOptional = universidadAuxiliar.getUniversidadPorId(this.academico
                    .getIdUniversidad());

            if (universidadOptional.isPresent()) {
                this.cmbAreaEstudios
                        .setValue(this.academico
                                .getAreaEstudios());
                this.tfCategoriaContratacion
                        .setText(this.academico
                                .getCategoriaContratacion());
                this.tfCorreo
                        .setText(this.academico
                                .getCorreoElectronico());
                this.tfTelefono
                        .setText(this.academico
                                .getNumeroTelefonico());
                setUniversidadAcademico(universidadOptional.get());
            }
        }

        if (academicoOptional.isEmpty() || universidadOptional.isEmpty()) {
            throw new ErrorDAO("Error al cargar los recursos de la ventana: Configuración de cuenta", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private boolean camposVacios () {
        boolean vacios;
        String areaEstudios = cmbAreaEstudios.getValue();
        String correo = tfCorreo.getText();
        String telefono = tfTelefono.getText();
        if (this.universidadAcademico
                .getNombre().equals("Universidad Veracruzana")) {
            String categoriaContratacion = tfCategoriaContratacion.getText();
            vacios = areaEstudios == null || categoriaContratacion == null || categoriaContratacion.isBlank() || correo == null || correo.isBlank() || telefono == null || telefono.isBlank();
        }
        else {
            vacios = areaEstudios == null || correo == null || correo.isBlank() || telefono == null || telefono.isBlank();
        }
        return vacios;
    }

    private boolean camposSinCambios () {
        boolean camposSinCambios;
        String areaEstudios = this.cmbAreaEstudios
                .getValue();
        String correo = this.tfCorreo
                .getText().trim();
        String telefono = this.tfTelefono
                .getText().trim();
        if (this.universidadAcademico
                .getNombre().equals("Universidad Veracruzana")) {
            String categoriaContratacion = this.tfCategoriaContratacion
                    .getText().trim();
            camposSinCambios = areaEstudios.equals(this.academico
                    .getAreaEstudios()) && categoriaContratacion.equals(this.academico
                    .getCategoriaContratacion()) && correo.equals(this.academico
                    .getCorreoElectronico()) && telefono.equals(this.academico
                    .getNumeroTelefonico());
        }
        else {
            camposSinCambios = areaEstudios.equals(this.academico
                    .getAreaEstudios()) && correo.equals(this.academico
                    .getCorreoElectronico()) && telefono.equals(this.academico
                    .getNumeroTelefonico());
        }
        return camposSinCambios;
    }

    private void etiquetarCamposVacios () {
        String correo = this.tfCorreo
                .getText();
        String telefono = this.tfTelefono
                .getText();
        this.lbObligatorioAreaEstudios.setVisible(this.cmbAreaEstudios
                .getValue() == null);
        this.lbObligatorioCorreo
                .setVisible(correo == null || correo.isBlank());
        this.lbObligatorioTelefono.setVisible(telefono == null || telefono.isBlank());

        if (this.universidadAcademico
                .getNombre().equals("Universidad Veracruzana")) {
            String categoriaContratacion = this.tfCategoriaContratacion
                    .getText();
            this.lbObligatorioCategoria
                    .setVisible(categoriaContratacion == null || categoriaContratacion.isBlank());
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

}
