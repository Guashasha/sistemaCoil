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
        int filasAfectadas;

        if (!estudiante.validarNulos()) {
            throw new ErrorDAO("Al menos un campo del estudiante esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = EstudianteDB.agregarEstudiante(estudiante);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return filasAfectadas;
    }

    @Override
    public int modificar (Estudiante estudiante) throws ErrorDAO {
        int filasAfectadas;

        if (!estudiante.validarNulos()) {
            throw new ErrorDAO("Al menos un campo del estudiante esta vacio", ErrorDAO.Tipo.VALIDACION);

        }
        if (!getEstudiantePorMatricula(estudiante.getMatricula()).isPresent()) {
            throw new ErrorDAO("La matricula no se encuentra registrada", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = EstudianteDB.editarEstudiante(estudiante);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return filasAfectadas;
    }

    @Override
    public Optional<Estudiante> getPorId (Integer id) throws ErrorDAO {
        Estudiante estudiante = null;

        if (!idValido(id)) {
            throw new ErrorDAO("El id del estudiante no es valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            estudiante = EstudianteDB.getPorId(id);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }

        return Optional.ofNullable(estudiante);
    }


    @Override
    public List<Estudiante> getTodos () throws ErrorDAO {
        List<Estudiante> listaEstudiantes = null;

        try {
            listaEstudiantes = EstudianteDB.getTodos();

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;

        }

        return listaEstudiantes;
    }

    @Override
    public Estudiante resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    @Override
    public Optional<Estudiante> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        Estudiante estudiante = null;
        if (!idValido(idPersona)) {
            throw new ErrorDAO("Id de persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            estudiante = EstudianteDB.getEstudiantePorIdPersona(idPersona);

        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;

        }
        return Optional.ofNullable(estudiante);
    }

    @Override
    public Optional<Estudiante> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        Estudiante estudiante = null;
        if (!cadenaValida(matricula)) {
            throw new ErrorDAO("matricula no valida", ErrorDAO.Tipo.VALIDACION);

        }
        try {
            estudiante = EstudianteDB.getEstudiantePorMatricula(matricula);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return Optional.ofNullable(estudiante);
    }

    @Override
    public List<Estudiante> getEstudiantePorUniversidad (int idUniversidad) throws ErrorDAO {
        List<Estudiante> listaEstudiantes = null;
        if (!idValido(idUniversidad)) {
            throw new ErrorDAO("Id de una universidad invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaEstudiantes = EstudianteDB.getEstudiantePorUniversidad(idUniversidad);
        }
        catch (ErrorDAO errorDAO) {
            throw errorDAO;
        }
        return listaEstudiantes;
    }

    private boolean cadenaValida (String cadena) {
        return cadena != null && !cadena.isBlank();
    }

    private boolean idValido (int id) {
        return id > 0;
    }


}
