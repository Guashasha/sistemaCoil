package DAO.Interfaces;

import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IEstudianteDAO extends IDAO<EstudianteDTO, Integer>{
    Optional<EstudianteDTO> getEstudiantePorIdPersona(int idPersona) throws ErrorDAO;
    Optional<EstudianteDTO> getEstudiantePorMatriculaYUniversidad (String matricula, int idUniversidad) throws ErrorDAO;
    List<EstudianteDTO> getEstudiantesSinColaboracionActivaOVinculadaPorUniversidad(int idUniversidad) throws ErrorDAO;
    Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO;

}
