package InterfazGrafica.Elemento;

import Logica.DAO.DAOAcademico;
import Logica.DAO.DAOCuenta;
import Logica.DAO.DAOPais;
import Logica.DAO.DAOUniversidad;
import Logica.Dominio.Academico;
import Logica.Dominio.Cuenta;
import Logica.Dominio.Pais;
import Logica.Dominio.Universidad;
import Logica.ErrorDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
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

    private Cuenta cuentaObtenida;

    private VBox lyInformacionCuenta;

    public VBox getLyInformacionCuenta () {
        return lyInformacionCuenta;
    }

    public void setLyInformacionCuenta (VBox lyInformacionCuenta) {
        this.lyInformacionCuenta = lyInformacionCuenta;
    }

    public void setCuentaObtenida (Cuenta cuentaObtenida) {
        this.cuentaObtenida = cuentaObtenida;
    }

    public Button getBtEvaluar () {
        return btEvaluar;
    }

    public void setBtEvaluar (Button btEvaluar) {
        this.btEvaluar = btEvaluar;
    }

    public Cuenta getCuentaObtenida () {
        return cuentaObtenida;
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

    }

    public Academico getAcademico (int idPersona) {
        Academico academico = null;
        DAOAcademico daoAcademico = new DAOAcademico();
        try {
            Optional academicoOptional = daoAcademico.getAcademicoPorIdPersona(idPersona);
            if (academicoOptional.isPresent()) {
                academico = (Academico) academicoOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Implementar un alert");
        }
        return academico;
    }

    public List<Cuenta> getCuentaPorEstado () {
        List<Cuenta> listaCuenta = null;
        DAOCuenta daoCuenta = new DAOCuenta();
        try {
            listaCuenta = daoCuenta.getCuentasPorTipo(Cuenta.EstadoCuenta.pendiente.toString());

        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Implementar un alert");

        }
        return listaCuenta;
    }

    public Universidad getUniversidadPorId (int idUnivesidad) {
        Universidad universidad = null;
        DAOUniversidad daoUniversidad = new DAOUniversidad();
        try {
            Optional universidadOptional = daoUniversidad.getUniversidadPorId(idUnivesidad);
            if (universidadOptional.isPresent()) {
                universidad = (Universidad) universidadOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Aqui va un alert");
        }
        return universidad;
    }

    public Pais getPaisPorId (int idPais) {
        Pais pais = null;
        DAOPais daoPais = new DAOPais();
        try {
            Optional paisOptional = daoPais.getPaisPorId(idPais);
            if (paisOptional.isPresent()) {
                pais = (Pais) paisOptional.get();
            }
        }
        catch (ErrorDAO errorDAO) {
            System.out.println("Aqui va un alert");
        }
        return pais;
    }

    public void setLabel (Cuenta cuenta) {
        lbUsuario.setText(cuenta.getNombreUsuario());
        Academico academico = getAcademico(cuenta.getIdPersona());
        lbNombre.setText(academico.getNombre());
        lbApellidos.setText(academico.getApellidoPaterno() + " " + academico.getApellidoMaterno());
        lbCorreo.setText(academico.getCorreoElectronico());
        lbCedula.setText(academico.getCedulaProfesional());
        Universidad universidad = getUniversidadPorId(academico.getIdUniversidad());
        lbUniversidad.setText(universidad.getNombre());
        Pais pais = getPaisPorId(universidad.getIdPais());
        lbPais.setText(pais.getNombre());
    }



}
