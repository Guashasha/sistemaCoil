package Logica.DAO;

import Logica.Dominio.Estudiante;
import Logica.ErrorDAO;
import Logica.Interfaces.IEstudianteDAO;

import java.util.List;

public class DAOEstudiante implements IEstudianteDAO {
    @Override
    public int agregar (Estudiante t) throws ErrorDAO {
        return 0;
    }

    @Override
    public int modificar (Integer y) throws ErrorDAO {
        return 0;
    }

    @Override
    public Estudiante getPorId (Integer y) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Estudiante> getTodos () throws ErrorDAO {
        return null;
    }

    @Override
    public Estudiante getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        return null;
    }

    @Override
    public Estudiante getEstudaintePorMatricula (String matricula) throws ErrorDAO {
        return null;
    }

    @Override
    public List<Estudiante> getEstudiantePorUniversidad (String nombreUniversidad) throws ErrorDAO {
        return null;
    }
}
