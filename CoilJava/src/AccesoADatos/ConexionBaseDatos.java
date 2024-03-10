package AccesoADatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    private Connection conexion;
    private final String NOMBRE_BASE_DE_DATOS = "jdbc:mysql://192.168.1.57:3307/coil";
    private final String USUARIO_BASE_DE_DATOS = "admin_COIL";
    private final String CLAVE_BASE_DE_DATOS = "habitacionDeVuelo";

    public void conectar () throws SQLException {
        if (this.conexion == null || this.conexion.isClosed()) {
            this.conexion = DriverManager.getConnection(NOMBRE_BASE_DE_DATOS, USUARIO_BASE_DE_DATOS, CLAVE_BASE_DE_DATOS);
        }
    }

    public Connection getConexion () throws SQLException {
        conectar();
        return this.conexion;
    }

    public void desconectar () throws SQLException {
        if (this.conexion != null && !this.conexion.isClosed()) {
            this.conexion.close();
        }
    }


}
