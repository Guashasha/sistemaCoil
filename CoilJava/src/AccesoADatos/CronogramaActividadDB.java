package AccesoADatos;

import Logica.Dominio.Actividad;
import Logica.Dominio.Colaboracion;
import Logica.Dominio.Periodo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CronogramaActividadDB {
    private static final ConexionBaseDatos CONEXION = new ConexionBaseDatos();
    public static int agregar (Actividad actividad, Colaboracion colaboracion, Periodo periodo) throws SQLException {
        int resultado = -1;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("insert into calendarioActividades (idActividad, idColaboracion, fechaInicio, fechaFin) values (?, ?, ?, ?);");

            consulta.setInt(1, actividad.getIdActividad());
            consulta.setInt(2, colaboracion.getIdColaboracion());
            consulta.setDate(3, Date.valueOf(periodo.getFechaInicio()));
            consulta.setDate(3, Date.valueOf(periodo.getFechaFin()));

            resultado = consulta.executeUpdate();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getPorActividadYColaboracion (int idActividad, int idColaboracion) throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select * from calendarioActividades where idActividad=? and idColaboracion=?");

            consulta.setInt(1, idActividad);
            consulta.setInt(2, idColaboracion);

            resultado = consulta.executeQuery();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }

    public static ResultSet getTodos () throws SQLException {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = CONEXION.getConexion().prepareStatement("select * from calendarioActividades;");

            resultado = consulta.executeQuery();
            consulta.close();
        }
        finally {
            CONEXION.desconectar();
        }

        return resultado;
    }
}
