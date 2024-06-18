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
    /**
     * Valida y registra una actividad en la base de datos
     * @param actividadDTO actividad a registrar en la base de datos
     * @return el numero de filas registradas en la base de datos
     * @throws ErrorDAO tipo validacion si la actividad es incorrecta, tipo conexión si fue un error de sql
     */
    public int agregar (ActividadDTO actividadDTO) throws ErrorDAO {
        if (!actividadDTO.esCorrecta()) {
            throw new ErrorDAO("La actividadDTO es incorrecta", Tipo.VALIDACION);
        }

        int resultado = -1;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.agregar(actividadDTO);
        }
        catch (ErrorDAO error) {
            throw error;
        }

        return resultado;
    }

    public int modificar (ActividadDTO actividadDTO) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Metodo no implementado");
    }

    /**
     * Consigue una actividad de la base de datos proporcionando su id
     * @param idActividad el id de la actividad a buscar
     * @return La actividad buscada si se encontró, empty en otro caso
     * @throws ErrorDAO tipo conexión si ocurre un error de sql
     */
    public Optional<ActividadDTO> getPorId (Integer idActividad) throws ErrorDAO {
        Optional<ActividadDTO> resultado;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.getPorId(idActividad);
        }
        catch (ErrorDAO error) {
            throw error;
        }

        return resultado;
    }

    /**
     * Consigue de la base de datos la primera actividad con el titulo especificado
     * @param titulo el titulo de la actividad que se quiere buscar
     * @return La actividad más reciente con el titulo especificado
     * @throws ErrorDAO tipo conexión si ocurrió un error de sql, tipo consulta si no se encontró ninguna actividad con ese titulo
     */
    public Optional<ActividadDTO> getPorTitulo (String titulo) throws ErrorDAO {
        Optional<ActividadDTO> resultado;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.getPorTitulo(titulo);
        }
        catch (ErrorDAO error) {
            throw error;
        }

        return resultado;
    }

    /**
     * Consigue de la base de datos todas las actividades que se encuentren vinculadas a una colaboración
     * @param idColaboracion el id de la colaboración a las que deben estár vinculadas las actividades
     * @return ArrayList de actividades, si no hay actividades vinculadas con esa colaboración el arraylist estará vacío
     * @throws ErrorDAO tipo conexion si ocurre un error de sql
     */
    public List<ActividadDTO> getPorIdColaboracion (int idColaboracion) throws ErrorDAO {
        List<ActividadDTO> resultado;
        ActividadDAO actividadDAO = new ActividadDAO();

        try {
            resultado = actividadDAO.getPorIdColaboracion(idColaboracion);
        }
        catch (ErrorDAO error) {
            throw error;
        }

        return resultado;
    }

}
