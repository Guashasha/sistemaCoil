package DAO;

import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class EstudianteAuxiliar {
    private final EstudianteDAO ESTUDIANTE_DAO = new EstudianteDAO();

    public int agregar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (existe(estudianteDTO.getMatricula())) {
            throw new ErrorDAO("El estudianteDTO con la matricula " + estudianteDTO.getMatricula() + " ya se encuentra registrado", Tipo.VALIDACION);
        }
        try {
            return ESTUDIANTE_DAO.agregar(estudianteDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public int modificar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (!existe(estudianteDTO.getMatricula())) {
            throw new ErrorDAO("La matricula no se encuentra registrada", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ESTUDIANTE_DAO.modificar(estudianteDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public Optional<EstudianteDTO> getPorId (Integer id) throws ErrorDAO {
        if (noEsIdValido(id)) {
            throw new ErrorDAO("El id del estudiante no es valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ESTUDIANTE_DAO.getPorId(id);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }


    public List<EstudianteDTO> getTodos () throws ErrorDAO {
        try {
            return ESTUDIANTE_DAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public EstudianteDTO resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    public Optional<EstudianteDTO> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        if (noEsIdValido(idPersona)) {
            throw new ErrorDAO("Id de persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return ESTUDIANTE_DAO.getEstudiantePorIdPersona(idPersona);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(),error.getTipo());
        }
    }

    public Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        try {
            probarMatricula(matricula);
            return ESTUDIANTE_DAO.getEstudiantePorMatricula(matricula);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    public List<EstudianteDTO> getEstudiantePorUniversidad (int idUniversidad) throws ErrorDAO {
        if (noEsIdValido(idUniversidad)) {
            throw new ErrorDAO("Id de una universidad invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return  ESTUDIANTE_DAO.getEstudiantePorUniversidad(idUniversidad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }
    private boolean noEsIdValido (int id) {
        return id <= 0;
    }

    private boolean existe (String matricula) {
        return getEstudiantePorMatricula(matricula).isPresent();
    }
    private void probarMatricula (String matricula) {
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setMatricula(matricula);
    }


}
