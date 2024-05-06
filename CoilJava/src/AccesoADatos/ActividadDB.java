package AccesoADatos;

import Logica.Dominio.Actividad;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActividadDB {
    private static final ConexionBaseDatos CONEXION = new ConexionBaseDatos();

    public static int agregarActividad (Actividad actividad) throws SQLException {
        int resultado = -1;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("insert into actividad (titulo, descripcion, tipo) values (?, ?, ?);");

            consulta.setString(1, actividad.getTitulo());
            consulta.setString(2, actividad.getDescripcion());
            consulta.setString(3, actividad.getTipo().toString());

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorId (Integer idActividad) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select * from actividad where idActividad=?");

            consulta.setInt(1, idActividad);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorTitulo (String titulo) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select * from actividad where titulo=?");

            consulta.setString(1, titulo);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorIdColaboracion (int idColaboracion) throws SQLException {
        ResultSet resultado;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select * from actividad where idColaboracion=?");

            consulta.setInt(1, idColaboracion);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getTodos () throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select * from actividad");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static int modificarActividad (Actividad actividad) throws SQLException {
        int resultado = -1;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("update actividad set descripcion=?, tipo=? where titulo=?");

            consulta.setString(1, actividad.getDescripcion());
            consulta.setString(2, actividad.getTipo().toString());
            consulta.setString(3, actividad.getTitulo());

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }
}
