package DAO;

import DAO.Interfaces.IRetroalimentacionActividadDAO;
import DTO.RetroalimentacionActividadDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RetroalimentacionActividadDAO implements IRetroalimentacionActividadDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class.getName());

    /**
     * Consigue una retroalimentación de actividad que tenga el id proporcionado
     *
     * @param id el id de la retroalimentación que se quiere buscar
     * @return La retroalimentación de actividad con el id proporcionado, empty si no se encuentra ninguna retroalimentación con la id proporcionada
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si no se encuentra ninguna retroalimentación con ese id
     */
    @Override
    public Optional<RetroalimentacionActividadDTO> getPorId(Integer id) throws ErrorDAO {
        ResultSet retroalimentacion = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
                    "select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.idRetroalimentacion=?;");

            consulta.setInt(1, id);

            retroalimentacion = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            BITACORA.warn(e);
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        try {
            if (retroalimentacion == null || !retroalimentacion.next()) {
                return Optional.empty();
            }
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("La retroalimentación no se encotró", ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.of(resultSetAObjeto(retroalimentacion));
    }

    /**
     * Consigué la retroalimentación de la actividad especificada y la persona que la realizó
     *
     * @param idPersona   la id de la persona que realizó la retroalimentación
     * @param idActividad la id de la actividad que se busca la retroalimentación
     * @return la retroalimentación de la actividad especificada, realizada por la persona especificada
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    public Optional<RetroalimentacionActividadDTO> getPorPersonaYActividad(int idPersona, int idActividad)
            throws ErrorDAO {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
                    "select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad where retroalimentacion.usuario=? and retroalimentacionActividad.actividad=?;");

            consulta.setInt(1, idPersona);
            consulta.setInt(2, idActividad);

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        try {
            if (resultado == null || !resultado.next()) {
                return Optional.empty();
            }
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("La retroalimentación no se encotró", ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    /**
     * Valida y registra una retroalimentación de actividad a la base de datos
     *
     * @param retroalimentacion la retroalimentación que se guardará en la base de datos
     * @return el numero de filas alteradas
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    @Override
    public int agregar(RetroalimentacionActividadDTO retroalimentacion) throws ErrorDAO {
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
        } catch (SQLException e) {
            BITACORA.warn(e);
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    @Override
    public int modificar(RetroalimentacionActividadDTO obj) throws ErrorDAO, ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("metodo no implementado");
    }

    /**
     * consigué todas las retroalimentaciones de actividad registradas en la base de datos
     *
     * @return ArrayList de retroalimentaciones de actividad
     * @throws ErrorDAO tipo conexion si ocurre un error de sql
     */
    public List<RetroalimentacionActividadDTO> getTodos() throws ErrorDAO {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
                    "select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad");

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            BITACORA.warn(e);
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        ArrayList<RetroalimentacionActividadDTO> retroalimentaciones = new ArrayList<>();

        if (resultado == null) {
            return retroalimentaciones;
        }

        try {
            while (resultado.next()) {
                RetroalimentacionActividadDTO retroalimentacion = resultSetAObjeto(resultado);

                if (retroalimentacion.esCorrecto()) {
                    retroalimentaciones.add(retroalimentacion);
                }
            }

            resultado.close();
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return retroalimentaciones;
    }

    /**
     * convierte un ResultSet que contenga los datos de una retroalimentación de actividad a un objeto RetroalimentacionActividadDTO
     *
     * @param resultados El ResultSet a convertir, debe ser de la forma: id, interaccionConPar, comentario, dificultad, interes, idUsuario, idActividad
     * @return Un objeto RetroalimentaciónActividiadDTO
     */
    public static RetroalimentacionActividadDTO resultSetAObjeto(ResultSet resultados) {
        RetroalimentacionActividadDTO retroalimentacion = new RetroalimentacionActividadDTO();

        try {
            retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
            retroalimentacion.setInteraccionConPar(resultados.getInt(2));
            retroalimentacion.setComentario(resultados.getString(3));
            retroalimentacion.setDificultad(resultados.getInt(4));
            retroalimentacion.setInteres(resultados.getInt(5));
            retroalimentacion.setIdUsuario(resultados.getInt(6));
            retroalimentacion.setIdActividad(resultados.getInt(7));
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        return retroalimentacion;
    }
}
