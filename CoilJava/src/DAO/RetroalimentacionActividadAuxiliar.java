package DAO;

import DTO.ActividadDTO;
import DTO.RetroalimentacionActividadDTO;
import Utilidades.ErrorDAO;
import Utilidades.ErrorDAO.Tipo;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class RetroalimentacionActividadAuxiliar {
    /**
     * Valida y registra una retroalimentación de actividad a la base de datos
     *
     * @param retroalimentacion la retroalimentación que se guardará en la base de datos
     * @return el numero de filas alteradas
     * @throws ErrorDAO tipo validación si la retroalimentación es incorrecta, duplicidad si el usuario ya retroalimentó la actividad, consulta si la actividad no existe, y conwxión si ocurre un error de sql
     */
    public int agregar(RetroalimentacionActividadDTO retroalimentacion) throws ErrorDAO {
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
        } catch (ErrorDAO error) {
        }

        return resultado;
    }

    /**
     * Consigue una retroalimentación de actividad que tenga el id proporcionado
     *
     * @param id el id de la retroalimentación que se quiere buscar
     * @return La retroalimentación de actividad con el id proporcionado, empty si no se encuentra ninguna retroalimentación con la id proporcionada
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo consulta si no se encuentra ninguna retroalimentación con ese id
     */
    public Optional<RetroalimentacionActividadDTO> getPorId(Integer id) throws ErrorDAO {
        if (id < 1) {
            throw new ErrorDAO("El id es invalido" + id, Tipo.VALIDACION);
        }

        Optional<RetroalimentacionActividadDTO> rsRetroalimentacion = Optional.empty();
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            rsRetroalimentacion = retroalimentacionDAO.getPorId(id);
        } catch (ErrorDAO error) {
            throw error;
        }

        return rsRetroalimentacion;
    }

    /**
     * consigué todas las retroalimentaciones de actividad registradas en la base de datos
     *
     * @return ArrayList de retroalimentaciones de actividad
     * @throws ErrorDAO tipo conexion si ocurre un error de sql
     */
    public List<RetroalimentacionActividadDTO> getTodos() throws ErrorDAO {
        List<RetroalimentacionActividadDTO> retroalimentaciones = null;
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            retroalimentaciones = retroalimentacionDAO.getTodos();
        } catch (ErrorDAO error) {
            throw error;
        }

        return retroalimentaciones;
    }

    /**
     * Consigué la retroalimentación de la actividad especificada y la persona que la realizó
     *
     * @param idPersona   la id de la persona que realizó la retroalimentación
     * @param idActividad la id de la actividad que se busca la retroalimentación
     * @return la retroalimentación de la actividad especificada, realizada por la persona especificada
     * @throws ErrorDAO tipo conexión si ocurre un error de sql, tipo validación si el id de la persona o de la actividad son incorrectos
     */
    public Optional<RetroalimentacionActividadDTO> getPorPersonaYActividad(int idPersona, int idActividad)
            throws ErrorDAO {
        if (idPersona < 1 || idActividad < 1) {
            throw new ErrorDAO("Las id's ingresadas son incorrectas", Tipo.VALIDACION);
        }

        Optional<RetroalimentacionActividadDTO> retroalimentacion = Optional.empty();
        RetroalimentacionActividadDAO retroalimentacionDAO = new RetroalimentacionActividadDAO();

        try {
            retroalimentacion = retroalimentacionDAO.getPorPersonaYActividad(idPersona, idActividad);
        } catch (ErrorDAO error) {
            throw error;
        }

        return retroalimentacion;
    }

}
