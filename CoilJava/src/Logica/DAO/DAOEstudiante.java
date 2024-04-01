package Logica.DAO;

import AccesoADatos.EstudianteDB;
import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;
import Logica.Interfaces.IEstudianteDAO;

import java.util.List;
import java.util.Optional;

public class DAOEstudiante implements IEstudianteDAO {
    @Override
    public int agregar (Estudiante estudiante) throws ErrorDAO {
        return EstudianteDB.agregarEstudiante(estudiante);
    }
    // FIXME Toma en cuenta que sucedera con modificar

    @Override
    public int modificar (Integer y) throws ErrorDAO {
        return 0;
    }

    @Override
    public Estudiante getPorId (Integer id) throws ErrorDAO {
        return EstudianteDB.getPorId(id);
    }

    @Override
    public List<Estudiante> getTodos () throws ErrorDAO {
        return EstudianteDB.getTodos();
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
        return EstudianteDB.getEstudiantePorUniversidad(nombreUniversidad);
    }

    @Override
    public int modificarEstudiante (Estudiante estudiante) throws ErrorDAO {
        return EstudianteDB.editarEstudiante(estudiante);
    }
}
