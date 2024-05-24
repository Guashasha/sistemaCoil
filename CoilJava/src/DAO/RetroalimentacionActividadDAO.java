package DAO;

import DAO.Interfaces.IRetroalimentacionActividadDAO;
import DTO.RetroalimentacionActividadDTO;
import AccesoDatos.AdministradorBaseDatos;
import Utilidades.ErrorDAO;
import jdk.jshell.spi.ExecutionControl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RetroalimentacionActividadDAO implements IRetroalimentacionActividadDAO {

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
      throw new ErrorDAO(e.getMessage(), ErrorDAO.Tipo.CONEXION);
    } finally {
      AdministradorBaseDatos.desconectar();
    }

    try {
      if (retroalimentacion == null || !retroalimentacion.next()) {
        return Optional.empty();
      }
    } catch (SQLException error) {
      throw new ErrorDAO("La retroalimentación no se encotró", ErrorDAO.Tipo.CONSULTA);
    }

    return Optional.of(resultSetAObjeto(retroalimentacion));
  }

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
      throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
    } finally {
      AdministradorBaseDatos.desconectar();
    }

    try {
      if (resultado == null || !resultado.next()) {
        return Optional.empty();
      }
    } catch (SQLException error) {
      throw new ErrorDAO("La retroalimentación no se encotró", ErrorDAO.Tipo.CONSULTA);
    }

    return Optional.of(resultSetAObjeto(resultado));
  }

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

  public List<RetroalimentacionActividadDTO> getTodos() throws ErrorDAO {
    ResultSet resultado = null;

    try {
      PreparedStatement consulta = AdministradorBaseDatos.getInstancia().prepareStatement(
          "select idRetroalimentacion, interaccionPar, comentario, dificultad, interes, usuario, actividad from retroalimentacion natural join retroalimentacionActividad");

      resultado = consulta.executeQuery();
      consulta.close();
    } catch (SQLException e) {
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
      throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONEXION);
    }

    return retroalimentaciones;
  }

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
      throw new ErrorDAO(error.getMessage(), ErrorDAO.Tipo.CONSULTA);
    }

    return retroalimentacion;
  }
}
