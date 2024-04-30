package Logica.Interfaces;

import Logica.Dominio.Estudiante;
import Utilidades.ErrorDAO;

import java.util.List;
import java.util.Optional;

public interface IEstudianteDAO extends IDAO<Estudiante, Integer>{
    public Optional<Estudiante> getEstudiantePorIdPersona(int idPersona) throws ErrorDAO;
    public Optional<Estudiante> getEstudiantePorMatricula (String matricula) throws ErrorDAO;
    List<Estudiante> getEstudiantePorUniversidad (int idUniversidad) throws ErrorDAO;

}
