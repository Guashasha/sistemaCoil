package AccesoADatos;

import Logica.Bitacora;
import Logica.Dominio.Actividad;
import Logica.Dominio.RetroalimentacionActividad;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ActividadDB {
    private static final ConexionBaseDatos db = new ConexionBaseDatos();
    private static final Bitacora bitacora = new Bitacora(RetroalimentacionActividad.class.getName());

    public static int agregarActividad (Actividad actividad) throws SQLException {
        int resultado = -1;

        PreparedStatement consulta = db.getConexion().prepareStatement("insert into actividad (titulo, descripcion, tipo) values (?, ?, ?);");

        consulta.setString(1, actividad.getTitulo());
        consulta.setString(2, actividad.getDescripcion());
        consulta.setString(3, actividad.getTipo().toString());

        resultado = consulta.executeUpdate();
        consulta.close();

        db.desconectar();

        return resultado;
    }

    public static ResultSet getPorId (Integer idActividad) throws SQLException {
        PreparedStatement consulta = db.getConexion().prepareStatement("select * from actividad where idActividad=?");

        consulta.setInt(1, idActividad);

        ResultSet resultado = consulta.executeQuery();
        consulta.close();

        db.desconectar();

        return resultado;
    }

    public static ResultSet getPorTitulo (String titulo) throws SQLException {
        PreparedStatement consulta = db.getConexion().prepareStatement("select * from actividad where titulo=?");

        consulta.setString(1, titulo);

        ResultSet resultado = consulta.executeQuery();
        consulta.close();

        db.desconectar();

        return resultado;
    }

    public static ResultSet getTodos () throws SQLException {
        PreparedStatement consulta = db.getConexion().prepareStatement("select * from actividad");

        ResultSet resultado = consulta.executeQuery();
        consulta.close();

        db.desconectar();

        return resultado;
    }

    public static int modificarActividad (Actividad actividad) throws SQLException {
        PreparedStatement consulta = db.getConexion().prepareStatement("update actividad set descripcion=?, tipo=? where titulo=?");

        consulta.setString(1, actividad.getDescripcion());
        consulta.setString(2, actividad.getTipo().toString());
        consulta.setString(3, actividad.getTitulo());

        int resultado = consulta.executeUpdate();
        consulta.close();

        db.desconectar();

        return resultado;
    }
}
