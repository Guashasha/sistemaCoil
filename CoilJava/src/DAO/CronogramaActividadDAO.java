package DAO;

import DAO.Interfaces.ICronogramaActividad;
import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import AccesoDatos.AdministradorBaseDatos;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase CronogramaActividadDAO se encarga de obtener información de las regiones en la base de datos y mandarlos a capas superiores mediante Transfer Objects.
 *
 * @author CronogramaActividadDAO
 */
public class CronogramaActividadDAO implements ICronogramaActividad {
    private static final Logger BITACORA = Logger.getLogger(CronogramaActividadAuxiliar.class);

    /**
     * Vincula una actvidad con una colaboración en la base de datos
     * @param actividadDTO los datos de la acitividad y la colaboración que se vincularán
     * @return el numero de filas insertadas en la base de datos
     * @throws ErrorDAO tipo conexion si ocurre un error de sql
     */
    @Override
    public int agregar (ActividadVinculadaDTO actividadDTO) throws ErrorDAO {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("insert into calendarioActividades (idActividad, idColaboracion, fechaFinalizacion) values (?, ?, null);");

            consulta.setInt(1, actividadDTO.getActividad().getIdActividad());
            consulta.setInt(2, actividadDTO.getColaboracion().getIdColaboracion());

            resultado = consulta.executeUpdate();
            consulta.close();
        } catch (SQLException e) {
            BITACORA.error(e);
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    /**
     * desvincula la actividad de la colaboración
     * @param actividadVinculada Los datos de la actividad y la colaboración que desean ser desvinculadas
     * @return el numero de filas modificadas
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    @Override
    public int desvincular (ActividadVinculadaDTO actividadVinculada) throws ErrorDAO {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("delete from calendarioActividades where idActividad=? and idColaboracion=?;");

            consulta.setInt(1, actividadVinculada.getActividad().getIdActividad());
            consulta.setInt(2, actividadVinculada.getColaboracion().getIdColaboracion());

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    /**
     * modifica la fecha de finalización de una vinculación entre una actividad y una colaboración
     * @param actividadVinculada el objeto que contiene la actividad, colaboración y la fecha
     * @return el numero de filas alteradas
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    @Override
    public int modificar(ActividadVinculadaDTO actividadVinculada) throws ErrorDAO {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("update calendarioActividades set fechaFinalizacion=? where idActividad=? and idColaboracion=?;");

            consulta.setDate(1, Date.valueOf(actividadVinculada.getPeriodo()));
            consulta.setInt(2, actividadVinculada.getActividad().getIdActividad());
            consulta.setInt(3, actividadVinculada.getColaboracion().getIdColaboracion());

            resultado = consulta.executeUpdate();
            consulta.close();
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    @Override
    public Optional<ActividadVinculadaDTO> getPorId(Integer y) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Una actividad vinculada no tiene id");
    }

    /**
     * Consigue una vinculación de actividad con colaboración proporcionando la actividad y la colaboración
     * @param idActividad el id de la activida que se busca
     * @param idColaboracion el id de la colaboración con la que debe estár vinculada
     * @return la actividad vinculada que pretenece a la colaboración y actividad proporcionadas, empty si no se encuentra
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    @Override
    public Optional<ActividadVinculadaDTO> getPorActividadYColaboracion(int idActividad, int idColaboracion) throws ErrorDAO {
        ResultSet resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from calendarioActividades where idActividad=? and idColaboracion=?;");

            consulta.setInt(1, idActividad);
            consulta.setInt(2, idColaboracion);

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            BITACORA.error(e);
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        try {
            if (resultado == null || !resultado.next()) {
                return Optional.empty();
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    /**
     * Consigue todas las actividades vinculadas con todas las colaboraciones
     * @return ArrayList con todas las vinculaciones
     * @throws ErrorDAO
     */
    @Override
    public List<ActividadVinculadaDTO> getTodos () throws ErrorDAO {
        ResultSet actividades;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from calendarioActividades;");

            actividades = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            BITACORA.error(e);
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        List<ActividadVinculadaDTO> actividadesLista = new ArrayList<>();

        if (actividades == null) {
            return actividadesLista;
        }

        try {
            while (actividades.next()) {
                ActividadVinculadaDTO actividadVinculadaDTO = resultSetAObjeto(actividades);

                if (actividadVinculadaDTO.esCorrecto()) {
                    actividadesLista.add(actividadVinculadaDTO);
                }
            }
        }
        catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadesLista;
    }

    /**
     * Convierte un ResultSet que contiene una vinculación entre una actividad y una colaboración en un objeto ActividadVinculada
     * @param resultados el ResultSet que contiene los datos de la actividad vinculada de la forma: idActividad, idColaboracion, fechaFinalización
     * @return un objeto Actividad vinculada con los datos del resultset
     */
    public ActividadVinculadaDTO resultSetAObjeto (ResultSet resultados) {
        ActividadVinculadaDTO actividadVinculadaDTO;

        try {
            ActividadAuxiliar actividadAuxiliar = new ActividadAuxiliar();
            ColaboracionAuxiliar colaboracionAuxiliar = new ColaboracionAuxiliar();

            Optional<ActividadDTO> actividad = actividadAuxiliar.getPorId(resultados.getInt(1));
            Optional<ColaboracionDTO> colaboracion = colaboracionAuxiliar.getPorId(resultados.getInt(2));
            Date fecha = resultados.getDate(3);

            if (actividad.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            if (colaboracion.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            if (fecha == null) {
                actividadVinculadaDTO = new ActividadVinculadaDTO(actividad.get(), colaboracion.get());
            }
            else {
                actividadVinculadaDTO = new ActividadVinculadaDTO(actividad.get(), colaboracion.get(), fecha.toLocalDate());
            }
        } catch (SQLException error) {
            BITACORA.error(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadVinculadaDTO;
    }
}
