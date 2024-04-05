package AccesoADatos;

import Logica.Bitacora;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ActividadDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    public static int agregarActividad (Actividad actividad) throws ErrorDAO {
        int resultado = -1;

        try {
            CallableStatement consulta = db.getConexion().prepareCall("insert into actividad (titulo, descripcion, tipo) values (?, ?, ?);");
            db.desconectar();

            consulta.setString(1, actividad.getTitulo());
            consulta.setString(2, actividad.getDescripcion());
            consulta.setString(3, actividad.getTipo().toString());

            resultado = consulta.executeUpdate();
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage());
        }

        return resultado;
    }

    public static ResultSet getPorId (Integer idActividad) {
        ResultSet resultado = null;

        try {
            CallableStatement consulta = db.getConexion().prepareCall("select * from actividad where idActividad=?");
            db.desconectar();

            consulta.setInt(1, idActividad);

            resultado = consulta.executeQuery();
        }
        catch (SQLException error) {
            bitacora.escribirError(error);

            throw new ErrorDAO(error.getMessage());
        }

        return resultado;
    }
}
