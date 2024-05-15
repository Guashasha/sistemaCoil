package DAO;

import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RetroalimentacionActividadAuxiliar {
    private static final Logger BITACORA = Logger.getLogger(RetroalimentacionActividadDTO.class.getName());

    public int agregar (RetroalimentacionActividadDTO retroalimentacion) throws ErrorDAO {
        if (!retroalimentacion.esCorrecto()) {
            throw new ErrorDAO("La retroalimentacion es incorrecta", Tipo.VALIDACION);
        }

        if (getPorPersonaYActividad(retroalimentacion.getIdUsuario(), retroalimentacion.getIdActividad()).isPresent()) {
            throw new ErrorDAO("La actividad ya fue calificada por el usuario", Tipo.DUPLICIDAD);
        }

        ActividadAuxiliar act = new ActividadAuxiliar();
        Optional<ActividadDTO> actividad = act.getPorId(retroalimentacion.getIdActividad());

        if (actividad.isEmpty()) {
            throw new ErrorDAO("La actividad no existe", Tipo.CONSULTA);
        }

        int resultado = -1;
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            resultado = retroalimentacionDAO.agregar(retroalimentacion);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return resultado;
    }

    public Optional<RetroalimentacionActividadDTO> getPorId (Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("El id es invalido" + id, Tipo.VALIDACION);
        }

        Optional<RetroalimentacionActividadDTO> rsRetroalimentacion = Optional.empty();
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            rsRetroalimentacion = retroalimentacionDAO.getPorId(id);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return rsRetroalimentacion;
    }

    public List<RetroalimentacionActividadDTO> getTodos () throws ErrorDAO {
        List<RetroalimentacionActividadDTO> retroalimentaciones = null;
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            retroalimentaciones = retroalimentacionDAO.getTodos();
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return retroalimentaciones;
    }

    public Optional<RetroalimentacionActividadDTO> getPorPersonaYActividad (int idPersona, int idActividad) throws ErrorDAO {
        if (idPersona < 1 || idActividad < 1) {
            throw new ErrorDAO("Las id's ingresadas son incorrectas", Tipo.VALIDACION);
        }

        Optional<RetroalimentacionActividadDTO> retroalimentacion = Optional.empty();
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            retroalimentacion = retroalimentacionDAO.getPorPersonaYActividad(idPersona, idActividad);
        }
        catch (ErrorDAO error) {
            BITACORA.error(error);
        }

        return retroalimentacion;
    }

}
