package DAO;

import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import DAO.Interfaces.ICuentaDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class CuentaAuxiliar implements ICuentaDAO {

    @Override
    public Optional<CuentaDTO> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        try {
            probarNombreUsuario(nombreUsuario);
            CuentaDTO cuentaDTO = CuentaDAO.getCuentaPorUsuario(nombreUsuario);
            return Optional.ofNullable(cuentaDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int actualizarNombreUsuario (CuentaDTO cuentaDTO) throws ErrorDAO {
        if (existeNombreUsuario(cuentaDTO)) {
            throw new ErrorDAO("El nombre " + cuentaDTO.getNombreUsuario() + " ya se encuentra ocupado", Tipo.VALIDACION);
        }
        try {
            return CuentaDAO.actualizarNombreUsuario(cuentaDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        try {
            probarUsuario(nombreUsuario, contrasena);
            return CuentaDAO.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int actualizarContrasena (CuentaDTO cuentaDTO, String contrasenaAntigua, String nuevaContrasena) throws ErrorDAO {
        try {
            probarContrasenas(contrasenaAntigua, nuevaContrasena);
            return CuentaDAO.actualizarContrasena(cuentaDTO, contrasenaAntigua, nuevaContrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int cambiarEstadoCuenta (CuentaDTO cuentaDTO, String estado) throws ErrorDAO {
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Error en el estado ingresado", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CuentaDAO.cambiarEstadoCuenta(cuentaDTO, estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<CuentaDTO> getCuentasPorTipo (String tipo) throws ErrorDAO {
        if (!cadenaValida(tipo)) {
            throw new ErrorDAO("Tipo de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CuentaDAO.getCuentaPorTipo(tipo);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<CuentaDTO> getCuentasPorEstado (String estado) throws ErrorDAO {
        if (!cadenaValida(estado)) {
            throw new ErrorDAO("Estado de cuenta invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return CuentaDAO.getCuentasPorEstado(estado);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int agregar (CuentaDTO cuentaDTO) throws ErrorDAO {
        try {
            return CuentaDAO.agregarCuenta(cuentaDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (CuentaDTO cuentaDTO) throws ErrorDAO {
        throw new ErrorDAO("Metodo no utlizado", ErrorDAO.Tipo.NO_IMPLEMENTADO);
    }

    @Override
    public Optional<CuentaDTO> getPorId (Integer id) throws ErrorDAO {
        if (!idValido(id)) {
            throw new ErrorDAO("Id de la cuenta no valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(CuentaDAO.getPorId(id));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<CuentaDTO> getTodos () throws ErrorDAO {
        try {
            return CuentaDAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public CuentaDTO resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
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
