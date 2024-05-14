package DAO;

import DTO.EstudianteDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import DAO.Interfaces.IEstudianteDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class EstudianteAuxiliar implements IEstudianteDAO {

    @Override
    public int agregar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (existe(estudianteDTO.getMatricula())) {
            throw new ErrorDAO("El estudianteDTO con la matricula " + estudianteDTO.getMatricula() + " ya se encuentra registrado", Tipo.VALIDACION);
        }
        try {
            return EstudianteDAO.agregarEstudiante(estudianteDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public int modificar (EstudianteDTO estudianteDTO) throws ErrorDAO {
        if (!existe(estudianteDTO.getMatricula())) {
            throw new ErrorDAO("La matricula no se encuentra registrada", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return EstudianteDAO.editarEstudiante(estudianteDTO);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public Optional<EstudianteDTO> getPorId (Integer id) throws ErrorDAO {
        if (noEsIdValido(id)) {
            throw new ErrorDAO("El id del estudiante no es valido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(EstudianteDAO.getPorId(id));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }


    @Override
    public List<EstudianteDTO> getTodos () throws ErrorDAO {
        try {
            return EstudianteDAO.getTodos();
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public EstudianteDTO resultSetAObjeto (ResultSet resultados) {
        return null;
    }

    @Override
    public Optional<EstudianteDTO> getEstudiantePorIdPersona (int idPersona) throws ErrorDAO {
        if (noEsIdValido(idPersona)) {
            throw new ErrorDAO("Id de persona invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            return Optional.ofNullable(EstudianteDAO.getEstudiantePorIdPersona(idPersona));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(),error.getTipo());
        }
    }

    @Override
    public Optional<EstudianteDTO> getEstudiantePorMatricula (String matricula) throws ErrorDAO {
        try {
            probarMatricula(matricula);
            return Optional.ofNullable(EstudianteDAO.getEstudiantePorMatricula(matricula));
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
    }

    @Override
    public List<EstudianteDTO> getEstudiantePorUniversidad (int idUniversidad) throws ErrorDAO {
        List<EstudianteDTO> listaEstudianteDTOS;
        if (noEsIdValido(idUniversidad)) {
            throw new ErrorDAO("Id de una universidad invalido", ErrorDAO.Tipo.VALIDACION);
        }
        try {
            listaEstudianteDTOS = EstudianteDAO.getEstudiantePorUniversidad(idUniversidad);
        }
        catch (ErrorDAO error) {
            throw new ErrorDAO(error.getMessage(), error.getTipo());
        }
        return listaEstudianteDTOS;
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
