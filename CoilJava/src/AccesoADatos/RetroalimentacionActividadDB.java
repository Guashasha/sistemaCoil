package AccesoADatos;


import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;

public class RetroalimentacionActividadDB {
    private ConexionBaseDatos db = new ConexionBaseDatos();

    public static int agregarRetroalimentacion (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        int resultado = -1;

        // TODO

        return resultado;
    }

    public static RetroalimentacionActividad getPorId (int id) {
        RetroalimentacionActividad retroalimentacion = null;

        // TODO

        return retroalimentacion;
    }
}
