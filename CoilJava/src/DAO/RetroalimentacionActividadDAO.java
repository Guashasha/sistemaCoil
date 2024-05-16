package DAO;

import DTO.RetroalimentacionActividadDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionActividadDAO {

    public static int agregarRetroalimentacion (RetroalimentacionActividadDTO retroalimentacion) throws SQLException {
        int resultado = -1;

        try {
            CallableStatement consulta = AdministradorBaseDatos.getInstancia()
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
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorId (int id) throws SQLException {
        ResultSet retroalimentacion = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.idRetroalimentacion=?;");

            consulta.setInt(1, id);

            retroalimentacion = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return retroalimentacion;
    }

    public static ResultSet getPorPersonaYActividad (int idPersona, int idActividad) throws SQLException {
        ResultSet retroalimentacion = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.usuario=? and retroalimentacionActividad.actividad=?;");

            consulta.setInt(1, idPersona);
            consulta.setInt(2, idActividad);

            retroalimentacion = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return retroalimentacion;
    }

    public static ResultSet getTodos () throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }
}
