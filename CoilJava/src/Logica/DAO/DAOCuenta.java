package Logica.DAO;

import AccesoADatos.CuentaDB;
import Logica.Dominio.Cuenta;
import Logica.ErrorDAO;
import Logica.Interfaces.ICuentaDAO;
import org.apache.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOCuenta implements ICuentaDAO {
    private static final Logger BITACORA = Logger.getLogger(DAOCuenta.class);


    @Override
    public Optional<Cuenta> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        Cuenta cuenta = null;
        if (!cadenaValida(nombreUsuario)) {
            throw new ErrorDAO("Nombre usuario invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            cuenta = CuentaDB.getCuentaPorUsuario(nombreUsuario);

        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());

        }
        return Optional.ofNullable(cuenta);
    }

    @Override
    public int actualizarNombreUsuario (Cuenta cuenta) throws ErrorDAO {

        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Al menos un campo de la cuenta esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        if (!getCuentaPorUsuario(cuenta.getNombreUsuario()).isPresent()) {
            throw new ErrorDAO("El usuario ya se encuentra registrado", ErrorDAO.Tipo.DUPLICIDAD);
        }

        int filasAfectadas = -1;
        try {
            filasAfectadas = CuentaDB.actualizarNombreUsuario(cuenta);

        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }

        return filasAfectadas;
    }

    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        boolean resultado = false;

        if (!cadenaValida(nombreUsuario)) {
            throw new ErrorDAO("nombre de usuario invalido", ErrorDAO.Tipo.VALIDACION);
        }
        if (!cadenaValida(contrasena)) {
            throw new ErrorDAO("contrasena invalido", ErrorDAO.Tipo.VALIDACION);
        }

        try {
            resultado = CuentaDB.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }

        return resultado;
    }

    @Override
    public int actualizarContrasena (Cuenta cuenta, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO {

        if (!cadenaValida(contrasenaAntigua)) {
            throw new ErrorDAO("contrasena antigua invalida", ErrorDAO.Tipo.VALIDACION);
        }
        if (!cadenaValida(nuevaContrasena)) {
            throw new ErrorDAO("Nueva contrasena invalida", ErrorDAO.Tipo.VALIDACION);
        }
        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Error en la cuenta", ErrorDAO.Tipo.VALIDACION);
        }

        int filasAfectadas = -1;
        try {
            filasAfectadas = CuentaDB.actualizarContrasena(cuenta, contrasenaAntigua, nuevaContrasena);

        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public int cambiarEstadoCuenta (Cuenta cuenta, String estado) throws ErrorDAO {

        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Error en la cuenta", ErrorDAO.Tipo.VALIDACION);
        }
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Error en el estado ingresado", ErrorDAO.Tipo.VALIDACION);
        }

        int filasAfectadas = -1;
        try {
            filasAfectadas = CuentaDB.cambiarEstadoCuenta(cuenta, estado);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }
    //todo
    @Override
    public List<Cuenta> getCuentasPorTipo (String tipo) throws ErrorDAO {
        List<Cuenta> listaCuenta = null;
        if (!cadenaValida(tipo)) {
            throw new ErrorDAO("Tipo de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaCuenta = CuentaDB.getCuentaPorTipo(tipo);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaCuenta;
    }

    @Override
    public List<Cuenta> getCuentasPorEstado (String estado) throws ErrorDAO {
        List<Cuenta> listaCuentas = null;
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Estado de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaCuentas = CuentaDB.getCuentasPorEstado(estado);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaCuentas;
    }

    @Override
    public int agregar (Cuenta cuenta) throws ErrorDAO {

        if (!cuenta.validarNulos()) {
            throw new ErrorDAO("Al menos un dato de la cuenta esta vacio", ErrorDAO.Tipo.VALIDACION);
        }

        int filasAfectadas = -1;
        try {
            filasAfectadas = CuentaDB.agregarCuenta(cuenta);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public int modificar (Cuenta cuenta) throws ErrorDAO {
        throw new ErrorDAO("Metodo no utlizado", ErrorDAO.Tipo.NO_IMPLEMENTADO);
    }

    @Override
    public Optional<Cuenta> getPorId (Integer id) throws ErrorDAO {
        Cuenta cuenta = null;
        if (!idValido(id)) {
            throw new ErrorDAO("Id de la cuenta no valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            cuenta = CuentaDB.getPorId(id);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return Optional.ofNullable(cuenta);
    }

    @Override
    public List<Cuenta> getTodos () throws ErrorDAO {
        List<Cuenta> listaCuentas = null;
        try {
            listaCuentas = CuentaDB.getTodos();
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return listaCuentas;
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
