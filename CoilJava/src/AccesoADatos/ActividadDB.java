package AccesoADatos;

import Logica.Bitacora;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;

import java.sql.ResultSet;

public class ActividadDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    public static int agregarActividad (Actividad actividad) throws ErrorDAO {
        return 0;
    }

    public static ResultSet getPorId (Integer idActividad) {
        return null;
    }
}
