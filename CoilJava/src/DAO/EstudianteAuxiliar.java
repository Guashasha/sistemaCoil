package DAO;

import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import java.util.List;
import java.util.Optional;

/**
 * La clase EstudianteAuxiliar funciona como intermediario entre el cliente y las clases DAO. Procesa y valida la información de los parámetros antes de mandarla a las clases DAO
 */
public class EstudianteAuxiliar {
    private final EstudianteDAO ESTUDIANTE_DAO = new EstudianteDAO();

    /**
     * Valida los parámetros para registrar un estudiante en la base de datos, con la clase EstudianteDAO.
     *
     * @param estudiante estudiante con los datos necesarios para registrarlo.
     * @return número de filas afectadas por la sentencia SQL
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de datos.
     */
    public int agregar (EstudianteDTO estudiante) throws ErrorDAO {
        if (estudiante == null) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        else if (estudianteExiste(estudiante.getMatricula())) {
            throw new ErrorDAO("El estudiante con la matricula " + estudiante.getMatricula() + " ya se encuentra registrado", Tipo.VALIDACION);
        }
        return ESTUDIANTE_DAO.agregar(estudiante);
    }

    /**
     * Valida los parámetros para registrar un estudiante en la base de datos, con la clase EstudianteDAO
     *
     * @param estudiante Estudiante con los datos ya modificados.
     * @return número de filas afectadas por la sentencia SQL.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de aatos.
     */
    public int modificar (EstudianteDTO estudiante) throws ErrorDAO {
        if (estudiante == null) {
            throw new ErrorDAO("Algo salió mal, inténtelo de nuevo más tarde", ErrorDAO.Tipo.VALIDACION);
        }
        return ESTUDIANTE_DAO.agregar(estudiante);
    }

    /**
     * Valida los parámetros para obtener un estudiante de acuerdo su id asociado en la base de datos, con la clase EstudianteDAO
     *
     * @param id id del estudiante a buscar
     * @return Objeto optional con el estudiante encontrado o un objeto Optional vacío si no encuentra resultados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de aatos.
     */
    public Optional<EstudianteDTO> getPorId (Integer id) throws ErrorDAO {
        if (noEsIdValido(id)) {
            throw new ErrorDAO("El id del estudiante no es valido", ErrorDAO.Tipo.VALIDACION);
        }
        return ESTUDIANTE_DAO.getPorId(id);
    }

    /**
     * Valida los parámetros para obtener un estudiante de acuerdo a su id de persona asociado en la base de datos, con la clase EstudianteDAO
     *
     * @param idPersona id de persona asociado al estudiante a buscar.
     * @return Objeto Optional con el estudiante encontrado o un objeto Optional vacío si no encuentra resultados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de aatos.
     */
    public Optional<EstudianteDTO> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        if (noEsIdValido(idPersona)) {
            throw new ErrorDAO("Id de persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        return ESTUDIANTE_DAO.getEstudiantePorIdPersona(idPersona);
    }

    /**
     * Valida los parámetros para obtener un estudiante de acuerdo a su matrícula y universidad, con la clase EstudianteDAO.
     *
     * @param matricula     Matricula del estudiante a buscar.
     * @param idUniversidad id de la universidad a la que se asocia el estudiante.
     * @return Objeto Optional con el estudiante encontrado o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de aatos.
     */
    public Optional<EstudianteDTO> getEstudiantePorMatriculaYUniversidad (String matricula, int idUniversidad) throws ErrorDAO {
        if (noEsIdValido(idUniversidad)) {
            throw new ErrorDAO("La universidad no es válida", Tipo.VALIDACION);
        }
        probarMatricula(matricula);
        return ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(matricula, idUniversidad);
    }

    /**
     * Valida los parámetros para obtener los estudiantes que no se encuentran actualmente en una colaboración vinculada o activa, y que están asociados a una universidad específica
     *
     * @param idUniversidad id de la universidad asociada a los estudiantes.
     * @return Lista con los estudiantes encontrados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de aatos.
     */
    public List<EstudianteDTO> getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad (int idUniversidad) throws ErrorDAO {
        if (noEsIdValido(idUniversidad)) {
            throw new ErrorDAO("Id de una universidad invalido", ErrorDAO.Tipo.VALIDACION);
        }
        return ESTUDIANTE_DAO.getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(idUniversidad);
    }

    /**
     * Valida los parámetros para obtener un estudiante de acuerdo a su matricula, con la clase EstudianteDAO
     *
     * @param matricula Matricula del estudiante a buscar.
     * @return Objeto Optional con el estudiante encontrado o un objeto Optional vacío si no se encuentran resultados.
     * @throws ErrorDAO si ocurre un error en la validación de los parámetros o durante el acceso a la base de aatos
     */
    public Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        probarMatricula(matricula);
        return ESTUDIANTE_DAO.getEstudiantePorMatricula(matricula);
    }

    private boolean noEsIdValido (int id) {
        return id <= 0;
    }

    private boolean estudianteExiste (String matricula) throws ErrorDAO {
        return getEstudiantePorMatricula(matricula).isPresent();
    }

    private void probarMatricula (String matricula) throws ErrorDAO {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setMatricula(matricula);
    }


}
