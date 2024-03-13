package Logica.Interfaces;

import Logica.Dominio.EstudianteUV;
import Logica.ErrorDAO;

import java.util.List;

public interface IEstudianteUVDAO {
    EstudianteUV getEstudiantePorMatricula(String matricula) throws ErrorDAO;

    List<EstudianteUV> getEstudiantesPorFacultad(String nombreFacultad) throws ErrorDAO;
    List<EstudianteUV> getEstudiantesPorRegion(String nombreRegion) throws ErrorDAO;
}
