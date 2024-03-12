package Logica.Interfaces;

import Logica.Dominio.EstudianteExterno;
import Logica.ErrorDAO;

import java.util.List;

public interface IEstudianteExternoDAO {
    EstudianteExterno getEstudianteExternoPorMatricula(String matricula) throws ErrorDAO;

    List<EstudianteExterno> getEstudiantesPorUniversidad(String nombreUniversidad) throws ErrorDAO;

    List<EstudianteExterno> getEstudiantesPorPaisOrigen(String paisOrigen) throws ErrorDAO;
}
