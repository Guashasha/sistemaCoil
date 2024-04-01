package AccesoADatos;

import Logica.Bitacora;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RetroalimentacionActividadDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();
    private static Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    public static int agregarRetroalimentacion (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        int resultado = -1;

        try {
            CallableStatement consulta;

            consulta = db.getConexion()
                         .prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            db.desconectar();

            consulta.setInt(1, retroalimentacion.getInteraccionConPar());
            consulta.setInt(2, retroalimentacion.getDificultad());
            consulta.setInt(3, retroalimentacion.getInteres());
            consulta.setInt(4, retroalimentacion.getId());
            consulta.setInt(6, retroalimentacion.getIdUsuario());

            if (retroalimentacion.getComentario().isEmpty()) {
                consulta.setString(5, null);
            }
            else {
                consulta.setString(5, retroalimentacion.getComentario().get());
            }

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage());
        }

        return resultado;
    }

    public static ResultSet getPorId (int id) {
        ResultSet retroalimentacion = null;

        try {
            CallableStatement consulta = null;
            consulta = db.getConexion().prepareCall("select * from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.idRetroalimentacion=1;");

            db.desconectar();

            retroalimentacion = consulta.executeQuery();
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage());
        }

        return retroalimentacion;
    }
}
