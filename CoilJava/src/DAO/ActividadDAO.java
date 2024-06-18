package DAO;

import DAO.Interfaces.IActividadDAO;
import DTO.ActividadDTO;
import AccesoDatos.AdministradorBaseDatos;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActividadDAO implements IActividadDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class);

    /**
     * Agrega una actividad a la base de datos
     * @param actividadDTO la actividad a agregar
     * @return el numero de filas afectadas
     * @throws ErrorDAO tipo conexión cuando ocurre un error de sql
     */
    @Override
    public int agregar (ActividadDTO actividadDTO) throws ErrorDAO {
        int resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("insert into actividad (titulo, descripcion, tipo) values (?, ?, ?);");

            consulta.setString(1, actividadDTO.getTitulo());
            consulta.setString(2, actividadDTO.getDescripcion());
            consulta.setString(3, actividadDTO.getTipo().toString());

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

    @Override
    public int modificar (ActividadDTO actividad) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Consigue una actividad proporcionando su id
     * @param idActividad el id de la actividad que se deséa conseguir
     * @return la actividad con el id especificado, empty si no se encontró una actividad con ese id
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si el resultSet de la consulta no contiene entradas y se accesa (no debería suceder)
     */
    @Override
    public Optional<ActividadDTO> getPorId (Integer idActividad) throws ErrorDAO {
        ResultSet resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select * from actividad where idActividad=?");

            consulta.setInt(1, idActividad);

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
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    /**
     * Consigue la primera actividad que tenga un titulo especificado
     * @param titulo el titulo que debe tener la actividad
     * @return la actividad más reciente que tenga el titulo especificado
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si el ResultSet de la consulta es nulo y se accede (no debería suceder)
     */
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
            BITACORA.error(error);
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
            BITACORA.error(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }

        return Optional.of(resultSetAObjeto(resultado));
    }

    /**
     * Consigue todas las actividades que estén vinculadas con una colaboración
     * @param idColaboracion el id de la colaboración con que deben estár asociadas las actividades
     * @return ArrayList de ActividadDTO con todas las actividades vinculadas con la colaboración, ArrayList vacío si no hay actividades vinculadas
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    @Override
    public List<ActividadDTO> getPorIdColaboracion (Integer idColaboracion) throws ErrorDAO {
        ResultSet resultado;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement("select idActividad, titulo, descripcion, tipo from actividad natural join calendarioActividades where idColaboracion=?");

            consulta.setInt(1, idColaboracion);

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException e) {
            BITACORA.error(e);
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
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividades;
    }

    @Override
    public List<ActividadDTO> getTodos () throws NotImplementedException {
        throw new NotImplementedException();
    }

    /**
     * Convierte un ResultSet que contenga una actividad de la forma: id, titulo, descripcion, tipo
     * @param resultados El ResultSet del que se desea leer el objeto actividad (debe estár en una posición valida antes de llamar el metodo)
     * @return ActividadDTO con los datos leidos del ResultSet
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
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
            BITACORA.error(error);
            throw new ErrorDAO("Ocurrió un error con la base de datos: " + error.getMessage(), ErrorDAO.Tipo.CONEXION);
        }

        return actividadDTO;
    }
}
