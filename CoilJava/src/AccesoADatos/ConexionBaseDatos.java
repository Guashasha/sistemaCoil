package AccesoADatos;

import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBaseDatos {
    private static final Logger BITACORA = Logger.getLogger(ConexionBaseDatos.class);
    private static Connection conexion;
    private static final String URL_DB_PROPERTY = "db.url";
    private static final String USUARIO_DB_PROPERTY = "db.usuario";
    private static final String CLAVE_DB_PROPERTY = "db.clave";

    private ConexionBaseDatos () {

    }

    public static Connection getInstancia () throws ErrorDAO {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = getConexion();
            }
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
           throw new ErrorDAO("No fue posible realizar la conexion con la base de datos.\nConctacte a un técnico"
                    , ErrorDAO.Tipo.CONEXION);
        }
        return conexion;
    }

    private static Connection getConexion () throws SQLException {
        Connection nuevaConexion;
        Properties propiedades = new ConexionBaseDatos().getConfiguracionDB();
        if (propiedades != null) {
            nuevaConexion = DriverManager.getConnection(
                    propiedades.getProperty(URL_DB_PROPERTY),
                    propiedades.getProperty(USUARIO_DB_PROPERTY),
                    propiedades.getProperty(CLAVE_DB_PROPERTY)
            );
        }
        else {
            throw new SQLException("No es posible encontrar las credenciales de la base de datos");
        }
        return nuevaConexion;
    }

    public static boolean desconectar () throws ErrorDAO {
       boolean estaCerrado = false;
       try {
           if (conexion != null) {
               conexion.close();
           }
           estaCerrado = true;
       } catch (SQLException error) {
           BITACORA.fatal(error.getMessage());
           throw new ErrorDAO("Algo sucedio mal con el sistema. \nContacte con un técnico", ErrorDAO.Tipo.CONEXION);
       }
       return estaCerrado;
    }

    public static boolean rollback () throws ErrorDAO {
        boolean seRevirtio = false;
        try {
            if (conexion != null) {
                conexion.rollback();
            }
            seRevirtio = true;
        }
        catch (SQLException error) {
            BITACORA.fatal(error.getMessage());
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }
        return seRevirtio;
    }

    private Properties getConfiguracionDB () {
        Properties configuracion = null;
        try {
            InputStream archivoConfiguracion = new FileInputStream("src/Utilidades/configuracionDB.properties");
            if (archivoConfiguracion != null) {
                configuracion = new Properties();
                configuracion.load(archivoConfiguracion);
            }
            archivoConfiguracion.close();
        }
        catch (FileNotFoundException error) {
            BITACORA.fatal(error.getMessage());
        }
        catch (IOException error){
            BITACORA.fatal(error.getMessage());
        }
        return configuracion;
    }


}
