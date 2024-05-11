package Logica.DAO;

import AccesoADatos.CuentaDB;
import Logica.Dominio.Cuenta;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import Logica.Interfaces.ICuentaDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOCuenta implements ICuentaDAO {

    @Override
    public Optional<Cuenta> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        try {
            probarNombreUsuario(nombreUsuario);
            Cuenta cuenta = CuentaDB.getCuentaPorUsuario(nombreUsuario);
            return Optional.ofNullable(cuenta);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int actualizarNombreUsuario (Cuenta cuenta) throws ErrorDAO {
        if (existeNombreUsuario(cuenta)) {
            throw new ErrorDAO("El nombre " + cuenta.getNombreUsuario() + " ya se encuentra ocupado", Tipo.VALIDACION);
        }
        try {
            return CuentaDB.actualizarNombreUsuario(cuenta);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        try {
            probarUsuario(nombreUsuario, contrasena);
            return CuentaDB.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int actualizarContrasena (Cuenta cuenta, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO {
        try {
            probarContrasenas(contrasenaAntigua, nuevaContrasena);
            return CuentaDB.actualizarContrasena(cuenta, contrasenaAntigua, nuevaContrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int cambiarEstadoCuenta (Cuenta cuenta, String estado) throws ErrorDAO {
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Error en el estado ingresado", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CuentaDB.cambiarEstadoCuenta(cuenta, estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Cuenta> getCuentasPorTipo (String tipo) throws ErrorDAO {
        if (!cadenaValida(tipo)) {
            throw new ErrorDAO("Tipo de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CuentaDB.getCuentaPorTipo(tipo);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Cuenta> getCuentasPorEstado (String estado) throws ErrorDAO {
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Estado de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CuentaDB.getCuentasPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int agregar (Cuenta cuenta) throws ErrorDAO {
        try {
            return CuentaDB.agregarCuenta(cuenta);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (Cuenta cuenta) throws ErrorDAO {
        throw new ErrorDAO("Metodo no utlizado", ErrorDAO.Tipo.NO_IMPLEMENTADO);
    }

    @Override
    public Optional<Cuenta> getPorId (Integer id) throws ErrorDAO {
        if (!idValido(id)) {
            throw new ErrorDAO("Id de la cuenta no valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(CuentaDB.getPorId(id));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Cuenta> getTodos () throws ErrorDAO {
        try {
            return CuentaDB.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
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

    private void probarNombreUsuario (String usuario) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario(usuario);
    }

    private boolean existeNombreUsuario (Cuenta cuenta) {
        return getCuentaPorUsuario(cuenta.getNombreUsuario()).isPresent();
    }
    public void usuarioExistente (Cuenta cuenta) {
        if (getCuentaPorUsuario(cuenta.getNombreUsuario()).isPresent()) {
            throw new ErrorDAO("El nombre de usuario " + cuenta.getNombreUsuario() + " ya se encuentra registado", Tipo.DUPLICIDAD);
        }
    }


    private void probarUsuario (String nombre, String contrasena) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNombreUsuario(nombre);
        cuenta.setContrasena(contrasena);
    }

    private void probarContrasenas (String contrasenaAntigua, String contrasenaNueva) {
        Cuenta cuenta = new Cuenta();
        cuenta.setContrasena(contrasenaAntigua);
        cuenta.setContrasena(contrasenaNueva);
    }
}
