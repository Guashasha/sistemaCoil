package InterfazGrafica.Items;

import DAO.PaisAuxiliar;
import DAO.UniversidadAuxiliar;
import DTO.ColaboracionDTO;
import DTO.PaisDTO;
import DTO.UniversidadDTO;
import Utilidades.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PropuestaItemControlador implements Initializable {
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnRechazar;
    @FXML
    private Label lbApellidos;
    @FXML
    private Label lbNombre;
    @FXML
    private TextArea taTemaInteres;
    @FXML
    private Label lbUniversidad;
    @FXML
    private TextArea taObjetivo;
    @FXML
    private Label lbPais;

    private ColaboracionDTO colaboracionDTO;

    public ColaboracionDTO getColaboracionDTO () {
        return colaboracionDTO;
    }

    public Button getBtnAceptar () {
        return btnAceptar;
    }

    public Button getBtnRechazar () {
        return btnRechazar;
    }

    public void setColaboracionDTO (ColaboracionDTO colaboracionDTO) {
        this.colaboracionDTO = colaboracionDTO;
    }


    public void inicializarLabels () {
        taTemaInteres.setText(colaboracionDTO.getTemaInteres());
        taObjetivo.setText(colaboracionDTO.getObjetivo());
        taObjetivo.setEditable(false);
        lbNombre.setText(colaboracionDTO.getAnfitrion()
                                        .getNombre());
        lbApellidos.setText(colaboracionDTO.getAnfitrion()
                                           .getApellidos() + " " + colaboracionDTO.getAnfitrion());
        UniversidadDTO universidadDTO = getUniversidad();
        lbUniversidad.setText(universidadDTO.getNombre());
        lbPais.setText(getPais(universidadDTO.getIdPais()).getIso());
    }

    private UniversidadDTO getUniversidad () {
        UniversidadAuxiliar universidadAuxiliar = new UniversidadAuxiliar();
        Optional<UniversidadDTO> universidadDTOOptional = universidadAuxiliar.getUniversidadPorId(colaboracionDTO.getAnfitrion()
                                                                                                                 .getIdUniversidad());
        if (universidadDTOOptional.isPresent()) {
            return universidadDTOOptional.get();
        }
        else {
            throw new ErrorDAO("No se encuentro la universidad", ErrorDAO.Tipo.CONSULTA);
        }
    }

    private PaisDTO getPais (int id) {
        PaisAuxiliar paisAuxiliar = new PaisAuxiliar();
        Optional<PaisDTO> paisDTOOptional = paisAuxiliar.getPaisPorId(id);
        if (paisDTOOptional.isPresent()) {
            return paisDTOOptional.get();
        }
        else {
            throw new ErrorDAO("No se encontro el pais", ErrorDAO.Tipo.CONSULTA);
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        if (colaboracionDTO != null) {
            inicializarLabels();
        }
    }
}
