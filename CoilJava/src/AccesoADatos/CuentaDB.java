package AccesoADatos;

import Logica.Dominio.Cuenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDB {

    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static Cuenta getCuentaPorUsuario (String nombreUsuario) throws SQLException {
        String cuentaPorUsuarioSQL = "SELECT * from cuenta WHERE nombreUsuario = ?";
        Cuenta cuenta = null;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement cuentaPorUsuario = CONEXION_BASE_DATOS.getConexion().
                                                                    prepareStatement(cuentaPorUsuarioSQL);
            cuentaPorUsuario.setString(1, nombreUsuario);
            ResultSet resultadoCuentaUsuario = cuentaPorUsuario.executeQuery();

            if (resultadoCuentaUsuario.next()) {
                cuenta = convertirCuenta(resultadoCuentaUsuario);
            }

            cuentaPorUsuario.close();
            resultadoCuentaUsuario.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return cuenta;

    }

    public static int actualizarNombreUsuario (Cuenta cuenta) throws SQLException {
        String actualizarUsuarioSQL = "UPDATE cuenta SET nombreUsuario = ? WHERE idCuenta = ?";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement actualizarUsuario = CONEXION_BASE_DATOS.getConexion().
                                                                     prepareStatement(actualizarUsuarioSQL);
            actualizarUsuario.setString(1, cuenta.getNombreUsuario());
            actualizarUsuario.setInt(2, cuenta.getIdCuenta());

            filasAfectadas = actualizarUsuario.executeUpdate();

            actualizarUsuario.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return  filasAfectadas;
    }

    public static boolean verificarCredenciales (String nombreUsuario, String contrasena) throws SQLException {
        String verificarCredencialesSQL = "{CALL verificar_credenciales(?, ?, ?)}";
        boolean validacion;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement verificarCredenciales = CONEXION_BASE_DATOS.getConexion().
                                                                         prepareCall(verificarCredencialesSQL);
            verificarCredenciales.setString(1, nombreUsuario);
            verificarCredenciales.setString(2, contrasena);
            verificarCredenciales.registerOutParameter(3, Types.BOOLEAN);

            verificarCredenciales.execute();

            validacion = verificarCredenciales.getBoolean(3);

            verificarCredenciales.close();
        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return validacion;

    }

    public static int actualizarContrasena (Cuenta cuenta, String contrasenaAntigua, String contrasenaNueva) throws SQLException {
        String actualizarContrasenaSQL = "{CALL cambiar_contrasena(?,?,?,?)}";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement actualizarContrasena = CONEXION_BASE_DATOS.getConexion().
                                                                        prepareCall(actualizarContrasenaSQL);
            actualizarContrasena.setInt(1, cuenta.getIdCuenta());
            actualizarContrasena.setString(2, cuenta.getNombreUsuario());
            actualizarContrasena.setString(3, contrasenaAntigua);
            actualizarContrasena.setString(4, contrasenaNueva);

            filasAfectadas = actualizarContrasena.executeUpdate();

            actualizarContrasena.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;

    }

    public static int cambiarEstadoCuenta (Cuenta cuenta, String estado) throws SQLException {
        String cambiarEstadoCuentaSQL = "UPDATE cuenta SET estado = ? WHERE idCuenta = ?";
        int filasAfectadas;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement cambiarEstadoCuenta = CONEXION_BASE_DATOS.getConexion().
                                                                       prepareStatement(cambiarEstadoCuentaSQL);
            cambiarEstadoCuenta.setString(1, estado);
            cambiarEstadoCuenta.setInt(2, cuenta.getIdCuenta());

            filasAfectadas = cambiarEstadoCuenta.executeUpdate();

            cambiarEstadoCuenta.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }
        return filasAfectadas;

    }

    public static List<Cuenta> getCuentaPorTipo (String tipo) throws SQLException {
        String getCuentaPorTipoSQL = "SELECT * FROM cuenta WHERE tipo = ?";
        List<Cuenta> listaCuentas = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement getCuentaPorTipo = CONEXION_BASE_DATOS.getConexion().
                                                                    prepareStatement(getCuentaPorTipoSQL);
            getCuentaPorTipo.setString(1, tipo);

            ResultSet resultadoGetCuentaPorTipo = getCuentaPorTipo.executeQuery();

            while (resultadoGetCuentaPorTipo.next()) {
                Cuenta cuenta = convertirCuenta(resultadoGetCuentaPorTipo);
                listaCuentas.add(cuenta);
            }

            getCuentaPorTipo.close();
            resultadoGetCuentaPorTipo.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return listaCuentas;

    }

    public static List<Cuenta> getCuentasPorEstado (String estado) throws SQLException {
        String getCuentasPorEstadoSQL = "SELECT * FROM cuenta WHERE estado = ?";
        List<Cuenta> listaCuentas = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement getCuentasPorEstado = CONEXION_BASE_DATOS.getConexion().
                                                                       prepareStatement(getCuentasPorEstadoSQL);
            getCuentasPorEstado.setString(1, estado);

            ResultSet resultadoGetCuentasPorEstado = getCuentasPorEstado.executeQuery();

            while (resultadoGetCuentasPorEstado.next()) {
                Cuenta cuenta = convertirCuenta(resultadoGetCuentasPorEstado);
                listaCuentas.add(cuenta);
            }

            getCuentasPorEstado.close();
            resultadoGetCuentasPorEstado.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return listaCuentas;

    }

    public static int agregarCuenta (Cuenta cuenta) throws SQLException {
        String agregarCuentaSQL = "{CALL registrar_cuenta(?,?,?,?,?)}";
        int filasAfectadas = -1;

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement agregarCuenta = CONEXION_BASE_DATOS.getConexion().
                                                                 prepareCall(agregarCuentaSQL);
            agregarCuenta.setInt(1, cuenta.getIdPersona());
            agregarCuenta.setString(2, cuenta.getNombreUsuario());
            agregarCuenta.setString(3, cuenta.getContrasena());
            agregarCuenta.setString(4, cuenta.getTipo().
                                             toString());
            agregarCuenta.setString(5, cuenta.getEstado().
                                             toString());

            filasAfectadas = agregarCuenta.executeUpdate();

            agregarCuenta.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return filasAfectadas;

    }

    public static Cuenta getPorId (int id) throws SQLException {
        String getPorIdSQL = "SELECT * from cuenta WHERE idCuenta = ?";
        Cuenta cuenta = null;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement getPorId = CONEXION_BASE_DATOS.getConexion().
                                                            prepareStatement(getPorIdSQL);
            getPorId.setInt(1, id);

            ResultSet resultadoGetPorId = getPorId.executeQuery();

            while (resultadoGetPorId.next()) {
                cuenta = convertirCuenta(resultadoGetPorId);
            }

            getPorId.close();
            resultadoGetPorId.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return cuenta;

    }

    public static List<Cuenta> getTodos () throws SQLException {
        String getTodosSQL = "SELECT * from cuenta";
        List<Cuenta> listaCuenta = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement getTodos = CONEXION_BASE_DATOS.getConexion().
                                                            prepareStatement(getTodosSQL);

            ResultSet resultadoGetTodos = getTodos.executeQuery();

            while (resultadoGetTodos.next()) {
                Cuenta cuenta = convertirCuenta(resultadoGetTodos);
                listaCuenta.add(cuenta);
            }

            getTodos.close();
            resultadoGetTodos.close();

        }
        finally {
            CONEXION_BASE_DATOS.desconectar();

        }

        return listaCuenta;

    }


    private static Cuenta convertirCuenta (ResultSet resultado) throws SQLException {
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(resultado.getInt("idCuenta"));
        cuenta.setIdPersona(resultado.getInt("idPersona"));
        cuenta.setNombreUsuario(resultado.getString("nombreUsuario"));
        cuenta.setContrasena(resultado.getString("contrasena"));
        cuenta.setTipo(Cuenta.TipoUsuario.
                               valueOf(resultado.getString("tipo")));
        cuenta.setEstado(Cuenta.EstadoCuenta.
                                 valueOf(resultado.getString("estado")));

        return cuenta;
    }

}
