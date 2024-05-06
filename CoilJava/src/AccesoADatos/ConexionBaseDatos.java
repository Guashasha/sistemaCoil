package AccesoADatos;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBaseDatos {
    private static final Logger BITACORA = Logger.getLogger(ConexionBaseDatos.class);


    private Connection conexion;
    private final String NOMBRE_DB_PROPERTY = "db.nombreDB";
    private final String USUARIO_DB_PROPERTY = "db.usuario";
    private final String CLAVE_DB_PROPERTY = "db.clave";

    public void conectar () throws SQLException {
        Properties configuracion = getConfiguracionDB();
        String nombreDb = configuracion.getProperty(NOMBRE_DB_PROPERTY);
        String usuarioDb = configuracion.getProperty(USUARIO_DB_PROPERTY);
        String claveDB = configuracion.getProperty(CLAVE_DB_PROPERTY);

        if (this.conexion == null || this.conexion.isClosed()) {
            this.conexion = DriverManager.getConnection(nombreDb, usuarioDb, claveDB);
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

    private Properties getConfiguracionDB () {
        Properties configuracion = new Properties();
        InputStream archivoConfiguracion = null;
        try {
            archivoConfiguracion = new FileInputStream("src/configuracionDB.properties");
            configuracion.load(archivoConfiguracion);
        }
        catch (IOException error) {
            BITACORA.fatal(error.getMessage());
        }
        finally {
            try {
                if (archivoConfiguracion != null) {
                    archivoConfiguracion.close();
                }
            }
            catch (IOException error) {
                BITACORA.fatal(error.getMessage());
            }
        }

        return configuracion;
    }


}
