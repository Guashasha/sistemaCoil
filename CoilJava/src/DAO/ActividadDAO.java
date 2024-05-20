package DAO;

import DAO.Interfaces.IActividadDAO;
import DTO.ActividadDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActividadDAO implements IActividadDAO {

    @Override
    public int agregar (ActividadDTO actividadDTO) throws ErrorDAO {
        int resultado = -1;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("insert into actividad (titulo, descripcion, tipo) values (?, ?, ?);");

            consulta.setString(1, actividadDTO.getTitulo());
            consulta.setString(2, actividadDTO.getDescripcion());
            consulta.setString(3, actividadDTO.getTipo().toString());

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
    public int modificar (ActividadDTO actividad) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public Optional<ActividadDTO> getPorId (Integer idActividad) throws ErrorDAO {
        ResultSet resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad where idActividad=?");

            consulta.setInt(1, idActividad);

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
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    @Override
    public Optional<ActividadDTO> getPorTitulo (String titulo) throws ErrorDAO {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad where titulo=?");

            consulta.setString(1, titulo);

            resultado = consulta.executeQuery();
            consulta.close();
        }
        catch (SQLException error) {
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }
        finally {
            AdministradorBaseDatos.desconectar();
        }

        try {
            if (resultado == null || !resultado.next()) {
                return Optional.empty();
            }
        }
        catch (SQLException error) {
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    @Override
    public List<ActividadDTO> getPorIdColaboracion (Integer idColaboracion) throws ErrorDAO {
        ResultSet resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select idActividad, titulo, descripcion, tipo from actividad natural join calendarioActividades where idColaboracion=?");

            consulta.setInt(1, idColaboracion);

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        List<ActividadDTO> actividades = new ArrayList<>();

        if (resultado == null) {
            return actividades;
        }

        try {
            while (resultado.next()) {
                ActividadDTO actividadDTO = resultSetAObjeto(resultado);

                if (actividadDTO.esCorrecta()) {
                    actividades.add(actividadDTO);
                }
            }

            resultado.close();
        }
        catch (SQLException error) {
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividades;
    }

    @Override
    public List<ActividadDTO> getTodos () throws ErrorDAO {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad");

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        List<ActividadDTO> actividades = new ArrayList<>();

        if (resultado == null) {
            return actividades;
        }

        try {
            while (resultado.next()) {
                ActividadDTO actividadDTO = resultSetAObjeto(resultado);

                if (actividadDTO.esCorrecta()) {
                    actividades.add(actividadDTO);
                }
            }

            resultado.close();
        }
        catch (SQLException error) {
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividades;
    }

    public static ActividadDTO resultSetAObjeto (ResultSet resultados) throws ErrorDAO {
        ActividadDTO actividadDTO = null;

        try {
            actividadDTO = new ActividadDTO();
            actividadDTO.setIdActividad(resultados.getInt(1));
            actividadDTO.setTitulo(resultados.getString(2));
            actividadDTO.setDescripcion(resultados.getString(3));
            actividadDTO.setTipo(ActividadDTO.TipoActividad.valueOf(resultados.getString(4)));
        }
        catch (SQLException error) {
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadDTO;
    }
}
