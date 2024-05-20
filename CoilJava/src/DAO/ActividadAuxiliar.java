package DAO;

import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import jdk.jshell.spi.ExecutionControl;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Optional;

public class ActividadAuxiliar {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class);

    public int agregar (ActividadDTO actividadDTO) throws ErrorDAO {
        if (!actividadDTO.esCorrecta()) {
            throw new ErrorDAO("La actividadDTO es incorrecta", Tipo.VALIDACION);
        }

        if (getPorTitulo(actividadDTO.getTitulo()).isPresent()) {
            throw new ErrorDAO("la actividadDTO ya existe", Tipo.DUPLICIDAD);
        }

        int resultado = -1;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.agregar(actividadDTO);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return resultado;
    }

    public int modificar (ActividadDTO actividadDTO) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Metodo no implementado");
    }

    public Optional<ActividadDTO> getPorId (Integer idActividad) throws ErrorDAO {
        Optional<ActividadDTO> resultado;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.getPorId(idActividad);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return resultado;
    }

    public Optional<ActividadDTO> getPorTitulo (String titulo) {
        Optional<ActividadDTO> resultado;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.getPorTitulo(titulo);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return resultado;
    }

    public List<ActividadDTO> getPorIdColaboracion (int idColaboracion) throws ErrorDAO {
        List<ActividadDTO> resultado;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.getPorIdColaboracion(idColaboracion);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return resultado;
    }

    public List<ActividadDTO> getTodos () throws ErrorDAO {
        List<ActividadDTO> resultados;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultados = actividadDAO.getTodos();
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
            throw error;
        }

        return resultados;
    }

}
