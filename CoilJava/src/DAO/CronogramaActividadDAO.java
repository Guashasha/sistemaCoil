package DAO;

import DTO.ActividadDTO;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
import AccesoDatos.AdministradorBaseDatos;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CronogramaActividadDAO {
    public static int agregar (ActividadDTO actividadDTO, ColaboracionDTO colaboracionDTO, PeriodoDTO periodoDTO) throws SQLException {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("insert into calendarioActividades (idActividad, idColaboracion, fechaInicio, fechaFin) values (?, ?, ?, ?);");

            consulta.setInt(1, actividadDTO.getIdActividad());
            consulta.setInt(2, colaboracionDTO.getIdColaboracion());
            consulta.setDate(3, Date.valueOf(periodoDTO.getFechaInicio()));
            consulta.setDate(3, Date.valueOf(periodoDTO.getFechaFin()));

            resultado = consulta.executeUpdate();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorActividadYColaboracion (int idActividad, int idColaboracion) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from calendarioActividades where idActividad=? and idColaboracion=?");

            consulta.setInt(1, idActividad);
            consulta.setInt(2, idColaboracion);

            resultado = consulta.executeQuery();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    public static ResultSet getTodos () throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from calendarioActividades;");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }
}
