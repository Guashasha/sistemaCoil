package DAO;

import DTO.RetroalimentacionColaboracionDTO;
import AccesoDatos.AdministradorBaseDatos;
import DAO.Interfaces.IRetroalimentacionColaboracionDAO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class RetroalimentacionColaboracionDAO implements IRetroalimentacionColaboracionDAO {

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

  @Override
  public Optional<RetroalimentacionColaboracionDTO> getPorId(Integer id) throws ErrorDAO {
    ResultSet resultado = null;

    try {
      PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
          "select * from retroalimentacion as rt natural join retroalimentacionColaboracion where rt.usuario=?");

      consulta.setInt(1, id);

      resultado = consulta.executeQuery();
      consulta.close();
    } catch (SQLException error) {
      throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
    } finally {
      AdministradorBaseDatos.desconectar();
    }

    try {
      if (resultado == null || !resultado.next()) {
        return Optional.empty();
      }
    } catch (SQLException error) {
      throw new ErrorDAO("La retroalimentación no se encotró", Tipo.CONSULTA);
    }

    return Optional.ofNullable(resultSetAObjeto(resultado));
  }

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
      throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
    } finally {
      AdministradorBaseDatos.desconectar();
    }

    try {
      if (resultado == null || !resultado.next()) {
        return Optional.empty();
      }
    } catch (SQLException error) {
      throw new ErrorDAO("La retroalimentacion no se encontró", Tipo.CONSULTA);
    }

    return Optional.ofNullable(resultSetAObjeto(resultado));
  }

  public List<RetroalimentacionColaboracionDTO> getTodos() throws ErrorDAO {
    ResultSet resultado = null;

    try {
      PreparedStatement consulta = AdministradorBaseDatos.getInstancia()
          .prepareStatement("select * from retroalimentacion natural join retroalimentacionColaboracion");

      resultado = consulta.executeQuery();
      consulta.close();
    } catch (SQLException error) {
      throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
    } finally {
      AdministradorBaseDatos.desconectar();
    }

    List<RetroalimentacionColaboracionDTO> retroalimentaciones = new ArrayList<>();

    if (resultado == null) {
      return retroalimentaciones;
    }

    try {
      while (resultado.next()) {
        RetroalimentacionColaboracionDTO retroalimentacion = resultSetAObjeto(resultado);

        if (retroalimentacion != null) {
          retroalimentaciones.add(retroalimentacion);
        }
      }

      resultado.close();
    } catch (SQLException error) {
      throw new ErrorDAO(error.getMessage(), Tipo.CONEXION);
    }

    return retroalimentaciones;
  }

  private static RetroalimentacionColaboracionDTO resultSetAObjeto(ResultSet resultados) throws ErrorDAO {
    RetroalimentacionColaboracionDTO retroalimentacion = new RetroalimentacionColaboracionDTO();

    try {
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
    } catch (SQLException error) {
      throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
    }

    return retroalimentacion;
  }
}
