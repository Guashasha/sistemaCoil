package Logica.Interfaces;

import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;

import java.util.List;

public interface IEstudianteDAO extends IDAO<Estudiante, Integer>{
    public Estudiante getEstudiantePorIdPersona(int idPersona) throws ErrorDAO;
    public Estudiante getEstudaintePorMatricula (String matricula) throws ErrorDAO;
    List<Estudiante> getEstudiantePorUniversidad (String nombreUniversidad) throws ErrorDAO;

}
