package AccesoADatos;

import Logica.Dominio.RetroalimentacionActividad;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionActividadDB {
    private static final ConexionBaseDatos CONEXION = new ConexionBaseDatos();

    public static int agregarRetroalimentacion (RetroalimentacionActividad retroalimentacion) throws SQLException {
        int resultado = -1;

        try {
            CallableStatement consulta = CONEXION.getConexion()
                    .prepareCall("call insertarRetroalimentacionActividad (?, ?, ?, ?, ?, ?)");

            consulta.setInt(1, retroalimentacion.getInteraccionConPar());
            consulta.setInt(2, retroalimentacion.getDificultad());
            consulta.setInt(3, retroalimentacion.getInteres());
            consulta.setInt(4, retroalimentacion.getIdActividad());
            consulta.setInt(6, retroalimentacion.getIdUsuario());

            if (retroalimentacion.getComentario().isEmpty()) {
                consulta.setString(5, null);
            } else {
                consulta.setString(5, retroalimentacion.getComentario().get());
            }

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorId (int id) throws SQLException {
        ResultSet retroalimentacion = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.idRetroalimentacion=?;");

            consulta.setInt(1, id);

            retroalimentacion = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return retroalimentacion;
    }

    public static ResultSet getPorPersonaYActividad (int idPersona, int idActividad) throws SQLException {
        ResultSet retroalimentacion = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.usuario=? and retroalimentacionActividad.actividad=?;");

            consulta.setInt(1, idPersona);
            consulta.setInt(2, idActividad);

            retroalimentacion = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return retroalimentacion;
    }

    public static ResultSet getTodos () throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }
}
