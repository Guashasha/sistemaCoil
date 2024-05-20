package DAO;

import DAO.Interfaces.ICronogramaActividad;
import DTO.ActividadDTO;
import DTO.ActividadVinculadaDTO;
import AccesoDatos.AdministradorBaseDatos;
import DTO.ColaboracionDTO;
import DTO.PeriodoDTO;
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

class CronogramaActividadDAO implements ICronogramaActividad {
    @Override
    public int agregar (ActividadVinculadaDTO actividadDTO) throws ErrorDAO {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("insert into calendarioActividades (idActividad, idColaboracion, fechaInicio, fechaFin) values (?, ?, ?, ?);");

            consulta.setInt(1, actividadDTO.getActividad().getIdActividad());
            consulta.setInt(2, actividadDTO.getColaboracion().getIdColaboracion());
            consulta.setDate(3, Date.valueOf(actividadDTO.getPeriodo().getFechaInicio()));
            consulta.setDate(4, Date.valueOf(actividadDTO.getPeriodo().getFechaFin()));

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
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("update from calendarioActividades set fechaInicio=?, fechaFin=? where idActividad=? and idColaboracion=?;");

            consulta.setDate(1, Date.valueOf(actividadVinculada.getPeriodo().getFechaInicio()));
            consulta.setDate(2, Date.valueOf(actividadVinculada.getPeriodo().getFechaFin()));
            consulta.setInt(3, actividadVinculada.getActividad().getIdActividad());
            consulta.setInt(4, actividadVinculada.getColaboracion().getIdColaboracion());

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
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from calendarioActividades where idActividad=? and idColaboracion=?");

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

            Optional<ActividadDTO> actividad = actividadAuxiliar.getPorId(resultados.getInt(2));
            Optional<ColaboracionDTO> colaboracion = colaboracionAuxiliar.getPorId(resultados.getInt(3));
            LocalDate fechaInicio = resultados.getDate(4)
                    .toLocalDate();
            LocalDate fechaFin = resultados.getDate(5)
                    .toLocalDate();

            if (actividad.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            if (colaboracion.isEmpty()) {
                throw new ErrorDAO("la actividad buscada para vinculación no existe", ErrorDAO.Tipo.CONSULTA);
            }

            actividadVinculadaDTO = new ActividadVinculadaDTO(actividad.get(), colaboracion.get(), new PeriodoDTO(fechaInicio, fechaFin));
        } catch (SQLException error) {
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadVinculadaDTO;
    }
}
