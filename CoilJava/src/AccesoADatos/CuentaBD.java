package AccesoADatos;

import Logica.Dominio.CuentaAcademico;
import Logica.ErrorDAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CuentaBD {
    private static final ConexionBaseDatos CONEXION_BASE_DATOS = new ConexionBaseDatos();

    public static CuentaAcademico getCuentaPorCedulaProfesional (String cedula) {
        String consulta = "SELECT * from cuenta WHERE idAcademico = ?";
        CuentaAcademico cuentaAcademico = null;
        try {
            CONEXION_BASE_DATOS.conectar();
            PreparedStatement consultaCuenta = CONEXION_BASE_DATOS.getConexion().prepareStatement(consulta);
            consultaCuenta.setString(1, cedula);
            

        }
        catch (SQLException e) {
            throw new ErrorDAO(e.getMessage());
        }


    }
}
