package AccesoADatos;

import Logica.Bitacora;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.Dominio.RetroalimentacionColaboracion;

public class RetroalimentacionColaboracionDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    public static int agregarRetroalimentacion (RetroalimentacionColaboracion retroalimentacion) {
        return 0;
    }
}
