package DAO;

import DTO.ColaboracionDTO;
import DTO.RetroalimentacionActividadDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RetroalimentacionColaboracionAuxiliar {
  private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class.getName());

  public int agregar(RetroalimentacionColaboracionDTO retroalimentacion) throws ErrorDAO {
    if (!retroalimentacion.esCorrecta()) {
      throw new ErrorDAO("los datos de la colaboracion son invalidos", Tipo.VALIDACION);
    }

    if (getPorPersonaYColaboracion(retroalimentacion.getIdUsuario(), retroalimentacion.getColaboracion()).isPresent()) {
      throw new ErrorDAO("La colaboración ya fue calificada por el usuario", Tipo.DUPLICIDAD);
    }

    ColaboracionAuxiliar col = new ColaboracionAuxiliar();
    Optional<ColaboracionDTO> colaboracion = col.getColaboracionPorId(retroalimentacion.getColaboracion());

    if (colaboracion.isEmpty()) {
      throw new ErrorDAO("La colaboración no existe", Tipo.CONSULTA);
    } else if (colaboracion.get()
        .getEstado() != ColaboracionDTO.EstadoColaboracion.enRevision) {
      throw new ErrorDAO("La colaboración no puede ser evaluada aún", Tipo.VALIDACION);
    }

    int resultado = -1;
    RetroalimentacionColaboracionDAO retroalimentacionDAO = new RetroalimentacionColaboracionDAO();

    try {
      resultado = retroalimentacionDAO.agregar(retroalimentacion);
    } catch (ErrorDAO error) {
      BITACORA.error(error);
    }

    return resultado;
  }

  public int modificar(RetroalimentacionColaboracionDTO obj) throws ErrorDAO {
    throw new ErrorDAO("metodo no disponible para el objeto", Tipo.VALIDACION);
  }

  public Optional<RetroalimentacionColaboracionDTO> getPorId(Integer id) throws ErrorDAO {
    if (id < 1) {
      throw new ErrorDAO("la id proporcionada no es correcta", Tipo.VALIDACION);
    }

    Optional<RetroalimentacionColaboracionDTO> retroalimentacion = Optional.empty();
    RetroalimentacionColaboracionDAO retroalimentacionDAO = new RetroalimentacionColaboracionDAO();

    try {
      retroalimentacion = retroalimentacionDAO.getPorId(id);
    } catch (ErrorDAO error) {
      BITACORA.error(error);
    }

    return retroalimentacion;
  }

  public Optional<RetroalimentacionColaboracionDTO> getPorPersonaYColaboracion(int idPersona, int idColaboracion) {
    if (idPersona < 1 || idColaboracion < 1) {
      throw new ErrorDAO("alguna de las id proporcionadas no es correcta", Tipo.VALIDACION);
    }

    Optional<RetroalimentacionColaboracionDTO> retroalimentacion = Optional.empty();
    RetroalimentacionColaboracionDAO retroalimentacionDAO = new RetroalimentacionColaboracionDAO();

    try {
      retroalimentacion = retroalimentacionDAO.getPorPersonaYColaboracion(idPersona, idColaboracion);
    } catch (ErrorDAO error) {
      BITACORA.error(error);
    }

    return retroalimentacion;
  }

  public List<RetroalimentacionColaboracionDTO> getTodos() throws ErrorDAO {
    List<RetroalimentacionColaboracionDTO> resultados = null;
    RetroalimentacionColaboracionDAO retroalimentacionDAO = new RetroalimentacionColaboracionDAO();

    try {
      resultados = retroalimentacionDAO.getTodos();
    } catch (ErrorDAO error) {
      BITACORA.error(error);
    }

    if (resultados == null) {
      throw new ErrorDAO("No hay retroalimentaciones registradas", Tipo.CONSULTA);
    }

    return resultados;
  }
}
