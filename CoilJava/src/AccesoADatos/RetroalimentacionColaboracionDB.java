package AccesoADatos;

import Logica.Bitacora;
import Logica.Dominio.RetroalimentacionActividad;
import Logica.Dominio.RetroalimentacionColaboracion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionColaboracionDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();

    public static int agregarRetroalimentacion (RetroalimentacionColaboracion retroalimentacion) throws SQLException {
        return 0;
    }

    public static ResultSet getPorId (int id) throws SQLException {
        return null;
    }

    public static ResultSet getPorPersonaYColaboracion (int idPersona, int idColaboracion) throws SQLException {
        return null;
    }

    public static ResultSet getTodos () throws SQLException {
        PreparedStatement consulta = db.getConexion().prepareStatement("select * from retroalimentacion natural join retroalimentacionColaboracion");

        ResultSet resultado = consulta.executeQuery();
        consulta.close();

        db.desconectar();

        return resultado;
    }
}
