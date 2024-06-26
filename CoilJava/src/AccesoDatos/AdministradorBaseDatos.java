package AccesoDatos;

import Utilidades.ErrorDAO;
import org.apache.log4j.Logger;
import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * Clase que administra la conexión a la base de datos, incluyendo las operaciones
 * de conexión, desconexión y rollback. Implementa el patrón Singleton para asegurar
 * que solo exista una instancia de la conexión a la base de datos.
 */
public class AdministradorBaseDatos {
    private static final Logger BITACORA = Logger.getLogger(AdministradorBaseDatos.class);
    private static Connection conexion;
    private static final String URL_DB_PROPERTY = "db.url";
    private static final String USUARIO_DB_PROPERTY = "db.usuario";
    private static final String CLAVE_DB_PROPERTY = "db.clave";

    /**
     * Constructor privado para evitar la creación de instancias de la clase.
     * La clase utiliza el patrón Singleton para asegurar una única conexión.
     */
    private AdministradorBaseDatos () {

    }

    /**
     * Obtiene la instancia única de la conexión a la base de datos.
     * Si la conexión no existe o está cerrada, se establece una nueva conexión.
     *
     * @return conexion regresa una conexión a la base de datos
     * @throws ErrorDAO tipo conexión si ocurre un error al intentar conectarse
     */
    public static Connection getInstancia () throws ErrorDAO {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = getConexion();
            }
        }
        catch (SQLNonTransientConnectionException error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Tiempo de espera de conexión agotado. Por favor, inténtelo nuevamente.", ErrorDAO.Tipo.CONEXION);
        }
        catch (SQLInvalidAuthorizationSpecException error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Credenciales de base de datos incorrectas.", ErrorDAO.Tipo.CONEXION);
        }
        catch (SQLException error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("No fue posible realizar la conexion con la base de datos.\nContacte a un técnico", ErrorDAO.Tipo.CONEXION);
        }
        return conexion;
    }

    /**
     * Establece una nueva conexión a la base de datos utilizando las propiedades configuradas.
     *
     * @return una nueva conexión a la base de datos
     * @throws SQLException si ocurre un error al intentar conectarse
     */
    private static Connection getConexion () throws SQLException {
        Connection nuevaConexion;
        Properties propiedades = new AdministradorBaseDatos().getConfiguracionDB();
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

    /**
     * Desconecta la conexión a la base de datos si está abierta.
     *
     * @return true si la conexión se cerró exitosamente, false en caso contrario
     * @throws ErrorDAO tipo conexión si ocurre un error al intentar desconectar
     */
    public static boolean desconectar () throws ErrorDAO {
        boolean estaCerrado = false;
        try {
            if (conexion != null) {
                conexion.close();
            }
            estaCerrado = true;
        }
        catch (SQLException error) {
            BITACORA.fatal(error);
            throw new ErrorDAO("Algo sucedio mal con el sistema. \nContacte con un técnico", ErrorDAO.Tipo.CONEXION);
        }
        return estaCerrado;
    }

    /**
     * Realiza un rollback en la conexión a la base de datos si está abierta.
     *
     * @return true si el rollback se realizó exitosamente, false en caso contrario
     * @throws ErrorDAO tipo conexión si ocurre un error al intentar realizar el rollback
     */
    public static boolean rollback () throws ErrorDAO {
        boolean seRevirtio = false;
        try {
            if (conexion != null) {
                conexion.rollback();
            }
            seRevirtio = true;
        }
        catch (SQLException error) {
            BITACORA.fatal(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }
        return seRevirtio;
    }

    /**
     * Obtiene la configuración de la base de datos desde un archivo de propiedades.
     *
     * @return un objeto Properties con la configuración de la base de datos, o null si ocurre un error
     */
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
            BITACORA.fatal(error);
        }
        catch (IOException error) {
            BITACORA.fatal(error);
        }
        return configuracion;
    }
}
