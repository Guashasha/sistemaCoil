package Logica.DAO;

import AccesoADatos.EstudianteDB;
import Logica.Dominio.Estudiante;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import Logica.Interfaces.IEstudianteDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOEstudiante implements IEstudianteDAO {

    @Override
    public int agregar (Estudiante estudiante) throws ErrorDAO {
        if (existe(estudiante.getMatricula())) {
            throw new ErrorDAO("El estudiante con la matricula " + estudiante.getMatricula() + " ya se encuentra registrado", Tipo.VALIDACION);
        }
        try {
            return EstudianteDB.agregarEstudiante(estudiante);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (Estudiante estudiante) throws ErrorDAO {
        if (!existe(estudiante.getMatricula())) {
            throw new ErrorDAO("La matricula no se encuentra registrada", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return EstudianteDB.editarEstudiante(estudiante);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<Estudiante> getPorId (Integer id) throws ErrorDAO {
        if (noEsIdValido(id)) {
            throw new ErrorDAO("El id del estudiante no es valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(EstudianteDB.getPorId(id));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }


    @Override
    public List<Estudiante> getTodos () throws ErrorDAO {
        try {
            return EstudianteDB.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Estudiante resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    @Override
    public Optional<Estudiante> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        if (noEsIdValido(idPersona)) {
            throw new ErrorDAO("Id de persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(EstudianteDB.getEstudiantePorIdPersona(idPersona));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(),error.getTipo());
        }
    }

    @Override
    public Optional<Estudiante> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        try {
            probarMatricula(matricula);
            return Optional.ofNullable(EstudianteDB.getEstudiantePorMatricula(matricula));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<Estudiante> getEstudiantePorUniversidad (int idUniversidad) throws ErrorDAO {
        List<Estudiante> listaEstudiantes;
        if (noEsIdValido(idUniversidad)) {
            throw new ErrorDAO("Id de una universidad invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaEstudiantes = EstudianteDB.getEstudiantePorUniversidad(idUniversidad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
        return listaEstudiantes;
    }
    private boolean noEsIdValido (int id) {
        return id <= 0;
    }

    private boolean existe (String matricula) {
        return getEstudiantePorMatricula(matricula).isPresent();
    }
    private void probarMatricula (String matricula) {
        Estudiante estudiante = new Estudiante();
        estudiante.setMatricula(matricula);
    }


}
