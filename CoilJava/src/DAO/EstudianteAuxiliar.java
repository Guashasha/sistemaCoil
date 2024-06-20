package DAO;

import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
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
        return ESTUDIANTE_DAO.modificar(estudiante);
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
        probarMatricula(matricula);
        return ESTUDIANTE_DAO.getEstudiantePorMatriculaYUniversidad(matricula, idUniversidad);
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

    private boolean estudianteExiste (String matricula) throws ErrorDAO {
        return getEstudiantePorMatricula(matricula).isPresent();
    }

    private void probarMatricula (String matricula) throws ErrorDAO {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setMatricula(matricula);
    }


}
