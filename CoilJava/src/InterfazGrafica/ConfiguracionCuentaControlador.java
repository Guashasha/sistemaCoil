package InterfazGrafica;

import DAO.AcademicoAuxiliar;
import DAO.AcademicoDAO;
import DAO.UniversidadDAO;
import DTO.AcademicoDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfiguracionCuentaControlador {
    @FXML
    private ComboBox<String> cbCategoriaContratacion;
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
    private UniversidadDTO universidadDelAcademico;

    public void setRecursos (BorderPane pnVentanaPrincipal, AcademicoDTO academico) throws ErrorDAO {
        if (pnVentanaPrincipal != null && academico != null) {
            this.pnVentanaPrincipal = pnVentanaPrincipal;
            this.academico = academico;
            llenarComboBoxAreasEstudio();
            llenarComboBoxCategoriaContratacion();
            autocompletarCampos();
        }
        else {
            throw new ErrorDAO("Error al cargar recursos de la ventana: Configuración de cuenta", ErrorDAO.Tipo.VALIDACION);
        }
    }

    private void setUniversidadDelAcademico (UniversidadDTO universidadDelAcademico) throws ErrorDAO {
        this.universidadDelAcademico = universidadDelAcademico;
        if (!universidadDelAcademico.getNombre()
                                    .equals("Universidad Veracruzana")) {
            this.lbCategoriaContratacion.setVisible(false);
            this.cbCategoriaContratacion.setVisible(false);
        }
    }

    @FXML
    private void regresar () {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setContentText("Cualquier cambio no guardado se perderá");
        alerta.setHeaderText(null);
        alerta.showAndWait()
              .ifPresent(response -> {
                  if (response == ButtonType.OK) {
                      this.pnVentanaPrincipal.setCenter(null);
                  }
              });
    }

    @FXML
    private void guardaCambios () {
        if (!camposVacios() && camposActualizados()) {
            int filasAfectadas;
            AcademicoDTO academicoEditado = new AcademicoDTO();
            academicoEditado.setIdPersona(this.academico.getIdPersona());
            academicoEditado.setNombre(this.academico.getNombre());
            academicoEditado.setApellidos(this.academico.getApellidos());
            academicoEditado.setIdUniversidad(this.academico.getIdUniversidad());
            academicoEditado.setCedulaProfesional(this.academico.getCedulaProfesional());
            academicoEditado.setNumeroPersonal(this.academico.getNumeroPersonal());
            academicoEditado.setIdFacultad(this.academico.getIdFacultad());

            try {
                academicoEditado.setAreaEstudios(this.cmbAreaEstudios.getValue());
                academicoEditado.setCategoriaContratacion(this.cbCategoriaContratacion.getValue());
                academicoEditado.setNumeroTelefonico(this.tfTelefono.getText());
                filasAfectadas = actualizarDatosAcademico(academicoEditado);
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

    private int actualizarDatosAcademico (AcademicoDTO academicoEditado) throws ErrorDAO {
        int filasAfectadas;
        if (correoActualizado()) {
            AcademicoAuxiliar academicoAuxiliar = new AcademicoAuxiliar();
            academicoEditado.setCorreoElectronico(this.tfCorreo.getText());
            filasAfectadas = academicoAuxiliar.modificar(academicoEditado);
        }
        else {
            AcademicoDAO academicoDAO = new AcademicoDAO();
            academicoEditado.setCorreoElectronico(this.academico.getCorreoElectronico());
            filasAfectadas = academicoDAO.modificar(academicoEditado);
        }
        return filasAfectadas;
    }

    private void llenarComboBoxAreasEstudio () {
        List<String> listaAreasEstudio = new ArrayList<>();
        listaAreasEstudio.add("Económico-Administrativo");
        listaAreasEstudio.add("Humanidades");
        listaAreasEstudio.add("Técnica");
        listaAreasEstudio.add("Ciencias de la Salud");
        listaAreasEstudio.add("Biología-Agropecuarias");
        listaAreasEstudio.add("DGRI");
        this.cmbAreaEstudios.setItems(FXCollections.observableArrayList(listaAreasEstudio));
    }

    private void autocompletarCampos () throws ErrorDAO {
        AcademicoAuxiliar academicoAuxiliar = new AcademicoAuxiliar();
        Optional<AcademicoDTO> academico = academicoAuxiliar.getPorId(this.academico.getIdPersona());
        Optional<UniversidadDTO> universidadDelAcademico = Optional.empty();

        if (academico.isPresent()) {
            this.academico = academico.get();
            UniversidadDAO universidadDAO = new UniversidadDAO();
            universidadDelAcademico = universidadDAO.getUniversidadPorId(this.academico.getIdUniversidad());

            if (universidadDelAcademico.isPresent()) {
                this.cmbAreaEstudios.setValue(this.academico.getAreaEstudios());
                this.cbCategoriaContratacion.setValue(this.academico.getCategoriaContratacion());
                this.tfCorreo.setText(this.academico.getCorreoElectronico());
                this.tfTelefono.setText(this.academico.getNumeroTelefonico());
                setUniversidadDelAcademico(universidadDelAcademico.get());
            }
        }

        if (academico.isEmpty() || universidadDelAcademico.isEmpty()) {
            throw new ErrorDAO("Error al cargar los recursos de la ventana: Configuración de cuenta", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private boolean camposVacios () {
        boolean camposVacios;
        String areaEstudios = cmbAreaEstudios.getValue();
        String correo = tfCorreo.getText();
        String telefono = tfTelefono.getText();
        if (this.universidadDelAcademico.getNombre()
                                        .equals("Universidad Veracruzana")) {
            String categoriaContratacion = cbCategoriaContratacion.getValue();
            camposVacios = areaEstudios == null || categoriaContratacion == null || correo == null || correo.isBlank() || telefono == null || telefono.isBlank();
        }
        else {
            camposVacios = areaEstudios == null || correo == null || correo.isBlank() || telefono == null || telefono.isBlank();
        }
        return camposVacios;
    }

    private boolean camposActualizados () {
        boolean camposActualizados;
        String areaEstudios = this.cmbAreaEstudios.getValue();
        String correo = this.tfCorreo.getText()
                                     .trim();
        String telefono = this.tfTelefono.getText()
                                         .trim();
        if (this.universidadDelAcademico.getNombre()
                                        .equals("Universidad Veracruzana")) {
            String categoriaContratacion = this.cbCategoriaContratacion.getValue();
            camposActualizados = !areaEstudios.equals(this.academico.getAreaEstudios()) || !categoriaContratacion.equals(this.academico.getCategoriaContratacion()) || !correo.equals(this.academico.getCorreoElectronico()) || !telefono.equals(this.academico.getNumeroTelefonico());
        }
        else {
            camposActualizados = !areaEstudios.equals(this.academico.getAreaEstudios()) || !correo.equals(this.academico.getCorreoElectronico()) || !telefono.equals(this.academico.getNumeroTelefonico());
        }
        return camposActualizados;
    }

    private boolean correoActualizado () {
        String correo = this.tfCorreo.getText()
                                     .trim();
        return !correo.equals(this.academico.getCorreoElectronico());
    }

    private void etiquetarCamposVacios () {
        String correo = this.tfCorreo.getText();
        String telefono = this.tfTelefono.getText();
        this.lbObligatorioAreaEstudios.setVisible(this.cmbAreaEstudios.getValue() == null);
        this.lbObligatorioCorreo.setVisible(correo == null || correo.isBlank());
        this.lbObligatorioTelefono.setVisible(telefono == null || telefono.isBlank());

        if (this.universidadDelAcademico.getNombre()
                                        .equals("Universidad Veracruzana")) {
            String categoriaContratacion = this.cbCategoriaContratacion.getValue();
            this.lbObligatorioCategoria.setVisible(categoriaContratacion == null || categoriaContratacion.isBlank());
        }
    }

    private void mostrarMensajeEmergente (String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setContentText(mensaje);
        alerta.setHeaderText(null);
        alerta.show();
    }

    private void llenarComboBoxCategoriaContratacion () {
        ArrayList<String> categoriasContratacion = new ArrayList<>();
        categoriasContratacion.add("Planta");
        categoriasContratacion.add("Interino por plaza");
        categoriasContratacion.add("Interino por persona");
        categoriasContratacion.add("Interino por tiempo determinado");
        categoriasContratacion.add("Interino por obra determinada");
        categoriasContratacion.add("Interino por falta de grado");
        categoriasContratacion.add("suplente");
        categoriasContratacion.add("Trabajos específicos");
        categoriasContratacion.add("Interino por plaza con plaza");
        categoriasContratacion.add("Interino por persona con plaza");
        categoriasContratacion.add("Suplente con plaza");
        categoriasContratacion.add("Eventual");
        categoriasContratacion.add("Beca trabajo");
        categoriasContratacion.add("Apoyo");
        categoriasContratacion.add("Beca subsidio");
        categoriasContratacion.add("Beca posgrado");
        categoriasContratacion.add("Beca sistema nacional de investigación");
        categoriasContratacion.add("Beca profesional");

        this.cbCategoriaContratacion.setItems(FXCollections.observableArrayList(categoriasContratacion));
    }

}
