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
        String consultaSQL = "SELECT * from cuenta WHERE nombreUsuario = ?";
        CuentaDTO cuenta = null;
        try {
            PreparedStatement consultaCuenta = AdministradorBaseDatos.getInstancia().
                                                                    prepareStatement(consultaSQL);
            consultaCuenta.setString(1, nombreUsuario);
            ResultSet resultado = consultaCuenta.executeQuery();

            if (resultado.next()) {
                cuenta = convertirCuenta(resultado);
            }
            consultaCuenta.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuenta", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return Optional.ofNullable(cuenta);
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
        String actualizacionSQL = "UPDATE cuenta SET nombreUsuario = ? WHERE idCuenta = ?";
        int filasAfectadas;
        try {
            PreparedStatement actualizacionUsuario = AdministradorBaseDatos.getInstancia().
                                                                     prepareStatement(actualizacionSQL);
            actualizacionUsuario.setString(1, cuentaDTO.getNombreUsuario());
            actualizacionUsuario.setInt(2, cuentaDTO.getIdCuenta());

            filasAfectadas = actualizacionUsuario.executeUpdate();

            actualizacionUsuario.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
        String procedimientoSQL = "{CALL verificar_credenciales(?, ?, ?)}";
        boolean validacion;
        try {
            CallableStatement procedimientoCuenta = AdministradorBaseDatos.getInstancia().
                                                                         prepareCall(procedimientoSQL);
            procedimientoCuenta.setString(1, nombreUsuario);
            procedimientoCuenta.setString(2, contrasena);
            procedimientoCuenta.registerOutParameter(3, Types.BOOLEAN);

            procedimientoCuenta.execute();

            validacion = procedimientoCuenta.getBoolean(3);
            procedimientoCuenta.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
        String procedimientoSQL = "{CALL cambiar_contrasena(?,?,?,?)}";
        int filasAfectadas;
        try {
            CallableStatement procedimientoCuenta = AdministradorBaseDatos.getInstancia().
                                                                        prepareCall(procedimientoSQL);
            procedimientoCuenta.setInt(1, cuentaDTO.getIdCuenta());
            procedimientoCuenta.setString(2, cuentaDTO.getNombreUsuario());
            procedimientoCuenta.setString(3, contrasenaAntigua);
            procedimientoCuenta.setString(4, contrasenaNueva);

            filasAfectadas = procedimientoCuenta.executeUpdate();

            procedimientoCuenta.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
        String actualizacionSQL = "UPDATE cuenta SET estado = ? WHERE idCuenta = ?";
        int filasAfectadas;

        try {
            PreparedStatement actualizacionCuenta = AdministradorBaseDatos.getInstancia().
                                                                       prepareStatement(actualizacionSQL);
            actualizacionCuenta.setString(1, estado);
            actualizacionCuenta.setInt(2, cuentaDTO.getIdCuenta());

            filasAfectadas = actualizacionCuenta.executeUpdate();

            actualizacionCuenta.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
        String consultaSQL = "SELECT * FROM cuenta WHERE tipo = ?";
        List<CuentaDTO> listaCuentaDTOS = new ArrayList<>();
        try {
            PreparedStatement consultaCuenta = AdministradorBaseDatos.getInstancia().
                                                                    prepareStatement(consultaSQL);
            consultaCuenta.setString(1, tipo);

            ResultSet resultado = consultaCuenta.executeQuery();

            while (resultado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultado);
                listaCuentaDTOS.add(cuentaDTO);
            }
            consultaCuenta.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
        String consultaSQL = "SELECT * FROM cuenta WHERE estado = ?";
        List<CuentaDTO> listaCuentaDTOS = new ArrayList<>();
        try {
            PreparedStatement consultaCuenta = AdministradorBaseDatos.getInstancia().
                                                                       prepareStatement(consultaSQL);
            consultaCuenta.setString(1, estado);

            ResultSet resultado = consultaCuenta.executeQuery();

            while (resultado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultado);
                listaCuentaDTOS.add(cuentaDTO);
            }
            consultaCuenta.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
        int filasAfectadas;
        try {
            CallableStatement procedimientoCuenta = AdministradorBaseDatos.getInstancia().
                                                                 prepareCall(procedimientoSQL);
            procedimientoCuenta.setInt(1, cuentaDTO.getIdPersona());
            procedimientoCuenta.setString(2, cuentaDTO.getNombreUsuario());
            procedimientoCuenta.setString(3, cuentaDTO.getContrasena());
            procedimientoCuenta.setString(4, cuentaDTO.getTipo().
                                             toString());
            procedimientoCuenta.setString(5, cuentaDTO.getEstado().
                                             toString());
            filasAfectadas = procedimientoCuenta.executeUpdate();
            procedimientoCuenta.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al crear la cuenta", Tipo.INSERCION);
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
        String consultaSQL = "SELECT * from cuenta WHERE idCuenta = ?";
        CuentaDTO cuentaDTO = null;
        try {
            PreparedStatement consultaCuenta = AdministradorBaseDatos.getInstancia().
                                                            prepareStatement(consultaSQL);
            consultaCuenta.setInt(1, id);

            ResultSet resultado = consultaCuenta.executeQuery();

            while (resultado.next()) {
                cuentaDTO = convertirCuenta(resultado);
            }
            consultaCuenta.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuenta por su identficador", Tipo.CONSULTA);
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
            BITACORA.warn(error.getMessage());
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
        String consultaSQL = "SELECT * from cuenta";
        List<CuentaDTO> listaCuentaDTO = new ArrayList<>();
        try {
            PreparedStatement consultaCuenta = AdministradorBaseDatos.getInstancia().
                                                            prepareStatement(consultaSQL);

            ResultSet resultado = consultaCuenta.executeQuery();
            while (resultado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultado);
                listaCuentaDTO.add(cuentaDTO);
            }
            consultaCuenta.close();
            resultado.close();
        }
        catch (SQLException error) {
            BITACORA.warn(error.getMessage());
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
