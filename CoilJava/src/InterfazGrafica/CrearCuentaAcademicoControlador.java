package InterfazGrafica;

import DAO.AcademicoDAO;
import DAO.CuentaDAO;
import DAO.FacultadDAO;
import DAO.RegionAuxiliar;
import DTO.*;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrearCuentaAcademicoControlador {
    private BorderPane ventanaPrincipal;

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApPaterno;
    @FXML
    private TextField tfApMaterno;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCedulaProfesional;
    @FXML
    private TextField tfNumeroPersonal;
    @FXML
    private ComboBox<String> cbFacultad;
    @FXML
    private ComboBox<String> cbAreaEstudios;
    @FXML
    private ComboBox<String> cbCategoriaContratacion;
    @FXML
    private ComboBox<String> cbRegion;

    public void initialize (BorderPane ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        llenarComboBoxAreasEstudio();
        llenarComboBoxRegion();
        llenarComboBoxCategoriaContratacion();
    }

    @FXML
    private void registrarCuenta () {
        AcademicoDTO academico = leerCamposAcademico();

        if (academico != null) {
            CuentaDTO cuenta = new CuentaDTO();
            try {
                cuenta.setNombreUsuario(academico.getCedulaProfesional());
                cuenta.setContrasena(academico.getNumeroPersonal());
                cuenta.setTipo(CuentaDTO.TipoUsuario.academico);
                cuenta.setEstado(CuentaDTO.EstadoCuenta.aceptada);
            }
            catch (ErrorDAO error) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("Ocurrió un error");
                alerta.setContentText(error.getMessage());
                alerta.showAndWait();
                return;
            }

            AcademicoDAO dao = new AcademicoDAO();

            try {
                dao.agregarAcademicoConCuenta(academico, cuenta);
            } catch (ErrorDAO e) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("Ocurrió un error");
                alerta.setContentText(e.getMessage());
                alerta.showAndWait();
                return;
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Cuenta creada");
            alerta.setContentText("La cuenta se creó exitosamente");
            alerta.showAndWait();
            limpiarCampos();
        }
    }

    void limpiarCampos () {
        tfNombre.setText("");
        tfApMaterno.setText("");
        tfApPaterno.setText("");
        tfCorreo.setText("");
        tfTelefono.setText("");
        tfCedulaProfesional.setText("");
        tfNumeroPersonal.setText("");
    }

    private AcademicoDTO leerCamposAcademico () {
        if (datosInvalidos()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Datos incorrectos");
            alerta.setContentText("Algunos de los datos ingresados son incorrectos, intente de nuevo");
            alerta.showAndWait();

            return null;
        }

        String nombre = tfNombre.getText();
        String aPaterno = tfApPaterno.getText();
        String aMaterno = tfApMaterno.getText();
        String correo = tfCorreo.getText();
        String telefono = tfTelefono.getText();
        String numeroPersonal = tfNumeroPersonal.getText();
        String cedula = tfCedulaProfesional.getText();
        String areaEstudios = cbAreaEstudios.getValue();
        String nombreFacultad = cbFacultad.getValue();
        String categoriaContratacion = cbCategoriaContratacion.getValue();

        AcademicoDAO dao = new AcademicoDAO();

        if (dao.getAcademicoPorCedula(cedula).isPresent()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Cuenta ya existente");
            alerta.setContentText("Los datos ingresados ya pertenecen a una cuenta");
            alerta.showAndWait();
            return null;
        }

        FacultadDAO facultadDAO = new FacultadDAO();
        Optional<FacultadDTO> facultad;

        try {
            facultad = facultadDAO.getFacultadPorNombre(nombreFacultad);

            if (facultad.isEmpty()) {
                return null;
            }
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al cargar los datos");
            alerta.setContentText("No se pudo recuperar la lista de facultades.");
            alerta.showAndWait();
            return null;
        }

        AcademicoDTO persona = new AcademicoDTO();

        try {
            persona.setNombre(nombre);
            persona.setApellidoPaterno(aPaterno);
            persona.setApellidoMaterno(aMaterno);
            persona.setCorreoElectronico(correo);
            persona.setNumeroTelefonico(telefono);
            persona.setNumeroPersonal(numeroPersonal);
            persona.setCedulaProfesional(cedula);
            persona.setAreaEstudios(areaEstudios);
            persona.setIdFacultad(facultad.get().getId());
            persona.setCategoriaContratacion(categoriaContratacion);
            persona.setIdUniversidad(1);
        }
        catch (ErrorDAO error) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Datos incorrectos");
            alerta.setContentText(error.getMessage());
            alerta.showAndWait();

            return null;
        }

        return persona;
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
        this.cbAreaEstudios.setItems(areaEstudioObservable);
    }

    @FXML
    private void llenarComboBoxFacultades () {
        FacultadDAO dao = new FacultadDAO();
        this.cbFacultad.setItems(null);
        ArrayList<String> nombresFacultades = new ArrayList<>();

        try {
            List<FacultadDTO> facultades = dao.getFacultadPorRegion(this.cbRegion.getValue());

            for (FacultadDTO facultad : facultades) {
                nombresFacultades.add(facultad.getNombre());
            }
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al cargar los datos");
            alerta.setContentText("No se pudo recuperar la lista de facultades.");
            alerta.showAndWait();
        }

        this.cbFacultad.setItems(FXCollections.observableArrayList(nombresFacultades));
    }

    private void llenarComboBoxRegion () {
        RegionAuxiliar dao = new RegionAuxiliar();
        ArrayList<String> regiones = new ArrayList<>();

        try {
            List<RegionDTO> listaRegiones = dao.getTodasAlfabeticamente();

            for (RegionDTO region : listaRegiones) {
                regiones.add(region.getNombre());
            }
        } catch (ErrorDAO e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al cargar los datos");
            alerta.setContentText("No se pudo recuperar la lista de regiones.");
            alerta.showAndWait();
        }

        this.cbRegion.setItems(FXCollections.observableArrayList(regiones));
    }

    private void llenarComboBoxCategoriaContratacion () {
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("planta");
        categorias.add("interino por plaza");
        categorias.add("interino por persona");
        categorias.add("interino por tiempo determinado");
        categorias.add("interino por obra determinada");
        categorias.add("interino por falta de grado");
        categorias.add("suplente");
        categorias.add("trabajos especificos");
        categorias.add("interino por plaza con plaza");
        categorias.add("interino por persona con plaza");
        categorias.add("suplente con plaza");
        categorias.add("eventual");
        categorias.add("beca trabajo");
        categorias.add("apoyo");
        categorias.add("beca subsidio");
        categorias.add("beca posgrado");
        categorias.add("beca sistema nacional de investigación");
        categorias.add("beca profesional");

        this.cbCategoriaContratacion.setItems(FXCollections.observableArrayList(categorias));
    }

    private boolean datosInvalidos() {
        String nombre = tfNombre.getText();
        String aPaterno = tfApPaterno.getText();
        String aMaterno = tfApMaterno.getText();
        String correo = tfCorreo.getText();
        String telefono = tfTelefono.getText();
        String numeroPersonal = tfNumeroPersonal.getText();
        String cedula = tfCedulaProfesional.getText();
        String areaEstudios = cbAreaEstudios.getValue();
        String facultad = cbFacultad.getValue();
        String categoriaContratacion = cbCategoriaContratacion.getValue();

        return nombre.isBlank() || aPaterno.isBlank() || aMaterno.isBlank() || correo.isBlank()
                || telefono.isBlank() || numeroPersonal.isBlank() || cedula.isBlank()
                || areaEstudios == null || areaEstudios.isBlank() ||
                facultad.isBlank() || categoriaContratacion.isBlank();
    }
}
