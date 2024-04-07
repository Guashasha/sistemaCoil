package Logica.DAO;

import Logica.Dominio.Colaboracion;
import Logica.Dominio.Cuenta;
import Logica.ErrorDAO;
import Logica.Interfaces.ICuentaDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOCuenta implements ICuentaDAO {


    @Override
    public Optional<Cuenta> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public int actualizarNombreUsuario (String nombreUsuario) throws ErrorDAO {
        return 0;
    }

    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        return false;
    }

    @Override
    public int actualizarContrasena (Cuenta cuenta, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO {
        return 0;
    }

    @Override
    public int cambiarEstadoCuenta (Cuenta cuenta, String estado) throws ErrorDAO {
        return 0;
    }

    @Override
    public int agregar (Cuenta t) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar (Cuenta obj) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<Cuenta> getPorId (Integer y) throws ErrorDAO {
        return Optional.empty();
    }

    @Override
    public List<Cuenta> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Cuenta resultSetAObjeto (ResultSet resultados) {
        return null;
    }
}
