package DAO;

import DTO.ActividadDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActividadDAO {

    public static int agregarActividad (ActividadDTO actividadDTO) throws SQLException {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("insert into actividad (titulo, descripcion, tipo) values (?, ?, ?);");

            consulta.setString(1, actividadDTO.getTitulo());
            consulta.setString(2, actividadDTO.getDescripcion());
            consulta.setString(3, actividadDTO.getTipo().toString());

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorId (Integer idActividad) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad where idActividad=?");

            consulta.setInt(1, idActividad);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorTitulo (String titulo) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad where titulo=?");

            consulta.setString(1, titulo);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorIdColaboracion (int idColaboracion) throws SQLException {
        ResultSet resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select idActividad, titulo, descripcion, tipo from actividad natural join calendarioActividades where idColaboracion=?");

            consulta.setInt(1, idColaboracion);

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
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }
}
