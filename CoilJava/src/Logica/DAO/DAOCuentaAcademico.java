package Logica.DAO;

import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;
import Logica.Interfaces.ICuentaAcademicoDAO;

import java.util.List;
import java.util.Optional;

public class DAOCuentaAcademico implements ICuentaAcademicoDAO {
    @Override
    public Optional<CuentaAcademico> getCuentaPorCedulaProfesional (String cedulaProfesional) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public Optional<CuentaAcademico> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public int actualizarInformacionCuenta (CuentaAcademico cuenta) throws ErrorDAO {
        return 0;
    }

    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        return false;
    }

    @Override
    public int cambiarContrasena (String nombreUsuario, String nuevaContrasena) throws ErrorDAO {
        return 0;
    }

    @Override
    public int cambiarEstadoCuenta (CuentaAcademico cuenta) throws ErrorDAO {
        return 0;
    }

    @Override
    public int agregar (CuentaAcademico t) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar (Integer y) throws ErrorDAO {
        return 0;
    }

    @Override
    public CuentaAcademico getPorId (Integer y) throws ErrorDAO {
        return null;
    }

    @Override
    public List<CuentaAcademico> getTodos () throws ErrorDAO {
        return null;
    }
}
