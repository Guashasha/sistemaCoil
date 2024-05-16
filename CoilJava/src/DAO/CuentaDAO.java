package DAO;

import DTO.CuentaDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {
    private static final Logger BITACORA = Logger.getLogger(CuentaDAO.class);

    public static CuentaDTO getCuentaPorUsuario (String nombreUsuario) throws ErrorDAO {
        String cuentaPorUsuarioSQL = "SELECT * from cuenta WHERE nombreUsuario = ?";
        CuentaDTO cuentaDTO = null;
        try {
            PreparedStatement cuentaPorUsuario = AdministradorBaseDatos.getInstancia().
                                                                    prepareStatement(cuentaPorUsuarioSQL);
            cuentaPorUsuario.setString(1, nombreUsuario);
            ResultSet resultadoCuentaUsuario = cuentaPorUsuario.executeQuery();

            if (resultadoCuentaUsuario.next()) {
                cuentaDTO = convertirCuenta(resultadoCuentaUsuario);
            }
            cuentaPorUsuario.close();
            resultadoCuentaUsuario.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuentaDTO", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return cuentaDTO;
    }

    public static int actualizarNombreUsuario (CuentaDTO cuentaDTO) throws ErrorDAO {
        String actualizarUsuarioSQL = "UPDATE cuenta SET nombreUsuario = ? WHERE idCuenta = ?";
        int filasAfectadas;
        try {
            PreparedStatement actualizarUsuario = AdministradorBaseDatos.getInstancia().
                                                                     prepareStatement(actualizarUsuarioSQL);
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

    public static boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        String verificarCredencialesSQL = "{CALL verificar_credenciales(?, ?, ?)}";
        boolean validacion;
        try {
            CallableStatement verificarCredenciales = AdministradorBaseDatos.getInstancia().
                                                                         prepareCall(verificarCredencialesSQL);
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

    public static int actualizarContrasena (CuentaDTO cuentaDTO, String contrasenaAntigua, String contrasenaNueva) throws ErrorDAO {
        String actualizarContrasenaSQL = "{CALL cambiar_contrasena(?,?,?,?)}";
        int filasAfectadas;
        try {
            CallableStatement actualizarContrasena = AdministradorBaseDatos.getInstancia().
                                                                        prepareCall(actualizarContrasenaSQL);
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

    public static int cambiarEstadoCuenta (CuentaDTO cuentaDTO, String estado) throws ErrorDAO {
        String cambiarEstadoCuentaSQL = "UPDATE cuenta SET estado = ? WHERE idCuenta = ?";
        int filasAfectadas;

        try {
            PreparedStatement cambiarEstadoCuenta = AdministradorBaseDatos.getInstancia().
                                                                       prepareStatement(cambiarEstadoCuentaSQL);
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

    public static List<CuentaDTO> getCuentaPorTipo (String tipo) throws ErrorDAO {
        String getCuentaPorTipoSQL = "SELECT * FROM cuenta WHERE tipo = ?";
        List<CuentaDTO> listaCuentaDTOS = new ArrayList<>();
        try {
            PreparedStatement getCuentaPorTipo = AdministradorBaseDatos.getInstancia().
                                                                    prepareStatement(getCuentaPorTipoSQL);
            getCuentaPorTipo.setString(1, tipo);

            ResultSet resultadoGetCuentaPorTipo = getCuentaPorTipo.executeQuery();

            while (resultadoGetCuentaPorTipo.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultadoGetCuentaPorTipo);
                listaCuentaDTOS.add(cuentaDTO);
            }
            getCuentaPorTipo.close();
            resultadoGetCuentaPorTipo.close();
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

    public static List<CuentaDTO> getCuentasPorEstado (String estado) throws ErrorDAO {
        String getCuentasPorEstadoSQL = "SELECT * FROM cuenta WHERE estado = ?";
        List<CuentaDTO> listaCuentaDTOS = new ArrayList<>();
        try {
            
            PreparedStatement getCuentasPorEstado = AdministradorBaseDatos.getInstancia().
                                                                       prepareStatement(getCuentasPorEstadoSQL);
            getCuentasPorEstado.setString(1, estado);

            ResultSet resultadoGetCuentasPorEstado = getCuentasPorEstado.executeQuery();

            while (resultadoGetCuentasPorEstado.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultadoGetCuentasPorEstado);
                listaCuentaDTOS.add(cuentaDTO);
            }
            getCuentasPorEstado.close();
            resultadoGetCuentasPorEstado.close();
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

    public static int agregarCuenta (CuentaDTO cuentaDTO) throws ErrorDAO {
        String agregarCuentaSQL = "{CALL registrar_cuenta(?,?,?,?,?)}";
        int filasAfectadas = -1;
        try {
            CallableStatement agregarCuenta = AdministradorBaseDatos.getInstancia().
                                                                 prepareCall(agregarCuentaSQL);
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

    public static CuentaDTO getPorId (int id) throws ErrorDAO {
        String getPorIdSQL = "SELECT * from cuenta WHERE idCuenta = ?";
        CuentaDTO cuentaDTO = null;
        try {
            PreparedStatement getPorId = AdministradorBaseDatos.getInstancia().
                                                            prepareStatement(getPorIdSQL);
            getPorId.setInt(1, id);

            ResultSet resultadoGetPorId = getPorId.executeQuery();

            while (resultadoGetPorId.next()) {
                cuentaDTO = convertirCuenta(resultadoGetPorId);
            }
            getPorId.close();
            resultadoGetPorId.close();
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO("Error al obtener la cuentaDTO por su identficador", Tipo.CONSULTA);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }
        return cuentaDTO;
    }

    public static List<CuentaDTO> getTodos () throws ErrorDAO {
        String getTodosSQL = "SELECT * from cuenta";
        List<CuentaDTO> listaCuentaDTO = new ArrayList<>();
        try {
            PreparedStatement getTodos = AdministradorBaseDatos.getInstancia().
                                                            prepareStatement(getTodosSQL);

            ResultSet resultadoGetTodos = getTodos.executeQuery();
            while (resultadoGetTodos.next()) {
                CuentaDTO cuentaDTO = convertirCuenta(resultadoGetTodos);
                listaCuentaDTO.add(cuentaDTO);
            }
            getTodos.close();
            resultadoGetTodos.close();
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


    private static CuentaDTO convertirCuenta (ResultSet resultado) throws SQLException {
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
