package DAO.Interfaces;

import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IEstudianteDAO extends IDAO<EstudianteDTO, Integer>{
    public Optional<EstudianteDTO> getEstudiantePorIdPersona(int idPersona) throws ErrorDAO;
    public Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO;
    List<EstudianteDTO> getEstudiantePorUniversidad (int idUniversidad) throws ErrorDAO;

}
