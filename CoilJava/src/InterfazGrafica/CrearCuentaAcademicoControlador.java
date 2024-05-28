package InterfazGrafica;

import DAO.AcademicoDAO;
import DAO.CuentaDAO;
import DAO.FacultadDAO;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import DTO.FacultadDTO;
import DTO.PersonaDTO;
import Utilidades.ErrorDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrearCuentaAcademicoControlador {
    private BorderPane ventanaPrincipal;

    @FXML
    private Label tfNombre;
    @FXML
    private Label tfApPaterno;
    @FXML
    private Label tfApMaterno;
    @FXML
    private Label tfCorreo;
    @FXML
    private Label tfTelefono;
    @FXML
    private Label tfCedulaProfesional;
    @FXML
    private Label tfNumeroPersonal;
    @FXML
    private ComboBox<FacultadDTO> cbFacultad;
    @FXML
    private ComboBox<String> cbAreaEstudios;
    @FXML
    private ComboBox<String> cbCategoriaContratacion;

    public void initialize (BorderPane ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        llenarComboBoxAreasEstudio();
        llenarComboBoxFacultades();
    }

    @FXML
    private void registrarCuenta () {
        AcademicoDTO academico = leerCamposAcademico();

        if (academico != null) {
            CuentaDTO cuenta = new CuentaDTO();
            cuenta.setNombreUsuario(academico.getCedulaProfesional());
            cuenta.setContrasena(academico.getNumeroPersonal());
            cuenta.setTipo(CuentaDTO.TipoUsuario.academico);
            cuenta.setEstado(CuentaDTO.EstadoCuenta.aceptada);

            AcademicoDAO dao = new AcademicoDAO();

            try {
                dao.agregarAcademicoConCuenta(academico, cuenta);
            } catch (ErrorDAO e) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setHeaderText("Ocurrió un error");
                alerta.setContentText(e.getMessage());
                alerta.showAndWait();
            }
        }
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
        FacultadDTO facultad = cbFacultad.getValue();
        String categoriaContratacion = cbCategoriaContratacion.getValue();


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
            persona.setIdFacultad(facultad.getId());
            persona.setCategoriaContratacion(categoriaContratacion);
        }
        catch (ErrorDAO error) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Datos incorrectos");
            alerta.setContentText("Algunos de los datos ingresados son incorrectos, intente de nuevo");
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

    private void llenarComboBoxFacultades () {
        FacultadDAO dao = new FacultadDAO();

        try {
            List<FacultadDTO> facultades = dao.getTodasAlfabeticamente();
            this.cbFacultad.setItems((ObservableList<FacultadDTO>) facultades);
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al cargar los datos");
            alerta.setContentText("No se pudo recuperar la lista de facultades.");
            alerta.showAndWait();
        }
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
        FacultadDTO facultad = cbFacultad.getValue();
        String categoriaContratacion = cbCategoriaContratacion.getValue();

        return nombre.isBlank() || aPaterno.isBlank() || aMaterno.isBlank() || correo.isBlank()
                || telefono.isBlank() || numeroPersonal.isBlank() || cedula.isBlank()
                || areaEstudios == null || areaEstudios.isBlank() ||
                facultad == null || categoriaContratacion == null || categoriaContratacion.isBlank();
    }
}
