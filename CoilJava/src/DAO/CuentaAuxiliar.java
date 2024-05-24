package DAO;

import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class CuentaAuxiliar {
    private final CuentaDAO CUENTA_DAO = new CuentaDAO();

    public Optional<CuentaDTO> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        try {
            probarNombreUsuario(nombreUsuario);
            return CUENTA_DAO.getCuentaPorUsuario(nombreUsuario);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int actualizarNombreUsuario (CuentaDTO cuentaDTO) throws ErrorDAO {
        if (existeNombreUsuario(cuentaDTO)) {
            throw new ErrorDAO("El nombre " + cuentaDTO.getNombreUsuario() + " ya se encuentra ocupado", Tipo.VALIDACION);
        }
        try {
            return CUENTA_DAO.actualizarNombreUsuario(cuentaDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        try {
            probarUsuario(nombreUsuario, contrasena);
            return CUENTA_DAO.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int actualizarContrasena (CuentaDTO cuentaDTO, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO {
        try {
            probarContrasenas(contrasenaAntigua, nuevaContrasena);
            return CUENTA_DAO.actualizarContrasena(cuentaDTO, contrasenaAntigua, nuevaContrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int cambiarEstadoCuenta (CuentaDTO cuentaDTO, String estado) throws ErrorDAO {
        if (esCadenaInvalida(estado)) {
            throw new ErrorDAO("Error en el estado ingresado", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CUENTA_DAO.cambiarEstadoCuenta(cuentaDTO, estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<CuentaDTO> getCuentasPorTipo (String tipo) throws ErrorDAO {
        if (esCadenaInvalida(tipo)) {
            throw new ErrorDAO("Tipo de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CUENTA_DAO.getCuentasPorTipo(tipo);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<CuentaDTO> getCuentasPorEstado (String estado) throws ErrorDAO {
        if (esCadenaInvalida(estado)) {
            throw new ErrorDAO("Estado de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CUENTA_DAO.getCuentasPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int agregar (CuentaDTO cuentaDTO) throws ErrorDAO {
        try {
            return CUENTA_DAO.agregar(cuentaDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int modificar (CuentaDTO cuentaDTO) throws ErrorDAO {
        throw new ErrorDAO("Metodo no utlizado", ErrorDAO.Tipo.NO_IMPLEMENTADO);
    }

    public Optional<CuentaDTO> getPorId (Integer id) throws ErrorDAO {
        if (!idValido(id)) {
            throw new ErrorDAO("Id de la cuenta no valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CUENTA_DAO.getPorId(id);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<CuentaDTO> getTodos () throws ErrorDAO {
        try {
            return CUENTA_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public CuentaDTO resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    private boolean esCadenaInvalida (String cadena) {
        return cadena == null || cadena.isBlank();
    }

    private boolean idValido (int id) {
        return id > 0;
    }

    private void probarNombreUsuario (String usuario) {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNombreUsuario(usuario);
    }

    private boolean existeNombreUsuario (CuentaDTO cuentaDTO) {
        return getCuentaPorUsuario(cuentaDTO.getNombreUsuario()).isPresent();
    }
    public void usuarioExistente (CuentaDTO cuentaDTO) {
        if (getCuentaPorUsuario(cuentaDTO.getNombreUsuario()).isPresent()) {
            throw new ErrorDAO("El nombre de usuario " + cuentaDTO.getNombreUsuario() + " ya se encuentra registado", Tipo.DUPLICIDAD);
        }
    }


    private void probarUsuario (String nombre, String contrasena) {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setNombreUsuario(nombre);
        cuentaDTO.setContrasena(contrasena);
    }

    private void probarContrasenas (String contrasenaAntigua, String contrasenaNueva) {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setContrasena(contrasenaAntigua);
        cuentaDTO.setContrasena(contrasenaNueva);
    }
}
