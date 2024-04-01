package AccesoADatos;

import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuentaAcademicoDB {
    // TODO terminar cuentaDB - tomar en cuenta si me importa el academico o no.

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
                cuentaAcademico = convertirCuentaEstudiante(resultadoConsulta);
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
        String consulta = "SELECT * from cuenta WHERE idAcademico = ?";
        CuentaAcademico cuentaAcademico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaCuenta = CONEXION_BASE_DATOS.getConexion().
                                                                  prepareStatement(consulta);
            consultaCuenta.setString(1, usuario);
            ResultSet resultadoConsulta = consultaCuenta.executeQuery();

            if (resultadoConsulta.next()) {
                cuentaAcademico = convertirCuentaEstudiante(resultadoConsulta);
            }
        }
        catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }
        return cuentaAcademico;
    }

    private static CuentaAcademico convertirCuentaEstudiante (ResultSet resultado) throws SQLException {
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
