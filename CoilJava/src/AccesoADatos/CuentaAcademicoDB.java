package AccesoADatos;

import Logica.Dominio.Academico;
import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuentaAcademicoDB {

    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static CuentaAcademico getCuentaPorCedulaProfesional (String cedula) throws ErrorDAO {
        String consulta = "SELECT * from cuenta WHERE idAcademico = ?";
        CuentaAcademico cuentaAcademico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaCuenta = CONEXION_BASE_DATOS.getConexion().
                                                                  prepareStatement(consulta);
            consultaCuenta.setString(1, cedula);
            ResultSet resultadoConsulta = consultaCuenta.executeQuery();
            if (resultadoConsulta.next()) {
                cuentaAcademico = convertirCuentaAcademico(resultadoConsulta);
            }
            consultaCuenta.close();
            CONEXION_BASE_DATOS.desconectar();
            resultadoConsulta.close();

        }
        catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }
        return cuentaAcademico;
    }

    public static CuentaAcademico getCuentaPorUsuario (String usuario) throws ErrorDAO {
        String consulta = "SELECT * from cuenta WHERE nombreUsuario = ?";
        CuentaAcademico cuentaAcademico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaCuenta = CONEXION_BASE_DATOS.getConexion().
                                                                  prepareStatement(consulta);
            consultaCuenta.setString(1, usuario);
            ResultSet resultadoConsulta = consultaCuenta.executeQuery();

            if (resultadoConsulta.next()) {
                cuentaAcademico = convertirCuentaAcademico(resultadoConsulta);
            }
            resultadoConsulta.close();
            consultaCuenta.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO(error.getMessage());
        }
        return cuentaAcademico;
    }

    public static int actualizarNombreUsuario (CuentaAcademico cuentaAcademico, String nombreUsuario) throws ErrorDAO {
        int filasAfectadas;
        String actualizarNombreUsuarioSQL = "UPDATE cuenta set nombreUsuario = ? WHERE idCuenta = ?";

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement actualizarNombreUsuario = CONEXION_BASE_DATOS.getConexion().
                                                                           prepareStatement(actualizarNombreUsuarioSQL);
            actualizarNombreUsuario.setString(1, nombreUsuario);
            actualizarNombreUsuario.setInt(2, cuentaAcademico.getIdCuenta());

            filasAfectadas = actualizarNombreUsuario.executeUpdate();

            actualizarNombreUsuario.close();
            CONEXION_BASE_DATOS.desconectar();

        }
        catch (SQLException error) {
            throw new ErrorDAO(error.getMessage());
        }
        return filasAfectadas;

    }

    public static boolean verificarCredenciales (String nombreUsuario, String contrasena) throws ErrorDAO {
        boolean credencialEncontrada = false;
        String verificarCredencialesSQL = "SELECT * FROM cuenta WHERE nombreUsuario = ? AND contrasena = ?";

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement verificarCredenciales = CONEXION_BASE_DATOS.getConexion().
                                                                         prepareStatement(verificarCredencialesSQL);
            verificarCredenciales.setString(1, nombreUsuario);
            verificarCredenciales.setString(2,contrasena);

            CuentaAcademico cuentaAcademico = null;
            ResultSet resultadoVerificarCredenciales = verificarCredenciales.executeQuery();
            if (resultadoVerificarCredenciales.next()) {
                cuentaAcademico = convertirCuentaAcademico(resultadoVerificarCredenciales);
            }
            if (cuentaAcademico != null) {
                credencialEncontrada = true;
            }
            resultadoVerificarCredenciales.close();
            verificarCredenciales.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO(error.getMessage());
        }

        return credencialEncontrada;
    }

    public static int actualizarContrasena (CuentaAcademico cuenta, String contrasenaAntigua,String nuevaContrasena) throws ErrorDAO {
        int filasAfectadas;
        String actualizarContrasenaSQL = "UPDATE cuenta set contrasena = ? WHERE idCuenta = ? and contrasena = ?";

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement actualizarContrasena = CONEXION_BASE_DATOS.getConexion().
                                                                        prepareStatement(actualizarContrasenaSQL);
            actualizarContrasena.setString(1,nuevaContrasena);
            actualizarContrasena.setInt(2, cuenta.getIdCuenta());
            actualizarContrasena.setString(3, contrasenaAntigua);

            filasAfectadas = actualizarContrasena.executeUpdate();

            actualizarContrasena.close();
            CONEXION_BASE_DATOS.desconectar();

        }
        catch (SQLException error) {
            throw new ErrorDAO(error.getMessage());
        }

        return filasAfectadas;
    }

    public static int cambiarEstadoCuenta (CuentaAcademico cuentaAcademico, String estado) throws ErrorDAO {
        int filasAfectadas;
        String cambiarEstadoProcedimientSQL ="{CALL cambiar_estadoCuenta(?, ?)}";

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement cambiarEstadoProcedimiento = CONEXION_BASE_DATOS.getConexion().
                                                                             prepareCall(cambiarEstadoProcedimientSQL);
            cambiarEstadoProcedimiento.setInt(1, cuentaAcademico.getIdCuenta());
            cambiarEstadoProcedimiento.setString(2, estado);

            filasAfectadas = cambiarEstadoProcedimiento.executeUpdate();

            cambiarEstadoProcedimiento.close();
            CONEXION_BASE_DATOS.desconectar();

        }
        catch (SQLException error) {
            throw new ErrorDAO (error.getMessage());
        }
        return filasAfectadas;

    }

    public static int agregaCuentaAcademico (CuentaAcademico cuentaAcademico) throws ErrorDAO {
        int filasAfectadas;
        String agregarCuentaProcedimientoSQL = "{CALL registrar_cuentaAcademico(?,?,?,?)}";

        try {
            CONEXION_BASE_DATOS.conectar();
            CallableStatement agregarCuentaProcedimiento = CONEXION_BASE_DATOS.getConexion().
                                                                              prepareCall(agregarCuentaProcedimientoSQL);
            agregarCuentaProcedimiento.setString(1,cuentaAcademico.getIdAcademico());
            agregarCuentaProcedimiento.setString(2, cuentaAcademico.getNombreUsuario());
            agregarCuentaProcedimiento.setString(3, cuentaAcademico.getContrasena());
            agregarCuentaProcedimiento.setString(4, cuentaAcademico.getEstado().toString());

            filasAfectadas = agregarCuentaProcedimiento.executeUpdate();

            agregarCuentaProcedimiento.close();
            CONEXION_BASE_DATOS.desconectar();
        }
        catch (SQLException error) {
            throw new ErrorDAO (error.getMessage());
        }
        return filasAfectadas;

    }

    public static CuentaAcademico getPorId (int idCuenta) {
        String consultaPorIdSQL = "SELECT * FROM cuenta WHERE idCuenta = ?";
        CuentaAcademico cuentaAcademico = null;

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaPorId = CONEXION_BASE_DATOS.getConexion().
                                                                  prepareStatement(consultaPorIdSQL);
            consultaPorId.setInt(1, idCuenta);

            ResultSet resultaConsultaPorId = consultaPorId.executeQuery();

            while (resultaConsultaPorId.next()) {
                cuentaAcademico = convertirCuentaAcademico (resultaConsultaPorId);
            }

            resultaConsultaPorId.close();
            consultaPorId.close();
            CONEXION_BASE_DATOS.desconectar();

        }
        catch (SQLException error) {
            throw new ErrorDAO (error.getMessage());
        }
        return  cuentaAcademico;
    }

    public static List<CuentaAcademico> getTodos() {
        String consultaGetTodosSQL = "SELECT * FROM cuenta";
        List<CuentaAcademico> listaCuentaAcademicos = new ArrayList<>();

        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaGetTodos = CONEXION_BASE_DATOS.getConexion().
                                                                    prepareStatement(consultaGetTodosSQL);
            ResultSet resultadoConsultaGetTodos = consultaGetTodos.executeQuery();
            while (resultadoConsultaGetTodos.next()) {
                CuentaAcademico cuentaAcademico = convertirCuentaAcademico(resultadoConsultaGetTodos);
                listaCuentaAcademicos.add(cuentaAcademico);
            }

            consultaGetTodos.close();
            resultadoConsultaGetTodos.close();
            CONEXION_BASE_DATOS.desconectar();

        }
        catch (SQLException error) {
            throw new ErrorDAO (error.getMessage());
        }
        return listaCuentaAcademicos;
    }


    private static CuentaAcademico convertirCuentaAcademico (ResultSet resultado) throws SQLException {
        CuentaAcademico cuentaAcademico = new CuentaAcademico();
        cuentaAcademico.setIdCuenta(resultado.getInt("idCuenta"));
        cuentaAcademico.setIdAcademico(resultado.getString("idAcademico"));
        cuentaAcademico.setNombreUsuario(resultado.getString("nombreUsuario"));
        cuentaAcademico.setContrasena(resultado.getString("contrasena"));

        String estadoResultado = resultado.getString("estado");

        CuentaAcademico.EstadoCuenta estadoCuenta = CuentaAcademico.EstadoCuenta.
                valueOf(estadoResultado);
        cuentaAcademico.setEstado(estadoCuenta);

        return cuentaAcademico;
    }

}
