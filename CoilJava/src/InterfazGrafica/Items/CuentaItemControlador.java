package InterfazGrafica.Items;

import DAO.*;
import DTO.AcademicoDTO;
import DTO.CuentaDTO;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CuentaItemControlador implements Initializable {
    @FXML
    private Button btEvaluar;

    @FXML
    private Label lbApellidos;

    @FXML
    private Label lbCedula;

    @FXML
    private Label lbCorreo;

    @FXML
    private Label lbNombre;

    @FXML
    private Label lbPais;

    @FXML
    private Label lbUniversidad;

    @FXML
    private Label lbUsuario;

    private CuentaDTO cuentaDTOObtenida;
    private AcademicoDTO academicoDTO;

    public void setCuentaObtenida (CuentaDTO cuentaDTOObtenida) {
        this.cuentaDTOObtenida = cuentaDTOObtenida;
    }

    public Button getBtEvaluar () {
        return btEvaluar;
    }

    public CuentaDTO getCuentaObtenida () {
        return cuentaDTOObtenida;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        initializeLabels();
    }

    private void initializeLabels() {
        if (cuentaDTOObtenida != null) {
            setLabel();
        }
    }

    public Label getLbUniversidad () {
        return lbUniversidad;
    }


    public AcademicoDTO getAcademico (int idPersona) {
        AcademicoDTO academicoDTO = null;
        AcademicoAuxiliar academicoAuxiliar = new AcademicoAuxiliar();
        try {
            Optional academicoOptional = academicoAuxiliar.getPorId(idPersona);
            if (academicoOptional.isPresent()) {
                academicoDTO = (AcademicoDTO) academicoOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Implementar un alert");
        }
        this.academicoDTO = academicoDTO;
        return academicoDTO;
    }

    public UniversidadDTO getUniversidadPorId (int idUnivesidad) {
        UniversidadDTO universidadDTO = null;
        UniversidadDAO universidadDAO = new UniversidadDAO();
        try {
            Optional universidadOptional = universidadDAO.getUniversidadPorId(idUnivesidad);
            if (universidadOptional.isPresent()) {
                universidadDTO = (UniversidadDTO) universidadOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Aqui va un alert");
        }
        return universidadDTO;
    }

    public PaisDTO getPaisPorId (int idPais) {
        PaisDTO paisDTO = null;
        PaisDAO paisDAO = new PaisDAO();
        try {
            Optional paisOptional = paisDAO.getPaisPorId(idPais);
            if (paisOptional.isPresent()) {
                paisDTO = (PaisDTO) paisOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Aqui va un alert");
        }
        return paisDTO;
    }

    public void setLabel () {
        lbUsuario.setText(cuentaDTOObtenida.getNombreUsuario());
        setToolTip(lbUsuario);
        AcademicoDTO academicoDTO = getAcademico(cuentaDTOObtenida.getIdPersona());
        lbNombre.setText(academicoDTO.getNombre());
        setToolTip(lbNombre);
        lbApellidos.setText(academicoDTO.getApellidos());
        setToolTip(lbApellidos);
        lbCorreo.setText(academicoDTO.getCorreoElectronico());
        setToolTip(lbCorreo);
        lbCedula.setText(academicoDTO.getCedulaProfesional());
        setToolTip(lbCedula);
        UniversidadDTO universidadDTO = getUniversidadPorId(academicoDTO.getIdUniversidad());
        lbUniversidad.setText(universidadDTO.getNombre());
        setToolTip(lbUniversidad);
        PaisDTO paisDTO = getPaisPorId(universidadDTO.getIdPais());
        lbPais.setText(paisDTO.getNombre());
        setToolTip(lbPais);
    }

    private void setToolTip (Label label) {
        Tooltip tooltip = new Tooltip(label.getText());
        Tooltip.install(label, tooltip);
    }

}
