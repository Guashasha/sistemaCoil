package Logica.DAO;

import AccesoADatos.CuentaDB;
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
        Cuenta cuenta = null;
        if (!cadenaValida(nombreUsuario)) {
            throw new ErrorDAO("Nombre usuario invalido");
        }
        try {
            cuenta = CuentaDB.getCuentaPorUsuario(nombreUsuario);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;

        }
        return Optional.ofNullable(cuenta);
    }

    @Override
    public int actualizarNombreUsuario (Cuenta cuenta) throws ErrorDAO {
        int filasAfectadas;
        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Al menos un campo de la cuenta esta vacio");
        }
        if (!getCuentaPorUsuario(cuenta.getNombreUsuario()).isPresent()) {
            throw new ErrorDAO("El usuario ya se encuentra registrado");
        }
        try {
            filasAfectadas = CuentaDB.actualizarNombreUsuario(cuenta);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return filasAfectadas;
    }

    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        boolean resultado;

        if (!cadenaValida(nombreUsuario)) {
            throw new ErrorDAO("nombre de usuario invalido");
        }
        if (!cadenaValida(contrasena)) {
            throw new ErrorDAO("contrasena invalido");
        }

        try {
            resultado = CuentaDB.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return resultado;
    }

    @Override
    public int actualizarContrasena (Cuenta cuenta, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO {
        int filasAfectadas;
        if (!cadenaValida(contrasenaAntigua)) {
            throw new ErrorDAO("contrasena antigua invalida");
        }
        if (!cadenaValida(nuevaContrasena)) {
            throw new ErrorDAO("Nueva contrasena invalida");
        }
        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Error en la cuenta");
        }
        try {
            filasAfectadas = CuentaDB.actualizarContrasena(cuenta, contrasenaAntigua, nuevaContrasena);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return filasAfectadas;
    }

    @Override
    public int cambiarEstadoCuenta (Cuenta cuenta, String estado) throws ErrorDAO {
        int filasAfectadas;
        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Error en la cuenta");
        }
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Error en el estado ingresado");
        }
        try {
            filasAfectadas = CuentaDB.cambiarEstadoCuenta(cuenta, estado);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return filasAfectadas;
    }
    //todo
    @Override
    public List<Cuenta> getCuentasPorTipo (String tipo) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Cuenta> getCuentasPorEstado (String estado) throws ErrorDAO {
        return null;
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

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private boolean idValido (int id) {
        return id > 0;
    }
}
