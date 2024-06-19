package DAO;

import DTO.CuentaDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;

import java.util.List;
import java.util.Optional;

/**
 * La clase CuentaAuxiliar se encarga de obtener información de las regiones en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 *
 * @author FerRMZ
 */
public class CuentaAuxiliar {
    private final CuentaDAO CUENTA_DAO = new CuentaDAO();

    /**
     * Obtiene un objeto CuentaDTO basado en el nombre de usuario.
     *
     * @param nombreUsuario el nombre de usuario por el cual se desea filtrar.
     * @return un objeto Optional que contiene el CuentaDTO que cumple con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public Optional<CuentaDTO> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        try {
            probarNombreUsuario(nombreUsuario);
            return CUENTA_DAO.getCuentaPorUsuario(nombreUsuario);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Verifica las credenciales de un usuario.
     *
     * @param nombreUsuario el nombre de usuario.
     * @param contrasena    la contraseña.
     * @return true si las credenciales son válidas, false en caso contrario.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        try {
            probarUsuario(nombreUsuario, contrasena);
            return CUENTA_DAO.verificarCredenciales(nombreUsuario, contrasena);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Cambia el estado de una cuenta.
     *
     * @param cuentaDTO el objeto CuentaDTO que se desea actualizar.
     * @param estado    el nuevo estado de la cuenta.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
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

    /**
     * Obtiene una lista de cuentas basado en el estado.
     *
     * @param estado el estdo de cuenta por el cual se desea filtrar.
     * @return una lista de objetos CuentaDTO que cumplen con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
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

    /**
     * Agrega un objeto CuentaDTO.
     *
     * @param cuentaDTO el objeto CuentaDTO que se desea agregar.
     * @return el ID de la cuenta agregada.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int agregar (CuentaDTO cuentaDTO) throws ErrorDAO {
        try {
            return CUENTA_DAO.agregar(cuentaDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    /**
     * Modifica un objeto CuentaDTO.
     *
     * @param cuentaDTO el objeto CuentaDTO que se desea modificar.
     * @return el número de filas afectadas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public int modificar (CuentaDTO cuentaDTO) throws ErrorDAO {
        throw new ErrorDAO("Metodo no utlizado", ErrorDAO.Tipo.NO_IMPLEMENTADO);
    }

    /**
     * Obtiene un objeto CuentaDTO basado en el ID.
     *
     * @param id el ID de la cuenta por la cual se desea filtrar.
     * @return un objeto Optional que contiene el CuentaDTO que cumple con los criterios especificados.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
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

    /**
     * Obtiene una lista de todas las cuentas.
     *
     * @return una lista de objetos CuentaDTO de todas las cuentas.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    public List<CuentaDTO> getTodos () throws ErrorDAO {
        try {
            return CUENTA_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
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

}
