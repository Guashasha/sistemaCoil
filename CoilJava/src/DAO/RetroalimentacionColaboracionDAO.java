package DAO;

import DTO.RetroalimentacionColaboracionDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetroalimentacionColaboracionDAO {

    public static int agregarRetroalimentacion (RetroalimentacionColaboracionDTO retroalimentacion) throws SQLException {
        int resultado = -1;

        try {
            CallableStatement consulta = AdministradorBaseDatos.getInstancia().prepareCall("call insertarRetroalimentacionColaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            consulta.setInt(1, retroalimentacion.getInteraccionConPar());
            consulta.setInt(3, retroalimentacion.getHabilidadesObtenidas());
            consulta.setInt(4, retroalimentacion.getCalificacion());
            consulta.setInt(5, retroalimentacion.getIntercambioCultural());
            consulta.setInt(6, retroalimentacion.getMejoraDelLenguaje());
            consulta.setInt(7, retroalimentacion.getTrabajoColaborativo());
            consulta.setInt(8, retroalimentacion.getMejoraFormacionProfesional());
            consulta.setInt(9, retroalimentacion.getIdUsuario());
            consulta.setInt(10, retroalimentacion.getColaboracion());

            if (retroalimentacion.getComentario().isPresent()) {
                consulta.setString(2, retroalimentacion.getComentario().get());
            }
            else {
                consulta.setString(2, null);
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
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from retroalimentacion as rt natural join retroalimentacionColaboracion where rt.usuario=?");

            consulta.setInt(1, id);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorPersonaYColaboracion (int idPersona, int idColaboracion) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from retroalimentacion as rt natural join retroalimentacionColaboracion as rc where rt.usuario=? and rc.colaboracion=?");

            consulta.setInt(1, idPersona);
            consulta.setInt(2, idColaboracion);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getTodos () throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from retroalimentacion natural join retroalimentacionColaboracion");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }
}
