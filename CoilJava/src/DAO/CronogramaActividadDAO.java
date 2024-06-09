package DAO;

import DAO.Interfaces.ICronogramaActividad;
import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import AccesoDatos.AdministradorBaseDatos;
import DTO.ColaboracionDTO;
import Utilidades.ErrorDAO;
import jdk.jshell.spi.ExecutionControl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CronogramaActividadDAO implements ICronogramaActividad {
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
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

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
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

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
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    @Override
    public List<ActividadVinculadaDTO> getTodos () throws ErrorDAO {
        ResultSet actividades;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from calendarioActividades;");

            actividades = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
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
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadesLista;
    }

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
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadVinculadaDTO;
    }
}
