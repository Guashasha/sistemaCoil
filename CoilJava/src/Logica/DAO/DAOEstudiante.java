package Logica.DAO;

import AccesoADatos.EstudianteDB;
import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;
import Logica.Interfaces.IEstudianteDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOEstudiante implements IEstudianteDAO {
    @Override
    public int agregar (Estudiante estudiante) throws ErrorDAO {
        return EstudianteDB.agregarEstudiante(estudiante);
    }

    @Override
    public int modificar (Estudiante obj) throws ErrorDAO {
        return 0;
    }

    @Override
    public Optional<Estudiante> getPorId (Integer y) throws ErrorDAO {
        return Optional.empty();
    }


    @Override
    public List<Estudiante> getTodos () throws ErrorDAO {
        return EstudianteDB.getTodos();
    }

    @Override
    public Estudiante resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    @Override
    public Optional<Estudiante> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        return Optional.ofNullable(EstudianteDB.getEstudiantePorIdPersona(idPersona));
    }

    @Override
    public Optional<Estudiante> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        return Optional.ofNullable(EstudianteDB.getEstudiantePorMatricula(matricula));
    }

    @Override
    public List<Estudiante> getEstudiantePorUniversidad (String nombreUniversidad) throws ErrorDAO {
        return null;
    }

    @Override
    public int modificarEstudiante (Estudiante estudiante) throws ErrorDAO {
        return EstudianteDB.editarEstudiante(estudiante);
    }
}
