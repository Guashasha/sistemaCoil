package Logica.DAO;

import AccesoADatos.EstudianteDB;
import Logica.Dominio.Estudiante;
import Utilidades.ErrorDAO;
import Logica.Interfaces.IEstudianteDAO;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOEstudiante implements IEstudianteDAO {
    private static final Logger BITACORA = Logger.getLogger(DAOEstudiante.class);


    @Override
    public int agregar (Estudiante estudiante) throws ErrorDAO {
        int filasAfectadas = -1;

        if (!estudiante.validarNulos()) {
            throw new ErrorDAO("Al menos un campo del estudiante esta vacio", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = EstudianteDB.agregarEstudiante(estudiante);
        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }
        return filasAfectadas;
    }

    @Override
    public int modificar (Estudiante estudiante) throws ErrorDAO {
        int filasAfectadas = -1;

        if (!estudiante.validarNulos()) {
            throw new ErrorDAO("Al menos un campo del estudiante esta vacio", ErrorDAO.Tipo.VALIDACION);

        }
        if (!getEstudiantePorMatricula(estudiante.getMatricula()).isPresent()) {
            throw new ErrorDAO("La matricula no se encuentra registrada", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            filasAfectadas = EstudianteDB.editarEstudiante(estudiante);

        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
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
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
        }

        return Optional.ofNullable(estudiante);
    }


    @Override
    public List<Estudiante> getTodos () throws ErrorDAO {
        List<Estudiante> listaEstudiantes = null;

        try {
            listaEstudiantes = EstudianteDB.getTodos();

        }
        catch (SQLException error) {
            BITACORA.error(error.getMessage());

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
        catch (SQLException error) {
            BITACORA.error(error.getMessage());

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
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
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
        catch (SQLException error) {
            BITACORA.error(error.getMessage());
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
