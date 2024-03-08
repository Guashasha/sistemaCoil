package Logica.Interfaces;

import Logica.Dominio.Estudiante;
import Logica.Dominio.Universidad;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IEstudianteDAO {

    int agregarEstudiante(Estudiante estudiante) throws SQLException;
    int modificarEstudiante(int idEstudiante) throws SQLException;
    ArrayList<Estudiante> estudiantesPorUniversidad(int idUniversidad) throws SQLException;


}
