package DAO;

import DAO.Interfaces.ICuentaDAO;
import DTO.CuentaDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaDAO implements ICuentaDAO {
    private static final Logger BITACORA = Logger.getLogger(CuentaDAO.class);

    /**
     * Obtiene una cuenta basada en el nombre de usuario.
     *
     * @param nombreUsuario el nombre de usuario de la cuenta que se desea obtener.
     * @return un objeto Optional que contiene la cuenta encontrada o vacío si no se encuentra la cuenta.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<CuentaDTO> getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        String consulta = "SELECT * from cuenta WHERE nombreUsuario = ?";
        CuentaDTO cuentaDTO = null;
        try {
            PreparedStatement cuentaPorUsuario = AdministradorBaseDatos.getInstancia().
                                                                    prepareStatement(consulta);
            cuentaPorUsuario.setString(1, nombreUsuario);
            ResultSet resultado = cuentaPorUsuario.executeQuery();

            if (resultado.next()) {
                cuentaDTO = convertirCuenta(resultado);
            }
            cuentaPorUsuario.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuentaDTO", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(cuentaDTO);
    }

    /**
     * Actualiza el nombre de usuario de una cuenta.
     *
     * @param cuentaDTO el objeto CuentaDTO que contiene la información de la cuenta a actualizar.
     * @return el número de filas afectadas por la actualización.
     * @throws ErrorDAO si ocurre un error durante la actualización en la base de datos.
     */
    @Override
    public int actualizarNombreUsuario (CuentaDTO cuentaDTO) throws ErrorDAO {
        String consulta = "UPDATE cuenta SET nombreUsuario = ? WHERE idCuenta = ?";
        int filasAfectadas;
        try {
            PreparedStatement actualizarUsuario = AdministradorBaseDatos.getInstancia().
                                                                     prepareStatement(consulta);
            actualizarUsuario.setString(1, cuentaDTO.getNombreUsuario());
            actualizarUsuario.setInt(2, cuentaDTO.getIdCuenta());

            filasAfectadas = actualizarUsuario.executeUpdate();

            actualizarUsuario.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al actualizar el nombre de usuario", Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return  filasAfectadas;
    }

    /**
     * Verifica las credenciales de una cuenta y obtiene un booleano como parametro sálida.
     *
     * @param nombreUsuario el nombre de usuario.
     * @param contrasena la contraseña.
     * @return true si las credenciales son válidas, false en caso contrario.
     * @throws ErrorDAO si ocurre un error durante la verificación en la base de datos.
     */
    @Override
    public boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        String consulta = "{CALL verificar_credenciales(?, ?, ?)}";
        boolean validacion;
        try {
            CallableStatement verificarCredenciales = AdministradorBaseDatos.getInstancia().
                                                                         prepareCall(consulta);
            verificarCredenciales.setString(1, nombreUsuario);
            verificarCredenciales.setString(2, contrasena);
            verificarCredenciales.registerOutParameter(3, Types.BOOLEAN);

            verificarCredenciales.execute();

            validacion = verificarCredenciales.getBoolean(3);
            verificarCredenciales.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al verificar las credenciales", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return validacion;
    }

    /**
     * Actualiza la contraseña de una cuenta por medio de un procedimiento almacenado.
     *
     * @param cuentaDTO el objeto CuentaDTO que contiene la información de la cuenta a actualizar.
     * @param contrasenaAntigua la contraseña antigua.
     * @param contrasenaNueva la nueva contraseña.
     * @return el número de filas afectadas por la actualización.
     * @throws ErrorDAO si ocurre un error durante la actualización en la base de datos.
     */
    @Override
    public int actualizarContrasena (CuentaDTO cuentaDTO, String contrasenaAntigua, String contrasenaNueva) throws ErrorDAO {
        String consulta = "{CALL cambiar_contrasena(?,?,?,?)}";
        int filasAfectadas;
        try {
            CallableStatement actualizarContrasena = AdministradorBaseDatos.getInstancia().
                                                                        prepareCall(consulta);
            actualizarContrasena.setInt(1, cuentaDTO.getIdCuenta());
            actualizarContrasena.setString(2, cuentaDTO.getNombreUsuario());
            actualizarContrasena.setString(3, contrasenaAntigua);
            actualizarContrasena.setString(4, contrasenaNueva);

            filasAfectadas = actualizarContrasena.executeUpdate();

            actualizarContrasena.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al actualizar la contraseña", Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Cambia el estado de una cuenta.
     *
     * @param cuentaDTO el objeto CuentaDTO que contiene la información de la cuenta con el estado al que se desea actualizar y su identificador.
     * @param estado el nuevo estado de la cuenta.
     * @return el número de filas afectadas por la actualización.
     * @throws ErrorDAO si ocurre un error durante la actualización en la base de datos.
     */
    @Override
    public int cambiarEstadoCuenta (CuentaDTO cuentaDTO, String estado) throws ErrorDAO {
        String consulta = "UPDATE cuenta SET estado = ? WHERE idCuenta = ?";
        int filasAfectadas;

        try {
            PreparedStatement cambiarEstadoCuenta = AdministradorBaseDatos.getInstancia().
                                                                       prepareStatement(consulta);
            cambiarEstadoCuenta.setString(1, estado);
            cambiarEstadoCuenta.setInt(2, cuentaDTO.getIdCuenta());

            filasAfectadas = cambiarEstadoCuenta.executeUpdate();

            cambiarEstadoCuenta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al cambiar el estado de la cuentaDTO", Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Obtiene una lista de cuentas basadas en su tipo.
     *
     * @param tipo el tipo de las cuentas que se desean obtener.
     * @return una lista de objetos CuentaDTO que cumplen con el criterio especificado.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public List<CuentaDTO> getCuentasPorTipo (String tipo) throws ErrorDAO {
        String consulta = "SELECT * FROM cuenta WHERE tipo = ?";
        List<CuentaDTO> listaCuentaDTOS = new ArrayList<>();
        try {
            PreparedStatement getCuentaPorTipo = AdministradorBaseDatos.getInstancia().
                                                                    prepareStatement(consulta);
            getCuentaPorTipo.setString(1, tipo);

            ResultSet resultado = getCuentaPorTipo.executeQuery();

            while (resultado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultado);
                listaCuentaDTOS.add(cuentaDTO);
            }
            getCuentaPorTipo.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las cuentas por su clasificación", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaCuentaDTOS;
    }

    /**
     * Obtiene una lista de cuentas basadas en su estado en el que se encuentran registradas en la base de datos.
     *
     * @param estado el estado de las cuentas que se desean obtener.
     * @return una lista de objetos CuentaDTO que cumplen con el criterio especificado.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public List<CuentaDTO> getCuentasPorEstado (String estado) throws ErrorDAO {
        String consulta = "SELECT * FROM cuenta WHERE estado = ?";
        List<CuentaDTO> listaCuentaDTOS = new ArrayList<>();
        try {

            PreparedStatement getCuentasPorEstado = AdministradorBaseDatos.getInstancia().
                                                                       prepareStatement(consulta);
            getCuentasPorEstado.setString(1, estado);

            ResultSet resultado = getCuentasPorEstado.executeQuery();

            while (resultado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultado);
                listaCuentaDTOS.add(cuentaDTO);
            }
            getCuentasPorEstado.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener las cuentas por su estado", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaCuentaDTOS;
    }

    /**
     * Agrega una nueva cuenta a la base de datos.
     *
     * @param cuentaDTO el objeto CuentaDTO que contiene la información de la cuenta a agregar.
     * @return el número de filas afectadas por la inserción.
     * @throws ErrorDAO si ocurre un error durante la inserción en la base de datos.
     */
    @Override
    public int agregar (CuentaDTO cuentaDTO) throws ErrorDAO {
        String procedimientoSQL = "{CALL registrar_cuenta(?,?,?,?,?)}";
        int filasAfectadas = -1;
        try {
            CallableStatement agregarCuenta = AdministradorBaseDatos.getInstancia().
                                                                 prepareCall(procedimientoSQL);
            agregarCuenta.setInt(1, cuentaDTO.getIdPersona());
            agregarCuenta.setString(2, cuentaDTO.getNombreUsuario());
            agregarCuenta.setString(3, cuentaDTO.getContrasena());
            agregarCuenta.setString(4, cuentaDTO.getTipo().
                                             toString());
            agregarCuenta.setString(5, cuentaDTO.getEstado().
                                             toString());
            filasAfectadas = agregarCuenta.executeUpdate();
            agregarCuenta.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al crear la cuentaDTO", Tipo.INSERCION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return filasAfectadas;
    }

    /**
     * Modifica una cuenta existente en la base de datos.
     *
     * @param obj el objeto CuentaDTO que contiene la información de la cuenta a modificar.
     * @return el número de filas afectadas por la modificación.
     * @throws ErrorDAO si ocurre un error durante la modificación en la base de datos.
     * @throws ExecutionControl.NotImplementedException si el método no está implementado.
     */
    @Override
    public int modificar (CuentaDTO obj) throws ErrorDAO, ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Metodo no implementado");
    }

    /**
     * Obtiene una cuenta basada en su ID.
     *
     * @param id el ID de la cuenta que se desea obtener.
     * @return un objeto Optional que contiene la cuenta encontrada o vacío si no se encuentra la cuenta.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<CuentaDTO> getPorId (Integer id) throws ErrorDAO {
        String consulta = "SELECT * from cuenta WHERE idCuenta = ?";
        CuentaDTO cuentaDTO = null;
        try {
            PreparedStatement getPorId = AdministradorBaseDatos.getInstancia().
                                                            prepareStatement(consulta);
            getPorId.setInt(1, id);

            ResultSet resultado = getPorId.executeQuery();

            while (resultado.next()) {
                cuentaDTO = convertirCuenta(resultado);
            }
            getPorId.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuentaDTO por su identficador", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(cuentaDTO);
    }

    /**
     * Obtiene una cuenta basada en el ID de la persona asociada.
     *
     * @param idPersona el ID de la persona asociada a la cuenta que se desea obtener.
     * @return un objeto Optional que contiene la cuenta encontrada o vacío si no se encuentra la cuenta.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public Optional<CuentaDTO> getCuentaPorPersona (int idPersona) throws ErrorDAO {
        CuentaDTO cuenta = null;

        try {
            PreparedStatement query = AdministradorBaseDatos.getInstancia().prepareStatement("SELECT * FROM cuenta WHERE idPersona=?;");
            query.setInt(1, idPersona);
            ResultSet resultado = query.executeQuery();

            if (resultado.next()) {
                cuenta = convertirCuenta(resultado);
            }
        } catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuenta por el id de la persona", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return Optional.ofNullable(cuenta);
    }

    /**
     * Obtiene todas las cuentas registradas en la base de datos.
     *
     * @return una lista de todos los objetos CuentaDTO registrados en la base de datos.
     * @throws ErrorDAO si ocurre un error durante la consulta a la base de datos.
     */
    @Override
    public List<CuentaDTO> getTodos () throws ErrorDAO {
        String consulta = "SELECT * from cuenta";
        List<CuentaDTO> listaCuentaDTO = new ArrayList<>();
        try {
            PreparedStatement getTodos = AdministradorBaseDatos.getInstancia().
                                                            prepareStatement(consulta);

            ResultSet resultado = getTodos.executeQuery();
            while (resultado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultado);
                listaCuentaDTO.add(cuentaDTO);
            }
            getTodos.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener todas las cuentas", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return listaCuentaDTO;
    }



    private CuentaDTO convertirCuenta (ResultSet resultado) throws SQLException {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setIdCuenta(resultado.getInt("idCuenta"));
        cuentaDTO.setIdPersona(resultado.getInt("idPersona"));
        cuentaDTO.setNombreUsuario(resultado.getString("nombreUsuario"));
        cuentaDTO.setContrasena(resultado.getString("contrasena"));
        cuentaDTO.setTipo(CuentaDTO.TipoUsuario.
                               valueOf(resultado.getString("tipo")));
        cuentaDTO.setEstado(CuentaDTO.EstadoCuenta.
                                 valueOf(resultado.getString("estado")));
        return cuentaDTO;
    }
}
