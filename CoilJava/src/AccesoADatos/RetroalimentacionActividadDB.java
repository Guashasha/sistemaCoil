package AccesoADatos;


import Logica.Dominio.RetroalimentacionActividad;
import Logica.ErrorDAO;

import java.sql.CallableStatement;
import java.sql.SQLException;

public class RetroalimentacionActividadDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();

    public static int agregarRetroalimentacion (RetroalimentacionActividad retroalimentacion) throws ErrorDAO {
        int resultado = -1;

        try {
            CallableStatement consulta;

            if (retroalimentacion.getComentario().isEmpty())
                consulta = db.getConexion()
                             .prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, null, ?)");
            else
                consulta = db.getConexion()
                             .prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            db.desconectar();

            consulta.setInt(1, retroalimentacion.getInteraccionConPar());
            consulta.setInt(2, retroalimentacion.getDificultad());
            consulta.setInt(3, retroalimentacion.getInteres());
            consulta.setInt(4, retroalimentacion.getId());
            consulta.setString(5, retroalimentacion.getComentario().get());
            consulta.setInt(6, retroalimentacion.getIdUsuario());

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        catch (SQLException error) {

            throw new ErrorDAO(error.getMessage());
        }

        return resultado;
    }

    public static RetroalimentacionActividad getPorId (int id) {
        RetroalimentacionActividad retroalimentacion = null;

        // TODO

        return retroalimentacion;
    }
}
