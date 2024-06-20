package DAO;

import DTO.ColaboracionDTO;
import DTO.RetroalimentacionColaboracionDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;

import java.util.Optional;

public class RetroalimentacionColaboracionAuxiliar {
  /**
   * Agrega a la base de datos una retroalimentación de colaboración
   * @param retroalimentacion la retroalimentación que se registrará en la base de datos
   * @return el numero de filas afectadas en la base de datos
   * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo validacion si la retroalimentación es incorrecta o está en un estado diferente a enRevision, tipo duplicidad si la persona ya retroalimentó la colaboración, consulta si no se puede encontrar la colaboración
   */
  public int agregar(RetroalimentacionColaboracionDTO retroalimentacion) throws ErrorDAO {
    if (!retroalimentacion.esCorrecta()) {
      throw new ErrorDAO("los datos de la retroalimentacion son invalidos", Tipo.VALIDACION);
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
      throw error;
    }

    return resultado;
  }

  /**
   * Consigué una retroalimentación de colaboración por su id
   * @param id el id de la retroalimentación que se busca
   * @return la retroalimentación colaboración con el id especificado
   * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si no se encuentra la retroalimentación con la id especificada, tipo validación si la id es incorrecta
   */
  public Optional<RetroalimentacionColaboracionDTO> getPorId(Integer id) throws ErrorDAO {
    if (id < 1) {
      throw new ErrorDAO("la id proporcionada no es correcta", Tipo.VALIDACION);
    }

    Optional<RetroalimentacionColaboracionDTO> retroalimentacion = Optional.empty();
    RetroalimentacionColaboracionDAO retroalimentacionDAO = new RetroalimentacionColaboracionDAO();

    try {
      retroalimentacion = retroalimentacionDAO.getPorId(id);
    } catch (ErrorDAO error) {
    }

    return retroalimentacion;
  }

  /**
   * Consigue la retroalimentación de colaboración a partir de la persona que realizó la retroalimentación y la colaboración retroalimentada
   * @param idPersona la id de la persona que realiza la retroalimentación
   * @param idColaboracion la id de la colaboración retroalimentada
   * @return la retroalimentación de colaboración especificada que realizó la persona especificada
   * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si no se encuentra la retroalimentación, tipo validación si alguna de las id's es incorrecta
   */
  public Optional<RetroalimentacionColaboracionDTO> getPorPersonaYColaboracion(int idPersona, int idColaboracion) {
    if (idPersona < 1 || idColaboracion < 1) {
      throw new ErrorDAO("alguna de las id proporcionadas no es correcta", Tipo.VALIDACION);
    }

    Optional<RetroalimentacionColaboracionDTO> retroalimentacion = Optional.empty();
    RetroalimentacionColaboracionDAO retroalimentacionDAO = new RetroalimentacionColaboracionDAO();

    try {
      retroalimentacion = retroalimentacionDAO.getPorPersonaYColaboracion(idPersona, idColaboracion);
    } catch (ErrorDAO error) {
    }

    return retroalimentacion;
  }
}
