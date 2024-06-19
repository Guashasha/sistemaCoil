package DAO;

import DTO.RetroalimentacionActividadDTO;
import DTO.RetroalimentacionColaboracionDTO;
import AccesoDatos.AdministradorBaseDatos;
import DAO.Interfaces.IRetroalimentacionColaboracionDAO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RetroalimentacionColaboracionDAO implements IRetroalimentacionColaboracionDAO {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class.getName());

    /**
     * Agrega a la base de datos una retroalimentación de colaboración
     *
     * @param retroalimentacion la retroalimentación que se registrará en la base de datos
     * @return el numero de filas afectadas en la base de datos
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    @Override
    public int agregar(RetroalimentacionColaboracionDTO retroalimentacion) throws ErrorDAO {
        int resultado = -1;

        try {
            CallableStatement consulta = AdministradorBaseDatos.getInstancia()
                    .prepareCall("call insertarRetroalimentacionColaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
            } else {
                consulta.setString(2, null);
            }

            resultado = consulta.executeUpdate();
            consulta.close();
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        return resultado;
    }

    @Override
    public int modificar(RetroalimentacionColaboracionDTO x) throws NotImplementedException {
        throw new NotImplementedException("el objeto no implementará el metodo");
    }

    /**
     * Consigué una retroalimentación de colaboración por su id
     *
     * @param id el id de la retroalimentación que se busca
     * @return la retroalimentación colaboración con el id especificado
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si no se encuentra la retroalimentación con la id especificada
     */
    @Override
    public Optional<RetroalimentacionColaboracionDTO> getPorId(Integer id) throws ErrorDAO {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
                    "select * from retroalimentacion as rt natural join retroalimentacionColaboracion where rt.idRetroalimentacion=?");

            consulta.setInt(1, id);

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
            throw new ErrorDAO("La retroalimentación no se encotró", Tipo.CONSULTA);
        }

        return Optional.ofNullable(resultSetAObjeto(resultado));
    }

    /**
     * Consigue la retroalimentación de colaboración a partir de la persona que realizó la retroalimentación y la colaboración retroalimentada
     *
     * @param idPersona      la id de la persona que realiza la retroalimentación
     * @param idColaboracion la id de la colaboración retroalimentada
     * @return la retroalimentación de colaboración especificada que realizó la persona especificada
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si no se encuentra la retroalimentación
     */
    public Optional<RetroalimentacionColaboracionDTO> getPorPersonaYColaboracion(int idPersona, int idColaboracion)
            throws ErrorDAO {
        ResultSet resultado = null;

        try {
            PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
                    "select * from retroalimentacion as rt natural join retroalimentacionColaboracion as rc where rt.usuario=? and rc.colaboracion=?");

            consulta.setInt(1, idPersona);
            consulta.setInt(2, idColaboracion);

            resultado = consulta.executeQuery();
            consulta.close();
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
        } finally {
            AdministradorBaseDatos.desconectar();
        }

        try {
            if (resultado == null || !resultado.next()) {
                return Optional.empty();
            }
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO("La retroalimentacion no se encontró", Tipo.CONSULTA);
        }

        return Optional.ofNullable(resultSetAObjeto(resultado));
    }

    public List<RetroalimentacionColaboracionDTO> getTodos() throws NotImplementedException {
        throw new NotImplementedException("el objeto no implementará el metodo");
    }

    /**
     * Convierte un ResultSet con los datos de una retroalimentación de colaboración en un objeto RetroalimentacionColaboracionDTO, el ResultSet debe encontrarse en una posición con información
     *
     * @param resultados el ResultSet con la forma: id, interaccionPar, comentario, idUsuario, habilidadesObtenidas, calificacion, intercambioCultural, mejoraLenguaje, trabajoColaborativo, mejoraFormacionProfesional, idColaboracion
     * @return Una retroalimentación de colaboración
     * @throws ErrorDAO tipo consulta si no es posible conseguir los datos del ResultSet
     */
    private static RetroalimentacionColaboracionDTO resultSetAObjeto(ResultSet resultados) throws ErrorDAO {
        try {
            RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();

            retroalimentacion.setIdRetroalimentacion(resultados.getInt(1));
            retroalimentacion.setInteraccionConPar(resultados.getInt(2));
            retroalimentacion.setComentario(resultados.getString(3));
            retroalimentacion.setIdUsuario(resultados.getInt(4));
            retroalimentacion.setHabilidadesObtenidas(resultados.getInt(5));
            retroalimentacion.setCalificacion(resultados.getInt(6));
            retroalimentacion.setIntercambioCultural(resultados.getInt(7));
            retroalimentacion.setMejoraDelLenguaje(resultados.getInt(8));
            retroalimentacion.setTrabajoColaborativo(resultados.getInt(9));
            retroalimentacion.setMejoraFormacionProfesional(resultados.getInt(10));
            retroalimentacion.setColaboracion(resultados.getInt(11));

            return retroalimentacion;
        } catch (SQLException error) {
            BITACORA.warn(error);
            throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
        }
    }
}
